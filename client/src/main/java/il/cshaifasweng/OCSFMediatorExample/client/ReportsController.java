package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.time.MonthDay;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.CinemaManager;
import il.cshaifasweng.OCSFMediatorExample.entities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ReportsController {
    private final ObservableList<Theater> theaterslist = FXCollections.observableArrayList();
    private int branchid;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private HBox chartBox;

    @FXML
    private ChoiceBox<Theater> TheaterList;

    @FXML
    private Label infolabel;

    @FXML
    private ChoiceBox<String> ReportList;

    @FXML
    void openCatalog(ActionEvent event) {
        msgObject msg=new msgObject("#getAllMovies");
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message sent to server to get all movies");
    }

    @FXML
    void initialize() {
        assert TheaterList != null : "fx:id=\"TheaterList\" was not injected: check your FXML file 'Reports.fxml'.";
        assert infolabel != null : "fx:id=\"infolabel\" was not injected: check your FXML file 'Reports.fxml'.";
        assert ReportList != null : "fx:id=\"ReportList\" was not injected: check your FXML file 'Reports.fxml'.";
        if(SimpleClient.getUser().getPermission()!=5){
            TheaterList.setVisible(false);
            CinemaManager cm=(CinemaManager)SimpleClient.getUser();
            branchid=cm.getBranchid();
        }
        loadData();
    }
    void loadData(){
        ReportList.getItems().add("Choose an Option");
        ReportList.getItems().add("Tickets Sales");
        ReportList.getItems().add("Bundles&Links Sales");
        ReportList.getItems().add("Refunds");
        ReportList.getItems().add("Complaints");
        ReportList.setValue("Choose an Option");
        ReportList.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
           if(newValue.equals("Tickets Sales")) {
               chartBox.setVisible(false);
               if(SimpleClient.getUser().getPermission()==4||TheaterList.getSelectionModel().getSelectedItem()!=null)
                   getsalesreport(branchid);
           }
           if(newValue.equals("Complaints")){
               chartBox.setVisible(true);
               CategoryAxis xAxis=new CategoryAxis();
               NumberAxis yAxis=new NumberAxis();
               BarChart<String,Number> complaintchart=new BarChart<String,Number>(xAxis,yAxis);
               complaintchart.setTitle("Complaint Number");
               xAxis.setLabel("DAy");
               yAxis.setLabel("Complaint Number");
               chartBox.getChildren().add(complaintchart);
               Stage stage=(Stage)mainAnchor.getScene().getWindow();
               Scene scene= (Scene) mainAnchor.getScene();
               scene.setRoot(mainAnchor);
               stage.setScene(scene);
               stage.show();

           }
        });
        if (TheaterList.isVisible()){
            List<Theater>theater_list=(List<Theater>) SimpleClient.obj;
            theaterslist.addAll(theater_list);
            TheaterList.getItems().addAll(theaterslist);
            TheaterList.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)->{
                branchid=newValue.getTheaterId();
                ReportList.setValue("Choose an Option");
            });
            //fill the theater list
        }
    }
    private void getsalesreport(int branchid){
        msgObject msg=new msgObject("#getSalesReport",branchid);
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
