package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/22 15:27
 * @description:
 */
public enum AttachmentFileTypeEnum {
    ZIP(0, new String[]{".zip", ".rar", ".ZIP", ".RAR"}, "附件");
    private Integer type;
    private String[] suffix;
    private String desc;


    AttachmentFileTypeEnum(Integer type, String[] suffix, String desc) {
        this.type = type;
        this.suffix = suffix;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getSuffix() {
        return suffix;
    }
}
