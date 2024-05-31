package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.annotation.VerifyParam;
import com.easybbs.entity.enums.MessageStatusEnum;
import com.easybbs.entity.enums.MessageTypeEnum;
import com.easybbs.entity.enums.UserIntegralChangeTypeEnum;
import com.easybbs.entity.enums.UserIntegralOperTypeEnum;
import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.UserInfoQuery;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.service.UserInfoService;
import com.easybbs.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: iohw
 * @date: 2024/5/28 20:23
 * @description:
 */
@RestController
@RequestMapping("/user")
public class UcenterController extends ABaseController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserMessageService userMessageService;

    @RequestMapping("/loadUserList")
    public ResponseVO loadUserList(UserInfoQuery query) {
        return getSuccessResponseVo(userInfoService.getUserByParam(query));
    }

    @RequestMapping("/updateUserStatus")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO updateUserStatus(UserInfoQuery query) {
        userInfoService.updateUserByParam(query);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/sendMessage")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO sendMessage(@VerifyParam(required = true) String userId, @VerifyParam(required = true) String message, Integer integral) {
        UserMessage sysMsg = new UserMessage();
        sysMsg.setReceivedUserId(userId);
        sysMsg.setMessageContent(message);
        sysMsg.setMessageType(MessageTypeEnum.SYS.getType());
        sysMsg.setCreateTime(new Date());
        sysMsg.setStatus(MessageStatusEnum.NO_READ.getStatus());
        userMessageService.sendSysMsg(sysMsg);
        if (integral != null) {
            userInfoService.updateUserIntegral(userId, UserIntegralOperTypeEnum.ADMIN, UserIntegralChangeTypeEnum.ADD.getChangeType(), integral);
        }
        return getSuccessResponseVo(null);
    }
}
