package com.easybbs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: iohw
 * @date: 2024/5/27 19:37
 * @description:
 */
@Component
@PropertySource("classpath:easybbs.properties")
public class AdminConfig extends AppConfig {
    /**
     * 管理员账号
     */
    @Value("${admin.account:}")
    private String adminAccount;
    /**
     * 管理员密码
     */
    @Value("${admin.password:}")
    private String password;
    @Value("${web.api.url:}")
    private String webApiUtr;

    public String getAdminAccount() {
        return adminAccount;
    }

    public String getPassword() {
        return password;
    }

    public String getWebApiUtr() {
        return webApiUtr;
    }
}
