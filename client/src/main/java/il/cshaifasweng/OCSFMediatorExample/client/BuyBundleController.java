package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.Bundle;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class BuyBundleController {

    static private int price = 20;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="checkOutBtn"
    private Button checkOutBtn; // Value injected by FXMLLoader
    @FXML // fx:id="toPayAmount"
    private Label toPayAmount; // Value injected by FXMLLoader
    @FXML // fx:id="fName"
    private TextField fName; // Value injected by FXMLLoader
    @FXML // fx:id="lName"
    private TextField lName; // Value injected by FXMLLoader
    @FXML // fx:id="email"
    private TextField email; // Value injected by FXMLLoader
    @FXML // fx:id="mainlabel"
    private static Label mainlabel; // Value injected by FXMLLoader

    @FXML
        // This method is called by the FXMLLoader when initialization is complete

    void initialize() {
        assert checkOutBtn != null : "fx:id=\"checkOutBtn\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert mainlabel != null : "fx:id=\"mainlabel\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert toPayAmount != null : "fx:id=\"toPayAmount\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert fName != null : "fx:id=\"fName\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert lName != null : "fx:id=\"lName\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        toPayAmount.setText( price + "₪.");
    /*    assert amountList != null : "fx:id=\"amountList\" was not injected: check your FXML file 'BuyBundle.fxml'.";

        amountList.getItems().add("Amount to buy");
        amountList.getItems().add("1");
        amountList.getItems().add("2");
        amountList.getItems().add("3");
        amountList.getItems().add("4");
        amountList.getItems().add("5");
        amountList.setValue("Amount to buy");
        toPayAmount.setText("0");

        amountList.setOnAction((event) -> {
            int selectedIndex = amountList.getSelectionModel().getSelectedIndex();
            // Object selectedItem = amountList.getSelectionModel().getSelectedItem();
            toPayAmount.setText((selectedIndex * price) + "₪.");

        });*/
    }

    @FXML
    void checkOut(ActionEvent event) {
        Bundle bundle = new Bundle(email.getText(), fName.getText(), lName.getText());
        msgObject msg = new msgObject();
        msg.setMsg("BuyBundle");
        msg.setObject(bundle);
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void showMsg(boolean s)
    {
        if (s) {
            mainlabel.setTextFill(Color.color(0, 0.7, 0.3));
            mainlabel.setText("Bundle bought successfully!");
        }
        else {
            mainlabel.setTextFill(Color.color(0.8, 0, 0.2));
            mainlabel.setText("An error occured, try later!");

        }

    }

}
