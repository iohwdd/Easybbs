package com.easybbs.entity.enums;

/**
 * @author: iohw
 * @date: 2024/5/13 19:38
 * @description: 具体校验规则
 */
public enum VerifyRegexEnum {
    NO("","不校验"),
    IP("(\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5])","IP地址"),
    POSITIVE_INTEGER("^[0-9]*[1-9][0-9]*$","正整数"),
    NUMBER_LETTER_UNDER_LINE("^\\w+$","由数字、26个英文字母或者下划线组成的字符串"),
    EMAIL("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$","邮箱"),
    PHONE("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$","手机号码"),
    COMMON("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$","数字，字母，中文，下划线"),
    PASSWORD("^[a-zA-Z0-9!@#$%^&*()_+]{8,18}$","只能是数字，字母，特殊字符 8-18 位"),
    ACCOUNT("^[a-zA-Z][a-zA-Z0-9_]*$","字母开头，由数字、英文字母或者下划线组成"),
    MONEY("^[0-9]+(\\.[0-9]{1,2})?$","金额");
    private String regex;
    private String desc;

    VerifyRegexEnum(String regex, String desc) {
        this.regex = regex;
        this.desc = desc;
    }

    public String getRegex() {
        return regex;
    }

    public String getDesc() {
        return desc;
    }
}
