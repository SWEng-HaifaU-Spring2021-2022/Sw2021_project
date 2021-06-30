package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.*;

import java.util.List;

public class ReportinfoEvent {
    private Object receivedData;
    public ReportinfoEvent(msgObject msg){
        this.receivedData=msg;
    }
    public  Object getReceivedData(){
        return  receivedData;
    }
}
