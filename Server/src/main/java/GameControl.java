import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameControl {
	
	public HashMap<String, ArrayList<String>> usedWord;
	public String word;
	public char[] lettersInWord;
	public int numLetter;
	public String category;
	public int numWin;
	public int numLost;
	public int round;
	public char guessLetter;
	public int guessRemain;
	public int gameStatus; // -1 means lost or not start the round, 0 means running, 1 means win
	
	private void initializeUsedWord() {
		if (usedWord == null) {
			usedWord = new HashMap<>();
		}
		else {
			usedWord.clear();
		}
		for (String key : Server.WordBank.keySet()) {
			usedWord.put(key, new ArrayList<String>());
		}
	}
	
	// reset the game
	public void resetGame() {
		initializeUsedWord();
		numWin = 0;
		numLost = 0;
		round = 0;
		guessRemain = 0;
		gameStatus = -1;
	}
	
	// initialize the current round
	public void startRound() {
		round++;
		guessRemain = 6;
		gameStatus = 0;
	}
	
	// get random word correspond to client's chosen category in WordBank
	public void getWord() {
		ArrayList<String> wordList = Server.WordBank.get(category);
		//System.out.println(wordList.toString());
		int length = wordList.size();
		Random rand = new Random();
		
		while(true) {
			int randomIndex = rand.nextInt(length);
			word = wordList.get(randomIndex); // pick a random word
			
			if (!usedWord.get(category).contains(word)) {
				usedWord.get(category).add(word); // add word to usedWord list if it is not chosen before
				break;
			}
		} 
		
		numLetter = word.length();
		lettersInWord = word.toCharArray();
	}
	
	// check client's guess letter in a word
	public ArrayList<Integer> checkGuess() {
		ArrayList<Integer> position = new ArrayList<>();
		int length = lettersInWord.length;
		for (int i = 0; i < length; i++) {
			if (lettersInWord[i] == guessLetter) {
				// cross that letter from the list
				lettersInWord[i] = '_';
				
				// add index of that letter in the list
				position.add(i);
			}
		}
		
		return position;
	}
}
