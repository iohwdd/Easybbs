package com.easybbs.mapper;

import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.UserMessageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【user_message(用户消息)】的数据库操作Mapper
 * @createDate 2024-05-09 16:56:17
 * @Entity com.easybbs.entity.po.UserMessage
 */
//@Mapper
public interface UserMessageMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserMessage record);

    int insertSelective(UserMessage record);

    UserMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserMessage record);

    int updateByPrimaryKey(UserMessage record);

    UserMessage selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(@Param("articleId") String articleId, @Param("commentId") Integer commentId, @Param("sendUserId") String sendUserId, @Param("messageType") Integer messageType);

    Integer getCountByParam(@Param("query") UserMessageQuery query);

    List<UserMessage> getListByParam(@Param("query") UserMessageQuery query);

    void updateByReceivedUserIdAndMessageType(@Param("query") UserMessageQuery query);
}
