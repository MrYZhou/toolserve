package middle.task.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ScheduleConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public Scheduler scheduler() throws IOException {
        schedulerProperties();
        return  schedulerFactory().getScheduler();
    }
    @Bean
    public SchedulerFactoryBean schedulerFactory() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);


        factory.setQuartzProperties(schedulerProperties());

        //调度标识名 集群中每一个实例都必须使用相同的名称
        factory.setSchedulerName("scheduler");

        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        // 可选，QuartzScheduler
        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        // 设置自动启动，默认为true
        factory.setAutoStartup(true);
        factory.setTaskExecutor(getExecutor() );
        // 延时启动
        factory.setStartupDelay(0);
        return factory;
    }
    @Bean
    public Properties schedulerProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("/application-dev.yml"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public Executor getExecutor() throws IOException {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        int core = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor.setCorePoolSize(core);
        threadPoolExecutor.setMaxPoolSize(core);
        threadPoolExecutor.setQueueCapacity(core);
        return threadPoolExecutor;
    }

}
