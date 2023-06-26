package com.lar.config.database;

import org.noear.wood.DbContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        prefix = "spring.datasource"
)
public class WoodConfig {
    private String url;

    private String username;
    private String password;

    @Bean
    public DbContext getDbContext() {
        return new DbContext("", url, username, password);
    }
}
