package common.config.database;

import cn.hutool.core.lang.Snowflake;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "snowflake")
public class SnowFlakeConfiguration {
    /**
     * 工作节点ID
     */
    private Long workerId;

    /**
     * 数据中心ID
     */
    private Long datacenterId;


    /**
     * 初始化SnowflakeIdWorker Bean
     *
     * @return SnowflakeIdWorker
     */
    @Bean
    public Snowflake snowflake() {
        return new Snowflake(workerId, datacenterId);
    }
}