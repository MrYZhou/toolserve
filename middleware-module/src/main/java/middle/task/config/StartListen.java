package middle.task.config;

import middle.task.job.Job2;
import middle.task.job.MyJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartListen  implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    Scheduler scheduler;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        JobDetail job1 = JobBuilder.newJob(Job2.class).withIdentity("job1","group1")
                .build();

        Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("trigger2","group2")
                .startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(2))
                .startNow()
                .build();

//        Scheduler scheduler = null;
        try {
//            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(job1,trigger1);
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }
}
