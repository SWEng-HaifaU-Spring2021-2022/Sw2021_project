package il.cshaifasweng.OCSFMediatorExample.server;
import java.time.LocalDate;
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
            LocalDate ld=LocalDate.now();
            LocalTime lt=LocalTime.now();
            if((cm.getDate().plusDays(1).equals(ld)||ld.isAfter(cm.getDate()))&&(lt.isAfter(cm.getSendTime())||lt.equals(cm.getSendTime()))&&cm.isStatus().equals("Not answered")){
                EmailUtil.sendEmailComplaint(cm);
                cm.setStatus("Refund");
                DBase.UpdateComplaint(cm);
            }
        }
    }
      
}
