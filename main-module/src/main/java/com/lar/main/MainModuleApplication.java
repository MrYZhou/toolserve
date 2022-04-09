package com.lar.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScans({
        @ComponentScan("com.lar")
})
public class MainModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainModuleApplication.class, args);
    }

}
