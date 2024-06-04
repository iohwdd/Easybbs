package com.easybbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: iohw
 * @date: 2024/5/7 21:26
 * @description:
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.easybbs.mapper"})
public class EasybbsAdminAppliction {
    public static void main(String[] args) {
        SpringApplication.run(EasybbsAdminAppliction.class,args);
    }
}
