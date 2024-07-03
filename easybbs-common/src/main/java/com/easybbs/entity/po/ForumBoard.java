package com.easybbs.entity.po;

import java.io.Serializable;
import java.util.List;

/**
 * 文章板块信息
 *
 * @TableName forum_board
 */
public class ForumBoard implements Serializable {
    /**
     * 板块ID
     */
    private Integer boardId;

    /**
     * 父级板块ID
     */
    private Integer pBoardId;
    /**
     * 子版块
     */
    private List<ForumBoard> children;

    /**
     * 板块名
     */
    private String boardName;

    private String pBoardName;
    /**
     * 封面
     */
    private String cover;

    /**
     * 描述
     */
    private String boardDesc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 0:只允许管理员发帖 1:任何人可以发帖
     */
    private Integer postType;

    private static final long serialVersionUID = 1L;

    public ForumBoard() {
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

    public List<ForumBoard> getChildren() {
        return children;
    }

    public void setChildren(List<ForumBoard> children) {
        this.children = children;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBoardDesc() {
        return boardDesc;
    }

    public void setBoardDesc(String boardDesc) {
        this.boardDesc = boardDesc;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getPostType() {
        return postType;
    }

    public void setPostType(Integer postType) {
        this.postType = postType;
    }
}