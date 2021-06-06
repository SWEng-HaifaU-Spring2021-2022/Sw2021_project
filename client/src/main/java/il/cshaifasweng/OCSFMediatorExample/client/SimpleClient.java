package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.event.ActionEvent;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import javafx.application.Platform;

import javax.xml.catalog.Catalog;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	public static Object obj=null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println(msg.getClass().toString());
		//System.out.println(temp.getMsg());
		if(msg.getClass().equals(msgObject.class)) {
			msgObject temp=(msgObject)msg;
			System.out.println("msg arrived");
			msgObject tempmsg=(msgObject)msg;
			System.out.println(tempmsg.getMsg());
			if(tempmsg.getMsg().equals("AllMovies")) {
				Platform.runLater(()->{
					try {
						obj=tempmsg.getObject();
						//App.setRoot("Catalog");
						App.setRoot("GridCatalog");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
			else if(tempmsg.getMsg().equals("Theaters Retrived")) {

				Platform.runLater(()->{
					//CatalogController catalog=new CatalogController();
					MovieGridController MovieGrid=new MovieGridController();
					ArrayList<Theater>TheatersList=(ArrayList<Theater>)tempmsg.getObject();
					try {
						System.out.println("Openning the edit Page");
						MovieGrid.openEditpage(TheatersList);
						//catalog.openEditPage(TheatersList);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

			}
			else if(tempmsg.getMsg().equals("newmovieShowadd")){
				System.out.println("an object have been added");
				Platform.runLater(()->{
					RefreshCatalog();
				});
			}
			else if(tempmsg.getMsg().equals("MovieShow Deleted")){
				System.out.println("an object have been deleted");
				Platform.runLater(()->{
					MovieGridController MGC=new MovieGridController();
					List<Theater>LT=(List<Theater>) tempmsg.getObject();
					try {
						RefreshCatalog();
						System.out.println("before opening the edit page");
						MGC.openEditpage(LT);
					} catch (IOException e) {
						e.printStackTrace();
					}
				/*	List<MovieShow> MSL=(List<MovieShow>) tempmsg.getObject();
					EditTimeController EditController=new EditTimeController();
					//EditController.reloadTable(MSL);*/
				});
			}
			else if(tempmsg.getMsg().equals("movie show updated")){
				System.out.println("an object have been updated");
				Platform.runLater(()->{
					RefreshCatalog();
				});
			}
			else if(tempmsg.getMsg().equals("Movie deleted")){
				System.out.println("Movie have been deleted");
				Warning newwarning=new Warning("Movie have been deleted");
				EventBus.getDefault().post(new WarningEvent((Warning)newwarning));
				RefreshCatalog();
			}
			else if(tempmsg.getMsg().equals("failed")){
				Warning newwarning=new Warning("Some thing went wrong");
				EventBus.getDefault().post(new WarningEvent((Warning)newwarning));
			}
			else if(tempmsg.getMsg().equals("a price request added")){
				System.out.println("Price request have been added to the DB");
				Warning newwarning=new Warning("The request successfully sent");
				EventBus.getDefault().post(new WarningEvent((Warning)newwarning));
			}
			else if(tempmsg.getMsg().equals("movie added successfully")){
				Warning newwarning=new Warning("movie added successfully");
				EventBus.getDefault().post(new WarningEvent((Warning)newwarning));
			}
		}
		 else if(msg.getClass().equals(AdvancedMsg.class)){
			System.out.println("advanced message");
			AdvancedMsg advMsg=(AdvancedMsg)msg;
			System.out.println("advanced message1");
			if(advMsg.getMsg().equals("MovieShow Deleted")||advMsg.getMsg().equals("newmovieShowadd")||advMsg.getMsg().equals("MovieShow Updated")){
				System.out.println("advanced message2"+ advMsg.getMsg());
				Platform.runLater(()->{
					List<Theater>TL=(List<Theater>) advMsg.getObjectList().get(0);
					//List<MovieShow>MSL=(List<MovieShow>) advMsg.getObjectList().get(1);
					Movie movie=(Movie)advMsg.getObjectList().get(1);
					MovieGridController MGC=new MovieGridController();
					try {
						MGC.reopenEditpage(TL,movie);
						RefreshCatalog();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

			}
		}


	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}
	private void RefreshCatalog(){
		msgObject new_msg=new msgObject("#getAllMovies");
		try {
			SimpleClient.getClient().sendToServer(new_msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("message sent to server to refresh the catalog page");
	}
}