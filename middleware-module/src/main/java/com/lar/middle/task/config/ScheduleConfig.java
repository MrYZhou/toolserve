package com.lar.middle.task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ScheduleConfig {

    @Autowired
    private DataSource dataSource;

    // 除了在springboot生命周期配置, 在这里配置的触发器bean会自动被触发，但是这种好像非常耗费内存。推测是quartz频繁访问同一个对象、
//    @Bean
//    public JobDetail testJobDetail() {
//        JobDetail JobDetail = JobBuilder.newJob(Job3.class)
//                .storeDurably() //必须调用该方法，添加任务
//                .build();
//
//        return JobDetail;
//    }
//
//    @Bean
//    public Trigger testTrigger() {
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .forJob(testJobDetail())
//                .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?")) //对触发器配置任务
//                .build();
//        return trigger;
//    }

    // 下面的配置需要生成quartz的sql表才可以使用
//    @Bean
//    public Scheduler scheduler() throws IOException {
//        schedulerProperties();
//        return schedulerFactory().getScheduler();
//    }
//
//    @Bean
//    public SchedulerFactoryBean schedulerFactory() throws IOException {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        factory.setDataSource(dataSource);
//
//
//        factory.setQuartzProperties(schedulerProperties());
//
//        //调度标识名 集群中每一个实例都必须使用相同的名称
//        factory.setSchedulerName("scheduler");
//
//        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
//        // 可选，QuartzScheduler
//        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
//        factory.setOverwriteExistingJobs(true);
//        // 设置自动启动，默认为true
//        factory.setAutoStartup(true);
//        factory.setTaskExecutor(getExecutor());
//        // 延时启动
//        factory.setStartupDelay(0);
//        return factory;
//    }
//
//    @Bean
//    public Properties schedulerProperties() throws IOException {
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
////        propertiesFactoryBean.setLocation(new ClassPathResource("/application-dev.yml"));
//        propertiesFactoryBean.afterPropertiesSet();
//        return propertiesFactoryBean.getObject();
//    }
//
//    @Bean
//    public Executor getExecutor() throws IOException {
//        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
//        int core = Runtime.getRuntime().availableProcessors();
//        threadPoolExecutor.setCorePoolSize(core);
//        threadPoolExecutor.setMaxPoolSize(core);
//        threadPoolExecutor.setQueueCapacity(core);
//        return threadPoolExecutor;
//    }

}
