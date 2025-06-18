package com.lar.common.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 自定义数据源配置
 */
@Configuration
public class DatabasePoolConfig {
    private static String host;
    private static String port;
    private static String username;
    private static String password;
    private static String dbname;
    @Value("${spring.datasource.host}")
    public void setHost(String val) {
        host = val;
    }

    @Value("${spring.datasource.port}")
    public void setPort(String val) {
        port = val;
    }

    @Value("${spring.datasource.username}")
    public void setUsername(String val) {
        username = val;
    }

    @Value("${spring.datasource.password}")
    public void setPassword(String val) {
        password = val;
    }
    @Value("${spring.datasource.dbname}")
    public void setDbname(String val) {
        dbname = val;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(30); // 设置最大连接池大小,峰值容量
        config.setMinimumIdle(5); // 设置最小空闲连接数,常态空闲连接
        config.setIdleTimeout(600000); // 设置非核心连接的空闲超时时间,10分钟（毫秒）
        config.setMaxLifetime(1800000); // 设置连接的最大生命周期,30分钟（毫秒）
        config.setConnectionTestQuery("SELECT 1"); // 设置连接验证时执行的SQL语句
        config.setValidationTimeout(10000); // 设置连接验证的超时时间（毫秒）
        config.setKeepaliveTime(120000); // 设置连接保活的时间间隔（毫秒）
        config.setConnectionTimeout(60000); // 设置获取连接的最大等待时间（毫秒）
        config.setLeakDetectionThreshold(120000); // 设置检测连接被占用但未归还的时间阈值，超过该时间未关闭的连接会被标记为泄漏
        // MySQL特有优化
        config.addDataSourceProperty("cachePrepStmts", "true");  // 启用预编译缓存
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }
}
