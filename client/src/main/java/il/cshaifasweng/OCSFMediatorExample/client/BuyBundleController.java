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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Label mainlabel; // Value injected by FXMLLoader
    @FXML
    private Label instLabel;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete

    void initialize() {
        assert checkOutBtn != null : "fx:id=\"checkOutBtn\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert mainlabel != null : "fx:id=\"mainlabel\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert toPayAmount != null : "fx:id=\"toPayAmount\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert fName != null : "fx:id=\"fName\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert lName != null : "fx:id=\"lName\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'BuyBundle.fxml'.";
        toPayAmount.setText(price + "₪.");
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
    void onEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) checkOut(null);
    }


    @FXML
    void checkOut(ActionEvent event) {
        Bundle bundle = new Bundle(email.getText(), fName.getText(), lName.getText());
        msgObject msg = new msgObject();
        msg.setMsg("BuyBundle");
        msg.setObject(bundle);
        try {
            SimpleClient.getClient().sendToServer(msg);
            while (SimpleClient.obj==null ||
                    !(((msgObject) SimpleClient.obj).getMsg().equals("BundleBought") ||
                    ((msgObject) SimpleClient.obj).getMsg().equals("BuyingBundleError"))) {
                instLabel.setText("Making payment.");
                instLabel.setText("Making payment..");
                instLabel.setText("Making payment...");
            }
            if (((msgObject) SimpleClient.obj).getMsg().equals("BundleBought")) {
                instLabel.setTextFill(Color.color(0, 0.7, 0.3));
                instLabel.setText("Bundle bought successfully!");
            } else {
                instLabel.setTextFill(Color.color(0.8, 0, 0.2));
                instLabel.setText("An error occured, try later!");

            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        try {
            App.setRoot("GridCatalog");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
