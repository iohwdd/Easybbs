package com.easybbs.entity.query;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/23 19:08
 * @description:
 */
public class ForumArticleQuery {
    private Integer boardId;
    private Integer pBoardId;
    private String boardName;
    private String pBoardName;
    private String articleId;
    private String userId;
    private Integer status;//状态 -1:已删除  0:待审核 1:已审核
    private List<String> articleIdList;
    private String titleFuzzy;
    private String nickNameFuzzy;
    private Integer attachmentType;// 是否有附件 0:无附件  1:有附件
    private Integer topType;
    private Integer pageNo;
    private Integer orderType;

    public ForumArticleQuery() {
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public Integer getpBoardId() {
        return pBoardId;
    }

    public void setpBoardId(Integer pBoardId) {
        this.pBoardId = pBoardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getpBoardName() {
        return pBoardName;
    }

    public void setpBoardName(String pBoardName) {
        this.pBoardName = pBoardName;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getArticleIdList() {
        return articleIdList;
    }

    public void setArticleIdList(List<String> articleIdList) {
        this.articleIdList = articleIdList;
    }

    public String getTitleFuzzy() {
        return titleFuzzy;
    }

    public void setTitleFuzzy(String titleFuzzy) {
        this.titleFuzzy = titleFuzzy;
    }

    public String getNickNameFuzzy() {
        return nickNameFuzzy;
    }

    public void setNickNameFuzzy(String nickNameFuzzy) {
        this.nickNameFuzzy = nickNameFuzzy;
    }

    public Integer getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(Integer attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Integer getTopType() {
        return topType;
    }

    public void setTopType(Integer topType) {
        this.topType = topType;
    }

    @Override
    public String toString() {
        return "ForumArticleQuery{" +
                "boardId=" + boardId +
                ", pBoardId=" + pBoardId +
                ", boardName='" + boardName + '\'' +
                ", pBoardName='" + pBoardName + '\'' +
                ", articleId='" + articleId + '\'' +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", articleIdList=" + articleIdList +
                ", titleFuzzy='" + titleFuzzy + '\'' +
                ", nickNameFuzzy='" + nickNameFuzzy + '\'' +
                ", attachmentType=" + attachmentType +
                ", topType=" + topType +
                ", pageNo=" + pageNo +
                ", orderType=" + orderType +
                '}';
    }
}
