package middle.task.config;

import middle.task.job.MyJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

// 初始化定时需要,先注释不用
//@Component
public class StartListen implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    Scheduler scheduler;
    /**
     * 必须在生命周期ApplicationListener下注入，其他地方注入可能导致
     * bean还未生成
     */
    @Autowired
    SchedulerFactoryBean schedulerFactory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        JobDetail job1 = JobBuilder.newJob(MyJob.class).withIdentity("job1", "group1")
                .build();

        Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("trigger2", "group2")
                .startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(2))
                .startNow()
                .build();

        try {
            Scheduler scheduler = null;
//         Scheduler   scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler = schedulerFactory.getScheduler();
            scheduler.scheduleJob(job1, trigger1);
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }
}
