package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.annotation.VerifyParam;
import com.easybbs.config.WebConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.ArticleStatusEnum;
import com.easybbs.entity.enums.CommentStatusEnum;
import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.po.ForumArticleAttachment;
import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.query.ForumBoardQuery;
import com.easybbs.entity.query.ForumCommentQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.ForumArticleAttachmentService;
import com.easybbs.service.ForumArticleService;
import com.easybbs.service.ForumCommentService;
import com.easybbs.utils.SysCacheUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/27 20:06
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
    private WebConfig webConfig;
    @Autowired
    private ForumCommentService forumCommentService;

    @RequestMapping("/loadArticle")
    public ResponseVO loadArticle(ForumArticleQuery query, Integer pageNo) {
        return getSuccessResponseVo(forumArticleService.findListByParam(query, pageNo));
    }

    @RequestMapping("/delArticle")
    public ResponseVO delArticle(String articleIds) {
        String[] ids = articleIds.split(",");
        forumArticleService.delByArticleId(ids);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/getAttachment")
    public ResponseVO getAttachment(String articleId) {
        return getSuccessResponseVo(forumArticleAttachmentService.findByArticleId(articleId).get(0));
    }

    @RequestMapping("/attachmentDownload")
    @GlobalInterceptor(checkParams = true)
    public void attachmentDownload(HttpServletRequest request, HttpServletResponse response, @VerifyParam(required = true) String fileId) {
        ForumArticleAttachment attachment = forumArticleAttachmentService.downloadAttachment4Admin(fileId);
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

    @RequestMapping("/auditArticle")
    public ResponseVO auditArticle(String articleIds) {
        ForumArticleQuery query = new ForumArticleQuery();
        query.setArticleIdList(Arrays.asList(articleIds.split(",")));
        query.setStatus(ArticleStatusEnum.NORMAL.getStatus());
        forumArticleService.updateArticleByParam(query);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/loadComment4Article")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadComment4Article(Integer pageNo, Integer pageSize, @VerifyParam(required = true) String articleId) {
        final String ORDER_TYPE = "comment_id desc";//最新(comment_id为自增)
        if (!SysCacheUtils.getSysSetting().getCommentSetting().getCommentOpen()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        ForumCommentQuery forumCommentQuery = new ForumCommentQuery();
        forumCommentQuery.setArticleId(articleId);
        forumCommentQuery.setOrderBy(ORDER_TYPE);//评论排序规则
        forumCommentQuery.setpCommentId(0);
        forumCommentQuery.setPageNo(pageNo);
        forumCommentQuery.setPageSize(pageSize == null ? PageSize.SIZE50.getSize() : pageSize);
        forumCommentQuery.setLoadChildren(true);//是否加载二级评论
        return getSuccessResponseVo(forumCommentService.findComment(forumCommentQuery));
    }

    @RequestMapping("/loadComment")
    public ResponseVO loadComment(ForumCommentQuery query) {
        PageHelper.startPage(query.getPageNo() == null ? 1 : query.getPageNo(), query.getPageSize() == null ? PageSize.SIZE50.getSize() : query.getPageSize());
        List<ForumComment> list = forumCommentService.findListByParam(query);
        PaginationResultVO<ForumComment> resultVO = new PaginationResultVO<>();
        resultVO.setList(list);
        PageInfo<ForumComment> pageInfo = new PageInfo<>(list);
        resultVO.setTotalCount((int) pageInfo.getTotal());
        resultVO.setPageNo(pageInfo.getPageNum());
        resultVO.setPageSize(pageInfo.getPageSize());
        resultVO.setPageTotal(pageInfo.getPages());
        return getSuccessResponseVo(resultVO);
    }

    @RequestMapping("/delComment")
    public ResponseVO delComment(String commentIds) {
        forumCommentService.delByCommentIds(commentIds.split(","));
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/auditComment")
    public ResponseVO auditComment(String commentIds) {
        ForumCommentQuery query = new ForumCommentQuery();
        query.setCommentIdList(Arrays.asList(commentIds.split(",")));
        query.setStatus(CommentStatusEnum.NORMAL.getType());
        forumCommentService.updateByParam(query);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/topArticle")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO topArticle(@VerifyParam(required = true) String articleId, @VerifyParam(required = true) Integer topType) {
        ForumArticleQuery query = new ForumArticleQuery();
        query.setArticleId(articleId);
        query.setTopType(topType);
        forumArticleService.updateArticleByParam(query);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/updateBoard")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO updateBoard(ForumArticleQuery query) {
        forumArticleService.updateArticleByParam(query);
        return getSuccessResponseVo(null);
    }
}
