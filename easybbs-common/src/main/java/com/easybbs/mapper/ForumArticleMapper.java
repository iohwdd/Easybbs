package com.easybbs.mapper;


import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.query.ForumArticleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【forum_article(文章信息)】的数据库操作Mapper
 * @createDate 2024-05-09 16:56:17
 * @Entity com.easybbs.entity.po.ForumArticle
 */
@Mapper
public interface ForumArticleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ForumArticle record);

    int insertSelective(ForumArticle record);

    ForumArticle selectByPrimaryKey(@Param("articleId") String id);

    int updateByPrimaryKeySelective(ForumArticle record);

    int updateByPrimaryKey(ForumArticle record);

    List<ForumArticle> selectAll();

    List<ForumArticle> selectByWrapper(@Param("boardId") Integer boardId, @Param("pBoardId") Integer pBoardId, @Param("orderSql") String orderSql, @Param("currentUserId") String currentUserId);

    void updateArticleCount(@Param("updateType") Integer updateType, @Param("number") Integer number, @Param("articleId") String articleId);

    ForumArticle getForumArticleByArticleId(@Param("articleId") String articleId);

    int findCountByParam(@Param("query") ForumArticleQuery query);

    List<ForumArticle> findListByParams(@Param("query") ForumArticleQuery query);

    List<ForumArticle> selectByKeyword(@Param("keyword") String keyword);

    void delByArticleIds(@Param("ids") String[] ids);

    void updateArticleByParam(@Param("query") ForumArticleQuery query);
}
