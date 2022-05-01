package com.lar.main;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
/** bean扫描 */
@ComponentScans({
  @ComponentScan("com.lar"),
  @ComponentScan("common"),
  @ComponentScan("middle"),
  @ComponentScan("system")
})
// 允许异步
@EnableAsync
// jpa扫描
@EnableJpaRepositories(basePackages = {"com.lar.main", "com.lar.security", "system.menu"})
//  jpa实体扫描
@EntityScan(basePackages = {"com.lar", "system.menu"})
public class MainModuleApplication {

  public static void main(String[] args) {

    ConfigurableApplicationContext run =
        new SpringApplicationBuilder()
            .sources(MainModuleApplication.class)
            .bannerMode(Banner.Mode.OFF)
            .run(args);
    System.out.println(run);
    //        SpringApplication.run(MainModuleApplication.class, args);
  }
}
