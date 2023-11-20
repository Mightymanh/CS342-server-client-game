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
	
	// close all sockets related to Server and clear clientThreadList
	public void shutdownServer() {
		try {
			listenT.serverS.close();
			int length = clientThreadList.size();
			for (int i = length - 1; i >= 0; i++) {
				ClientThread cl = clientThreadList.get(i);
				cl.shutdownThread();
			}
			System.out.println("Shut down server with " + length + " running threads");
		}
		catch (Exception ex) {
			System.out.println("Cannot close server for some reason");
		}
	}
	
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
		
		ServerSocket serverS;
		
		public void run() {
			try {
				
				serverS = new ServerSocket(port);
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
		
		// shut down client thread socket and remove thread from list
		public void shutdownThread() {
			try {
				this.connection.close();
			} 
			catch (IOException e1) {
				System.out.println("Cannot close client for some reason");
			}
			
			clientThreadList.remove(this);
		}
		
		// main
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
				while (true) {
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
						gc.postRoundUpdate();
						sendGameOutCome(); 
					} while (gc.gameStatus == 0);
					
					break;
				}
			}
			catch (Exception e) {
				callback.accept("Some thing is wrong with client #" + count + ". Terminating client.");
				shutdownThread();
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

			// receive start game signal
			message = (GameDetail)in.readObject();
			if (message.gameStatus == 2) { // if client says starts game (gameStatus = 0) then server starts game
				callback.accept("Client #" + count + " start game");
				gc.resetGame(); // zero game components
				gc.gameStatus = 0; // signal that game is starting
			}
			else {
				System.out.println("Client #" + count + " not start game");
			}
		}
		
		// RECEIVE signal to start round
		public void startRound() throws ClassNotFoundException, IOException {
			if (DEBUG) {System.out.println("at startRound for client #" + count);} // TESTING
			
			// receive start round signal
		//	out.writeObject(message);
			message = (GameDetail)in.readObject();
			if (message.roundStatus == 0) { // if client says starts round (roundStatus = 0) then server starts round
				callback.accept("Client #" + count + " start round");
				gc.startRound();
			}
			else {
				System.out.println("Client #" + count + " not start round");
			}
		}
		
		// RECEIVE the client's category
		public void getClientCategory() throws ClassNotFoundException, IOException {
			if (DEBUG) {System.out.println("at getClientCategory for client #" + count);}

			// receive category
			message = (GameDetail)in.readObject();
			gc.category = message.category;
			callback.accept("Get client #" + count + " category: " + gc.category);
		}
		
		// SEND the number of letter of chosen word to client
		public void sendNumLetter() throws IOException {
			if (DEBUG) {System.out.println("at sendNumLetter for client #" + count);}
			callback.accept("client #0 get to guess word: " + gc.word + ", length: " + gc.numLetter);
			message.wordLength = gc.numLetter;
			out.reset();
			out.writeObject(message);
		}
		
		// RECEIVE the client's guess letter
		public void getClientGuess() throws ClassNotFoundException, IOException {
			if (DEBUG) {System.out.println("at getClientGuess for client #" + count);}

			// receive guess letter
			message = (GameDetail)in.readObject();
			gc.guessLetter = message.guessLetter;
			callback.accept("Get client #" + count + " guess letter: " + gc.guessLetter);
		}
		
		
		// SEND: check client's guess letter and send a respond
		public void respondClient() throws IOException {
			// check the guess letter
			ArrayList<Integer> position = gc.checkGuess();
			
			if (position.size() == 0) { // if client guess has guess the letter incorrectly
				gc.guessRemain--;
				if (gc.guessRemain == 0) { // if client has no guess left then client loses the round
					gc.roundStatus = -1;
					callback.accept("Client #" + count + " lose round" + gc.round);
					sendRoundLost(position);
				}
				else { // case when client still survive
					sendRoundContinue(position);
				}
			}
			else { // if the client guesses the letter correctly
				if (gc.numLetterRemain == 0) { // if client guess all the letter in the word then the client wins the round
					gc.roundStatus = 1;
					callback.accept("Client #" + count + " win round " + gc.round);
					sendRoundWin(position);
				} 
				else { // client needs to guess more letter to finish
					sendRoundContinue(position);
				}
			}
		}
		
		// SEND game outcome
		public void sendGameOutCome() throws IOException {
			if (gc.gameStatus == 1) {
				callback.accept("Client #" + count + " wins game");
			}
			else if (gc.gameStatus == -1 ){
				callback.accept("Client #" + count + "loses game");
			}

			message.gameStatus = gc.gameStatus;
			out.reset();
			out.writeObject(message);
		}
		
		
		// helper functions
		private void sendRoundWin(ArrayList<Integer> position) throws IOException {
			message.position = position;
			message.guessRemain = gc.guessRemain;
			message.roundStatus = gc.roundStatus;
			out.reset();
			out.writeObject(message);
		}
		
		private void sendRoundLost(ArrayList<Integer> position) throws IOException {
			message.position = position;
			message.guessRemain = gc.guessRemain;
			message.roundStatus = gc.roundStatus;
			out.reset();
			out.writeObject(message);
		}
		
		private void sendRoundContinue(ArrayList<Integer> position) throws IOException {
			message.position = position;
			message.guessRemain = gc.guessRemain;
			out.reset();
			out.writeObject(message);
		}
	}
}
