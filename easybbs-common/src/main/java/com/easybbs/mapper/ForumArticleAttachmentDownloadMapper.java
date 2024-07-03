package com.easybbs.mapper;


import com.easybbs.entity.po.ForumArticleAttachmentDownload;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【forum_article_attachment_download(用户附件下载)】的数据库操作Mapper
* @createDate 2024-05-09 16:56:17
* @Entity com.easybbs.entity.po.ForumArticleAttachmentDownload
*/
//@Mapper
public interface ForumArticleAttachmentDownloadMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ForumArticleAttachmentDownload record);

    int insertSelective(ForumArticleAttachmentDownload record);

    ForumArticleAttachmentDownload selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ForumArticleAttachmentDownload record);

    int updateByPrimaryKey(ForumArticleAttachmentDownload record);

    ForumArticleAttachmentDownload selectByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);

    void insertOrUpdate(@Param("bean") ForumArticleAttachmentDownload updateDownload);

}
