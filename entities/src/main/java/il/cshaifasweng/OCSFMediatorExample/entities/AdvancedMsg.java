package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdvancedMsg implements Serializable{
    String msg;
    List<Object> objectList;
    public AdvancedMsg(){
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public  AdvancedMsg(String msg){
        this.msg=msg;
        this.objectList=new ArrayList<>();
    }

    public List<Object> getObjectList() {
        return objectList;
    }
    public void addobject(Object obj){
        this.objectList.add(obj);
    }
}
