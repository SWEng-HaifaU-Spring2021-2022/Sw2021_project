package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;

public class App 
{
	
	private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);
       // server.test();
        server.listen();
        System.out.println("Server is up");
    }
}
