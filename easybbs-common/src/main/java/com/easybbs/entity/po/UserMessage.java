package com.easybbs.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户消息
 *
 * @TableName user_message
 */
public class UserMessage implements Serializable {
    /**
     * 自增ID
     */
    private Integer messageId;

    /**
     * 接收人用户ID
     */
    private String receivedUserId;

    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 发送人用户ID
     */
    private String sendUserId;

    /**
     * 发送人昵称
     */
    private String sendNickName;

    /**
     * 0:系统消息 1:评论 2:文章点赞  3:评论点赞 4:附件下载
     */
    private Integer messageType;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 1:未读 2:已读
     */
    private Integer status;
    /**
     * 父评论
     */
    private Integer pCommentId;
    /**
     * 父评论内容
     */
    private String pCommentContent;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public String getpCommentContent() {
        return pCommentContent;
    }

    public void setpCommentContent(String pCommentContent) {
        this.pCommentContent = pCommentContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Integer getpCommentId() {
        return pCommentId;
    }

    public void setpCommentId(Integer pCommentId) {
        this.pCommentId = pCommentId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getReceivedUserId() {
        return receivedUserId;
    }

    public void setReceivedUserId(String receivedUserId) {
        this.receivedUserId = receivedUserId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", messageId=").append(messageId);
        sb.append(", receivedUserId=").append(receivedUserId);
        sb.append(", articleId=").append(articleId);
        sb.append(", articleTitle=").append(articleTitle);
        sb.append(", commentId=").append(commentId);
        sb.append(", sendUserId=").append(sendUserId);
        sb.append(", sendNickName=").append(sendNickName);
        sb.append(", messageType=").append(messageType);
        sb.append(", messageContent=").append(messageContent);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}