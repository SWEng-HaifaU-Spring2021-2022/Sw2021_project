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

	public static Object obj=null;

	protected SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		//System.out.println("message arrived");
		msgObject temp=(msgObject)msg;
		//System.out.println(temp.getMsg());
		if(msg.getClass().equals(msgObject.class)) {
			System.out.print("msg arrived");
			msgObject tempmsg =(msgObject)msg;
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
				System.out.println("movie shows arrived:getting movie shows");
				Platform.runLater(()->{
					obj=tempmsg.getObject();
					CatalogController Catalog=new CatalogController();
					ArrayList<MovieShow>MSL=(ArrayList<MovieShow>)obj;
					try {
						System.out.println("Openning the edit Page");
						Catalog.openEditPage();
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
			}/*else if (tempmsg.getMsg().equals("getshowsdisplay")){
				System.out.println("\nsdaad");
				CatalogController catalog=new CatalogController();
				System.out.println("\nsdaad");
				catalog.displayscreeningtime((List<MovieShow>)tempmsg.getObject());
				System.out.println("\nsdaad");
				Platform.runLater(()->{

				});

			}*/
			
		}
	

	}

	public static Object getObj() {
		return obj;
	}
}

