/**
 * Sample Skeleton for 'ViewBundles.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.Bundle;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewBundlesController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="EmailTB"
    private TextField EmailTB; // Value injected by FXMLLoader

    @FXML // fx:id="FindBtn"
    private Button FindBtn; // Value injected by FXMLLoader

    @FXML // fx:id="msgLabel"
    private Label msgLabel; // Value injected by FXMLLoader

    @FXML // fx:id="BundlesTable"
    private TableView<Bundle> BundlesTable; // Value injected by FXMLLoader

    @FXML // fx:id="bundleNo"
    private TableColumn<Bundle, String> bundleNo; // Value injected by FXMLLoader

    @FXML // fx:id="LeftTickets"
    private TableColumn<Bundle, String> LeftTickets; // Value injected by FXMLLoader

    ObservableList<Bundle> list = FXCollections.observableArrayList();


    @FXML
    void ShowBundles(ActionEvent event) {

        msgObject msg = new msgObject("getBundles");
        if(EmailTB.getText()==null || EmailTB.getText()=="")
            msgLabel.setText("Please enter your E-mail");
        else {
            msgLabel.setText("Getting your bundles..");
            msg.setObject(EmailTB.getText());
            try {
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
                msgLabel.setText("An error occurred, try later!");
                return;
            }

            while(true)
            {
                msg = (msgObject)SimpleClient.obj;
                if(msg != null && msg.getMsg().equals("SentYourBundles")) break;
                msgLabel.setText("Getting your bundles.");
                msgLabel.setText("Getting your bundles..");
                msgLabel.setText("Getting your bundles...");

            }

            List<Bundle> Bun = (List<Bundle>) msg.getObject();

            if(Bun == null)
            {
                msgLabel.setText("You don't have any bundles!");
                return;
            } else {
                list.clear();
                for (Bundle b : Bun) {
                    list.add(b);
                }

                BundlesTable.setItems(list);
                msgLabel.setText("Hello " + Bun.get(0).getfName() + " " + Bun.get(0).getlName());
            }
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert EmailTB != null : "fx:id=\"EmailTB\" was not injected: check your FXML file 'ViewBundles.fxml'.";
        assert FindBtn != null : "fx:id=\"FindBtn\" was not injected: check your FXML file 'ViewBundles.fxml'.";
        assert msgLabel != null : "fx:id=\"msgLabel\" was not injected: check your FXML file 'ViewBundles.fxml'.";
        assert BundlesTable != null : "fx:id=\"BundlesTable\" was not injected: check your FXML file 'ViewBundles.fxml'.";
        assert bundleNo != null : "fx:id=\"bundleNo\" was not injected: check your FXML file 'ViewBundles.fxml'.";
        assert LeftTickets != null : "fx:id=\"LeftTickets\" was not injected: check your FXML file 'ViewBundles.fxml'.";

        bundleNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        LeftTickets.setCellValueFactory(new PropertyValueFactory<>("remainingEntries"));


    }
    @FXML
    void goHome(ActionEvent event) {
        try {
            App.setRoot("primary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
