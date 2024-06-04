package com.easybbs.entity.po;

/**
 * @author: iohw
 * @date: 2024/6/3 21:21
 * @description:
 */
public class Message {
    private Integer id;
    private String senderId;
    private String receiverId;
    private String text;
    private String createdTime;
    private Integer senderStatus;
    private Integer receiverStatus;

    public Message() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderStatus() {
        return senderStatus;
    }

    public void setSenderStatus(Integer senderStatus) {
        this.senderStatus = senderStatus;
    }

    public Integer getReceiverStatus() {
        return receiverStatus;
    }

    public void setReceiverStatus(Integer receiverStatus) {
        this.receiverStatus = receiverStatus;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
