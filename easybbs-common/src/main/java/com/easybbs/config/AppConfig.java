package com.easybbs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: iohw
 * @date: 2024/5/9 17:38
 * @description:
 */
@Component
public class AppConfig {
    @Value("${project.folder:}")
    private String projectFolder;
    @Value("${isDev:}")
    private Boolean isDev;

    public Boolean getDev() {
        return isDev;
    }

    public String getProjectFolder() {
        return projectFolder;
    }
}
