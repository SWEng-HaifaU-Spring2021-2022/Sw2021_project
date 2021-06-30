/**
 * Sample Skeleton for 'PurpleCard.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import il.cshaifasweng.OCSFMediatorExample.entities.Bundle;
import il.cshaifasweng.OCSFMediatorExample.entities.PurpleCard;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class PurpleCardController {

    ObservableList<PurpleCard> list = FXCollections.observableArrayList();


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private Tab AddInstTab;

    @FXML
    private Tab EditInstTab;

    @FXML
    private TabPane TabPane;

    @FXML // fx:id="AddSDate"
    private DatePicker AddSDate; // Value injected by FXMLLoader

    @FXML // fx:id="AddEDate"
    private DatePicker AddEDate; // Value injected by FXMLLoader

    @FXML // fx:id="AddFScreening"
    private CheckBox AddFScreening; // Value injected by FXMLLoader

    @FXML // fx:id="AddCap"
    private TextField AddCap; // Value injected by FXMLLoader

    @FXML // fx:id="Add"
    private Button Add; // Value injected by FXMLLoader

    @FXML // fx:id="InstTable"
    private TableView<PurpleCard> InstTable; // Value injected by FXMLLoader

    @FXML // fx:id="sDateCol"
    private TableColumn<PurpleCard, String> sDateCol; // Value injected by FXMLLoader

    @FXML // fx:id="eDateCol"
    private TableColumn<PurpleCard, String> eDateCol; // Value injected by FXMLLoader

    @FXML // fx:id="ProjCol"
    private TableColumn<PurpleCard, Boolean> ProjCol; // Value injected by FXMLLoader

    @FXML // fx:id="mCapCol"
    private TableColumn<PurpleCard, String> mCapCol; // Value injected by FXMLLoader

    @FXML // fx:id="EditSDate"
    private DatePicker EditSDate; // Value injected by FXMLLoader

    @FXML // fx:id="EditEDate"
    private DatePicker EditEDate; // Value injected by FXMLLoader

    @FXML // fx:id="EditFScreening"
    private CheckBox EditFScreening; // Value injected by FXMLLoader

    @FXML // fx:id="EditCap"
    private TextField EditCap; // Value injected by FXMLLoader

    @FXML // fx:id="EditApply"
    private Button EditApply; // Value injected by FXMLLoader

    @FXML // fx:id="AddMsgLabel"
    private Label AddMsgLabel; // Value injected by FXMLLoader

    @FXML
    private Label EditMsgLabel;

    @FXML
    private Button DeleteBtn;


    @FXML
    void addInst(ActionEvent event) {
        String maxCap = "0";
        if (AddFScreening.isSelected())
            maxCap += AddCap.getText();
        PurpleCard card = new PurpleCard(AddSDate.getValue(), AddEDate.getValue(), Integer.parseInt(maxCap), AddFScreening.isSelected());
        msgObject msg = new msgObject("AddInstruction", card);

        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (SimpleClient.obj == null || msg.getMsg().equals("AddedInstruction")) {
            try {
                AddMsgLabel.setText("Adding instruction.");
                TimeUnit.MILLISECONDS.sleep(250);
                AddMsgLabel.setText("Adding instruction..");
                TimeUnit.MILLISECONDS.sleep(250);
                AddMsgLabel.setText("Adding instruction...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            msg = (msgObject) SimpleClient.obj;
            if (msg != null && msg.getMsg().equals("AddedInstruction")) break;
        }
        AddSDate.setValue(null);
        AddEDate.setValue(null);
        AddCap.setText("");
        AddFScreening.setSelected(false);
        AddCap.setVisible(false);
        AddMsgLabel.setText("Instruction has been successfully added!");

        loadData();
    }

    @FXML
    void AddFrontCB(ActionEvent event) {
        if (AddFScreening.isSelected()) AddCap.setVisible(true);
        else AddCap.setVisible(false);
    }

    @FXML
    void EditFScreeningCB(ActionEvent event) {
        if (EditFScreening.isSelected()) {
            EditCap.setVisible(true);
            EditCap.setText("");
        } else EditCap.setVisible(false);
    }

    @FXML
    void applyEdit(ActionEvent event) {

        if (InstTable.getSelectionModel().getFocusedIndex() != -1 && InstTable.getSelectionModel().getSelectedItem() != null) {
            PurpleCard card = InstTable.getSelectionModel().getSelectedItem();
            String maxCap = "0";
            if (EditCap.isVisible())
                maxCap += EditCap.getText();
            card.setProjAllowed(EditFScreening.isSelected());
            card.setMaxCap(Integer.parseInt(maxCap));
            card.setStart(EditSDate.getValue());
            card.setEnd(EditEDate.getValue());
            msgObject msg = new msgObject("UpdateInstruction", card);
            try {
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (SimpleClient.obj == null || !msg.getMsg().equals("UpdatedInstruction")) {
                try {
                    EditMsgLabel.setText("Updating instruction.");
                    TimeUnit.MILLISECONDS.sleep(250);
                    EditMsgLabel.setText("Updating instruction..");
                    TimeUnit.MILLISECONDS.sleep(250);
                    EditMsgLabel.setText("Updating instruction...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msg = (msgObject) SimpleClient.obj;
                if (msg != null && msg.getMsg().equals("UpdatedInstruction")) break;
            }

            loadData();

            EditMsgLabel.setText("Instruction updated!");

        } else EditMsgLabel.setText("Please select an instruction to update!");


    }

    @FXML
    void DeleteInstruction(ActionEvent event) {

        if (InstTable.getSelectionModel().getFocusedIndex() != -1 && InstTable.getSelectionModel().getSelectedItem() != null) {
            PurpleCard card = InstTable.getSelectionModel().getSelectedItem();
            msgObject msg = new msgObject("DeleteInstruction", card);
            try {
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (SimpleClient.obj == null || !msg.getMsg().equals("DeletedInstruction")) {
                try {
                    EditMsgLabel.setText("Deleting instruction.");
                    TimeUnit.MILLISECONDS.sleep(250);
                    EditMsgLabel.setText("Deleting instruction..");
                    TimeUnit.MILLISECONDS.sleep(250);
                    EditMsgLabel.setText("Deleting instruction...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msg = (msgObject) SimpleClient.obj;
                if (msg != null && msg.getMsg().equals("DeletedInstruction")) break;
            }

            loadData();

            EditMsgLabel.setText("Instruction deleted!");

        } else EditMsgLabel.setText("Please select an instruction to delete!");


    }


    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert AddSDate != null : "fx:id=\"AddSDate\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert AddEDate != null : "fx:id=\"AddEDate\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert AddFScreening != null : "fx:id=\"AddFScreening\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert AddCap != null : "fx:id=\"AddCap\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert Add != null : "fx:id=\"Add\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert InstTable != null : "fx:id=\"InstTable\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert sDateCol != null : "fx:id=\"sDateCol\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert eDateCol != null : "fx:id=\"eDateCol\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert ProjCol != null : "fx:id=\"ProjCol\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert mCapCol != null : "fx:id=\"mCapCol\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert EditSDate != null : "fx:id=\"EditSDate\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert EditEDate != null : "fx:id=\"EditEDate\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert EditFScreening != null : "fx:id=\"EditFScreening\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert EditCap != null : "fx:id=\"EditCap\" was not injected: check your FXML file 'PurpleCard.fxml'.";
        assert EditApply != null : "fx:id=\"EditApply\" was not injected: check your FXML file 'PurpleCard.fxml'.";

        InstTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if (InstTable.getSelectionModel().getSelectedItem() != null) {
                    PurpleCard pc = InstTable.getSelectionModel().getSelectedItem();
                    EditSDate.setValue(pc.getStart());
                    EditEDate.setValue(pc.getEnd());
                    if (pc.getProjAllowed()) {
                        EditFScreening.setSelected(true);
                        EditCap.setVisible(true);
                        EditCap.setText(Integer.toString(pc.getMaxCap()));
                    } else {
                        EditFScreening.setSelected(false);
                        EditCap.setVisible(false);
                    }
                }
            }
        });

        AddCap.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    AddCap.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        TabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                if (TabPane.getSelectionModel().getSelectedIndex() == 1)
                    loadData();
            }
        });

        AddCap.setVisible(false);

        Callback<TableColumn<PurpleCard, Boolean>, TableCell<PurpleCard, Boolean>> booleanCellFactory =
                p -> new BooleanCell();
        ProjCol.setCellValueFactory(new PropertyValueFactory<>("ProjAllow"));
        ProjCol.setCellFactory(booleanCellFactory);
        mCapCol.setCellValueFactory(new PropertyValueFactory<>("maxCap"));
        sDateCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        eDateCol.setCellValueFactory(new PropertyValueFactory<>("end"));


        loadData();

    }


    void loadData() {
        msgObject msg = new msgObject("getAllInstructions");
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.clear();
        while (SimpleClient.obj == null || !msg.getMsg().equals("SentPurpleCards")) {
            try {
                TimeUnit.MILLISECONDS.sleep(250);
                if (SimpleClient.obj != null && msg != null && msg.getMsg().equals("SentPurpleCards")) break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(SimpleClient.obj.getClass().equals(msgObject.class)){
                msg = (msgObject) SimpleClient.obj;
            }
        }

        List<PurpleCard> pList = (List<PurpleCard>) (((msgObject) SimpleClient.obj).getObject());
        for (PurpleCard p : pList) {
            list.add(p);
            //  System.out.println(p.getProjAllowed());
        }
        InstTable.setItems(list);
        InstTable.refresh();
    }

}
