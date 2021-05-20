/**
 * Sample Skeleton for 'Catalog.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

import com.sun.prism.Image;
import il.cshaifasweng.OCSFMediatorExample.entities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.*;

public class CatalogController implements Initializable {

    static ObservableList<TheaterMovie> list = FXCollections.observableArrayList();

    @FXML // fx:id="homePage"
    private Button homePage; // Value injected by FXMLLoader

    @FXML // fx:id="MoviesTable"
    private TableView<TheaterMovie> MoviesTable; // Value injected by FXMLLoader

    @FXML // fx:id="imageCol"
    private TableColumn<TheaterMovie, Image> imageCol; // Value injected by FXMLLoader

    @FXML // fx:id="nameCol"
    private TableColumn<TheaterMovie, String> nameCol; // Value injected by FXMLLoader

    @FXML // fx:id="hebName"
    private TableColumn<TheaterMovie, String> hebName; // Value injected by FXMLLoader

    @FXML // fx:id="actorsCol"
    private TableColumn<TheaterMovie, String> actorsCol; // Value injected by FXMLLoader

    @FXML // fx:id="GenerCol"
    private TableColumn<TheaterMovie, String> GenerCol; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionCol"
    private TableColumn<TheaterMovie, String> descriptionCol; // Value injected by FXMLLoader

    @FXML // fx:id="producerCol"
    private TableColumn<TheaterMovie, String> producerCol; // Value injected by FXMLLoader

    @FXML // fx:id="EditBtn"
    private Button EditBtn; // Value injected by FXMLLoader

    @FXML // fx:id="testLabel"
    private Label testLabel; // Value injected by FXMLLoader

    public static TheaterMovie selectedMovie = new TheaterMovie();

    @FXML
    void editMovieBtn(ActionEvent event) throws IOException {
        int index = MoviesTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        selectedMovie = MoviesTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedMovie.getEngName());
        msgObject msg = new msgObject("#getshows", MoviesTable.getSelectionModel().getSelectedItem().getMovieId());
        App.getClient().sendToServer(msg);
        System.out.println("message sent to server to get all moviesshows for a the selcted movie");
    }

    public void openEditPage() throws IOException {

        //Movie selectedMovie = MoviesTable.getSelectionModel().getSelectedItem();
        //System.out.println("check1");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieTimeEdit.fxml"));
        Parent parent = loader.load();
        //System.out.println("check2");
        EditTimeController controller = (EditTimeController) loader.getController();
        //System.out.println("check3");
        System.out.println(selectedMovie.getEngName());
        controller.inflatUI(selectedMovie);
        //System.out.println("check4");
        Stage stage = new Stage();
        //System.out.println("check5");
        stage.setTitle("Edit Movie");
        stage.setScene(new Scene(parent));
        stage.show();
        stage.setOnHiding((e) -> {
            handleRefresh(new ActionEvent());

        });


    }

    private void handleRefresh(ActionEvent actionEvent) {
        // TODO Auto-generated method stub

    }

    private void initCol() {
        try {
            imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("engName"));
            hebName.setCellValueFactory(new PropertyValueFactory<>("hebName"));
            actorsCol.setCellValueFactory(new PropertyValueFactory<>("actors"));
            GenerCol.setCellValueFactory(new PropertyValueFactory<>("genere"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            producerCol.setCellValueFactory(new PropertyValueFactory<>("producer"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        msgObject Smsg = new msgObject("#getAllMovies", null);
        msgObject Cmsg = new msgObject("", null);
        try {
            App.getClient().sendToServer(Smsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message sent to server to get all movies");
        try {
            Cmsg = (msgObject) SimpleClient.getObj();
            List<TheaterMovie> m = (List<TheaterMovie>) Cmsg.getObject();
            loadData(m);
            autoResizeColumns(MoviesTable);
            System.out.println("done initialize");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void loadData(List<TheaterMovie> movieList) {
        try {
            list.clear();
            for (TheaterMovie m : movieList) {
                list.add(m);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        MoviesTable.setItems(list);
    }

    @FXML
    void goHomePage(ActionEvent event) throws IOException {
        try {
            App.setRoot("Primary");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    public static void autoResizeColumns(TableView<?> table)// method to reszie columns taken from StackOverFlow
    {
        // Set the right policy
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach((column) -> {
            // Minimal width = columnheader
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                // cell must not be empty
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    // remember new max-width
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            // set the new max-widht with some extra space
            column.setPrefWidth(max + 10.0d);
        });
    }


}
