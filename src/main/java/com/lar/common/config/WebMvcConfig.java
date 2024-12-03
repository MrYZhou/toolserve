package com.lar.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    // 定义自定义的 AsyncTaskExecutor Bean
    @Bean(name = "customAsyncTaskExecutor")
    public AsyncTaskExecutor customAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int corePoolSize = Runtime.getRuntime().availableProcessors(); // 核心线程数设置为 CPU 核心数
        int maxPoolSize = corePoolSize * 2; // 最大线程数设置为核心数的两倍
        int queueCapacity = 500; // 队列容量

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(true); // 关闭时等待任务完成
        executor.setAwaitTerminationSeconds(60); // 关闭时最多等待 60 秒

        executor.initialize(); // 初始化线程池

        return executor;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(customAsyncTaskExecutor()); // 使用自定义的 AsyncTaskExecutor替代@Async方法的默认执行器
        configurer.setDefaultTimeout(5000); // 设置异步请求的默认超时时间（毫秒）
    }
}
