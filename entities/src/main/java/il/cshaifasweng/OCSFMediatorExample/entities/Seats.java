package il.cshaifasweng.OCSFMediatorExample.entities;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.Serializable;

public class Seats implements Serializable {
    private static final long serialVersionUID = -2779473215117112413L;
    private Boolean[][] seats;
    int Rowsnum;
    public Seats(){}
    public Seats (int capacity){
         Rowsnum= (int) Math.round(capacity/10.0);
        seats=new Boolean[10][Rowsnum];
        for(int i=0;i<10;++i){
            for(int j=0;j<Rowsnum;++j){
                seats[i][j]=false;
            }
        }
    }
    public void ReserveSeat(int Row,int Col){
        seats[Col][Row]=true;
    }
    public Button getSeat(int Row, int Col){
        Button btn=new Button();
        btn.setStyle("-fx-max-width: 300");
        /*btn.setStyle( " -fx-padding: 5px;" +
                "-fx-border-insets:5px;" +
                "-fx-background-insets:5px;");*/
        if(seats[Row][Col]==false) {
            //System.out.println("not reserved");
            btn.setText("Reserve");
            btn.setStyle("-fx-background-color:#34eb80");
        }
        else{
           // System.out.println("reserved");
            btn.setText("Reserved");
            btn.setStyle("-fx-background-color:red");
            btn.setDisable(true);
        }
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String str="Seat, Row: "+Row+" Col: "+Col ;
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();
                thisStage.close();
            }
        });

       return  btn;
    }

    public int getRowsnum() {
        return Rowsnum;
    }
    public boolean getSeatInfo(int Row,int Col){
        return  seats[Col][Row];
    }
    public void setRowsnum(int rowsnum) {
        Rowsnum = rowsnum;
    }
}
