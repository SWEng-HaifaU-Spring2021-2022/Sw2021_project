package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class HomeMovieBuyWindowController {
    HomeMovie homeMovie;
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
    private Label TicketPriceLabel;

    @FXML
    private TextField VisaText;

    @FXML
    private TextField CvvText;

    @FXML
    private TextField startingHourText;

    @FXML
    private TextField expirationHourText;

    @FXML
    private Button buyBtn;

    @FXML
    private DatePicker linkDate;

    @FXML
    void initialize() {
        assert MovieNameLabel != null : "fx:id=\"MovieNameLabel\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert MovieImage != null : "fx:id=\"MovieImage\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert BuyerNameText != null : "fx:id=\"BuyerNameText\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert EmailTExt != null : "fx:id=\"EmailTExt\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert TicketPriceLabel != null : "fx:id=\"TicketPriceLabel\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert VisaText != null : "fx:id=\"VisaText\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert CvvText != null : "fx:id=\"CvvText\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";

    }
    public void setDetails(HomeMovie homeMovie){
        // curtheaterMovie=theatermovie;
        this.homeMovie=homeMovie;
        MovieNameLabel.setText(homeMovie.getEngName()+" / "+ homeMovie.getHebName());
        MovieImage.setImage(homeMovie.getImageProperty());
        TicketPriceLabel.setText(TicketPriceLabel.getText()+" :"+homeMovie.getEntryPrice());

    }
    @FXML
    void BuyHomeMovie(ActionEvent event) throws IOException {
        String buyerName=BuyerNameText.getText();
        String buyerEmail=EmailTExt.getText();
        String startHour=startingHourText.getText();
        String expirationHour= expirationHourText.getText();
        String visaNum=VisaText.getText();
        String cvv=CvvText.getText();
        LocalDate lD=linkDate.getValue();
        LocalTime lt1=LocalTime.parse(startHour);
        LocalTime lt2=LocalTime.parse(expirationHour);
        System.out.println("lt1= "+ lt1);
        System.out.println("lt2=" + lt2);
        HomeLinkTicket HLT=new HomeLinkTicket(buyerName,buyerEmail,lD,lt1,lt2,visaNum,cvv,this.homeMovie.getEngName(),this.homeMovie.getLink());
        msgObject msg=new msgObject("#addHomeTicket",HLT);
        SimpleClient.getClient().sendToServer(msg);
        BuyerNameText.clear();
        EmailTExt.clear();
        startingHourText.clear();
        expirationHourText.clear();
        VisaText.clear();
        CvvText.clear();
        linkDate.setValue(null);
    }
}
