package com.easybbs.mapper;


import com.easybbs.entity.po.ForumArticleAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【forum_article_attachment(文件信息)】的数据库操作Mapper
* @createDate 2024-05-09 16:56:17
* @Entity com.easybbs.entity.po.ForumArticleAttachment
*/
@Mapper
public interface ForumArticleAttachmentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ForumArticleAttachment record);

    int insertSelective(ForumArticleAttachment record);

    List<ForumArticleAttachment> selectByArticleId(@Param("articleId") String id);

    int updateByPrimaryKeySelective(ForumArticleAttachment record);

    int updateByPrimaryKey(ForumArticleAttachment record);

    ForumArticleAttachment selectByFileId(@Param("fileId") String fileId);

    void updateDownloadCount(@Param("fileId") String fileId);
}
