package common.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class QuartzJob extends QuartzJobBean {


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            System.out.println(context.getScheduler().getSchedulerInstanceId());

            System.out.println("==========name:"+context.getJobDetail().getKey().getName());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
