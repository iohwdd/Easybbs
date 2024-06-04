package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.annotation.VerifyParam;
import com.easybbs.entity.po.Message;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.entity.vo.web.UserMsgVO;
import com.easybbs.service.MessageService;
import com.easybbs.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: iohw
 * @date: 2024/6/3 21:23
 * @description:
 */
@RestController
@RequestMapping("/chart")
public class MessageController extends ABaseController {
    @Autowired
    MessageService messageService;
    @Autowired
    UserInfoService userInfoService;

    @RequestMapping("/loadMessageHistory")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO loadMessageHistory(@VerifyParam(required = true) String senderId, @VerifyParam(required = true) String receiverId) {

        return getSuccessResponseVo(messageService.loadMessageHistory(senderId, receiverId));
    }

    @RequestMapping("/loadUsers")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO loadUsers(@VerifyParam(required = true) String currentUserId) {
        List<Message> list = messageService.loadMessageList(currentUserId);
        List<String> senderIdList = list.stream().map(Message::getSenderId).collect(Collectors.toList());
        List<UserMsgVO> userList = userInfoService.getUserListById(senderIdList);
        userList = userList.stream().map(item -> {
            //获取最后一条消息
            String lastMessage = messageService.getLastMessage(item.getUserId(), currentUserId);
            item.setLastMessage(lastMessage);
            Integer noReadCount = messageService.getNoReadCount(item.getUserId(), currentUserId);
            item.setNoReadCount(noReadCount);
            return item;
        }).collect(Collectors.toList());


        return getSuccessResponseVo(userList);
    }

    @RequestMapping("/sendMessage")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO sendMessage(@VerifyParam(required = true) String senderId, @VerifyParam(required = true) String receiverId,
                                  @VerifyParam(required = true) String text) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setText(text);
        message.setSenderStatus(1);
        message.setReceiverStatus(0);
        messageService.sendMessage(message);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/getChartMsgCount")
    @GlobalInterceptor(checkLogin = true, checkParams = true)
    public ResponseVO getChartMsgCount(@VerifyParam(required = true) String currentUserId) {
        Integer count = 0;
        List<Message> list = messageService.loadMessageList(currentUserId);
        List<String> senderIdList = list.stream().map(Message::getSenderId).collect(Collectors.toList());
        List<UserMsgVO> userList = userInfoService.getUserListById(senderIdList);
        for (UserMsgVO user : userList) {
            Integer noReadCount = messageService.getNoReadCount(user.getUserId(), currentUserId);
            count += noReadCount;
        }
        return getSuccessResponseVo(count);
    }
}
