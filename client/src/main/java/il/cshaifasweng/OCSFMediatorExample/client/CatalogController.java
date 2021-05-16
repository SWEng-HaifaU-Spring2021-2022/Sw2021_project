/**
 * Sample Skeleton for 'Catalog.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Sample Skeleton for 'Catalog.fxml' Controller Class
 */


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;


public class CatalogController implements Initializable  {
    private final ObservableList<Movie> data = FXCollections.observableArrayList();
    @FXML // fx:id="homePage"
    private Button homePage; // Value injected by FXMLLoader

    @FXML // fx:id="MoviesTable"
    private TableView<Movie> MoviesTable; // Value injected by FXMLLoader

    @FXML // fx:id="imageCol"
    private TableColumn<Movie, String> imageCol; // Value injected by FXMLLoader

    @FXML // fx:id="nameCol"
    private TableColumn<Movie, String> nameCol; // Value injected by FXMLLoader

    @FXML // fx:id="actorsCol"
    private TableColumn<Movie, String> actorsCol; // Value injected by FXMLLoader

    @FXML // fx:id="ratingCol"
    private TableColumn<Movie, Number> ratingCol; // Value injected by FXMLLoader

    @FXML // fx:id="GenerCol"
    private TableColumn<Movie, String> GenerCol; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionCol"
    private TableColumn<Movie, String> descriptionCol; // Value injected by FXMLLoader

    @FXML // fx:id="producerCol"
    private TableColumn<Movie, String> producerCol; // Value injected by FXMLLoader

    @FXML // fx:id="ShowsCol"
    private TableColumn<Movie, Boolean> ShowsCol; // Value injected by FXMLLoader

    @FXML // fx:id="ShowtimeCol"
    private TableColumn<Movie, LocalDateTime> ShowtimeCol; // Value injected by FXMLLoader

    @FXML // fx:id="editCol"
    private TableColumn<Movie, Button> editCol; // Value injected by FXMLLoader

    @FXML // fx:id="editOp"
    private MenuItem editOp; // Value injected by FXMLLoader

    @FXML
    void goHomePage(ActionEvent event)throws IOException {
        try{
            App.setRoot("primary");
        }catch (Exception e){

        }

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();

    }
        private void initCol(){
      imageCol.setCellValueFactory(new PropertyValueFactory<>("img"));
      nameCol.setCellValueFactory(new PropertyValueFactory<>("EngName"));
      actorsCol.setCellValueFactory(new PropertyValueFactory<>("Actors"));
      producerCol.setCellValueFactory(new PropertyValueFactory<>("Producer"));
      descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
      ShowtimeCol.setCellValueFactory(new PropertyValueFactory<>("ShowTime"));
      GenerCol.setCellValueFactory(new PropertyValueFactory<>("Genere"));
      ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
      ShowsCol.setCellValueFactory(new PropertyValueFactory<>("showInTheater"));
  }

  private void loadData(){
      data.clear();
      LocalDateTime myObj = LocalDateTime.now();
      ImageView emp0photo = new ImageView(new Image(getClass().getResourceAsStream("test.jpg")));
      emp0photo.setFitHeight(100);
      emp0photo.setFitWidth(100);
      Movie m=new Movie(1,emp0photo,"test","טסט","wajeeh, alex,daniDev","WB","testmovie",4, myObj,true,"Action");
      data.add(m);
       MoviesTable.getItems().setAll(data);
       autoResizeColumns(MoviesTable);
  }
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
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
    void handleMovieTimeEdit(ActionEvent event) {

    }

}


