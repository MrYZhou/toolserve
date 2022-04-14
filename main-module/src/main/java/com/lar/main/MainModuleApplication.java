package com.lar.main;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScans({
        @ComponentScan("com.lar"),
        @ComponentScan("common"),
        @ComponentScan("middle"),
})
@EnableAsync
public class MainModuleApplication {

    public static void main(String[] args) {


        new SpringApplicationBuilder()
                .sources(MainModuleApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);


//        SpringApplication.run(MainModuleApplication.class, args);
    }

}
