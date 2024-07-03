package com.easybbs.service.impl;

import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.MessageStatusEnum;
import com.easybbs.entity.enums.MessageTypeEnum;
import com.easybbs.entity.enums.UserIntegralChangeTypeEnum;
import com.easybbs.entity.enums.UserIntegralOperTypeEnum;
import com.easybbs.entity.po.*;
import com.easybbs.exception.BusinessException;
import com.easybbs.mapper.*;
import com.easybbs.service.ForumArticleAttachmentService;
import com.easybbs.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/15 16:42
 * @description:
 */
@Service
public class ForumArticleAttachmentServiceImpl implements ForumArticleAttachmentService {
    @Autowired
    ForumArticleAttachmentMapper forumArticleAttachmentMapper;
    @Autowired
    ForumArticleAttachmentDownloadMapper forumArticleAttachmentDownloadMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    ForumArticleMapper forumArticleMapper;
    @Autowired
    UserMessageMapper userMessageMapper;
    @Autowired
    UserIntegralRecordMapper userIntegralRecordMapper;

    @Override
    public List<ForumArticleAttachment> findByArticleId(String articleId) {
        return forumArticleAttachmentMapper.selectByArticleId(articleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ForumArticleAttachment downloadAttachment(String fileId, SessionWebUserDto sessionWebUserDto) {
        ForumArticleAttachment attachment = forumArticleAttachmentMapper.selectByFileId(fileId);
        if (attachment == null) {
            throw new BusinessException("附件不存在");
        }
        ForumArticleAttachmentDownload download = null;
        if (attachment.getIntegral() > 0 && !sessionWebUserDto.getUserId().equals(attachment.getUserId())) {
            download = forumArticleAttachmentDownloadMapper.selectByFileIdAndUserId(fileId, sessionWebUserDto.getUserId());
            if (download == null) {
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.valueOf(sessionWebUserDto.getUserId()));
                if (userInfo.getCurrentIntegral() - attachment.getIntegral() < 0) {
                    throw new BusinessException("积分不够");
                }
            }
        }
        // 下载信息只用一条，多次下载更新下载次数
        ForumArticleAttachmentDownload updateDownload = new ForumArticleAttachmentDownload();
        updateDownload.setArticleId(attachment.getArticleId());
        updateDownload.setFileId(attachment.getFileId());
        updateDownload.setUserId(sessionWebUserDto.getUserId());
        updateDownload.setDownloadCount(1);// 没下过 ——> 插入信息的下载次数为1； 下过 ——> 更新该条信息下载次数+1
        forumArticleAttachmentDownloadMapper.insertOrUpdate(updateDownload);

        // 更新附件下载次数
        forumArticleAttachmentMapper.updateDownloadCount(fileId);

        //作者自己下载 或者 用户已经下载过了 就不用发消息与更新积分了
        if (sessionWebUserDto.getUserId().equals(attachment.getUserId()) || download != null) {
            return attachment;
        }

        // 扣下载人的积分
        UserIntegralRecord record = new UserIntegralRecord();
        userInfoService.updateUserIntegral(sessionWebUserDto.getUserId(), UserIntegralOperTypeEnum.USER_DOWNLOAD_ATTACHMENT,
                UserIntegralChangeTypeEnum.REDUCE.getChangeType(), attachment.getIntegral());

        // 给作者加积分
        userInfoService.updateUserIntegral(attachment.getUserId(), UserIntegralOperTypeEnum.USER_DOWNLOAD_ATTACHMENT,
                UserIntegralChangeTypeEnum.ADD.getChangeType(), attachment.getIntegral());
        UserIntegralRecord record1 = new UserIntegralRecord();

        //记录消息
        ForumArticle forumArticle = forumArticleMapper.getForumArticleByArticleId(attachment.getArticleId());
        UserMessage userMessage = new UserMessage();
        userMessage.setArticleId(attachment.getArticleId());
        userMessage.setReceivedUserId(attachment.getUserId());
        userMessage.setSendUserId(sessionWebUserDto.getUserId());
        userMessage.setSendNickName(sessionWebUserDto.getNickName());
        userMessage.setArticleTitle(forumArticle.getTitle());
        userMessage.setArticleId(forumArticle.getArticleId());
        userMessage.setCommentId(0);
        userMessage.setCreateTime(new Date());
        userMessage.setMessageType(MessageTypeEnum.DOWNLOAD_ATTACHMENT.getType());
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        userMessageMapper.insert(userMessage);
        return attachment;
    }

    @Override
    public ForumArticleAttachment downloadAttachment4Admin(String fileId) {
        ForumArticleAttachment attachment = forumArticleAttachmentMapper.selectByFileId(fileId);
        if (attachment == null) {
            throw new BusinessException("附件不存在");
        }
        return attachment;
    }
}
