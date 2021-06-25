package il.cshaifasweng.OCSFMediatorExample.server;
import java.io.IOException;
public class App
{

    private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);

        //server.test();
        //server.AddUsers();

        server.listen();
        System.out.println("Server is up");
        SchedulerSender.startJobScheduling();
       /* System.out.println("a small test:");
        try {
            SimpleServer.getAllMovies2();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}