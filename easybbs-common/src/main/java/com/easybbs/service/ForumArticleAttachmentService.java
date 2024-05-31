package com.easybbs.service;

import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.po.ForumArticleAttachment;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/15 16:42
 * @description:
 */
public interface ForumArticleAttachmentService {
    List<ForumArticleAttachment> findByArticleId(String articleId);

    ForumArticleAttachment downloadAttachment(String fileId, SessionWebUserDto attribute);

    ForumArticleAttachment downloadAttachment4Admin(String fileId);
}
