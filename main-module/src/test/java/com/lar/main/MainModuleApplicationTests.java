package com.lar.main;

import common.util.EmailTask;
import common.util.RedisUtil;
import middle.quartz.QuartzJob;
import middle.task.email.MailService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@SpringBootTest
class MainModuleApplicationTests {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    EmailTask emailTask;



    @Autowired
    private MailService mailService;

    @Autowired
    Scheduler scheduler;

    @Test
    void jobTest() throws SchedulerException {
        JobDetail job54 = scheduler.getJobDetail(JobKey.jobKey("job54"));
        if(job54==null){
            job54 = JobBuilder.newJob(QuartzJob.class).
                    withIdentity("job54")
                    .build();
        }

        Trigger trigger14322 = scheduler.getTrigger(TriggerKey.triggerKey("trigger14322"));
        if(trigger14322==null){
            trigger14322 = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                    .withIdentity("trigger14322")
//                .forJob(jobDetail)
                    .build();
        }

        scheduler.scheduleJob(job54,trigger14322);
        scheduler.start();
    }


    @Test
    void redisTest() {
        redisUtil.set("testutil","success");
    }



    @Test
    void emailTest() {

        mailService.sendSimpleTextMailActual("发送主题","发送内容",new String[]{"2271952106@qq.com"},null,null,null);

    }


    @Test
    void getload() {
        emailTask.sendMessge("主题","hello","1762861715@qq.com","2271952106@qq.com");
    }


}
