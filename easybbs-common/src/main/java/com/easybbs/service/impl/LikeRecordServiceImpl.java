package com.easybbs.service.impl;

import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.po.LikeRecord;
import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.LikeRecordQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mapper.ForumArticleMapper;
import com.easybbs.mapper.ForumCommentMapper;
import com.easybbs.mapper.LikeRecordMapper;
import com.easybbs.mapper.UserMessageMapper;
import com.easybbs.service.LikeRecordService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/15 19:47
 * @description:
 */
@Service
public class LikeRecordServiceImpl implements LikeRecordService {
    @Autowired
    LikeRecordMapper likeRecordMapper;
    @Autowired
    UserMessageMapper userMessageMapper;
    @Autowired
    ForumArticleMapper forumArticleMapper;
    @Autowired
    ForumCommentMapper forumCommentMapper;

    @Override
    public LikeRecord getLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
        return likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doLike(String objectId, String userId, String nickName, OperRecordTypeEnum operRecordTypeEnum) {
        UserMessage userMessage = new UserMessage();
        userMessage.setCreateTime(new Date());
        switch (operRecordTypeEnum) {
            case ARTICLE_LIKE:
                ForumArticle forumArticle = forumArticleMapper.selectByPrimaryKey(objectId);
                if (forumArticle == null) {
                    throw new BusinessException("文章不存在");
                }
                articleLike(objectId, forumArticle, userId, operRecordTypeEnum);
                userMessage.setArticleId(objectId);
                userMessage.setArticleTitle(forumArticle.getTitle());
                userMessage.setMessageType(MessageTypeEnum.ARTICLE_LIKE.getType());
                userMessage.setCommentId(Constants.ZERO);
                userMessage.setReceivedUserId(forumArticle.getUserId());
                break;
            case COMMENT_LIKE:
                ForumComment forumComment = forumCommentMapper.selectByPrimaryKey(Long.valueOf(objectId));
                if (forumComment == null) {
                    throw new BusinessException("评论不存在");
                }
                commentLike(objectId, forumComment, userId, operRecordTypeEnum);
                forumArticle = forumArticleMapper.selectByPrimaryKey(forumComment.getArticleId());
                userMessage.setArticleId(forumArticle.getArticleId());
                userMessage.setArticleTitle(forumArticle.getTitle());
                userMessage.setMessageType(MessageTypeEnum.COMMENT_LIKE.getType());
                userMessage.setCommentId(forumComment.getCommentId());
                userMessage.setReceivedUserId(forumComment.getUserId());
                userMessage.setMessageContent(forumComment.getContent());
                break;
        }
        userMessage.setSendUserId(userId);
        userMessage.setSendNickName(nickName);
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        if (!userId.equals(userMessage.getReceivedUserId())) {
            // 点赞再取消 用户仍会收到消息。相同消息不重复发。
            UserMessage msg = userMessageMapper.selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(userMessage.getArticleId(), userMessage.getCommentId(), userMessage.getSendUserId(), userMessage.getMessageType());
            if (msg == null) {
                userMessageMapper.insert(userMessage);
            }
        }
    }

    @Override
    public Integer findCountByParam(LikeRecordQuery recordQuery) {
        return likeRecordMapper.findCountByParam(recordQuery);
    }

    @Override
    public List<LikeRecord> findListByParam(LikeRecordQuery recordQuery) {
        return likeRecordMapper.findListByParam(recordQuery);
    }


    private void articleLike(String objectId, ForumArticle forumArticle, String userId, OperRecordTypeEnum operRecordTypeEnum) {
        LikeRecord record = likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, operRecordTypeEnum.getType());
        if (record != null) {
            likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, operRecordTypeEnum.getType());
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), -1, objectId);
        } else {
            LikeRecord likeRecord = new LikeRecord();
            likeRecord.setObjectId(objectId);
            likeRecord.setUserId(userId);
            likeRecord.setOpType(operRecordTypeEnum.getType());
            likeRecord.setCreateTime(new Date());
            likeRecord.setAuthorUserId(forumArticle.getUserId());
            likeRecordMapper.insert(likeRecord);
            forumArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), Constants.ONE, objectId);
        }
    }

    private void commentLike(String objectId, ForumComment forumComment, String userId, OperRecordTypeEnum operRecordTypeEnum) {
        LikeRecord record = likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, operRecordTypeEnum.getType());
        if (record != null) {
            likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, operRecordTypeEnum.getType());//已经点赞过，取消点赞 -> 删除点赞记录
            forumCommentMapper.updateCommentGoodCount(-1, objectId);
        } else {
            LikeRecord likeRecord = new LikeRecord();
            likeRecord.setObjectId(objectId);
            likeRecord.setUserId(userId);
            likeRecord.setOpType(operRecordTypeEnum.getType());
            likeRecord.setCreateTime(new Date());
            likeRecord.setAuthorUserId(forumComment.getUserId());
            likeRecordMapper.insert(likeRecord);
            forumCommentMapper.updateCommentGoodCount(1, objectId);
        }
    }

}
