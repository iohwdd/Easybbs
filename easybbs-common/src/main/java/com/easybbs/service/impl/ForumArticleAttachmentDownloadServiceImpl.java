package com.easybbs.service.impl;

import com.easybbs.config.WebConfig;
import com.easybbs.entity.po.ForumArticleAttachmentDownload;
import com.easybbs.mapper.ForumArticleAttachmentDownloadMapper;
import com.easybbs.service.ForumArticleAttachmentDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: iohw
 * @date: 2024/5/16 19:48
 * @description:
 */
@Service
public class ForumArticleAttachmentDownloadServiceImpl implements ForumArticleAttachmentDownloadService {
    @Autowired
    ForumArticleAttachmentDownloadMapper forumArticleAttachmentDownloadMapper;
    @Override
    public ForumArticleAttachmentDownload getByFileIdAndUserId(String fileId, String userId) {
        return forumArticleAttachmentDownloadMapper.selectByFileIdAndUserId(fileId,userId);
    }
}
