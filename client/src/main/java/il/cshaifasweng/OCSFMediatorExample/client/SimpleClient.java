package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import javafx.application.Platform;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;
	public static Object obj=null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println("message arrived");
		msgObject temp=(msgObject)msg;
		System.out.println(temp.getMsg());
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
			else if(tempmsg.getMsg().equals("movieShowsForMovie")) {
				System.out.println("movie shows arrived 1");
				Platform.runLater(()->{
					obj=tempmsg.getObject();
					CatalogController Catalog=new CatalogController();
					ArrayList<MovieShow>MSL=(ArrayList<MovieShow>)obj;
					System.out.println("the arraylist length: "+ MSL.size());
					for (MovieShow ms:MSL){
						System.out.println("inside the for");
						//System.out.println(ms.getMovie().getMovieId());
					}
					System.out.println("movie shows arrived 2");
					try {
						System.out.println("movie shows arrived  3");
						Catalog.openEditPage();
						System.out.println("movie shows arrived  4");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				
			}else if(tempmsg.getMsg().equals("newmovieShowadd")){
				System.out.println("an object have been added");
				Platform.runLater(()->{
				});
			}else if(tempmsg.getMsg().equals("MovieShow Deleted")){
				System.out.println("an object have been deleted");
				Platform.runLater(()->{

				});
			}else if(tempmsg.getMsg().equals("movie show updated")){
				System.out.println("an object have been updated");
				Platform.runLater(()->{

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

}

