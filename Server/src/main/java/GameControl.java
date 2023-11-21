import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameControl {
	static public HashMap<String, ArrayList<String>> WordBank;
	
	// round variable
	public String word;
	public char[] lettersInWord;
	public int numLetter;
	public int numLetterRemain;
	public String category;
	public int round;
	public char guessLetter;
	public int guessRemain;
	public int roundStatus; // -2 means round is not started, -1 means lost, 1 means win, 0 means running
	
	// game variable
	public HashMap<String, ArrayList<String>> UsedList;
	public int losingStreak;
	public HashMap<String, Integer> categoryStatus; 
	public int gameStatus; // -2 means game is not started, -1 means lost, 1 means win, 2 means running
	
	// prepare word bank
	private void prepareWordBank() {
		if (WordBank != null) return; // if WordBank is already prepared by server then no need to create new object again
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
		UsedList = new HashMap<>();

		for (String key : WordBank.keySet()) {
			UsedList.put(key, new ArrayList<String>());
		}
	}
	
	public GameControl() {
		prepareWordBank();
		initUsedList();
		categoryStatus = new HashMap<>();
		for (String key : WordBank.keySet()) {
			categoryStatus.put(key, 0);
		}
	}
	
	// reset the game
	public void resetGame() {
		initUsedList();
		round = 0;
		guessRemain = 0;
		losingStreak = 0;
		gameStatus = -2;
		roundStatus = -2;
		
		categoryStatus = new HashMap<>();
		for (String key : WordBank.keySet()) {
			categoryStatus.put(key, 0);
		}	
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
		int length = wordList.size();
		Random rand = new Random();
		
		// pick a random word from the wordList that is not used
		while(true) {
			int randomIndex = rand.nextInt(length);
			word = wordList.get(randomIndex); // get random index
			
			if (!UsedList.get(category).contains(word)) {
				UsedList.get(category).add(word); // add word to UsedList if it is not chosen before
				break;
			}
		}
		// set variables important for client and server communication
		numLetter = word.length();
		numLetterRemain = numLetter;
		lettersInWord = word.toCharArray();
	}
	
	// check client's guess letter in a word
	public ArrayList<Integer> checkGuess() {
		ArrayList<Integer> position = new ArrayList<>();
		for (int i = 0; i < numLetter; i++) {
			if (lettersInWord[i] == guessLetter) {
				// cross that letter from the list
				lettersInWord[i] = '_';
				
				// add index of that letter in the list
				position.add(i);
				
				// decrement numLetterRemain
				numLetterRemain--;
			}
		}
		
		return position;
	}

	// update gameStatus variable as necessary when a round ends
	public void postRoundUpdate() {
		
		if (roundStatus == -1) {// if client ended up losing the round
			losingStreak++;
			
			// check if client lost the game
			if (checkLosingGame()) {
				gameStatus = -1;
			}
		}
		else { // if client ended wining the round
			losingStreak = 0;
			categoryStatus.replace(category, 1);
			
			if (checkWinningGame()) {
				gameStatus = 1;
			}
		}
	}
	
	// check whether client loses the game
	private boolean checkLosingGame() {
		ArrayList<String> UsedWordInCategory = UsedList.get(category);
		if (losingStreak == 3 || UsedWordInCategory.size() == 3) {
			return true;
		}
		return false;
	}
	
	// check whether client wins the game
	private boolean checkWinningGame() {
		for (String cat : categoryStatus.keySet()) {
			if (categoryStatus.get(cat) == 0) 
				return false;
		}
		
		return true;
	}
}
