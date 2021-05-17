/**
 * Sample Skeleton for 'MovieTimeEdit.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent ;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EditTimeController implements Initializable {
    private final ObservableList<MovieShow> data = FXCollections.observableArrayList();

    @FXML // fx:id="ShowTimeTable"
    private TableView<MovieShow> ShowTimeTable; // Value injected by FXMLLoader

    @FXML // fx:id="showidCol"
    private TableColumn<MovieShow, Number> showidCol; // Value injected by FXMLLoader

    @FXML // fx:id="showTimeCol"
    private TableColumn<MovieShow, LocalDateTime> showTimeCol; // Value injected by FXMLLoader


    @FXML // fx:id="showtTimeTextBox"
    private TextField showtTimeTextBox; // Value injected by FXMLLoader

    @FXML // fx:id="UpdateButton"
    private Button UpdateButton; // Value injected by FXMLLoader

    @FXML
    void getSelected(MouseEvent event) {
        int index=ShowTimeTable.getSelectionModel().getSelectedIndex();
        if(index<=-1) {
            return;
        }

        showtTimeTextBox.setText(showTimeCol.getCellData(index).toString());

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();

    }
    private void initCol() {//TODO: update it to match the final class attributes
        showidCol.setCellValueFactory(new PropertyValueFactory<>("showid"));
        showTimeCol.setCellValueFactory(new PropertyValueFactory<>("showdate"));
    }
    public static void autoResizeColumns( TableView<?> table )//method to reszie columns taken from StackOverFlow
    {
        //Set the right policy
        table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach( (column) ->
        {
            //Minimal width = columnheader
            Text t = new Text( column.getText() );
            double max = t.getLayoutBounds().getWidth();
            for ( int i = 0; i < table.getItems().size(); i++ )
            {
                //cell must not be empty
                if ( column.getCellData( i ) != null )
                {
                    t = new Text( column.getCellData( i ).toString() );
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if ( calcwidth > max )
                    {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-widht with some extra space
            column.setPrefWidth( max + 10.0d );
        } );
    }
    @FXML
    void updateSelectedShowTime(ActionEvent event) {//TODO:send a request to the server to update the show time
        Stage stage = (Stage) UpdateButton.getScene().getWindow();
        stage.close();
    }
    public void inflatUI(Movie movie){//TODO: update it after getting the entities and a DB connection
       // MovieShow MS=new MovieShow(movie.getMovieid(),movie.getShowTime());
        initCol();
        //data.add(MS);
        ShowTimeTable.getItems().setAll(data);
        autoResizeColumns(ShowTimeTable);
    }

}
