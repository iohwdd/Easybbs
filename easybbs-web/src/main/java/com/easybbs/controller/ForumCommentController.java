package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.annotation.VerifyParam;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.query.ForumCommentQuery;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.ForumCommentService;
import com.easybbs.service.LikeRecordService;
import com.easybbs.utils.FileUtils;
import com.easybbs.utils.SysCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/16 20:25
 * @description:
 */
@RestController
@RequestMapping("/comment")
public class ForumCommentController extends ABaseController {
    @Autowired
    private ForumCommentService forumCommentService;
    @Autowired
    private LikeRecordService likeRecordService;


    @RequestMapping("/loadComment")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadComment(HttpSession session, @VerifyParam(required = true) String articleId, Integer pageNo, Integer orderType) {
        final String ORDER_TYPE0 = "top_type desc,good_count desc,comment_id asc";//热榜
        final String ORDER_TYPE1 = "comment_id desc";//最新(comment_id为自增)
        if (!SysCacheUtils.getSysSetting().getCommentSetting().getCommentOpen()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        ForumCommentQuery forumCommentQuery = new ForumCommentQuery();
        forumCommentQuery.setArticleId(articleId);
        String orderBy = orderType == null || orderType == 0 ? ORDER_TYPE0 : ORDER_TYPE1;
        forumCommentQuery.setOrderBy(orderBy);//评论排序规则
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        if (sessionWebUserDto != null) {
            forumCommentQuery.setCurrentUserId(sessionWebUserDto.getUserId());//当前登录用户id
            forumCommentQuery.setQueryLikeType(1);//查询登录用户是否对评论点赞
        }
        forumCommentQuery.setpCommentId(0);
        forumCommentQuery.setPageNo(pageNo);
        forumCommentQuery.setPageSize(PageSize.SIZE50.getSize());
        forumCommentQuery.setLoadChildren(true);//是否加载二级评论
        return getSuccessResponseVo(forumCommentService.findComment(forumCommentQuery));
    }

    @RequestMapping("/postComment")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO postComment(
            HttpSession session,
            @VerifyParam(required = true) String articleId,
            @VerifyParam(required = true) String pCommentId,
            @VerifyParam(max = 800) String content,
            MultipartFile image,
            String replyUserId) {
        if (!SysCacheUtils.getSysSetting().getCommentSetting().getCommentOpen()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (image == null && content == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        SessionWebUserDto userDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        ForumComment forumComment = new ForumComment();
        forumComment.setArticleId(articleId);
        forumComment.setpCommentId(Integer.valueOf(pCommentId));
        forumComment.setNickName(userDto.getNickName());
        forumComment.setUserId(userDto.getUserId());
        forumComment.setUserIpAddress(userDto.getProvince());
        forumComment.setContent(content);
        forumComment.setReplyUserId(replyUserId);
        forumComment.setTopType(CommentTopTypeEnum.NO_TOP.getType());

        forumCommentService.postComment(forumComment, image);

        if (forumComment.getpCommentId() != 0) {
            // 发的二级评论则要返回当前一级评论的所有二级评论
            ForumCommentQuery query = new ForumCommentQuery();
            query.setArticleId(articleId);
            query.setpCommentId(forumComment.getpCommentId());
            query.setOrderBy("comment_id desc");
            List<ForumComment> children = forumCommentService.findListByParam(query);
            return getSuccessResponseVo(children);
        }
        return getSuccessResponseVo(forumComment);
    }

    @RequestMapping("/doLike")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO doLike(HttpSession session, @VerifyParam(required = true) String commentId) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        likeRecordService.doLike(commentId, sessionWebUserDto.getUserId(), sessionWebUserDto.getNickName(), OperRecordTypeEnum.COMMENT_LIKE);
        //设置状态 已点赞/未点赞
        LikeRecord likeRecord = likeRecordService.getLikeRecordByObjectIdAndUserIdAndOpType(commentId, sessionWebUserDto.getUserId(), OperRecordTypeEnum.COMMENT_LIKE.getType());
        ForumComment forumComment = forumCommentService.getByCommentId(commentId);
        forumComment.setLikeType(likeRecord == null ? 0 : 1);
        return getSuccessResponseVo(forumComment);
    }

    @RequestMapping("/changeTopType")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO changeTopType(HttpSession session, @VerifyParam(required = true) String commentId, @VerifyParam(required = true) Integer topType) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        forumCommentService.changeTopType(sessionWebUserDto.getUserId(), commentId, topType);
        return getSuccessResponseVo(null);
    }
}
