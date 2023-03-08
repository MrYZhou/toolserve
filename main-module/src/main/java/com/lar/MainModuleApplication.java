package com.lar;

import com.fhs.trans.service.impl.DictionaryTransService;
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
    @Autowired  //注入字典翻译服务
    private DictionaryTransService dictionaryTransService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder().sources(MainModuleApplication.class).bannerMode(Banner.Mode.OFF).build(args);
        app.run();
//        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        //在某处将字典缓存刷新到翻译服务中
        Map<String, String> transMap = new HashMap<>();
        transMap.put("0", "男");
        transMap.put("1", "女");
        Map<String, String> transMap2 = new HashMap<>();
        transMap2.put("0", "书籍1");
        transMap2.put("1", "书籍2");
        dictionaryTransService.refreshCache("gender", transMap);
        dictionaryTransService.refreshCache("book", transMap2);
    }
}
