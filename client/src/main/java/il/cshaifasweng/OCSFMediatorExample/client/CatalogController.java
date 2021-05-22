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

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.beans.Observable;
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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.*;

public class CatalogController  implements Initializable {
	
	ObservableList<TheaterMovie> list = FXCollections.observableArrayList();

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

    public static TheaterMovie selectedMovie=new TheaterMovie();
    @FXML
    void editMovieBtn(ActionEvent event) throws IOException {
			int index = MoviesTable.getSelectionModel().getSelectedIndex();
			if (index <= -1) {
				return;
			}
			selectedMovie=MoviesTable.getSelectionModel().getSelectedItem();
			msgObject msg=new msgObject("#getshows",MoviesTable.getSelectionModel().getSelectedItem().getMovieId());
        	SimpleClient.getClient().sendToServer(msg);
        	System.out.println("message sent to server to get all moviesshows for a the selcted movie");	
    }
    
    public void openEditPage() throws IOException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieTimeEdit.fxml"));
			Parent parent = loader.load();
			EditTimeController controller = (EditTimeController) loader.getController();
			controller.inflatUI(selectedMovie);
			Stage stage = new Stage();
			stage.setTitle("Edit Movie");
			stage.setScene(new Scene(parent));
			stage.show();
			stage.setOnHiding((e) -> {
				handleRefresh(new ActionEvent());

			});
    	
    	
    	
    }
    private void handleRefresh(ActionEvent actionEvent) {
		try {
			//SimpleClient.getClient().sendToServer("#warning");
			msgObject msg=new msgObject("#getAllMovies");
			SimpleClient.getClient().sendToServer(msg);
			System.out.println("message sent to server to get all movies");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private  void initCol() {
    	try {
    		imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
    		
        	nameCol.setCellValueFactory(new PropertyValueFactory<>("engName"));
        	hebName.setCellValueFactory(new PropertyValueFactory<>("hebName"));
        	actorsCol.setCellValueFactory(new PropertyValueFactory<>("actors"));
        	GenerCol.setCellValueFactory(new PropertyValueFactory<>("genere"));
        	descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        	producerCol.setCellValueFactory(new PropertyValueFactory<>("producer"));

    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	initCol();
    	List<TheaterMovie>m= (List<TheaterMovie>)SimpleClient.obj;
    	loadData(m);
    	autoResizeColumns(MoviesTable);
    	System.out.println("done initialize");
    }
    public void loadData(List<TheaterMovie> movieList) {
    	try {
    		list.clear();
        	for(TheaterMovie m: movieList) {
        		list.add(m);
        	}
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	MoviesTable.setItems(list);
    }

    @FXML
    void goHomePage(ActionEvent event)throws IOException {
    	try {
    		App.setRoot("Primary");
    	}
    	catch(IOException ex){
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

	@FXML
	void ShowScreeningTime(MouseEvent event) {
		int index=MoviesTable.getSelectionModel().getSelectedIndex();
		if(index<=-1) {
			return;
		}
		else{
			selectedMovie=MoviesTable.getSelectionModel().getSelectedItem();
			List<MovieShow>templist=selectedMovie.getMSList();
			String str="";
			System.out.println(templist);
			for (MovieShow ms:templist){

				str+=ms.toString()+"\n";
			}
			testLabel.setText(str);
		/*	msgObject msg=new msgObject("#getshowsdisplay",MoviesTable.getSelectionModel().getSelectedItem().getMovieId());
			try {
				SimpleClient.getClient().sendToServer(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("message sent to server to get all moviesshows for a the selcted movie to display it");
			while(SimpleClient.obj!=null){
				System.out.println("obj is null");
			}*/
		}

	}

}
