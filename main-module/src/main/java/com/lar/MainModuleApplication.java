package com.lar;

import com.larry.service.DictService;
import org.noear.wood.DbContext;
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

//    // 初始化字典的数据
    @Autowired
    DictService dictService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder().sources(MainModuleApplication.class).bannerMode(Banner.Mode.OFF).build(args);
        app.run();
    }


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, String> transMap = new HashMap<>();
        transMap.put("0", "书籍1");
        transMap.put("1", "书籍2");
        // 带类别的
        dictService.putDictType("book", transMap);
        // 不带类别
        dictService.putDictItem("355643017543027584","书籍3");

        // 配置数据源
        Map<String, DbContext> db = dictService.getDb();
        String url = "jdbc:mysql://127.0.0.1:3306/study?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=" +
                "UTC&allowPublicKeyRetrieval=true";
        String username = "root";
        String password ="root";
        DbContext context  = new DbContext("",url,username,password);
        db.put("main",context);
        dictService.setDb(db);
    }
}
