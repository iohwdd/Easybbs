package com.easybbs.service.impl;

import com.easybbs.config.AppConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SysSetting4AuditDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.FileUploadDto;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.ForumArticleAttachment;
import com.easybbs.entity.po.ForumBoard;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mapper.ForumArticleAttachmentMapper;
import com.easybbs.mapper.ForumArticleMapper;
import com.easybbs.mapper.ForumBoardMapper;
import com.easybbs.service.ForumArticleService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.FileUtils;
import com.easybbs.utils.ImageUtils;
import com.easybbs.utils.StringUtils;
import com.easybbs.utils.SysCacheUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/14 19:16
 * @description:
 */
@Service
public class ForumArticleServiceImpl implements ForumArticleService {
    @Autowired
    ForumArticleMapper forumArticleMapper;
    @Autowired
    ForumBoardMapper forumBoardMapper;
    @Autowired
    FileUtils fileUtils;
    @Autowired
    ForumArticleAttachmentMapper forumArticleAttachmentMapper;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    ImageUtils imageUtils;
    @Autowired
    AppConfig appConfig;

    @Override
    public PaginationResultVO findListByPage(Integer boardId, Integer pBoardId, Integer orderType, Integer pageNo, String currentUserId) {
        pageNo = pageNo == null ? 1 : pageNo;
        PageHelper.startPage(pageNo, 15);
        String orderSql = ArticleOrderTypeEnum.getByType(orderType).getOrderSql();

        List<ForumArticle> list = forumArticleMapper.selectByWrapper(boardId, pBoardId, orderSql, currentUserId);
        PageInfo<ForumArticle> pageInfo = new PageInfo<>(list);

        PaginationResultVO resultVO = new PaginationResultVO<>();
        resultVO.setPageNo(pageNo);
        resultVO.setPageSize(pageInfo.getPageSize());
        resultVO.setPageTotal(pageInfo.getPages());
        resultVO.setTotalCount((int) pageInfo.getTotal());
        resultVO.setList(list);
        return resultVO;
    }

    @Override
    public ForumArticle readArticle(String articleId) {
        ForumArticle forumArticle = forumArticleMapper.selectByPrimaryKey(articleId);
        if (forumArticle == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        if (forumArticle.getStatus().equals(ArticleStatusEnum.NORMAL.getStatus())) {
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.READ_COUNT.getType(), Constants.ONE, articleId);
        }
        return forumArticle;
    }

    @Override
    public void postArticle(Boolean isAdmin, ForumArticle article, ForumArticleAttachment articleAttachment, MultipartFile cover, MultipartFile attachment) {
        resetBoardInfo(isAdmin, article);
        article.setArticleId(StringUtils.getRandomString(Constants.LENGTH_15));
        article.setPostTime(new Date());
        article.setLastUpdateTime(new Date());

        if (cover != null) {
            FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
            article.setCover(fileUploadDto.getLocalPath());
        }

        if (attachment != null) {
            uploadAttachment(article, articleAttachment, attachment, false);
            article.setAttachmentType(ArticleAttachmentTypEnum.HAVE_ATTACHMENT.getType());
        } else {
            article.setAttachmentType(ArticleAttachmentTypEnum.NO_ATTACHMENT.getType());
        }
        //文章审核信息
        if (isAdmin) {
            article.setStatus(ArticleStatusEnum.NORMAL.getStatus());
        } else {
            SysSetting4AuditDto auditDto = SysCacheUtils.getSysSetting().getAuditSetting();
            article.setStatus(auditDto.getPostAudit() ? ArticleStatusEnum.AUDIT.getStatus() : ArticleStatusEnum.NORMAL.getStatus());
        }
        //替换图片
        String content = article.getContent();
        if (!StringUtils.isEmpty(content)) {
            String month = imageUtils.resetImageHtml(content);
            String replaceMonth = month + "/";
            content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
            article.setContent(content);
            String markdownContent = article.getMarkdownContent();
            if (!StringUtils.isEmpty(markdownContent)) {
                markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
                article.setMarkdownContent(markdownContent);
            }
        }
        article.setReadCount(0);
        article.setCommentCount(0);
        article.setGoodCount(0);
        article.setTopType(0);
        forumArticleMapper.insert(article);
        //给发帖用户加积分
        Integer integral = SysCacheUtils.getSysSetting().getPostSetting().getPostIntegral();
        if (integral > 0 && article.getStatus().equals(ArticleStatusEnum.NORMAL.getStatus())) {
            userInfoService.updateUserIntegral(article.getUserId(), UserIntegralOperTypeEnum.POST_ARTICLE, UserIntegralChangeTypeEnum.ADD.getChangeType(), integral);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(Boolean isAdmin, ForumArticle article, ForumArticleAttachment articleAttachment, MultipartFile cover, MultipartFile attachment) {
        ForumArticle dbInfo = forumArticleMapper.selectByPrimaryKey(String.valueOf(article.getArticleId()));
        if (!isAdmin && !dbInfo.getUserId().equals(article.getUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        resetBoardInfo(isAdmin, article);
        if (cover != null) {
            FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
            article.setCover(fileUploadDto.getLocalPath());
        }
        if (attachment != null) {
            // 更新并上传附件
            uploadAttachment(article, articleAttachment, attachment, true);
            article.setAttachmentType(ArticleAttachmentTypEnum.HAVE_ATTACHMENT.getType());
        } else {
            article.setAttachmentType(ArticleAttachmentTypEnum.NO_ATTACHMENT.getType());
        }

        ForumArticleAttachment dbAttachment = null;
        List<ForumArticleAttachment> list = forumArticleAttachmentMapper.selectByArticleId(article.getArticleId());
        if (!list.isEmpty()) {
            dbAttachment = list.get(0);
        }

        if (dbAttachment != null) {
            if (article.getAttachmentType() == Constants.ZERO) {
                // 删除旧附件
                new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + dbAttachment.getFilePath()).delete();
                forumArticleAttachmentMapper.deleteByPrimaryKey(Long.valueOf(dbAttachment.getFileId()));
            } else {
                // 修改附件 上面已经完成了附件文件的更新，这里只需要更新附件下载积分
                if (!dbAttachment.getIntegral().equals(articleAttachment.getIntegral())) {
                    ForumArticleAttachment integralUpdate = new ForumArticleAttachment();
                    integralUpdate.setIntegral(articleAttachment.getIntegral());
                    integralUpdate.setFileId(dbAttachment.getFileId());
                    forumArticleAttachmentMapper.updateByPrimaryKeySelective(integralUpdate);
                }
            }
        }
        //文章是否需要审核
        if (isAdmin) {
            article.setStatus(ArticleStatusEnum.NORMAL.getStatus());
        } else {
            SysSetting4AuditDto auditDto = SysCacheUtils.getSysSetting().getAuditSetting();
            article.setStatus(auditDto.getPostAudit() ? ArticleStatusEnum.AUDIT.getStatus() : ArticleStatusEnum.NORMAL.getStatus());
        }

        //替换图片
        String content = article.getContent();
        if (!StringUtils.isEmpty(content)) {
            String month = imageUtils.resetImageHtml(content);
            String replaceMonth = month + "/";
            content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
            article.setContent(content);
            String markdownContent = article.getMarkdownContent();
            if (!StringUtils.isEmpty(markdownContent)) {
                markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
                article.setMarkdownContent(markdownContent);
            }
        }
        forumArticleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public int findCountByParam(ForumArticleQuery query) {
        return forumArticleMapper.findCountByParam(query);
    }

    @Override
    public PaginationResultVO findListByParam(ForumArticleQuery query, Integer pageNo) {
        pageNo = pageNo == null ? 1 : pageNo;
        PageHelper.startPage(pageNo, 15);
        List<ForumArticle> list = forumArticleMapper.findListByParams(query);
        PaginationResultVO resultVO = new PaginationResultVO<>();
        resultVO.setList(list);
        resultVO.setPageNo(pageNo);
        PageInfo<ForumArticle> pageInfo = new PageInfo<>(list);
        resultVO.setTotalCount((int) pageInfo.getTotal());
        resultVO.setPageSize(pageInfo.getPageSize());
        resultVO.setPageTotal(pageInfo.getPages());
        return resultVO;
    }

    @Override
    public PaginationResultVO search(Integer pageNo, String keyword) {
        pageNo = pageNo == null ? 1 : pageNo;
        PageHelper.startPage(pageNo, PageSize.SIZE50.getSize());
        List<ForumArticle> list = forumArticleMapper.selectByKeyword(keyword);
        PaginationResultVO<ForumArticle> resultVO = new PaginationResultVO<>();
        PageInfo<ForumArticle> pageInfo = new PageInfo<>(list);
        resultVO.setList(list);
        resultVO.setPageTotal(pageInfo.getPages());
        resultVO.setPageSize(pageInfo.getPageSize());
        resultVO.setPageNo(pageInfo.getPageNum());
        resultVO.setTotalCount((int) pageInfo.getTotal());
        return resultVO;
    }

    @Override
    public void delByArticleId(String[] ids) {
        forumArticleMapper.delByArticleIds(ids);
    }

    @Override
    public void updateArticleByParam(ForumArticleQuery query) {
        ForumBoard board = null;
        if (query.getBoardId() != 0) {
            board = forumBoardMapper.selectByPrimaryKey(Long.valueOf(query.getBoardId()));
        }
        ForumBoard pBoard = forumBoardMapper.selectByPrimaryKey(Long.valueOf(query.getpBoardId()));
        if (board == null && query.getBoardId() != 0 || pBoard == null) {
            throw new BusinessException("板块不存在");
        }
        query.setBoardName("");
        if (board != null) {
            query.setBoardName(board.getBoardName());
        }
        query.setpBoardName(pBoard.getBoardName());
        forumArticleMapper.updateArticleByParam(query);
    }

    private void resetBoardInfo(Boolean isAdmin, ForumArticle forumArticle) {
        ForumBoard pBoard = forumBoardMapper.selectByPrimaryKey(Long.valueOf(forumArticle.getpBoardId()));
        if (pBoard == null || pBoard.getPostType() == Constants.ZERO && !isAdmin) {
            throw new BusinessException("一级板块不存在");
        }
        forumArticle.setpBoardName(pBoard.getBoardName());
        if (forumArticle.getBoardId() != null && forumArticle.getBoardId() != 0) {
            ForumBoard board = forumBoardMapper.selectByPrimaryKey(Long.valueOf(forumArticle.getBoardId()));
            if (board == null || board.getPostType() == Constants.ZERO && !isAdmin) {
                throw new BusinessException("二级板块不存在");
            }
            forumArticle.setBoardName(board.getBoardName());
        } else {
            //文章属于一级板块
            forumArticle.setBoardId(0);
            forumArticle.setBoardName("");
        }
    }

    private void uploadAttachment(ForumArticle article, ForumArticleAttachment articleAttachment, MultipartFile file, boolean isUpdate) {
        Integer allowSizeMb = SysCacheUtils.getSysSetting().getPostSetting().getAttachmentSize();
        long allowSize = allowSizeMb * 1024 * 1024;
        if (file.getSize() > allowSize) {
            throw new BusinessException("附件最大只能上传" + allowSizeMb + "MB");
        }
        //旧的附件
        ForumArticleAttachment dbInfo = null;
        //修改
        if (isUpdate) {
            List<ForumArticleAttachment> list = forumArticleAttachmentMapper.selectByArticleId(article.getArticleId());
            if (!list.isEmpty()) {
                dbInfo = list.get(0);
                //在本地删除旧的附件
                new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + dbInfo.getFilePath()).delete();
            }
        }
        //上传新的附件到本地
        FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(file, Constants.FILE_FOLDER_ATTACHMENT, FileUploadTypeEnum.ARTICLE_ATTACHMENT);
        if (dbInfo == null) {
            //新增附件
            articleAttachment.setFileId(StringUtils.getRandomNumber(Constants.LENGTH_15));
            articleAttachment.setArticleId(article.getArticleId());
            articleAttachment.setFileName(fileUploadDto.getOriginFileName());
            articleAttachment.setFilePath(fileUploadDto.getLocalPath());
            articleAttachment.setFileSize(file.getSize());
            articleAttachment.setFileType(AttachmentFileTypeEnum.ZIP.getType());
            articleAttachment.setDownloadCount(Constants.ZERO);
            articleAttachment.setUserId(article.getUserId());
            forumArticleAttachmentMapper.insert(articleAttachment);
        } else {
            //修改附件
            ForumArticleAttachment attachment = new ForumArticleAttachment();
            attachment.setFileName(fileUploadDto.getOriginFileName());
            attachment.setFileSize(file.getSize());
            attachment.setFilePath(fileUploadDto.getLocalPath());
            attachment.setFileId(dbInfo.getFileId());
            forumArticleAttachmentMapper.updateByPrimaryKeySelective(attachment);
        }

    }

}
