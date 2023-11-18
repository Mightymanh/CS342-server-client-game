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
	
	public Server(int port, Consumer<Serializable> call) {
		this.port = port;
		this.count = 0;
		
		// start listening to a port and connect to clients
		listenT = new ListenThread();
		listenT.start();
		
		//
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
				System.out.println("Streams not open");
			}
			
			// doing one game TODO: DO MULTIPLE GAMES
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
		
		// send signal to client that server is waiting for client to start game
		public void initGameDetail() {
			message = new GameDetail();
			message.gameStatus = -2;
			try {
				out.writeObject(message);
			} 
			catch(Exception e) {
				System.out.println("Error in initGameDetail for client #" + count);
			}
		}
		
		// receive client signal to start game, initialize all game component
		public void freshStartGame() {
			while (true) {
				try {
					message = (GameDetail)in.readObject();
					if (message.gameStatus == 0) { // if client says starts game (gameStatus = 0) then server starts game
						callback.accept("Client #" + count + " start game");
						gc.resetGame(); // zero game components
						break;
					}
					else {
						System.out.println("Client #" + count + " not start game");
					}
				}
				catch (Exception e){
					System.out.println("Error in freshStartGame for client #" + count + ". Terminating client");
					clientThreadList.remove(this);
					break;
				}
			}
		}
		
		public void startRound() {
			gc.startRound();
			callback.accept("Client #" + count + " are about to enter round " + gc.round);
			
			// send message to client
			message = new GameDetail();
			message.roundStatus = 0;
			try {
				out.writeObject(message);
			}
			catch (Exception e) {
				System.out.println("Error in startRound for client #" + count);		
			}
		}
		
		// get the client's category
		public void getClientCategory() {
			try {
				message = (GameDetail)in.readObject();
				gc.category = message.category;
				callback.accept("Get client #" + count + " category: " + gc.category);
			} catch (Exception e) {
				callback.accept("Fail to get category from client #" + count);
			}
		}
		
		// send the number of letter of chosen word to client
		public void sendNumLetter() {
			message.wordLength = gc.numLetter;
			try {
				out.writeObject(message);
			}
			catch (Exception e) {
				System.out.println("Error in sendNumLetter for client #" + count);
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
			}
		}
		
		
		// check client's guess letter and send a respond
		public void respondClient() {
			// check the guess letter
			ArrayList<Integer> position = gc.checkGuess();
			
			if (position.size() == 0) { // if client guess incorrectly then decrement guessRemaining
				gc.guessRemain--;
			}
			else if (position.size() == gc.numLetter) { // if client guess all the letter in the word then the client wins
				gc.gameStatus = 1;
			}
			
			// send the position that guess letter is in the word
			try {
				message.position = position;
				message.guessRemain = gc.guessRemain;
				out.writeObject(message);
			}
			catch (Exception e) {
				System.out.println("Error in responding to client guess for client #" + count);
			}
		}
	}
}
