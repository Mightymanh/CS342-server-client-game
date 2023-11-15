import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {

    String word;

    Socket socket;

    GameDetail gameInfo;

    ObjectOutputStream out;

    ObjectInputStream in;



    public boolean connect(int port) {

        try{
            System.out.println("trying to connect");
            socket = new Socket("127.0.0.1",port);
             out = new ObjectOutputStream(socket.getOutputStream());
             in = new ObjectInputStream(socket.getInputStream());
            socket.setTcpNoDelay(true);
            return true;
        } catch (Exception e) {
            System.out.println("Connection failed");
            return false;
        }
    }



}
