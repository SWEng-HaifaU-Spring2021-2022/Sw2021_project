/**
 * Sample Skeleton for 'GridCatalog.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.HomeMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieShow;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterMovie;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.security.auth.RefreshFailedException;
import javax.security.auth.Refreshable;


import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class GridCatalogController implements Initializable {
	public List<Movie> movieList = (List<Movie>) SimpleClient.obj;
	private List<Movie> reserveList=(List<Movie>) SimpleClient.obj;
	private static int rowsNum = 2;
	private static int colsNum = 2;
	private int page = 1;
	private int pages = 1;
	public static Movie clickedMovie;
	@FXML // fx:id="homeBtn"
	private Button homeBtn; // Value injected by FXMLLoader

	@FXML // fx:id="prevPageBtn"
	private Button prevPageBtn; // Value injected by FXMLLoader

	@FXML // fx:id="CatalogGrid"
	private GridPane CatalogGrid; // Value injected by FXMLLoader

	@FXML // fx:id="nextPageBtn"
	private Button nextPageBtn; // Value injected by FXMLLoader

	@FXML
	private Button addBtn;
	@FXML // fx:id="searchBtn"
	private Button searchBtn; // Value injected by FXMLLoader
	@FXML // fx:id="firstDate"
	private DatePicker firstDate; // Value injected by FXMLLoader
	@FXML // fx:id="lastDate"
	private DatePicker lastDate; // Value injected by FXMLLoader
	@FXML // fx:id="movieType"
	private ChoiceBox<String> movieType; // Value injected by FXMLLoader
	@FXML // fx:id="theaterBranch"
    private ChoiceBox<String> theaterBranch; // Value injected by FXMLLoader
	@FXML
    private Label pagesLabel;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			fillGrids();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*pages = movieList.size() / 4 + 1;
		 pagesLabel.setText("page " + page + " out of " + pages);
		Platform.runLater(() -> {
			CatalogGrid.getChildren().clear();
			for (int i = 0; i < rowsNum; i++) {
				for (int j = 0; j < colsNum; j++) {
					Pair<Parent, Object> viewData = null;
					try {
						viewData = LayoutManager.getInstance().getFXML("MovieGrid");
					} catch (IOException e) {
						e.printStackTrace();
					}
					Node itemCell = viewData.getKey();
					MovieGridController controller = (MovieGridController) viewData.getValue();
					int index = (page - 1) * colsNum * rowsNum + i * colsNum + j;
					if (index >= movieList.size())
						break;
					Movie item = movieList.get(index);
					controller.setMovieGrid(item);
					controller.setDisplay();
					CatalogGrid.add(itemCell, j, i);
				}
			}
		});*/
		movieType.getItems().add("All Movies");
		movieType.setValue("All Movies");
		movieType.getItems().add("Coming Soon");
		movieType.getItems().add("Home Movie");
		movieType.getItems().add("Theater Movie");
		movieType.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)-> {
			List<Movie> FilteredMovieList = new ArrayList<>();
			for (Movie m : reserveList)
			{   
				
				if(newValue.equals("Theater Movie")&&m.getClass().equals(TheaterMovie.class))
				{   
				
					FilteredMovieList.add(m);
				}
				if(newValue.equals("Coming Soon")&&m.getClass().equals(Movie.class))
				{   
				
					FilteredMovieList.add(m);
				}
				if(newValue.equals("Home Movie")&&m.getClass().equals(HomeMovie.class))
				{   
				
					FilteredMovieList.add(m);
				}
				
			}
			if(newValue.equals("All Movies")) {
				movieList=reserveList;
				try {
					fillGrids();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				movieList=FilteredMovieList;
				try {
					fillGrids();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*
			 * for(Movie m:FilteredMovieList) { System.out.println(m.getEngName());
			 * 
			 * }
			 */
			
		});
		theaterBranch.getItems().add("Haifa");
		theaterBranch.getItems().add("Herzilya");
		theaterBranch.getItems().add("Tel-Aviv");
		theaterBranch.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)-> {
			List<Movie> FilteredMovieList = new ArrayList<>();
			for(Movie m: reserveList)
			{
				if(m.getClass().equals(TheaterMovie.class))
				{
					TheaterMovie TM = (TheaterMovie) m;
					List<MovieShow> MSL = TM.getMSList();
					for(MovieShow ms : MSL)
					{
						if(newValue.equals(ms.getTheater().getLocation().toString()))
						{
							FilteredMovieList.add(m);
						}
					}
					
				}
			}
			movieList=FilteredMovieList;
			try {
				fillGrids();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		
	
	}

	private void fillGrids() throws IOException {
		pages = movieList.size() / 4 + 1;
		 pagesLabel.setText("page " + page + " out of " + pages);
		Platform.runLater(() -> {
			CatalogGrid.getChildren().clear();
			for (int i = 0; i < rowsNum; i++) {
				for (int j = 0; j < colsNum; j++) {
					Pair<Parent, Object> viewData = null;
					try {
						viewData = LayoutManager.getInstance().getFXML("MovieGrid");
					} catch (IOException e) {
						e.printStackTrace();
					}
					Node itemCell = viewData.getKey();
					MovieGridController controller = (MovieGridController) viewData.getValue();
					int index = (page - 1) * colsNum * rowsNum + i * colsNum + j;
					if (index >= movieList.size())
						break;
					Movie item = movieList.get(index);
					controller.setMovieGrid(item);
					controller.setDisplay();
					CatalogGrid.add(itemCell, j, i);
				}
			}
		});
	}

	@FXML
	void nextPage(ActionEvent event) throws IOException {
		if (page < movieList.size() / 4 + 1) {
			page++;
			fillGrids();
		}
	}

	@FXML
	void prevPage(ActionEvent event) throws IOException {
		if (page > 1) {
			page--;
			fillGrids();
		}
	}

	@FXML
	void addNewMovie(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddComingMovie.fxml"));
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// AddComingMovie addmovie=(AddComingMovie)loader.getController();
		Stage stage = new Stage();
		stage.setTitle("add coming soon movie");
		stage.setScene(new Scene(parent));
		stage.show();
		/*
		 * stage.setOnHiding((e) -> { handleRefresh(new ActionEvent()); });
		 */
	}

	private void handleRefresh(ActionEvent actionEvent) {
		try {
			// SimpleClient.getClient().sendToServer("#warning");
			msgObject msg = new msgObject("#getAllMovies");
			SimpleClient.getClient().sendToServer(msg);
			System.out.println("message sent to server to get all movies");
			System.out.println(clickedMovie.getEngName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void goHome(ActionEvent event) throws IOException {
		App.setRoot("Primary");
	}

	@FXML

	void search_Filter(ActionEvent event) {
		LocalDate ld1 = firstDate.getValue();
		LocalDate ld2 = lastDate.getValue();
		List<Movie> FilteredMovieList = new ArrayList<>();
		for (Movie m : reserveList) {
			if (m.getClass().equals(TheaterMovie.class)) {
				TheaterMovie TM = (TheaterMovie) m;
				List<MovieShow> MSL = TM.getMSList();
				for (MovieShow ms : MSL) {
					if (ld2 == null) {
						if (ld1.isBefore(ms.getShowDate()) || ld1.isEqual(ms.getShowDate()) ) {
							FilteredMovieList.add(m);
						}
					}

					else {

						if (ld1.isBefore(ms.getShowDate()) || ld1.isEqual(ms.getShowDate())) {

							if (ld2.isAfter(ms.getShowDate()) || ld2.isEqual(ms.getShowDate())) {
								FilteredMovieList.add(m);

							}

						}

					}

				}

			}

		}
		movieList=FilteredMovieList;
		try {
			fillGrids();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
