package com.easybbs.service.impl;

import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.po.Message;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.exception.BusinessException;
import com.easybbs.mapper.MessageMapper;
import com.easybbs.mapper.UserInfoMapper;
import com.easybbs.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/6/3 21:28
 * @description:
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public List<Message> loadMessageHistory(String senderId, String currentUserId) {
        //将当前用户与点击用户之间的消息，设置为当前用户已读
        messageMapper.updateStatusByCurrentId(senderId, currentUserId);
        return messageMapper.getMessageByReceiverIdAndSenderId(senderId, currentUserId);
    }

    @Override
    public String getLastMessage(String senderId, String receiverId) {
        return messageMapper.getLastMessage(senderId, receiverId);
    }

    @Override
    public List<Message> loadMessageList(String receiverId) {
        return messageMapper.getMessageByReceiverId(receiverId);
    }

    @Override
    public void sendMessage(Message message) {
        UserInfo sender = userInfoMapper.selectByPrimaryKey(Long.valueOf(message.getSenderId()));
        UserInfo receiver = userInfoMapper.selectByPrimaryKey(Long.valueOf(message.getReceiverId()));
        if (sender == null || receiver == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        messageMapper.insert(message);

    }

    @Override
    public Integer getNoReadCount(String senderId, String receiverId) {
        return messageMapper.getNoReadCount(senderId, receiverId);
    }

}
