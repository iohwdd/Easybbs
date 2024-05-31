package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/9 8:45
 * @description: 用户状态
 */
public enum UserStatusEnum {
    DISABLE(0,"禁用"),
    ENABLE(1,"启用");
    private Integer status;
    private String msg;

    UserStatusEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
