package com.lar;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * bean扫描 ,默认是SpringBootApplication注解的所在包为根路径
 *
 * @SpringBootApplication 相当于@ComponentScans({
 * @ComponentScan("com.lar") })
 */
@SpringBootApplication
// 允许异步
@EnableAsync
// jpa扫描
@EnableJpaRepositories(basePackages = {"com.lar"})
// jpa实体扫描
@EntityScan(basePackages = {"com.lar"})
public class MainModuleApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = new SpringApplicationBuilder()
                .sources(MainModuleApplication.class)
//                .bannerMode(Banner.Mode.OFF)
                .run(args);
//        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }
}
