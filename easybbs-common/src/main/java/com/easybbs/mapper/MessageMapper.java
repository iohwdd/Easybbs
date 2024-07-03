package com.easybbs.mapper;

import com.easybbs.entity.po.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/6/3 21:29
 * @description:
 */
public interface MessageMapper {
    List<Message> getMessageByReceiverIdAndSenderId(@Param("senderId") String senderId, @Param("receiverId") String receiverId);

    String getLastMessage(@Param("senderId") String senderId, @Param("receiverId") String receiverId);

    List<Message> getMessageByReceiverId(@Param("receiverId") String receiverId);

    void insert(@Param("message") Message message);

    void updateStatusByCurrentId(@Param("senderId") String senderId, @Param("currentUserId") String currentUserId);

    Integer getNoReadCount(@Param("senderId") String senderId, @Param("receiverId") String receiverId);
}
