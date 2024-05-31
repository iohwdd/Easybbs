package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/20 14:08
 * @description:
 */
public enum CommentTopTypeEnum {
    NO_TOP(0,"未置顶"),
    TOP(1,"已置顶");
    private Integer type;
    private String desc;

    CommentTopTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static CommentTopTypeEnum getByType(Integer type){
        for(CommentTopTypeEnum item : CommentTopTypeEnum.values()) {
            if(item.getType().equals(type)) {
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
