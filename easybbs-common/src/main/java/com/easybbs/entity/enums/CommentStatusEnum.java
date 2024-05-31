package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/20 21:02
 * @description: 评论状态
 */
public enum CommentStatusEnum {
    DELETE(-1, "已删除"),
    NO_AUDIT(0, "待审核"),
    NORMAL(1, "正常");
    private Integer type;
    private String desc;

    CommentStatusEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static CommentStatusEnum getByType(Integer type) {
        for (CommentStatusEnum item : CommentStatusEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
