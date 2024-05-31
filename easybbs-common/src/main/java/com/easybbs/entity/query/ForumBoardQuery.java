package com.easybbs.entity.query;

/**
 * @author: iohw
 * @date: 2024/5/28 16:29
 * @description:
 */
public class ForumBoardQuery {
    private String boardId;
    private String pBoardId;
    private String boardName;
    private String pBoardName;
    private String boardDesc;
    private Integer postType;

    private String coverPath;
    private Integer sort;

    public ForumBoardQuery() {
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getpBoardName() {
        return pBoardName;
    }

    public void setpBoardName(String pBoardName) {
        this.pBoardName = pBoardName;
    }

    public Integer getPostType() {
        return postType;
    }

    public void setPostType(Integer postType) {
        this.postType = postType;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getpBoardId() {
        return pBoardId;
    }

    public void setpBoardId(String pBoardId) {
        this.pBoardId = pBoardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardDesc() {
        return boardDesc;
    }

    public void setBoardDesc(String boardDesc) {
        this.boardDesc = boardDesc;
    }
}
