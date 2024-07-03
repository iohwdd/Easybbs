package com.easybbs.entity.query;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/17 19:40
 * @description:
 */
public class ForumCommentQuery {
    private String articleId;
    private Integer pCommentId;
    private List<Integer> pcommentList;
    private String userId;
    private String currentUserId;
    private Integer queryLikeType;// 0未点赞 1已点赞
    private Boolean loadChildren;
    private Integer status;
    private Integer pageNo;
    private Integer pageSize;
    private String orderBy;
    private String contentFuzzy;

    private String nickNameFuzzy;
    private List<String> commentIdList;

    public ForumCommentQuery() {
    }

    public List<String> getCommentIdList() {
        return commentIdList;
    }

    public void setCommentIdList(List<String> commentIdList) {
        this.commentIdList = commentIdList;
    }

    public String getContentFuzzy() {
        return contentFuzzy;
    }

    public void setContentFuzzy(String contentFuzzy) {
        this.contentFuzzy = contentFuzzy;
    }

    public String getNickNameFuzzy() {
        return nickNameFuzzy;
    }

    public void setNickNameFuzzy(String nickNameFuzzy) {
        this.nickNameFuzzy = nickNameFuzzy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getpCommentId() {
        return pCommentId;
    }

    public void setpCommentId(Integer pCommentId) {
        this.pCommentId = pCommentId;
    }

    public List<Integer> getPcommentList() {
        return pcommentList;
    }

    public void setPcommentList(List<Integer> pcommentList) {
        this.pcommentList = pcommentList;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Integer getQueryLikeType() {
        return queryLikeType;
    }

    public void setQueryLikeType(Integer queryLikeType) {
        this.queryLikeType = queryLikeType;
    }

    public Boolean getLoadChildren() {
        return loadChildren;
    }

    public void setLoadChildren(Boolean loadChildren) {
        this.loadChildren = loadChildren;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
