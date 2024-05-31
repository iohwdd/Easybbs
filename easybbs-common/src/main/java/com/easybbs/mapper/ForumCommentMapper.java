package com.easybbs.mapper;


import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.query.ForumCommentQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【forum_comment(评论)】的数据库操作Mapper
 * @createDate 2024-05-09 16:56:17
 * @Entity com.easybbs.entity.po.ForumComment
 */
//@Mapper
public interface ForumCommentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ForumComment record);

    int insertSelective(ForumComment record);

    ForumComment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ForumComment record);

    int updateByPrimaryKey(ForumComment record);

    List<ForumComment> selectList(@Param("params") ForumCommentQuery params);

    void updateCommentGoodCount(@Param("changeCount") Integer changeCount, @Param("commentId") String commentId);

    void updateTopTypeByArticleId(@Param("articleId") String articleId);

    List<ForumComment> findListByParam(@Param("query") ForumCommentQuery query);

    void delByCommentIds(@Param("commentIds") String[] commentIds);

    void updateByParam(@Param("query") ForumCommentQuery query);
}
