package com.easybbs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: iohw
 * @date: 2024/5/9 17:37
 * @description:
 */
@Component
public class WebConfig extends AppConfig {
    @Value("${spring.mail.username:}")
    private String userName;
    @Value("${admin.emails:}")
    private String adminEmail;

    public String getSendUserName() {
        return userName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }
}
