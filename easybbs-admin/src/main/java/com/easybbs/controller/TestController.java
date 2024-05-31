package com.easybbs.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: iohw
 * @date: 2024/5/7 21:27
 * @description:
 */
@RestController
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "123456";
    }
}
