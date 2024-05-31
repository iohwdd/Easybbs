package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/22 14:33
 * @description:
 */
public enum EditTypeEnum {
    RICH(0, "富文本"),
    MARKDOWN(1, "Markdown");
    private Integer type;
    private String desc;

    EditTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static EditTypeEnum getByType(Integer type) {
        for (EditTypeEnum item : EditTypeEnum.values()) {
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
