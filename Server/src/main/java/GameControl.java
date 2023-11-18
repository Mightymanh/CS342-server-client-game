import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameControl {
	public HashMap<String, ArrayList<String>> WordBank;
	
	// round variable
	public String word;
	public char[] lettersInWord;
	public int numLetter;
	public String category;
	public int round;
	public char guessLetter;
	public int guessRemain;
	public int roundStatus;
	
	// game variable
	public HashMap<String, ArrayList<String>> UsedList;
	public int numWin;
	public int numLost;
	public int losingStreak;
	public int[] categoryStatus; 
	public int gameStatus; // -2 means game is not started, -1 means lost, 0 means running, 1 means win
	
	// prepare word bank
	private void prepareWordBank() {
		WordBank = new HashMap<>();
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
	
	// prepare usedWord list
	private void initUsedList() {
		if (UsedList == null) {
			UsedList = new HashMap<>();
		}
		else {
			UsedList.clear();
		}
		
		for (String key : WordBank.keySet()) {
			UsedList.put(key, new ArrayList<String>());
		}
	}
	
	public GameControl() {
		prepareWordBank();
	}
	
	// reset the game
	public void resetGame() {
		initUsedList();
		numWin = 0;
		numLost = 0;
		round = 0;
		guessRemain = 0;
		losingStreak = 0;
		categoryStatus = new int[3];
		gameStatus = -2;
		roundStatus = -2;
	}
	
	// initialize the current round
	public void startRound() {
		round++;
		guessRemain = 6;
		roundStatus = 0;
	}
	
	// get random word correspond to client's chosen category in WordBank
	public void getWord() {
		ArrayList<String> wordList = WordBank.get(category);
		//System.out.println(wordList.toString());
		int length = wordList.size();
		Random rand = new Random();
		
		while(true) {
			int randomIndex = rand.nextInt(length);
			word = wordList.get(randomIndex); // pick a random word
			
			if (!UsedList.get(category).contains(word)) {
				UsedList.get(category).add(word); // add word to UsedList if it is not chosen before
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
