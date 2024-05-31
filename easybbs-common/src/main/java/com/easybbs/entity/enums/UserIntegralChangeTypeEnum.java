package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/13 14:18
 * @description: 积分变更类型
 */
public enum UserIntegralChangeTypeEnum {
    ADD(1,"增加"),
    REDUCE(-1,"减少");
    private Integer changeType;
    private String desc;

    public Integer getChangeType() {
        return changeType;
    }

    public String getDesc() {
        return desc;
    }

    UserIntegralChangeTypeEnum(Integer changeType, String desc) {
        this.changeType = changeType;
        this.desc = desc;
    }
}
