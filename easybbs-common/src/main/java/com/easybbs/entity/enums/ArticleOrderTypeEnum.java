package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/14 19:12
 * @description:
 */
public enum ArticleOrderTypeEnum {
    HOT(0,"top_type desc,comment_count desc,good_count desc,read_count desc","热榜"),
    SEND(1,"post_time asc","发布"),
    NEW(2,"post_time desc","最新");

    private Integer type;
    private String orderSql;
    private String desc;

    ArticleOrderTypeEnum(Integer type, String orderSql, String desc) {
        this.type = type;
        this.orderSql = orderSql;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public String getDesc() {
        return desc;
    }
    public static  ArticleOrderTypeEnum getByType(Integer type) {
        for(ArticleOrderTypeEnum item : ArticleOrderTypeEnum.values()) {
            if(item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
}
