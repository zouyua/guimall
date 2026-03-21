package com.gg.guimall.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
    "com.gg.guimall.web",      // 扫描 web 模块
    "com.gg.guimall.admin",    // 扫描 admin 模块
    "com.gg.guimall.common",   // 扫描 common 模块（工具类、异常处理等）
    "com.gg.guimall.jwt"       // 扫描 jwt 模块
})
public class GuimallWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuimallWebApplication.class,args);
    }
}
