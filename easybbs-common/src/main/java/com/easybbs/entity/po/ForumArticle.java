package com.easybbs.entity.po;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 文章信息
 *
 * @TableName forum_article
 */
public class ForumArticle implements Serializable {
    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 板块ID
     */
    private Integer boardId;

    /**
     * 板块名称
     */
    private String boardName;

    /**
     * 父级板块ID
     */
    private Integer pBoardId;

    /**
     * 父板块名称
     */
    private String pBoardName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 最后登录ip地址
     */
    private String userIpAddress;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 内容
     */
    private String content;

    /**
     * markdown内容
     */
    private String markdownContent;

    /**
     * 0:富文本编辑器 1:markdown编辑器
     */
    private Integer editorType;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postTime;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateTime;

    /**
     * 阅读数量
     */
    private Integer readCount;

    /**
     * 点赞数
     */
    private Integer goodCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 0未置顶  1:已置顶
     */
    private Integer topType;

    /**
     * 0:没有附件  1:有附件
     */
    private Integer attachmentType;

    /**
     * -1已删除 0:待审核  1:已审核
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

    public ForumArticle() {
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public Integer getpBoardId() {
        return pBoardId;
    }

    public void setpBoardId(Integer pBoardId) {
        this.pBoardId = pBoardId;
    }

    public String getpBoardName() {
        return pBoardName;
    }

    public void setpBoardName(String pBoardName) {
        this.pBoardName = pBoardName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public void setUserIpAddress(String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMarkdownContent() {
        return markdownContent;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    public Integer getEditorType() {
        return editorType;
    }

    public void setEditorType(Integer editorType) {
        this.editorType = editorType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getTopType() {
        return topType;
    }

    public void setTopType(Integer topType) {
        this.topType = topType;
    }

    public Integer getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(Integer attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ForumArticle other = (ForumArticle) that;
        return (this.getArticleId() == null ? other.getArticleId() == null : this.getArticleId().equals(other.getArticleId()))
                && (this.getBoardId() == null ? other.getBoardId() == null : this.getBoardId().equals(other.getBoardId()))
                && (this.getBoardName() == null ? other.getBoardName() == null : this.getBoardName().equals(other.getBoardName()))
                && (this.getpBoardName() == null ? other.getpBoardId() == null : this.getpBoardId().equals(other.getpBoardId()))
                && (this.getpBoardName() == null ? other.getpBoardName() == null : this.getpBoardName().equals(other.getpBoardName()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
                && (this.getUserIpAddress() == null ? other.getUserIpAddress() == null : this.getUserIpAddress().equals(other.getUserIpAddress()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getCover() == null ? other.getCover() == null : this.getCover().equals(other.getCover()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getMarkdownContent() == null ? other.getMarkdownContent() == null : this.getMarkdownContent().equals(other.getMarkdownContent()))
                && (this.getEditorType() == null ? other.getEditorType() == null : this.getEditorType().equals(other.getEditorType()))
                && (this.getSummary() == null ? other.getSummary() == null : this.getSummary().equals(other.getSummary()))
                && (this.getPostTime() == null ? other.getPostTime() == null : this.getPostTime().equals(other.getPostTime()))
                && (this.getLastUpdateTime() == null ? other.getLastUpdateTime() == null : this.getLastUpdateTime().equals(other.getLastUpdateTime()))
                && (this.getReadCount() == null ? other.getReadCount() == null : this.getReadCount().equals(other.getReadCount()))
                && (this.getGoodCount() == null ? other.getGoodCount() == null : this.getGoodCount().equals(other.getGoodCount()))
                && (this.getCommentCount() == null ? other.getCommentCount() == null : this.getCommentCount().equals(other.getCommentCount()))
                && (this.getTopType() == null ? other.getTopType() == null : this.getTopType().equals(other.getTopType()))
                && (this.getAttachmentType() == null ? other.getAttachmentType() == null : this.getAttachmentType().equals(other.getAttachmentType()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getArticleId() == null) ? 0 : getArticleId().hashCode());
        result = prime * result + ((getBoardId() == null) ? 0 : getBoardId().hashCode());
        result = prime * result + ((getBoardName() == null) ? 0 : getBoardName().hashCode());
        result = prime * result + ((getpBoardId() == null) ? 0 : getpBoardId().hashCode());
        result = prime * result + ((getpBoardName() == null) ? 0 : getpBoardName().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getUserIpAddress() == null) ? 0 : getUserIpAddress().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getCover() == null) ? 0 : getCover().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getMarkdownContent() == null) ? 0 : getMarkdownContent().hashCode());
        result = prime * result + ((getEditorType() == null) ? 0 : getEditorType().hashCode());
        result = prime * result + ((getSummary() == null) ? 0 : getSummary().hashCode());
        result = prime * result + ((getPostTime() == null) ? 0 : getPostTime().hashCode());
        result = prime * result + ((getLastUpdateTime() == null) ? 0 : getLastUpdateTime().hashCode());
        result = prime * result + ((getReadCount() == null) ? 0 : getReadCount().hashCode());
        result = prime * result + ((getGoodCount() == null) ? 0 : getGoodCount().hashCode());
        result = prime * result + ((getCommentCount() == null) ? 0 : getCommentCount().hashCode());
        result = prime * result + ((getTopType() == null) ? 0 : getTopType().hashCode());
        result = prime * result + ((getAttachmentType() == null) ? 0 : getAttachmentType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", articleId=").append(articleId);
        sb.append(", boardId=").append(boardId);
        sb.append(", boardName=").append(boardName);
        sb.append(", pBoardId=").append(pBoardId);
        sb.append(", pBoardName=").append(pBoardName);
        sb.append(", userId=").append(userId);
        sb.append(", nickName=").append(nickName);
        sb.append(", userIpAddress=").append(userIpAddress);
        sb.append(", title=").append(title);
        sb.append(", cover=").append(cover);
        sb.append(", content=").append(content);
        sb.append(", markdownContent=").append(markdownContent);
        sb.append(", editorType=").append(editorType);
        sb.append(", summary=").append(summary);
        sb.append(", postTime=").append(postTime);
        sb.append(", lastUpdateTime=").append(lastUpdateTime);
        sb.append(", readCount=").append(readCount);
        sb.append(", goodCount=").append(goodCount);
        sb.append(", commentCount=").append(commentCount);
        sb.append(", topType=").append(topType);
        sb.append(", attachmentType=").append(attachmentType);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}