package com.easybbs.service.impl;

import com.easybbs.entity.enums.MessageStatusEnum;
import com.easybbs.entity.enums.MessageTypeEnum;
import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.UserMessageQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.web.UserMessageCountVO;
import com.easybbs.mapper.UserMessageMapper;
import com.easybbs.service.UserMessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/24 14:53
 * @description:
 */
@Service
public class UserMessageServiceImpl implements UserMessageService {
    @Autowired
    UserMessageMapper userMessageMapper;

    @Override
    public UserMessageCountVO getMessageCount(String userId) {
        UserMessageQuery query = new UserMessageQuery();
        query.setReceivedUserId(userId);
        query.setStatus(MessageStatusEnum.NO_READ.getStatus());
        query.setMessageType(MessageTypeEnum.SYS.getType());
        Integer sys = userMessageMapper.getCountByParam(query);
        query.setMessageType(MessageTypeEnum.COMMENT.getType());
        Integer reply = userMessageMapper.getCountByParam(query);
        query.setMessageType(MessageTypeEnum.ARTICLE_LIKE.getType());
        Integer likePost = userMessageMapper.getCountByParam(query);
        query.setMessageType(MessageTypeEnum.COMMENT_LIKE.getType());
        Integer likeComment = userMessageMapper.getCountByParam(query);
        query.setMessageType(MessageTypeEnum.DOWNLOAD_ATTACHMENT.getType());
        Integer downloadAttachment = userMessageMapper.getCountByParam(query);
        UserMessageCountVO messageCountVO = new UserMessageCountVO();
        messageCountVO.setSys(sys);
        messageCountVO.setReply(reply);
        messageCountVO.setLikePost(likePost);
        messageCountVO.setLikeComment(likeComment);
        messageCountVO.setDownloadAttachment(downloadAttachment);
        messageCountVO.setTotal(sys + reply + likePost + likeComment + downloadAttachment);
        return messageCountVO;
    }

    @Override
    public PaginationResultVO loadMessageList(UserMessageQuery query, Integer pageNo) {
        pageNo = pageNo == null ? 1 : pageNo;
        PageHelper.startPage(pageNo, PageSize.SIZE50.getSize());
        List<UserMessage> list = userMessageMapper.getListByParam(query);
        PageInfo<UserMessage> pageInfo = new PageInfo<>(list);
        PaginationResultVO<UserMessage> resultVO = new PaginationResultVO<>();
        resultVO.setList(list);
        resultVO.setPageSize(pageInfo.getPageSize());
        resultVO.setTotalCount((int) pageInfo.getTotal());
        resultVO.setPageNo(pageNo);
        resultVO.setPageTotal(pageInfo.getPages());

        //将消息设置成已读
        query.setStatus(MessageStatusEnum.READ.getStatus());
        userMessageMapper.updateByReceivedUserIdAndMessageType(query);
        return resultVO;
    }

    @Override
    public void sendSysMsg(UserMessage message) {
        userMessageMapper.insert(message);
    }
}
