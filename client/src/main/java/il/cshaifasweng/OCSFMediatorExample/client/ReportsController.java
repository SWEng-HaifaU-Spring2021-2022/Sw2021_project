package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import il.cshaifasweng.OCSFMediatorExample.entities.CinemaManager;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        EventBus.getDefault().register(this);
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
                chartBox.getChildren().clear();
                if(SimpleClient.getUser().getPermission()==4||TheaterList.getSelectionModel().getSelectedItem()!=null){

                    getsalesreport(branchid);
                }else{
                    infolabel.setText("Select a Branch");
                }
            }
            if(newValue.equals("Complaints")){
                msgObject msg=new msgObject("#getAllComplaintsReport");
                System.out.println("Complaints sending msg");
                try {
                      SimpleClient.getClient().sendToServer(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if(newValue.equals("Bundles&Links Sales")){
                getBundlesLinksSales();
                chartBox.setVisible(false);
                chartBox.getChildren().clear();
            }
            if (newValue.equals("Refunds")){
                chartBox.setVisible(false);
                chartBox.getChildren().clear();
                msgObject msg=new msgObject("#getRefundValue");
                try {
                    SimpleClient.getClient().sendToServer(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    private void madeChart(List<Complaint>complaintList){
        CategoryAxis xAxis=new CategoryAxis();
        NumberAxis yAxis=new NumberAxis();
        BarChart<String,Number> complaintchart=new BarChart<String,Number>(xAxis,yAxis);
        complaintchart.setTitle("Complaint Number");
        complaintchart.setPrefWidth(846);
        complaintchart.setPrefHeight(846);
        xAxis.setLabel("Day");
        yAxis.setLabel("Complaint Number");
        complaintchart.getXAxis().setStyle("-fx-border-color: OrangeRed transparent transparent; -fx-border-width:3");
        complaintchart.getYAxis().setStyle("-fx-border-color: transparent OrangeRed transparent transparent; -fx-border-width:3");
        complaintchart.setStyle("-fx-background-color: #ffbd05");
        int monthDaysNum=LocalDate.now().getMonth().length(LocalDate.now().isLeapYear());
        LocalDate startDate=LocalDate.now().withDayOfMonth(1);
        LocalDate endDate=startDate.plusDays(monthDaysNum);
        XYChart.Series series=new XYChart.Series();
        for (LocalDate itDate=startDate;!itDate.isAfter(endDate);itDate=itDate.plusDays(1)){
            series.getData().add(new XYChart.Data( itDate.toString(),getNumOfComplaint(complaintList,itDate)));
        }
        complaintchart.getData().add(series);
        chartBox.setVisible(true);
        chartBox.getChildren().add(complaintchart);
        Stage stage=(Stage)mainAnchor.getScene().getWindow();
        Scene scene= (Scene) mainAnchor.getScene();
        scene.setRoot(mainAnchor);
        stage.setScene(scene);
        stage.show();

    }
    private int getNumOfComplaint(List<Complaint>list,LocalDate dt){
        int cnt=0;
        for(Complaint cmp:list){
            if(cmp.getDate().equals(dt)){
                cnt+=1;
            }
        }
        return  cnt;
    }
    private void getBundlesLinksSales(){
        msgObject msg=new msgObject("#getBundlesSalesReport",branchid);
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public  void onReportinfoEvent(ReportinfoEvent event) throws ExecutionException {
        Platform.runLater(()->{
            msgObject eventmsg=(msgObject) event.getReceivedData();
            System.out.println(eventmsg.getMsg());
            if (eventmsg.getMsg().equals("Bundles and links sales")){
                infolabel.setText("Bundles and links sales = "+(int)eventmsg.getObject());
            }
            if (eventmsg.getMsg().equals("branch revenue")){
                infolabel.setText("Sales amount until now for this month = "+(int)eventmsg.getObject());
            }
            if (eventmsg.getMsg().equals("Complaint List Reports")){
                System.out.println("before making the chart");
                madeChart((List<Complaint>) eventmsg.getObject());
                System.out.println("after making the chart");
            }
            if (eventmsg.getMsg().equals("The Refund Value")){
                infolabel.setText("Refund value for this month = "+(int)eventmsg.getObject());
            }
        });


    }
}
