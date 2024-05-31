package com.easybbs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: iohw
 * @date: 2024/5/9 8:30
 * @description:
 */
public class DateUtil {
    private static final Object lockObj = new Object();
    private static Map<String,ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();
    private static SimpleDateFormat getSdf(final String pattern){
        ThreadLocal<SimpleDateFormat> t1 = sdfMap.get(pattern);
        if(t1 == null){
            synchronized (lockObj){
                t1 = sdfMap.get(pattern);
                if(t1 == null){
                    t1 = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
                }
            }
        }
        return t1.get();
    }
    public static String format(Date date, String pattern){
        return  getSdf(pattern).format(date);
    }
    public static Date parse(String dateStr,String pattern){
        try {
            return getSdf(pattern).parse(dateStr);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return new Date();
    }
}
