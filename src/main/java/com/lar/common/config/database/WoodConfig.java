package com.lar.common.config.database;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.noear.wood.DbContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class WoodConfig {
    private String url;

    private String username;
    private String password;

//    @Bean
//    public DbContext getDbContext() {
//        // 创建HikariCP连接池
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(url);
//        config.setUsername(username);
//        config.setPassword(password);
//        config.setMaximumPoolSize(30); // 设置最大连接池大小
//        config.setMinimumIdle(30); // 设置最小空闲连接数
//        config.setIdleTimeout(120000); // 设置非核心连接的空闲超时时间（毫秒）
//        config.setMaxLifetime(60000); // 设置连接的最大生命周期（毫秒）
//        config.setConnectionTestQuery("SELECT 1"); // 设置连接验证时执行的SQL语句
//        config.setValidationTimeout(5000); // 设置连接验证的超时时间（毫秒）
//        config.setKeepaliveTime(120000); // 设置连接保活的时间间隔（毫秒）
//        config.setConnectionTimeout(5000); // 设置获取连接的最大等待时间（毫秒）
//        HikariDataSource dataSource = new HikariDataSource(config);
//        return new DbContext("", dataSource);
//    }

    @Bean
    public DbContext getDbContext2() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        // 设置基本属性
        dataSource.setUrl(url); // 数据库连接 URL
        dataSource.setUsername(username); // 数据库用户名
        dataSource.setPassword(password); // 数据库密码
        // 可选：设置其他配置项
        dataSource.setInitialSize(15); // 初始化连接数量
        dataSource.setMaxActive(30); // 最大连接数量
        dataSource.setMinIdle(15); // 最小空闲连接数量
        dataSource.setMinEvictableIdleTimeMillis(60000); // 连接空闲时间达到可驱逐的最小时间（毫秒）
        dataSource.setMaxEvictableIdleTimeMillis(25200000); // 连接空闲时间达到可驱逐的最大时间（毫秒）
        dataSource.setTestOnBorrow(false); // 获取连接时是否测试连接的有效性
        dataSource.setTestOnReturn(false); // 归还连接时是否测试连接的有效性
        dataSource.setTestWhileIdle(true); // 是否在空闲时测试连接的有效性
        dataSource.setValidationQuery("SELECT 1"); // 测试连接有效性的 SQL 查询
        dataSource.setValidationQueryTimeout(5); // 测试查询的超时时间（秒）
        dataSource.setKeepAlive(true); // 启用连接保活
        dataSource.setTimeBetweenEvictionRunsMillis(60000); // 驱逐线程执行任务的时间间隔（毫秒）
        dataSource.setKeepAliveBetweenTimeMillis(120000); // 保活检查的时间间隔（毫秒）
        return new DbContext("", dataSource);
    }
}
