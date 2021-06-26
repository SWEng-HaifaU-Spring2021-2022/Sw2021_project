package il.cshaifasweng.OCSFMediatorExample.client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class PurpleCardController {

        @FXML // ResourceBundle that was given to the FXMLLoader
        private ResourceBundle resources;

        @FXML // URL location of the FXML file that was given to the FXMLLoader
        private URL location;

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

        @FXML // fx:id="EditInstList"
        private ListView<?> EditInstList; // Value injected by FXMLLoader

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

        @FXML // This method is called by the FXMLLoader when initialization is complete
        void initialize() {
            assert AddSDate != null : "fx:id=\"AddSDate\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert AddEDate != null : "fx:id=\"AddEDate\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert AddFScreening != null : "fx:id=\"AddFScreening\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert AddCap != null : "fx:id=\"AddCap\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert Add != null : "fx:id=\"Add\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert EditInstList != null : "fx:id=\"EditInstList\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert EditSDate != null : "fx:id=\"EditSDate\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert EditEDate != null : "fx:id=\"EditEDate\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert EditFScreening != null : "fx:id=\"EditFScreening\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert EditCap != null : "fx:id=\"EditCap\" was not injected: check your FXML file 'PurpleCard.fxml'.";
            assert EditApply != null : "fx:id=\"EditApply\" was not injected: check your FXML file 'PurpleCard.fxml'.";

        }

}
