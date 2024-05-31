package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/15 19:37
 * @description:
 */
public enum OperRecordTypeEnum {
    ARTICLE_LIKE(0,"文章点赞"),
    COMMENT_LIKE(1,"评论点赞");

    private Integer type;
    private String desc;

    OperRecordTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
