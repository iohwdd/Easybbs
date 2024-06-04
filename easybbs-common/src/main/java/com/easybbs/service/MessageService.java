package com.easybbs.service;

import com.easybbs.entity.po.Message;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/6/3 21:27
 * @description:
 */
public interface MessageService {
    List<Message> loadMessageHistory(String senderId, String currentUserId);

    String getLastMessage(String senderId, String receiverId);

    List<Message> loadMessageList(String currentUserId);

    void sendMessage(Message message);

    Integer getNoReadCount(String senderId, String currentUserId);
}
