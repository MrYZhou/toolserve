package common.listener;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class AppStartListen implements ApplicationListener<ContextRefreshedEvent> {


    @Autowired
    Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            JobDetail job54 = scheduler.getJobDetail(JobKey.jobKey("job54"));
            if (job54 == null) {
                job54 = JobBuilder.newJob(QuartzJob.class).
                        withIdentity("job54")
                        .build();
            }
            // 开启定时


            Trigger trigger14322 = null;
            trigger14322 = scheduler.getTrigger(TriggerKey.triggerKey("trigger14322"));
            if (trigger14322 == null) {
                trigger14322 = TriggerBuilder.newTrigger()
                        .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                        .withIdentity("trigger14322")
                .forJob(job54)
                        .build();
            }

            scheduler.scheduleJob( trigger14322);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }



    }
}
