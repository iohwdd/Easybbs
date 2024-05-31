package com.easybbs.utils;

import com.easybbs.entity.enums.VerifyRegexEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: iohw
 * @date: 2024/5/13 20:12
 * @description: 正则校验
 */
public class VerifyUtils {
    public static Boolean verify(String regs, String value) {
        if(StringUtils.isEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regs);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    public static Boolean verify(VerifyRegexEnum regs, String value){
        return verify(regs.getRegex(), value);
    }
}
