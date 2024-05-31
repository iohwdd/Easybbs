package com.easybbs.entity.vo;

/**
 * @author: iohw
 * @date: 2024/5/9 8:16
 * @description:
 */
public class ResponseVO <T>{
    private String status;
    private Integer code;
    private T data;
    private String info;

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public T getData() {
        return data;
    }
}
