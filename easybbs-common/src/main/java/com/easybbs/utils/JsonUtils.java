package com.easybbs.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/10 19:12
 * @description:
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    public static String convertObject2Json(Object obj) {
        return JSON.toJSONString(obj);
    }
    public static <T> T convertJson2Object(String jsonContent,Class<T> classz) {
        return JSON.parseObject(jsonContent,classz);
    }
    public static <T> List<T> convertJsonArray2Object(String jsonContent, Class<T> classz) {
        return JSON.parseArray(jsonContent,classz);
    }
}
