package il.cshaifasweng.OCSFMediatorExample.server;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class SchedulerSender {
    public static void  startJobScheduling(){
        System.out.println("the remainder sender is working");
        try{
            JobDetail job1 = JobBuilder.newJob(RemainderSender.class)
                    .withIdentity("job1", "group1").build();

            Trigger trigger1 = TriggerBuilder.newTrigger()
                    .withIdentity("cronTrigger1", "group1")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?"))
                    .build();

            Scheduler scheduler1 = new StdSchedulerFactory().getScheduler();
            scheduler1.start();
            scheduler1.scheduleJob(job1, trigger1);
            JobDetail job2 = JobBuilder.newJob(ComplaintSender.class)
					.withIdentity("job2", "group2").build();
			
			Trigger trigger2 = TriggerBuilder.newTrigger()
					.withIdentity("cronTrigger2", "group2")
					.withSchedule(CronScheduleBuilder.cronSchedule("0/45 * * * * ?"))
					.build();
			
			Scheduler scheduler2 = new StdSchedulerFactory().getScheduler();
			scheduler2.start();
			scheduler2.scheduleJob(job2, trigger2);
            Thread.sleep(100000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
