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
import java.util.logging.Logger;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;

/**
 * Sample Skeleton for 'Catalog.fxml' Controller Class
 */


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class CatalogController implements Initializable  {
    private final ObservableList<Movie> data = FXCollections.observableArrayList();
//ToDO:fix edit the attributes names
    @FXML // fx:id="homePage"
    private Button homePage; // Value injected by FXMLLoader

    @FXML // fx:id="MoviesTable"
    private TableView<Movie> MoviesTable; // Value injected by FXMLLoader

    @FXML // fx:id="imageCol"
    private TableColumn<Movie, String> imageCol; // Value injected by FXMLLoader

    @FXML // fx:id="nameCol"
    private TableColumn<Movie, String> nameCol; // Value injected by FXMLLoader

    @FXML // fx:id="hebName"
    private TableColumn<Movie, String> hebName; // Value injected by FXMLLoader

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


    @FXML // fx:id="EditBtn"
    private Button EditBtn;

    @FXML // fx:id="testLabel"
    private Label testLabel; // Value injected by FXMLLoader

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
    private void initCol(){ //TODO: update it to match the final class attributes
      imageCol.setCellValueFactory(new PropertyValueFactory<>("img"));
      nameCol.setCellValueFactory(new PropertyValueFactory<>("EngName"));
      hebName.setCellValueFactory(new PropertyValueFactory<>("HebNmae"));
      actorsCol.setCellValueFactory(new PropertyValueFactory<>("Actors"));
      producerCol.setCellValueFactory(new PropertyValueFactory<>("Producer"));
      descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
      ShowtimeCol.setCellValueFactory(new PropertyValueFactory<>("ShowTime"));
      GenerCol.setCellValueFactory(new PropertyValueFactory<>("Genere"));
      ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
      ShowsCol.setCellValueFactory(new PropertyValueFactory<>("showInTheater"));
  }

  private void loadData(){//TODO: send a request to the server to get all the movies
      data.clear();
    /*  LocalDateTime myObj = LocalDateTime.now();
      ImageView emp0photo = new ImageView(new Image(getClass().getResourceAsStream("test.jpg")));
      emp0photo.setFitHeight(100);
      emp0photo.setFitWidth(100);
      Movie m=new Movie(1,emp0photo,"test","טסט","wajeeh, alex,daniDev","WB","testmovie",4, myObj,true,"Action");
      data.add(m);*/
       MoviesTable.getItems().setAll(data);
       autoResizeColumns(MoviesTable);
  }
    @FXML
    private void handleRefresh(ActionEvent event) {
        testLabel.setText("Success");
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
    void editMovieBtn(ActionEvent event) throws  IOException {
        try {
            int index=MoviesTable.getSelectionModel().getSelectedIndex();
            if(index<=-1) {
                return;
            }
            Movie selectedMovie=MoviesTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieTimeEdit.fxml"));
            Parent parent = loader.load();
            EditTimeController controller=(EditTimeController) loader.getController();
            controller.inflatUI(selectedMovie);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Movie");
            stage.setScene(new Scene(parent));
            stage.show();

            stage.setOnHiding((e) -> {
                handleRefresh(new ActionEvent());
            });
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("error Showing edit page");
        }

    }


}


