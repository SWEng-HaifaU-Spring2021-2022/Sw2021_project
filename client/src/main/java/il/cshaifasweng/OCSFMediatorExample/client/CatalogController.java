/**
 * Sample Skeleton for 'Catalog.fxml' Controller Class
 */
package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
import javax.security.auth.callback.Callback;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.*;
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

	ObservableList<Movie> list = FXCollections.observableArrayList();


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
	@FXML // fx:id="GenerCol"
	private TableColumn<Movie, String> GenerCol; // Value injected by FXMLLoader
	@FXML // fx:id="descriptionCol"
	private TableColumn<Movie, String> descriptionCol; // Value injected by FXMLLoader
	@FXML // fx:id="producerCol"
	private TableColumn<Movie, String> producerCol; // Value injected by FXMLLoader
	@FXML // fx:id="EditBtn"
	private Button EditBtn; // Value injected by FXMLLoader
	@FXML // fx:id="testLabel"
	private Label testLabel; // Value injected by FXMLLoader
	public static Movie selectedMovie=new TheaterMovie();
	@FXML

	void editMovieBtn(ActionEvent event) throws IOException {
		int index = MoviesTable.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		selectedMovie=MoviesTable.getSelectionModel().getSelectedItem();
		msgObject msg=new msgObject("#getAllTheatres");
		SimpleClient.getClient().sendToServer(msg);
		System.out.println("message sent to server to get all theaters ");
	}

	public void openEditPage(List<Theater> TheatersList) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieTimeEdit.fxml"));
		Parent parent = loader.load();
		EditTimeController controller = (EditTimeController) loader.getController();
		controller.inflatUI(selectedMovie,TheatersList);
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

			imageCol.setCellValueFactory(new PropertyValueFactory<>("ImgURL"));
			imageCol.setCellFactory(param -> new ImageTableCell<>());
			imageCol.prefWidthProperty().bind(MoviesTable.widthProperty().multiply(.2));

			nameCol.setCellValueFactory(new PropertyValueFactory<>("engName"));
			nameCol.prefWidthProperty().bind(MoviesTable.widthProperty().multiply(.13));

			hebName.setCellValueFactory(new PropertyValueFactory<>("hebName"));
			hebName.prefWidthProperty().bind(MoviesTable.widthProperty().multiply(.13));

			actorsCol.setCellValueFactory(new PropertyValueFactory<>("actors"));
			actorsCol.prefWidthProperty().bind(MoviesTable.widthProperty().multiply(.13));

			GenerCol.setCellValueFactory(new PropertyValueFactory<>("genere"));
			GenerCol.prefWidthProperty().bind(MoviesTable.widthProperty().multiply(.13));

			descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
			descriptionCol.prefWidthProperty().bind(MoviesTable.widthProperty().multiply(.15));

			producerCol.setCellValueFactory(new PropertyValueFactory<>("producer"));
			producerCol.prefWidthProperty().bind(MoviesTable.widthProperty().multiply(.13));

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
			//autoResizeColumns(MoviesTable);
		System.out.println("done initialize");
	}

	public void loadData(List<TheaterMovie> movieList) {
		try {
			list.clear();
			for(Movie m: movieList) {
				list.add(m);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		MoviesTable.setItems(list);
		//autoResizeColumns(MoviesTable);

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
				column.setPrefWidth(max);

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
			String str="";
			str+="English Name: "+selectedMovie.getEngName()+"\n Hebrew Name: "+selectedMovie.getHebName()+"\n Actors: "+selectedMovie.getActors()+"\n genere: "+selectedMovie.getGenere()+"\n Description: "+selectedMovie.getDescription()+"\n";
			if (selectedMovie.getClass().equals(TheaterMovie.class)){
				TheaterMovie TM= (TheaterMovie)selectedMovie;
				List<MovieShow>templist=TM.getMSList();
				for (MovieShow ms:templist){
					str+=ms.toString()+"\n";
				}
			}
			else if(selectedMovie.getClass().equals(HomeMovie.class)){
				str+="Home Movie \n";
			}
			else{
				str+="Coming soon ";
			}

			testLabel.setText(str);
		}

	}
	public void openEditPageV2(List<Theater> TheatersList,TheaterMovie TM) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieTimeEdit.fxml"));
		Parent parent = loader.load();
		EditTimeController controller = (EditTimeController) loader.getController();
		controller.inflatUI(selectedMovie,TheatersList);
		Stage stage = new Stage();
		stage.setTitle("Edit Movie");
		stage.setScene(new Scene(parent));
		stage.show();
		stage.setOnHiding((e) -> {
			handleRefresh(new ActionEvent());
		});
	}
}