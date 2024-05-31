package com.easybbs.utils;

import com.easybbs.entity.dto.SysSettingDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: iohw
 * @date: 2024/5/10 20:44
 * @description: 内存存储系统设置
 */
public class SysCacheUtils {
    private static final String KEY_SYS = "sys_setting";
    private static final Map<String, SysSettingDto> CACHE_DATA = new ConcurrentHashMap<>();
    public static SysSettingDto getSysSetting(){
        return CACHE_DATA.get(KEY_SYS);
    }
    public static void refresh(SysSettingDto sysSettingDto) {
        CACHE_DATA.put(KEY_SYS,sysSettingDto);
    }

}
