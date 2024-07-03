package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.annotation.VerifyParam;
import com.easybbs.config.AppConfig;
import com.easybbs.config.WebConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.ArticleStatusEnum;
import com.easybbs.entity.enums.EditTypeEnum;
import com.easybbs.entity.enums.OperRecordTypeEnum;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.po.*;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.entity.vo.web.ForumArticleAttachmentVO;
import com.easybbs.entity.vo.web.ForumArticleDetailVO;
import com.easybbs.entity.vo.web.ForumArticleVO;
import com.easybbs.entity.vo.web.UserDownLoadInfoVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mapper.ForumArticleMapper;
import com.easybbs.service.*;
import com.easybbs.utils.CopyTools;
import com.easybbs.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/14 19:17
 * @description:
 */
@RestController
@RequestMapping("/forum")
public class ForumArticleController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(ForumArticleController.class);
    @Autowired
    private ForumArticleService forumArticleService;
    @Autowired
    private ForumArticleAttachmentService forumArticleAttachmentService;
    @Autowired
    private LikeRecordService likeRecordService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ForumArticleAttachmentDownloadService forumArticleAttachmentDownloadService;
    @Autowired
    private ForumBoardService forumBoardService;
    @Autowired
    private ForumArticleMapper forumArticleMapper;
    @Autowired
    WebConfig webConfig;

    @RequestMapping("/loadArticle")
    public ResponseVO loadArticle(HttpSession session, Integer pageNo ,Integer orderType, Integer pBoardId,Integer boardId) {
        boardId = boardId == null || boardId == 0 ? null : boardId;

        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        String currentUserId = null;
        if (sessionWebUserDto != null) {
            currentUserId = sessionWebUserDto.getUserId();
        }

        PaginationResultVO resultVO = forumArticleService.findListByPage(boardId, pBoardId, orderType, pageNo, currentUserId);
        PaginationResultVO paginationVO = convert2PaginationVO(resultVO, ForumArticleVO.class);
        return getSuccessResponseVo(paginationVO);
    }

    @RequestMapping("/getArticleDetail")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getArticleDetail(HttpSession session, @VerifyParam(required = true) String articleId) {
        ForumArticle forumArticle = forumArticleService.readArticle(articleId);
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        if (forumArticle == null
                || forumArticle.getStatus().equals(ArticleStatusEnum.AUDIT.getStatus())
                && (sessionWebUserDto == null || !forumArticle.getUserId().equals(sessionWebUserDto.getUserId()) || sessionWebUserDto.getIsAdmin())
                || ArticleStatusEnum.DELETE.getStatus().equals(forumArticle.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }

        ForumArticleDetailVO detailVO = new ForumArticleDetailVO();
        detailVO.setForumArticle(CopyTools.copy(forumArticle, ForumArticleVO.class));

        //有附件
        if (forumArticle.getStatus() == Constants.ONE) {
            List<ForumArticleAttachment> forumArticleAttachmentList = forumArticleAttachmentService.findByArticleId(articleId);
            if (!forumArticleAttachmentList.isEmpty()) {
                detailVO.setAttachment(CopyTools.copy(forumArticleAttachmentList.get(0), ForumArticleAttachmentVO.class));
            }
        }
        //是否已点赞
        if (sessionWebUserDto != null) {
            LikeRecord likeRecord = likeRecordService.getLikeRecordByObjectIdAndUserIdAndOpType(articleId, sessionWebUserDto.getUserId(),
                    OperRecordTypeEnum.ARTICLE_LIKE.getType());
            if (likeRecord != null) {
                detailVO.setHaveLike(true);
            }
        }
        return getSuccessResponseVo(detailVO);
    }

    @RequestMapping("/doLike")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO doLike(HttpSession session, String articleId) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        likeRecordService.doLike(articleId, sessionWebUserDto.getUserId(), sessionWebUserDto.getNickName(), OperRecordTypeEnum.ARTICLE_LIKE);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/getUserDownloadInfo")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO getUserDownloadInfo(HttpSession session, @VerifyParam(required = true) String fileId) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        UserInfo userInfo = userInfoService.getUserInfoByUserId(sessionWebUserDto.getUserId());
        UserDownLoadInfoVO userDownLoadInfoVO = new UserDownLoadInfoVO();
        userDownLoadInfoVO.setUserIntegral(userInfo.getCurrentIntegral());
        ForumArticleAttachmentDownload attachmentDownload = forumArticleAttachmentDownloadService.getByFileIdAndUserId(fileId, userInfo.getUserId());
        if (attachmentDownload != null) {
            userDownLoadInfoVO.setHaveDownload(true);
        }
        return getSuccessResponseVo(userDownLoadInfoVO);
    }

    @RequestMapping("/attachmentDownload")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public void attachmentDownload(HttpSession session, HttpServletRequest request, HttpServletResponse response, @VerifyParam(required = true) String fileId) {
        ForumArticleAttachment attachment = forumArticleAttachmentService.downloadAttachment(fileId, (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY));
        InputStream in = null;
        OutputStream out = null;
        String downloadFileName = attachment.getFileName();
        // 文件路径
        String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + attachment.getFilePath();
        File file = new File(filePath);
        try {
            // 文件流
            in = new FileInputStream(file);
            out = response.getOutputStream();
            // 设置响应结果是 二进制文件
            response.setContentType("application/x-msdownload;charset=UTF-8");
            // 解决中文文件名乱码
            if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") > 0) { // IE浏览器
                downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
            } else {
                downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
            }
            /**
             * Content-Disposition属性有两种类型：inline 和 attachment
             * inline ：将文件内容直接显示在页面
             * attachment：弹出对话框让用户下载：disposition-type是以什么方式下载，如attachment为以附件方式下载
             */
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("下载异常", e);
            throw new BusinessException("下载失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                logger.error("IO异常", e);
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.error("IO异常", e);
            }
        }
    }

    @RequestMapping("/loadBoard4Post")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadBoard4Post(HttpSession session) {
        SessionWebUserDto userDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        Integer postType = null;
        if (!userDto.getIsAdmin()) {
            postType = Constants.ONE;
        }
        return getSuccessResponseVo(forumBoardService.getBoardTree(postType));
    }

    @RequestMapping("/postArticle")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO postArticle(HttpSession session,
                                  MultipartFile cover,
                                  MultipartFile attachment,
                                  Integer integral,
                                  @VerifyParam(required = true) String title,
                                  @VerifyParam(required = true) Integer pBoardId,
                                  Integer boardId,
                                  @VerifyParam(max = 200) String summary,
                                  @VerifyParam(required = true) Integer editorType,
                                  @VerifyParam(required = true) String content,
                                  String markdownContent) {
        title = StringUtils.escapeHtml(title);
        SessionWebUserDto userDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        if (userDto == null) {
            throw new BusinessException("登录后才能发帖");
        }
        ForumArticle forumArticle = new ForumArticle();
        forumArticle.setTitle(title);
        forumArticle.setpBoardId(pBoardId);
        forumArticle.setBoardId(boardId);
        forumArticle.setSummary(summary);
        forumArticle.setContent(content);
        if (editorType.equals(EditTypeEnum.MARKDOWN.getType()) && StringUtils.isEmpty(markdownContent)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (EditTypeEnum.getByType(editorType) == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        forumArticle.setMarkdownContent(markdownContent);
        forumArticle.setEditorType(editorType);
        forumArticle.setNickName(userDto.getNickName());
        forumArticle.setUserId(userDto.getUserId());
        forumArticle.setUserIpAddress(userDto.getProvince());
        // 附件信息
        ForumArticleAttachment forumArticleAttachment = new ForumArticleAttachment();
        forumArticleAttachment.setIntegral(integral == null ? 0 : integral);
        forumArticleAttachment.setUserId(userDto.getUserId());
        forumArticleService.postArticle(userDto.getIsAdmin(), forumArticle, forumArticleAttachment, cover, attachment);
        return getSuccessResponseVo(forumArticle.getArticleId());
    }

    @RequestMapping("/articleDetail4Update")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO articleDetail4Update(HttpSession session, @VerifyParam String articleId) {
        SessionWebUserDto userDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        ForumArticle forumArticle = forumArticleMapper.getForumArticleByArticleId(articleId);
        if (forumArticle == null || !forumArticle.getUserId().equals(userDto.getUserId())) {
            throw new BusinessException("文章不存在或者无权编辑该文章");
        }
        ForumArticleDetailVO detailVO = new ForumArticleDetailVO();
        detailVO.setForumArticle(CopyTools.copy(forumArticle, ForumArticleVO.class));
        if (forumArticle.getAttachmentType() == Constants.ONE) {
            List<ForumArticleAttachment> list = forumArticleAttachmentService.findByArticleId(articleId);
            if (!list.isEmpty()) {
                detailVO.setAttachment(CopyTools.copy(list.get(0), ForumArticleAttachmentVO.class));
            }
        }
        return getSuccessResponseVo(detailVO);
    }

    @RequestMapping("/updateArticle")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO updateArticle(HttpSession session,
                                    MultipartFile cover,
                                    MultipartFile attachment,
                                    Integer integral,
                                    @VerifyParam(required = true) String articleId,
                                    @VerifyParam(required = true) String title,
                                    @VerifyParam(required = true) Integer pBoardId,
                                    Integer boardId,
                                    @VerifyParam(max = 200) String summary,
                                    @VerifyParam(required = true) Integer editorType,
                                    @VerifyParam(required = true) String content,
                                    String markdownContent,
                                    @VerifyParam Integer attachmentType) {
        title = StringUtils.escapeHtml(title);
        SessionWebUserDto userDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);

        ForumArticle forumArticle = new ForumArticle();
        forumArticle.setArticleId(articleId);
        forumArticle.setpBoardId(pBoardId);
        forumArticle.setBoardId(boardId);
        forumArticle.setTitle(title);
        forumArticle.setContent(content);
        forumArticle.setMarkdownContent(markdownContent);
        forumArticle.setEditorType(editorType);
        forumArticle.setSummary(summary);
        forumArticle.setUserIpAddress(userDto.getProvince());
        forumArticle.setAttachmentType(attachmentType);
        forumArticle.setUserId(userDto.getUserId());
        //附件信息
        ForumArticleAttachment forumArticleAttachment = new ForumArticleAttachment();
        forumArticleAttachment.setIntegral(integral == null ? 0 : integral);

        forumArticleService.updateArticle(userDto.getIsAdmin(), forumArticle, forumArticleAttachment, cover, attachment);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/search")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO search(Integer pageNo, @VerifyParam(required = true, min = 3) String keyword) {
        PaginationResultVO resultVO = forumArticleService.search(pageNo, keyword);
        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/delComment")
    @GlobalInterceptor(checkParams = true,checkLogin = true)
    public ResponseVO delComment(HttpSession session,@VerifyParam(required = true) String commentIds) {
        SessionWebUserDto userDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        String userId = userDto.getUserId();
        forumArticleService.delComment(userId,commentIds);
        return getSuccessResponseVo(null);
    }
}
