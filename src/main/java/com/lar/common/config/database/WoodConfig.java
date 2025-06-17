package com.lar.common.config.database;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.noear.wood.DbContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class WoodConfig {
    private String url;

    private String username;
    private String password;

    @Bean
    @Primary
    public DbContext getDbContext() {
        // 创建HikariCP连接池
        HikariConfig config = new HikariConfig();
        url ="jdbc:mysql://192.168.11.254:3306/tenant?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        username = "init";
        password = "nacos11tm3DZ2AcH5E3hdzt";
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(20); // 设置最大连接池大小,峰值容量
        config.setMinimumIdle(5); // 设置最小空闲连接数,常态空闲连接
        config.setIdleTimeout(600000); // 设置非核心连接的空闲超时时间,10分钟（毫秒）
        config.setMaxLifetime(1800000); // 设置连接的最大生命周期,30分钟（毫秒）
        config.setConnectionTestQuery("SELECT 1"); // 设置连接验证时执行的SQL语句
        config.setValidationTimeout(150000); // 设置连接验证的超时时间（毫秒）
        config.setKeepaliveTime(120000); // 设置连接保活的时间间隔（毫秒）
        config.setConnectionTimeout(60000); // 设置获取连接的最大等待时间（毫秒）
        config.setLeakDetectionThreshold(120000); // 设置检测连接被占用但未归还的时间阈值，超过该时间未关闭的连接会被标记为泄漏
        // MySQL特有优化
        config.addDataSourceProperty("cachePrepStmts", "true");  // 启用预编译缓存
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource dataSource = new HikariDataSource(config);
        return new DbContext("", dataSource);
    }

    @Bean
    public DbContext getDbContext2() throws Exception {
        Properties props = new Properties();
        props.setProperty("url", url);
        props.setProperty("username", username);
        props.setProperty("password", password);
        props.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");

        // 可选：设置其他配置
        props.setProperty("initialSize", "15"); // 初始化连接数
        props.setProperty("maxActive", "30"); // 最大连接数
        props.setProperty("minIdle", "15"); // 核心连接数
        props.setProperty("minEvictableIdleTimeMillis", "60000"); // 连接会被销毁的最小空闲时间（毫秒）
        props.setProperty("maxEvictableIdleTimeMillis", "25200000"); // 连接会被销毁的最大空闲时间（毫秒）
        props.setProperty("testOnBorrow", "false"); // 获取连接时是否校验连接有效性
        props.setProperty("testOnReturn", "false"); // 归还连接时是否校验连接有效性
        props.setProperty("testWhileIdle", "true"); // 是否对空闲连接进行有效性校验
        props.setProperty("validationQuery", "SELECT 1"); // 校验连接时执行的SQL语句
        props.setProperty("validationQueryTimeout", "5"); // 校验连接的最大等待时间（秒）
        props.setProperty("keepAlive", "true"); // 连接保活的总开关
        props.setProperty("keepAliveBetweenTimeMillis", "120000"); // 连接保活的时间间隔（毫秒）
        props.setProperty("timeBetweenEvictionRunsMillis", "60000"); // 销毁线程执行任务的频率（毫秒）

        DataSource dataSource = DruidDataSourceFactory.createDataSource(props);
        return new DbContext("", dataSource);
    }
}
