import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Client {

    String word;

    Socket socket;

    GameDetail gameInfo;

    ObjectOutputStream out;

    ObjectInputStream in;

    HashMap<String, Integer>  categoryWon;



    public Client() {
        categoryWon = new HashMap<>();
        categoryWon.put("animal", 0);
        categoryWon.put("tools", 0);
        categoryWon.put("weather", 0);
    }
    public boolean connect(int port) {
        try{
            System.out.println("trying to connect");
            socket = new Socket("127.0.0.1",port);
             out = new ObjectOutputStream(socket.getOutputStream());
             in = new ObjectInputStream(socket.getInputStream());
            socket.setTcpNoDelay(true);
            return true;
        } catch (Exception e) {
            System.out.println("ConnectionInvalid failed");
            return false;
        }
    }




    public void updateWord(char letter, ArrayList<Integer> pos){
        if(pos== null || pos.size() == 0) {
            System.out.println("no place");
            return;
        }
        char[] wordArr = word.toCharArray();
        for(int i : pos) {
            wordArr[i] = letter;
        }
        word = String.valueOf(wordArr);
    }

    public boolean receiveObject(){
        try{
            this.gameInfo = (GameDetail)this.in.readObject();
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sentObject(){
        try{
            this.out.reset();
            this.out.writeObject(this.gameInfo);
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean initWord(){
        if(gameInfo.wordLength <= 0) {
            return false;
        }
        int  i =gameInfo.wordLength;

        char[] a = new char[i];

        Arrays.fill(a, 0, i , '_');

        word = String.valueOf(a);

        return true;
    }

    public void reset() {
        categoryWon.put("animal", 0);
        categoryWon.put("tools", 0);
        categoryWon.put("weather", 0);

        word = null;
        gameInfo=null;
    }



}
