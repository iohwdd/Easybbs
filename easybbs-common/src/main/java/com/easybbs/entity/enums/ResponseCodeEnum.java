package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/9 8:45
 * @description: 响应代码与信息
 */
public enum ResponseCodeEnum {
    CODE_200(200, "请求成功"),
    CODE_404(404, "请求地址不存在"),
    CODE_600(600, "请求参数错误"),
    CODE_601(601, "信息已存在"),
    CODE_500(500, "服务器返回错误，请联系管理员"),
    CODE_900(900, "http请求超时"),
    CODE_901(901, "登录超时");
    private Integer code;
    private String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
