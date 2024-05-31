package com.easybbs.service;

import com.easybbs.entity.po.ForumArticleAttachmentDownload;

/**
 * @author: iohw
 * @date: 2024/5/16 19:48
 * @description:
 */
public interface ForumArticleAttachmentDownloadService {
    ForumArticleAttachmentDownload getByFileIdAndUserId(String fileId, String userId);
}
