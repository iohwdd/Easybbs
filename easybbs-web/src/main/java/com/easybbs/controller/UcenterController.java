package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.annotation.VerifyParam;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.query.*;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.entity.vo.web.UserInfoVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.*;
import com.easybbs.utils.CopyTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: iohw
 * @date: 2024/5/23 19:03
 * @description:
 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController extends ABaseController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    ForumArticleService forumArticleService;
    @Autowired
    LikeRecordService likeRecordService;
    @Autowired
    ForumCommentService forumCommentService;

    @Autowired
    UserIntegralRecordService userIntegralRecordService;
    @Autowired
    UserMessageService userMessageService;

    @RequestMapping("/getUserInfo")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getUserInfo(@VerifyParam(required = true) String userId) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        if (null == userInfo || userInfo.getStatus().equals(UserStatusEnum.DISABLE.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        ForumArticleQuery query = new ForumArticleQuery();
        query.setUserId(userInfo.getUserId());
        query.setStatus(ArticleStatusEnum.NORMAL.getStatus());
        Integer postCount = forumArticleService.findCountByParam(query);

        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
        userInfoVO.setPostCount(postCount);

        LikeRecordQuery recordQuery = new LikeRecordQuery();
        recordQuery.setAuthorUserId(userId);
        Integer likeCount = likeRecordService.findCountByParam(recordQuery);
        userInfoVO.setLikeCount(likeCount);
        return getSuccessResponseVo(userInfoVO);
    }

    @RequestMapping("/loadUserArticle")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadUserArticle(@VerifyParam(required = true) String userId, Integer pageNo, Integer type) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        if (null == userInfo || userInfo.getStatus().equals(UserStatusEnum.DISABLE.getStatus())) {
            throw new BusinessException("用户不存在或者已被禁用");
        }
        ForumArticleQuery query = new ForumArticleQuery();
        if (type.equals(UserArticleTypeEnum.POST.getType())) {
            query.setUserId(userId);
            return getSuccessResponseVo(forumArticleService.findListByParam(query, pageNo));
        } else if (type.equals(UserArticleTypeEnum.COMMENT.getType())) {
            ForumCommentQuery commentQuery = new ForumCommentQuery();
            commentQuery.setUserId(userId);
            List<ForumComment> forumComment = forumCommentService.findListByParam(commentQuery);
            List<String> articleIdList = forumComment.stream().map(item -> {
                return item.getArticleId();
            }).distinct().collect(Collectors.toList());
            query.setArticleIdList(articleIdList);
            return getSuccessResponseVo(forumArticleService.findListByParam(query, pageNo));
        } else if (type.equals(UserArticleTypeEnum.LIKE.getType())) {
            LikeRecordQuery recordQuery = new LikeRecordQuery();
            recordQuery.setUserId(userId);
            recordQuery.setOpType(OperRecordTypeEnum.ARTICLE_LIKE.getType());
            List<LikeRecord> recordList = likeRecordService.findListByParam(recordQuery);
            List<String> articleIdList = recordList.stream().map(item -> {
                return item.getObjectId();
            }).distinct().collect(Collectors.toList());
            query.setArticleIdList(articleIdList);
            return getSuccessResponseVo(forumArticleService.findListByParam(query, pageNo));
        } else {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

    @RequestMapping("/loadUserIntegralRecord")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadUseloadUserIntegralRecordrArticle(HttpSession session, Integer pageNo, String createTimeStart, String createTimeEnd) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        String userId = sessionWebUserDto.getUserId();
        UserIntegralRecordQuery query = new UserIntegralRecordQuery();
        query.setUserId(userId);
        query.setCreateTimeStart(createTimeStart);
        query.setCreateTimeEnd(createTimeEnd);
        return getSuccessResponseVo(userIntegralRecordService.findByParam(pageNo, query));
    }

    @RequestMapping("/updateUserInfo")
    //性别 0:女 1:男
    public ResponseVO getUserInfo(HttpSession session, Integer sex, String personDescription, MultipartFile avatar) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(sessionWebUserDto.getUserId());
        userInfo.setSex(sex);
        userInfo.setPersonDescription(personDescription);
        userInfoService.update(userInfo, avatar);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/getMessageCount")
    public ResponseVO getMessageCount(HttpSession session) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        String userId = sessionWebUserDto.getUserId();
        return getSuccessResponseVo(userMessageService.getMessageCount(userId));
    }

    @RequestMapping("/loadMessageList")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadMessageList(HttpSession session, @VerifyParam(required = true) String code, Integer pageNo) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        String userId = sessionWebUserDto.getUserId();
        UserMessageQuery query = new UserMessageQuery();
        if (MessageTypeEnum.getByCode(code) == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        query.setMessageType(MessageTypeEnum.getByCode(code).getType());
        query.setReceivedUserId(userId);

        PaginationResultVO resultVO = userMessageService.loadMessageList(query, pageNo);
        return getSuccessResponseVo(resultVO);
    }
}
