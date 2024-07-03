package com.easybbs.mapper;


import com.easybbs.entity.po.ForumBoard;
import com.easybbs.entity.query.ForumBoardQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【forum_board(文章板块信息)】的数据库操作Mapper
 * @createDate 2024-05-09 16:56:17
 * @Entity com.easybbs.entity.po.ForumBoard
 */
//@Mapper
public interface ForumBoardMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ForumBoard record);

    int insertSelective(ForumBoard record);

    ForumBoard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ForumBoard record);

    int updateByPrimaryKey(ForumBoard record);

    List<ForumBoard> selectOrderListWithType(@Param("postType") Integer postType);

    void updateByParam(@Param("query") ForumBoardQuery query);

    void delChildBypBoardId(@Param("boardId") String boardId);
}
