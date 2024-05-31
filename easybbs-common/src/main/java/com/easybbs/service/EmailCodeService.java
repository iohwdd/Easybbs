package com.easybbs.service;

/**
 * @author: iohw
 * @date: 2024/5/9 16:25
 * @description:
 */
public interface EmailCodeService {
    /**
     * 发送邮箱验证码
     */
    void sendEmailCode(String email, Integer type);

    /**
     * 校验邮箱验证码
     * @param email
     * @param emailCode
     */
    void checkCode(String email, String emailCode);
}
