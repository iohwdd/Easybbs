package com.easybbs.exception;

import com.easybbs.entity.enums.ResponseCodeEnum;

/**
 * @author: iohw
 * @date: 2024/5/9 8:21
 * @description: 业务异常
 */
public class BusinessException extends RuntimeException{
    private ResponseCodeEnum codeEnum;
    private Integer code;
    private String message;

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
    public BusinessException(String message) {
        super(message);
        this.message = message;
    }
    public BusinessException(Throwable cause){
        super(cause);
    }

    public BusinessException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }

    public BusinessException(String message, Integer code, String message1) {
        super(message);
        this.code = code;
        this.message = message1;
    }
    public ResponseCodeEnum getCodeEnum() {
        return codeEnum;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 业务异常不需要找堆栈新增，提高效率
     * @return
     */
    public Throwable fillInStackTrace() {
        return this;
    }
}
