package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/23 19:40
 * @description:
 */
public enum UserArticleTypeEnum {
    POST(0, "发帖"),
    COMMENT(1, "评论"),
    LIKE(2, "点赞");
    private Integer type;
    private String desc;

    UserArticleTypeEnum(Integer type, String desc) {
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
