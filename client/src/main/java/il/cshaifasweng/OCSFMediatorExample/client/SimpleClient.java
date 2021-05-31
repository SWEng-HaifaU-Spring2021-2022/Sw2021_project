package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
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
		//System.out.println("message arrived");
		msgObject temp=(msgObject)msg;
		//System.out.println(temp.getMsg());
		if(msg.getClass().equals(msgObject.class)) {
			System.out.print("msg arrived");
			msgObject tempmsg=(msgObject)msg;
			if(tempmsg.getMsg().equals("AllMovies")) {
				Platform.runLater(()->{
					try {
						obj=tempmsg.getObject();
						App.setRoot("Catalog");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
			else if(tempmsg.getMsg().equals("Theaters Retrived")) {

				Platform.runLater(()->{
					CatalogController Catalog=new CatalogController();
					ArrayList<Theater>TheatersList=(ArrayList<Theater>)tempmsg.getObject();
					try {
						System.out.println("Openning the edit Page");
						Catalog.openEditPage(TheatersList);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

			}
			else if(tempmsg.getMsg().equals("newmovieShowadd")){
				System.out.println("an object have been added");
				Platform.runLater(()->{
				});
			}
			else if(tempmsg.getMsg().equals("MovieShow Deleted")){
				System.out.println("an object have been deleted");
				Platform.runLater(()->{
					List<MovieShow> MSL=(List<MovieShow>) tempmsg.getObject();
					EditTimeController EditController=new EditTimeController();
					EditController.reloadTable(MSL);
				});
			}
			else if(tempmsg.getMsg().equals("movie show updated")){
				System.out.println("an object have been updated");
				Platform.runLater(()->{
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