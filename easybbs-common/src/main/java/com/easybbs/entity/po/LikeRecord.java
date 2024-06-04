package com.easybbs.entity.po;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 点赞记录
 * @TableName like_record
 */
@Data
public class LikeRecord implements Serializable {
    /**
     * 自增ID
     */
    private Integer opId;

    /**
     * 操作类型0:文章点赞 1:评论点赞
     */
    private Integer opType;

    /**
     * 主体ID
     */
    private String objectId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 主体作者ID
     */
    private String authorUserId;

    private static final long serialVersionUID = 1L;

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
        LikeRecord other = (LikeRecord) that;
        return (this.getOpId() == null ? other.getOpId() == null : this.getOpId().equals(other.getOpId()))
            && (this.getOpType() == null ? other.getOpType() == null : this.getOpType().equals(other.getOpType()))
            && (this.getObjectId() == null ? other.getObjectId() == null : this.getObjectId().equals(other.getObjectId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getAuthorUserId() == null ? other.getAuthorUserId() == null : this.getAuthorUserId().equals(other.getAuthorUserId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOpId() == null) ? 0 : getOpId().hashCode());
        result = prime * result + ((getOpType() == null) ? 0 : getOpType().hashCode());
        result = prime * result + ((getObjectId() == null) ? 0 : getObjectId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getAuthorUserId() == null) ? 0 : getAuthorUserId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", opId=").append(opId);
        sb.append(", opType=").append(opType);
        sb.append(", objectId=").append(objectId);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", authorUserId=").append(authorUserId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}