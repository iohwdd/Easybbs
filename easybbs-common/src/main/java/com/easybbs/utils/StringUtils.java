package com.easybbs.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author: iohw
 * @date: 2024/5/9 16:18
 * @description:
 */
public class StringUtils {
    public static Boolean isEmpty(String str) {
        if (str == null || "".equalsIgnoreCase(str.trim()) || "null".equalsIgnoreCase(str)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getRandomString(Integer count) {
        return RandomStringUtils.random(count, true, true);
    }

    public static String getRandomNumber(Integer count) {
        return RandomStringUtils.random(count, false, true);
    }

    public static String encodeMd5(String str) {
        return StringUtils.isEmpty(str) ? null : DigestUtils.md5Hex(str);
    }

    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String escapeHtml(String content) {
        if (content.isEmpty()) {
            return content;
        }
        content = content.replace("<", "&lt;");
        content = content.replace(">", "&gt;");
        content = content.replace(" ", "&nbsp");
        content = content.replace("\n", "<br>");
        return content;
    }
}
