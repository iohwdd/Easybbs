package com.easybbs.service.impl;

import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.*;
import com.easybbs.entity.query.ForumCommentQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mapper.ForumArticleMapper;
import com.easybbs.mapper.ForumCommentMapper;
import com.easybbs.mapper.UserInfoMapper;
import com.easybbs.mapper.UserMessageMapper;
import com.easybbs.service.ForumCommentService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.FileUtils;
import com.easybbs.utils.StringUtils;
import com.easybbs.utils.SysCacheUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: iohw
 * @date: 2024/5/16 20:32
 * @description:
 */
@Service
public class ForumCommentServiceImpl implements ForumCommentService {
    @Autowired
    ForumCommentMapper forumCommentMapper;
    @Autowired
    ForumArticleMapper forumArticleMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserMessageMapper userMessageMapper;
    @Autowired
    private FileUtils fileUtils;

    @Override
    public PaginationResultVO findComment(ForumCommentQuery params) {
        PaginationResultVO paginationResultVO = new PaginationResultVO<>();
        PageHelper.startPage(params.getPageNo() == null ? 0 : params.getPageNo(), params.getPageSize());
        List<ForumComment> list = forumCommentMapper.selectList(params); //一级评论

        if (params.getLoadChildren() != null && params.getLoadChildren()) {
            //二级评论
            ForumCommentQuery subQuery = new ForumCommentQuery();
            subQuery.setArticleId(params.getArticleId());
            subQuery.setQueryLikeType(params.getQueryLikeType());
            subQuery.setCurrentUserId(params.getCurrentUserId());
            subQuery.setLoadChildren(params.getLoadChildren());
            subQuery.setStatus(params.getStatus());
            List<Integer> pcommentList = list.stream().map(ForumComment::getCommentId).distinct().collect(Collectors.toList());
            subQuery.setPcommentList(pcommentList);
            List<ForumComment> subCommentList = forumCommentMapper.selectList(subQuery);

            Map<Integer, List<ForumComment>> tempMap = subCommentList.stream().collect(Collectors.groupingBy(ForumComment::getpCommentId));
            list.forEach(item -> {
                item.setChildren(tempMap.get(item.getCommentId()));
            });
        }

        PageInfo pageInfo = new PageInfo<>(list);
        paginationResultVO.setPageNo(pageInfo.getPageNum());
        paginationResultVO.setPageTotal(pageInfo.getPages());
        paginationResultVO.setTotalCount((int) pageInfo.getTotal());
        paginationResultVO.setList(list);
        paginationResultVO.setPageSize(pageInfo.getPageSize());
        return paginationResultVO;
    }

    @Override
    public ForumComment getByCommentId(String commentId) {
        return forumCommentMapper.selectByPrimaryKey(Long.valueOf(commentId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeTopType(String userId, String commentId, Integer topType) {
        CommentTopTypeEnum topTypeEnum = CommentTopTypeEnum.getByType(topType);
        if (topTypeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        ForumComment forumComment = forumCommentMapper.selectByPrimaryKey(Long.valueOf(commentId));
        if (forumComment == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        ForumArticle forumArticle = forumArticleMapper.selectByPrimaryKey(forumComment.getArticleId());
        if (forumArticle == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!forumArticle.getUserId().equals(userId) || forumComment.getpCommentId() != 0) {
            //非作者或一级评论不可置顶
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (forumComment.getTopType().equals(topType)) {
            return;
        }
        if (topType.equals(CommentTopTypeEnum.TOP.getType())) {
            //如果要设为置顶，则先需要把其它所有的评论设置为未置顶
            forumCommentMapper.updateTopTypeByArticleId(forumArticle.getArticleId());
        }
        ForumComment updateInfo = new ForumComment();
        updateInfo.setCommentId(Integer.valueOf(commentId));
        updateInfo.setTopType(topType);
        forumCommentMapper.updateByPrimaryKeySelective(updateInfo);
    }

    @Override
    public List<ForumComment> findListByParam(ForumCommentQuery query) {
        return forumCommentMapper.selectList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postComment(ForumComment comment, MultipartFile image) {
        ForumArticle forumArticle = forumArticleMapper.getForumArticleByArticleId(comment.getArticleId());
        if (forumArticle == null || forumArticle.getStatus().equals(ArticleStatusEnum.AUDIT.getStatus())) {
            throw new BusinessException("回复的文章不存在");
        }
        ForumComment pComment = null;
        if (comment.getpCommentId() != 0) {
            pComment = forumCommentMapper.selectByPrimaryKey(Long.valueOf(comment.getpCommentId()));
            if (pComment == null) {
                throw new BusinessException("回复的评论不存在");
            }
        }
        //判断回复的用户是否存在
        if (!StringUtils.isEmpty(comment.getReplyUserId())) {
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.valueOf(comment.getReplyUserId()));
            if (userInfo == null) {
                throw new BusinessException("回复的用户不存在");
            }
            comment.setReplyNickName(userInfo.getNickName());
        }
        comment.setPostTime(new Date());
        if (image != null) {
            FileUploadDto uploadDto = fileUtils.uploadFile2Local(image, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.COMMENT_IMAGE);
            comment.setImgPath(uploadDto.getLocalPath());
        }
        Boolean needAudit = SysCacheUtils.getSysSetting().getAuditSetting().getCommentAudit();
        comment.setStatus(needAudit ? CommentStatusEnum.NO_AUDIT.getType() : CommentStatusEnum.NORMAL.getType());
        comment.setGoodCount(0);
        forumCommentMapper.insert(comment);

        if (needAudit) {
            return;
        }
        updateCommentInfo(comment, forumArticle, pComment);
    }

    @Override
    public void delByCommentIds(String[] commentIds) {
        forumCommentMapper.delByCommentIds(commentIds);
    }

    @Override
    public void updateByParam(ForumCommentQuery query) {
        forumCommentMapper.updateByParam(query);
    }

    public void updateCommentInfo(ForumComment comment, ForumArticle forumArticle, ForumComment pComment) {
        //评论可以获得的积分数量
        Integer integral = SysCacheUtils.getSysSetting().getCommentSetting().getCommentIntegral();
        if (integral > 0) {
            this.userInfoService.updateUserIntegral(comment.getUserId(), UserIntegralOperTypeEnum.POST_COMMENT,
                    UserIntegralChangeTypeEnum.ADD.getChangeType(), integral);
        }

        if (comment.getpCommentId() == 0) {
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.COMMENT_COUNT.getType(), Constants.ONE, comment.getArticleId());
        }

        //记录消息
        UserMessage userMessage = new UserMessage();
        userMessage.setMessageType(MessageTypeEnum.COMMENT.getType());
        userMessage.setArticleTitle(forumArticle.getTitle());
        userMessage.setMessageContent(comment.getContent());
        userMessage.setCreateTime(new Date());
        userMessage.setArticleId(comment.getArticleId());
        userMessage.setCommentId(comment.getCommentId());
        userMessage.setSendUserId(comment.getUserId());
        userMessage.setSendNickName(comment.getNickName());
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        userMessage.setpCommentId(comment.getpCommentId());
        if (comment.getpCommentId() != 0) {
            userMessage.setpCommentContent(pComment.getContent());
        }

        if (comment.getpCommentId() == 0) {
            userMessage.setReceivedUserId(forumArticle.getUserId());
        } else if (StringUtils.isEmpty(comment.getReplyUserId())) {
            //回复一级评论的评论
            userMessage.setReceivedUserId(pComment.getUserId());
        } else if (!StringUtils.isEmpty(comment.getReplyUserId())) {
            //回复二级评论的评论
            userMessage.setReceivedUserId(comment.getReplyUserId());
        }
        if (!comment.getUserId().equals(userMessage.getReceivedUserId())) {
            userMessageMapper.insert(userMessage);
        }
    }

}
