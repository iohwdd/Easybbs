package com.easybbs;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: iohw
 * @date: 2024/5/7 21:26
 * @description:
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.easybbs.mapper"})
@EnableTransactionManagement
public class EasybbsWebAppliction {
    public static void main(String[] args) {
        SpringApplication.run(EasybbsWebAppliction.class, args);
    }
}
