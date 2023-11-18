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
	int port;
	int count;
	ArrayList<ClientThread> clientThreadList;
	ListenThread listenT;
	Consumer<Serializable> callback;
	GameDetail message;
	
	// debug variable
	boolean DEBUG = true;
	
	public Server(int port, Consumer<Serializable> call) {
		this.port = port;
		this.count = 0;
		
		// start listening to a port and connect to clients
		listenT = new ListenThread();
		listenT.start();
		
		// list of client threads
		clientThreadList = new ArrayList<ClientThread>();
		
		// define callback function as call
		callback = call;
	}
	
	public class ListenThread extends Thread {
		public void run() {
			try(ServerSocket serverS = new ServerSocket(port);){
				//System.out.println("Server is waiting for a client on port: " + port);
				callback.accept("Server is waiting for a client on port: " + port);
				
				while(true) {
					ClientThread clientT = new ClientThread(serverS.accept(), count);
					callback.accept("Client has connected to server: client #" + count);
					clientThreadList.add(clientT);
					clientT.start();
					count++;
				}
			}
			catch(Exception e) {
				// e.printStackTrace();
				callback.accept("Server socket did not launch.");
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
				System.out.println("Streams not open for client #" + count);
				return;
			}
			
			// doing one game TODO: DO MULTIPLE GAMES
			try {
				initGameDetail();
				freshStartGame();
				do {
					startRound();
					getClientCategory();
					gc.getWord();
					sendNumLetter();
					while (gc.roundStatus == 0) {
						getClientGuess();
						respondClient();
					}	
				} while (gc.gameStatus == 0);
			}
			catch (Exception e) {
				callback.accept("Some thing is wrong with client #" + count + ". Terminating client.");
				clientThreadList.remove(this);
			}
			
		}
		
		// SEND signal to client that server is waiting for client to start game
		public void initGameDetail() throws IOException {
			if (DEBUG) {System.out.println("at initGameDetail for client #" + count);} // TESTING
			message = new GameDetail();
			message.gameStatus = -2;
			out.writeObject(message);
		}
		
		// RECEIVE client signal to start game, initialize all game component
		public void freshStartGame() throws ClassNotFoundException, IOException {
			if (DEBUG) {System.out.println("at freshStartGame for client #" + count);} // TESTING
			while (true) {
				message = (GameDetail)in.readObject();
				if (message.gameStatus == 0) { // if client says starts game (gameStatus = 0) then server starts game
					callback.accept("Client #" + count + " start game");
					gc.resetGame(); // zero game components
					gc.gameStatus = 0; // signal that game is starting
					break;
				}
				else {
					System.out.println("Client #" + count + " not start game");
				}
			}
		}
		
		// RECEIVE signal to start round
		public void startRound() throws ClassNotFoundException, IOException {
			if (DEBUG) {System.out.println("at startRound for client #" + count);} // TESTING
			while (true) {
				message = (GameDetail)in.readObject();
				if (message.roundStatus == 0) { // if client says starts round (roundStatus = 0) then server starts round
					callback.accept("Client #" + count + " start round");
					gc.startRound();
					break;
				}
				else {
					System.out.println("Client #" + count + " not start round");
				}
			}
		}
		
		// RECEIVE the client's category
		public void getClientCategory() throws ClassNotFoundException, IOException {
			if (DEBUG) {System.out.println("at getClientCategory for client #" + count);}
			while (true) {
				message = (GameDetail)in.readObject();
				gc.category = message.category;
				callback.accept("Get client #" + count + " category: " + gc.category);
				break;
			}
		}
		
		// SEND the number of letter of chosen word to client
		public void sendNumLetter() throws IOException {
			if (DEBUG) {System.out.println("at sendNumLetter for client #" + count);}
			message.wordLength = gc.numLetter;
			out.writeObject(message);
		}
		
		// RECEIVE the client's guess letter
		public void getClientGuess() throws ClassNotFoundException, IOException {
			if (DEBUG) {System.out.println("at getClientGuess for client #" + count);}
			while (true) {
				message = (GameDetail)in.readObject();
				gc.guessLetter = message.guessLetter;
				callback.accept("Get client #" + count + " guess letter: " + gc.guessLetter);
				break;
			}
		}
		
		
		// SEND: check client's guess letter and send a respond
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
	}
}
