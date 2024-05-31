package com.easybbs.service;

import com.easybbs.entity.dto.SysSettingDto;

/**
 * @author: iohw
 * @date: 2024/5/10 19:17
 * @description:
 */
public interface SysSettingService {
    void refreshCache();

    void saveSetting(SysSettingDto sysSettingDto);
}
