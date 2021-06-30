package il.cshaifasweng.OCSFMediatorExample.server;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;

import static java.time.temporal.ChronoUnit.HOURS;
public class ComplaintSender implements  Job{
	public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("executing.....");
        ArrayList<Complaint> complaintList= DBase.getComplaint();//ArrayList<Complaint> getComplaint()
        for(Complaint cm:complaintList){
            System.out.println(cm.getEmail());
            LocalTime lt= LocalTime.now();
            System.out.println(lt);
            Long remainingtime=lt.until(cm.getSendTime(),HOURS);
            System.out.println(remainingtime);
            System.out.println("complaint's sending time "+cm.getSendTime());
            if(remainingtime>=24&& cm.isStatus()=="Not answered"){
                EmailUtil.sendEmailComplaint(cm);
                cm.setStatus("Refund");
                System.out.println("remainder has been sent");
                DBase.UpdateComplaint(cm);
            }
        }
    }
      
}
