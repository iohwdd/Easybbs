package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/15 15:27
 * @description: 文章状态
 */
public enum ArticleStatusEnum {
    DELETE(-1,"已删除"),
    AUDIT(0,"待审核"),
    NORMAL(1,"正常");
    private Integer status;
    private String desc;

    ArticleStatusEnum(Integer type, String desc) {
        this.status = type;
        this.desc = desc;
    }
    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
    public static ArticleStatusEnum getByType(Integer status) {
        for(ArticleStatusEnum item : ArticleStatusEnum.values()) {
            if(item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }
}
