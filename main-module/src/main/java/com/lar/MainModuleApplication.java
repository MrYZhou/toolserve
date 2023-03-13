package com.lar;

import com.larry.spring.MybatisDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;


/**
 * bean扫描 ,默认是SpringBootApplication注解的所在包为根路径
 * &#064;SpringBootApplication  相当于@ComponentScans({  &#064;ComponentScan("com.lar")  })
 */
@SpringBootApplication
// 允许异步
@EnableAsync
public class MainModuleApplication implements ApplicationListener<ApplicationStartedEvent> {

    // 初始化字典的数据
    @Autowired(required = false)
    MybatisDictService dictService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder().sources(MainModuleApplication.class).bannerMode(Banner.Mode.OFF).build(args);
        app.run();
//        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, String> transMap2 = new HashMap<>();
        transMap2.put("0", "书籍1");
        transMap2.put("1", "书籍2");

        dictService.putDictType("book", transMap2);
    }
}
