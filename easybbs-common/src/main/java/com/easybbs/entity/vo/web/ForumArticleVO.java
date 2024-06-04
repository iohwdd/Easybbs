package com.easybbs.entity.vo.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author: iohw
 * @date: 2024/5/14 19:25
 * @description:
 */

/**
 * 文章信息
 */
public class ForumArticleVO implements Serializable {
    private String articleId;
    private Integer boardId;
    private String boardName;
    private Integer pBoardId;
    private String pBoardName;
    private String userId;
    private String nickName;
    private String userIpAddress;
    private String title;
    private String cover;
    private String content;
    private String markdownContent;
    private Integer editorType;
    private String summary;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;
    private Integer readCount;
    private Integer commentCount;
    private Integer goodCount;
    /**
     * 1置顶 0未置顶
     */
    private Integer topType;
    /**
     * 1有附件 0无附件
     */
    private Integer attachmentType;
    /**
     * -1 已删除 0 待审核 1 已审核
     */
    private Integer status;

    public ForumArticleVO() {
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public Integer getEditorType() {
        return editorType;
    }

    public void setEditorType(Integer editorType) {
        this.editorType = editorType;
    }

    public String getMarkdownContent() {
        return markdownContent;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public void setpBoardId(Integer pBoardId) {
        this.pBoardId = pBoardId;
    }

    public void setpBoardName(String pBoardName) {
        this.pBoardName = pBoardName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUserIpAddress(String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public void setTopType(Integer topType) {
        this.topType = topType;
    }

    public void setAttachmentType(Integer attachmentType) {
        this.attachmentType = attachmentType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getArticleId() {
        return articleId;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public Integer getpBoardId() {
        return pBoardId;
    }

    public String getpBoardName() {
        return pBoardName;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public String getTitle() {
        return title;
    }

    public String getCover() {
        return cover;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return summary;
    }

    public Date getPostTime() {
        return postTime;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public Integer getTopType() {
        return topType;
    }

    public Integer getAttachmentType() {
        return attachmentType;
    }

    public Integer getStatus() {
        return status;
    }
}
