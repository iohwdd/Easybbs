package com.easybbs.entity.vo.web;

/**
 * @author: iohw
 * @date: 2024/5/15 16:21
 * @description:
 */
public class ForumArticleDetailVO {
    private ForumArticleVO forumArticle;
    private ForumArticleAttachmentVO attachment;
    private Boolean haveLike = false;

    public ForumArticleVO getForumArticle() {
        return forumArticle;
    }

    public void setForumArticle(ForumArticleVO forumArticle) {
        this.forumArticle = forumArticle;
    }


    public ForumArticleAttachmentVO getAttachment() {
        return attachment;
    }

    public void setAttachment(ForumArticleAttachmentVO attachment) {
        this.attachment = attachment;
    }

    public Boolean getHaveLike() {
        return haveLike;
    }

    public void setHaveLike(Boolean haveLike) {
        this.haveLike = haveLike;
    }
}
