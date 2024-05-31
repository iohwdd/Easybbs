package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/22 15:27
 * @description:
 */
public enum ArticleAttachmentTypEnum {
    NO_ATTACHMENT(0, "没有附件"),
    HAVE_ATTACHMENT(1, "有附件");
    private Integer type;
    private String desc;


    ArticleAttachmentTypEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static ArticleAttachmentTypEnum getByType(Integer type) {
        for (ArticleAttachmentTypEnum item : ArticleAttachmentTypEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}
