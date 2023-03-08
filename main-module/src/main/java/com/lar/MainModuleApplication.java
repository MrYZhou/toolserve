package com.lar;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * bean扫描 ,默认是SpringBootApplication注解的所在包为根路径
 * &#064;SpringBootApplication  相当于@ComponentScans({  &#064;ComponentScan("com.lar")  })
 */
@SpringBootApplication
// 允许异步
@EnableAsync
public class MainModuleApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder().sources(MainModuleApplication.class).bannerMode(Banner.Mode.OFF).build(args);
        app.run();
//        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }
}
