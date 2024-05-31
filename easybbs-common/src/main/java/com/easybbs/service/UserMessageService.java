package com.easybbs.service;

import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.UserMessageQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.web.UserMessageCountVO;

/**
 * @author: iohw
 * @date: 2024/5/24 14:53
 * @description:
 */
public interface UserMessageService {
    UserMessageCountVO getMessageCount(String userId);

    PaginationResultVO loadMessageList(UserMessageQuery query, Integer pageNo);

    void sendSysMsg(UserMessage message);
}
