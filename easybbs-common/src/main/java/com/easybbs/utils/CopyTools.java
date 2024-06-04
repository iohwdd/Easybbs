package com.easybbs.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/14 19:33
 * @description:
 */
public class CopyTools {
    public static <T,S> List<T> copyList(List<S> sList, Class<T> classz) {
        List<T> list = new ArrayList<>();
        for(S s : sList) {
            T t = null;
            try {
                t = classz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(s,t);
            list.add(t);
        }
        return list;
    }
    public static <T,S> T copy(S s, Class<T> classz) {
        T t = null;
        try {
            t = classz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(s,t);
        return t;
    }
}
