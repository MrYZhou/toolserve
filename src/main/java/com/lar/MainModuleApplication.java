package com.lar;


import cn.xuyanwu.spring.file.storage.spring.EnableFileStorage;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * bean扫描 ,默认是SpringBootApplication注解的所在包为根路径
 * &#064;SpringBootApplication  相当于@ComponentScans({  &#064;ComponentScan("com.lar")  })
 */
@SpringBootApplication
// 允许异步
@EnableAsync
//存储
@EnableFileStorage
public class MainModuleApplication implements ApplicationListener<ApplicationStartedEvent> {


    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder().sources(MainModuleApplication.class).bannerMode(Banner.Mode.CONSOLE).build(args);
        app.run();
    }


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            // 加载 resources 目录下的文件
            File file2 = ResourceUtils.getFile("classpath:config/");

            File file = ResourceUtils.getFile("classpath:config/config.properties");
            try (FileInputStream fis = new FileInputStream(file)) {
                // 读取文件内容
                String content = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
