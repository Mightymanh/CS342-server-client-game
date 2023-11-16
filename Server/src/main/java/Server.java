import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Server {
	static HashMap<String, ArrayList<String>> WordBank;
	int port;
	int count;
	ArrayList<ClientThread> clientThreadList;
	ListenThread listenT;
	Consumer<Serializable> callback;
	GameDetail message;
	
	// prepare server's word bank
	private void prepareServerWordBank() {
		ArrayList<String> animalList = new ArrayList<>();
		ArrayList<String> weatherList = new ArrayList<>();
		ArrayList<String> toolList = new ArrayList<>();
		animalList.add("tiger");
		animalList.add("fish");
		animalList.add("ant");
		animalList.add("bee");
		weatherList.add("snow");
		weatherList.add("gale");
		weatherList.add("autumn");
		weatherList.add("hot");
		toolList.add("drill");
		toolList.add("tape");
		toolList.add("manual");
		WordBank.put("animal", animalList);
		WordBank.put("weather", weatherList);
		WordBank.put("tools", toolList);
	}
	
	public Server(Consumer<Serializable> call) {
		WordBank = new HashMap<>();
		prepareServerWordBank();
		
		// start listening to a port and connect to clients
		listenT = new ListenThread();
		listenT.start();
		
		// define callback function as call
		callback = call;
	}
	
	public class ListenThread extends Thread {
		public void run() {
			try(ServerSocket serverS = new ServerSocket(port);){
				System.out.println("Server is waiting for a client!");

				while(true) {
					ClientThread clientT = new ClientThread(serverS.accept(), count);
					callback.accept("Client has connected to server: client #" + count);
					clientThreadList.add(clientT);
					clientT.start();
					count++;
				}
			}
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}
		}
	}
	
	public class ClientThread extends Thread {
		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;
		GameControl gc;
		
		ClientThread(Socket connection, int count) {
			this.connection = connection;
			this.count = count;
			gc = new GameControl();
		}
		
		public void run() {
			// initialize input and output stream
			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);	
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}
			
			// while loop that starts the game
			freshStartGame();
			while (gc.round <= 3) {
				startRound();
				getClientCategory();
				gc.getWord();
				
				// when client's category is set,
				while (gc.gameStatus == 0) {
					getClientGuess();
					try {
						respondClient();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				endRound(gc.gameStatus);
			}
		}
		
		public void startRound() {
			gc.startRound();
		}
		
		// get the client's category
		public void getClientCategory() {
			try {
				message = (GameDetail)in.readObject();
				gc.category = message.category;
				callback.accept("Get client #" + count + " category: " + gc.category);
			} catch (Exception e) {
				callback.accept("Fail to get category from client #" + count);
				callback.accept("error: " + e.toString());
			}
		}
		
		// get the client's guess letter
		public void getClientGuess() {
			try {
				message = (GameDetail)in.readObject();
				gc.guessLetter = message.guessLetter;
				callback.accept("Get client #" + count + " guess letter: " + gc.guessLetter);
			} catch (Exception e) {
				callback.accept("Fail to get guess letter from client #" + count);
				callback.accept("error: " + e.toString());
			}
		}
		
		// check client's guess letter and send a respond
		public void respondClient() throws IOException {
			// check the guess letter
			ArrayList<Integer> position = gc.checkGuess();
			
			if (position.size() == 0) { // if client guess incorrectly then decrement guessRemaining
				gc.guessRemain--;
			}
			else if (position.size() == gc.numLetter) { // if client guess all the letter in the word then the client wins
				gc.gameStatus = 1;
			}
			
			// send the position that guess letter is in the word
			message.position = position;
			message.guessRemain = gc.guessRemain;
			out.writeObject(message);
		}
		
		public void endRound(int gameStatus) {
		}
		
		public void freshStartGame() {
			
		}
	}
}
