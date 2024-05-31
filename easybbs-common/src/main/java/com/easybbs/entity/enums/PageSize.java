package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/17 19:10
 * @description:
 */
public enum PageSize {
    SIZE15(15,"15"),
    SIZE50(50,"50");
    private Integer size;
    private String desc;

    PageSize(Integer size, String desc) {
        this.size = size;
        this.desc = desc;
    }

    public Integer getSize() {
        return size;
    }

    public String getDesc() {
        return desc;
    }
}
