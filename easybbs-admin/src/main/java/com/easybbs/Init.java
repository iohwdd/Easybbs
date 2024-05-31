package com.easybbs;

import com.easybbs.service.SysSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: iohw
 * @date: 2024/5/10 19:47
 * @description: 初始化系统设置
 */
@Component
public class Init implements ApplicationRunner {
    @Autowired
    SysSettingService sysSettingService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        sysSettingService.refreshCache();
    }
    @PostConstruct
    public void init(ApplicationArguments args) throws Exception {
        sysSettingService.refreshCache();
    }
}
