package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BuyTicketWindowController {
   // private TheaterMovie curtheaterMovie;
    static String SeatInfo;
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
    void BuyTicket(ActionEvent event) {//TODO: after adding the ticket entity

    }

    @FXML
    void initialize() {
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
       // curtheaterMovie=theatermovie;
        MovieNameLabel.setText(theatermovie.getEngName()+" / "+ theatermovie.getHebName());
        MovieImage.setImage(theatermovie.getImageProperty());
        TicketPriceLabel.setText(TicketPriceLabel.getText()+" :"+theatermovie.getEntryPrice());
        ObservableList<MovieShow> MSL = FXCollections.observableArrayList();
        MSL.addAll(theatermovie.getMSList());
        DatesList.getItems().addAll(MSL);
        DatesList.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)->{

        });
    }
    @FXML
    void openMap(ActionEvent event) throws IOException {
        /*FXMLLoader loader=new FXMLLoader(getClass().getResource(("SeatReservation.fxml")));
        Parent parent=loader.load();
        SeatReservationController controller=(SeatReservationController) loader.getController();*/
        if(DatesList.getSelectionModel().getSelectedItem()==null){
            messageLabel.setText("Choose a screening date");
            return;
        }
        GridPane rootGrid=new GridPane();

        Seats seats=DatesList.getSelectionModel().getSelectedItem().getSeats();
        for(int i=0;i<10;++i){
            for (int j=0;j<seats.getRowsnum();++j){
                rootGrid.add(getSeat(seats,i,j),i,j);
                // rootGrid.getChildren().add(seats.getSeat(i,j));
            }
        }
        Scene scene=new Scene(rootGrid,500,500);
        Stage stage=new Stage();
        stage.setTitle("Hall Map");
        stage.setScene(scene);
        stage.show();
        stage.setOnHiding((e) -> {
            handelPickingSeat(new ActionEvent());
        });

    }
    public Button getSeat(Seats seats, int Row, int Col){
        Button btn=new Button();
        btn.setStyle("-fx-max-width: 300");
        /*btn.setStyle( " -fx-padding: 5px;" +
                "-fx-border-insets:5px;" +
                "-fx-background-insets:5px;");*/
        if(seats.getSeatInfo(Row,Col)==false) {
            //System.out.println("not reserved");
            btn.setText("Reserve");
            btn.setStyle("-fx-fill:black; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
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
                BuyTicketWindowController.SeatInfo="Seat, Row: "+Row+" Col: "+Col ;
                System.out.println("Seat, Row: "+Row+" Col: "+Col);
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();
                thisStage.close();
            }
        });

        return  btn;
    }
    public void handelPickingSeat(ActionEvent actionEvent) {
        System.out.println("setting the label");
        seatInfoLabel.setText(SeatInfo);
    }

}