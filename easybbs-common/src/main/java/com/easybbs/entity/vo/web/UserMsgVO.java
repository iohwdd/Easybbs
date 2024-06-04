package com.easybbs.entity.vo.web;

/**
 * @author: iohw
 * @date: 2024/6/4 10:22
 * @description:
 */
public class UserMsgVO {
    private String userId;
    private String nickName;
    private String avatar;
    private String lastMessage;
    private Integer noReadCount;

    public UserMsgVO() {
    }

    public Integer getNoReadCount() {
        return noReadCount;
    }

    public void setNoReadCount(Integer noReadCount) {
        this.noReadCount = noReadCount;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
