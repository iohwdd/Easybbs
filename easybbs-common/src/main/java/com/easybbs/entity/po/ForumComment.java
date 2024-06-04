package com.easybbs.entity.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 评论
 *
 * @TableName forum_comment
 */

public class ForumComment implements Serializable {
    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 父级评论ID
     */
    private Integer pCommentId;

    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 图片
     */
    private String imgPath;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户ip地址
     */
    private String userIpAddress;

    /**
     * 回复人ID
     */
    private String replyUserId;

    /**
     * 回复人昵称
     */
    private String replyNickName;

    /**
     * 0:未置顶  1:置顶
     */
    private Integer topType;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;

    /**
     * good数量
     */
    private Integer goodCount;

    /**
     * 0:待审核  1:已审核
     */
    private Integer status;
    private Integer likeType; // 0未点赞 1已点赞
    private List<ForumComment> children;

    private static final long serialVersionUID = 1L;

    public ForumComment() {
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getpCommentId() {
        return pCommentId;
    }

    public void setpCommentId(Integer pCommentId) {
        this.pCommentId = pCommentId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyNickName() {
        return replyNickName;
    }

    public void setReplyNickName(String replyNickName) {
        this.replyNickName = replyNickName;
    }

    public Integer getTopType() {
        return topType;
    }

    public void setTopType(Integer topType) {
        this.topType = topType;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLikeType() {
        return likeType;
    }

    public void setLikeType(Integer likeType) {
        this.likeType = likeType;
    }

    public List<ForumComment> getChildren() {
        return children;
    }

    public void setChildren(List<ForumComment> children) {
        this.children = children;
    }
}