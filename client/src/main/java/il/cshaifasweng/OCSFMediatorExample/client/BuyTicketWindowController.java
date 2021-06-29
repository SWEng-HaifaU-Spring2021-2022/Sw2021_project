package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BuyTicketWindowController {
    private TheaterMovie curtheaterMovie;
    static String SeatInfo;
    private ArrayList<Seat>choosenSeats=new ArrayList<>();
   // public static Stage mapStage;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label MovieNameLabel;

    @FXML
    private ImageView MovieImage;

    @FXML
    private TextField BuyerNameText;

    @FXML
    private TextField EmailTExt;

    @FXML
    private ChoiceBox<MovieShow> DatesList;

    @FXML
    private ChoiceBox<String> SeatLists;

    @FXML
    private TextField VisaText;

    @FXML
    private TextField CvvText;

    @FXML
    private Label TicketPriceLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Label seatInfoLabel;



    @FXML
    void BuyTicket(ActionEvent event) throws IOException {//TODO: after adding the ticket entity
        String buyerName=BuyerNameText.getText();
        String buyeremail=EmailTExt.getText();
        LocalDate screeningDate=DatesList.getValue().getShowDate();
        LocalTime startingTime=LocalTime.parse(DatesList.getValue().getBeginTime());
        LocalTime endingTime=LocalTime.parse(DatesList.getValue().getEndTime());
        String visaNumber=VisaText.getText();
        String cvv=CvvText.getText();
        String brach=DatesList.getValue().getTheater().getLocation();
        String hall=DatesList.getValue().getHallnumber();
        TheaterTicket theaterticket=new TheaterTicket(buyeremail,curtheaterMovie.getEngName(),screeningDate,buyerName,visaNumber,cvv,brach,hall,startingTime,endingTime,DatesList.getSelectionModel().getSelectedItem().getMovieShowId(),DatesList.getSelectionModel().getSelectedItem().getTheater().getTheaterId());
        theaterticket.setReservedSeats(choosenSeats);
        theaterticket.setTotalCost(choosenSeats.size()*curtheaterMovie.getEntryPrice());
        AdvancedMsg advcmsg=new AdvancedMsg("#addTicket");
        advcmsg.addobject(theaterticket);
        advcmsg.addobject(DatesList.getValue());
        //msgObject msg=new msgObject("#addTicket",theaterticket);
        SimpleClient.getClient().sendToServer(advcmsg);
        System.out.println("sending saving request to the server");
      /* Stage thisStage = (Stage) MovieNameLabel.getScene().getWindow();
       thisStage.close();*/


    }

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        assert MovieNameLabel != null : "fx:id=\"MovieNameLabel\" was not injected: check your FXML file 'BuyTicketWindow.fxml'.";
        assert MovieImage != null : "fx:id=\"MovieImage\" was not injected: check your FXML file 'BuyTicketWindow.fxml'.";
        assert BuyerNameText != null : "fx:id=\"BuyerNameText\" was not injected: check your FXML file 'BuyTicketWindow.fxml'.";
        assert EmailTExt != null : "fx:id=\"EmailTExt\" was not injected: check your FXML file 'BuyTicketWindow.fxml'.";
        assert DatesList != null : "fx:id=\"DatesList\" was not injected: check your FXML file 'BuyTicketWindow.fxml'.";
        assert SeatLists != null : "fx:id=\"SeatLists\" was not injected: check your FXML file 'BuyTicketWindow.fxml'.";
        assert VisaText != null : "fx:id=\"VisaText\" was not injected: check your FXML file 'BuyTicketWindow.fxml'.";
        assert CvvText != null : "fx:id=\"CvvText\" was not injected: check your FXML file 'BuyTicketWindow.fxml'.";
        assert TicketPriceLabel != null : "fx:id=\"TicketPriceLabel\" was not injected: check your FXML file 'BuyTicketWindow.fxml'.";

    }
    public void setDetails(TheaterMovie theatermovie){
        curtheaterMovie=theatermovie;
        MovieNameLabel.setText(theatermovie.getEngName()+" / "+ theatermovie.getHebName());
        MovieImage.setImage(theatermovie.getImageProperty());
        TicketPriceLabel.setText(TicketPriceLabel.getText()+" :"+theatermovie.getEntryPrice());
        ObservableList<MovieShow> MSL = FXCollections.observableArrayList();
        MSL.addAll(theatermovie.getMSList());
        DatesList.getItems().clear();
        DatesList.getItems().addAll(MSL);
        DatesList.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)->{

        });
    }
    @FXML
    void openMap(ActionEvent event) throws IOException {
        //seatInfoLabel.setText("");
        /*FXMLLoader loader=new FXMLLoader(getClass().getResource(("SeatReservation.fxml")));
        Parent parent=loader.load();
        SeatReservationController controller=(SeatReservationController) loader.getController();*/
        if(DatesList.getSelectionModel().getSelectedItem()==null){
            messageLabel.setText("Choose a screening date");
            return;
        }
        GridPane rootGrid=new GridPane();
        VBox MapButtons = new VBox(50);

        Seats seats=DatesList.getSelectionModel().getSelectedItem().getSeats();
        for (int i=0;i<seats.getRowsnum();++i){
            HBox ButtonRow=new HBox(50);
            for(int j=0;j<10;++j){
                ButtonRow.getChildren().add(getSeat2(seats,j,i));
            }
            MapButtons.getChildren().add(ButtonRow);
        }
        HBox closingbtnHbox=new HBox(50);
        Button closingbtn=new Button();
        closingbtn.setText("close Hall Map");
        closingbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage thisStage = (Stage)  closingbtn.getScene().getWindow();
                thisStage.close();
            }
        });
        closingbtnHbox.getChildren().add(closingbtn);//adding the close button to the last row
        closingbtnHbox.setAlignment(Pos.CENTER);
        MapButtons.getChildren().add(closingbtnHbox);
        rootGrid.getChildren().add(MapButtons);
        rootGrid.setAlignment(Pos.CENTER);
        //opening the map
        Scene scene=new Scene(rootGrid,500,500);
        Stage stage=new Stage();
        stage.setTitle("Hall Map");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.show();
        stage.setOnHiding((e) -> {
            handelPickingSeat(new ActionEvent());
        });

    }

    public void handelPickingSeat(ActionEvent actionEvent) {
        System.out.println("setting the label");
        String reservedSeats="";
        for(Seat st:choosenSeats){
            reservedSeats+=st.toString();
        }
        seatInfoLabel.setText(reservedSeats);
    }
    public Button getSeat2(Seats seats, int Row, int Col){//I fliped them by mistake
        Button btn=new Button();
        btn.setMinWidth(133);
        btn.setMinHeight(25);
        btn.setText("Row: "+Col+" Col: "+ Row);
        if(seats.getSeatInfo(Col,Row)==false) {
            btn.setText("Row: "+Col+" Col: "+ Row);
            btn.setStyle("-fx-background-color: #34eb6e; ");
            if(choosenSeats.size()!=0){
                for(Seat st:choosenSeats){
                    if(st.getSeatCol()==Col&& st.getSeatRow()==Row){
                        btn.setStyle("-fx-background-color: #ebc934; ");
                        btn.setText("Row: "+Col+" Col: "+ Row+" selected");
                    }
                }
            }
            else{
                btn.setText("Row: "+Col+" Col: "+ Row);
                btn.setStyle("-fx-background-color: #34eb6e; ");
            }
        }
        else{
            // System.out.println("reserved");

            btn.setDisable(true);
            btn.setStyle("-fx-background-color: #eb5f34; ");
            btn.setText("Reserved");
        }
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Seat seat=new Seat();
                seat.setSeatCol(Col);
                seat.setSeatRow(Row);
                if(btn.getText().contains("selected")){
                    btn.setText("Row: "+Col+" Col: "+ Row);
                    btn.setStyle("-fx-background-color: #34eb6e; ");
                    for(Seat st:choosenSeats) {
                        if (seat.getSeatCol() == Col && seat.getSeatRow() == Row) {
                            choosenSeats.remove(st);
                            System.out.println(st.toString());
                            System.out.println("Deleted");
                            break;
                        }
                    }
                }else{
                    choosenSeats.add(seat);
                    btn.setStyle("-fx-background-color: #ebc934; ");
                    btn.setText("Row: "+Col+" Col: "+ Row+" selected");
                }
            }
        });

        return  btn;
    }
    @Subscribe
    public void  onBuyTicketEvent(BuyTicketEvent event){
        if(curtheaterMovie.getMovieId()==event.getTheaterMovie().getMovieId()){
            Platform.runLater(()->{
                setDetails(event.getTheaterMovie());
                BuyerNameText.clear();
                EmailTExt.clear();
                DatesList.setValue(null);
                VisaText.clear();
                CvvText.clear();
            });
        }
    }
}