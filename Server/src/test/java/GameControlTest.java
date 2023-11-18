import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GameControlTest {

	private Server server;
	private GameControl gc;
	
	@BeforeEach
	void setup() {
		gc = new GameControl();
	}
	
	// check if the input list contains unique elements
	private boolean checkUnique(ArrayList<String> list) {
		Set<String> set = new HashSet<>(list);
		return set.size() == list.size();
	}
	
	// initializeUsedWord
	@Test
	void testInitializeUsedWord1() {		
		for (String category : gc.UsedList.keySet()) {
			assertTrue(gc.WordBank.containsKey(category), "initializedUsedWord: usedWord not contains correct keys");
			assertEquals(0, gc.UsedList.get(category).size(), "initialziedUsedWord: list corresponding to category is not empty");
			
		}
	}
	
	@Test
	void testInitalizeUsedWord2() {
		gc.category = "animal";
		gc.getWord();
		assertTrue(!gc.UsedList.get("animal").isEmpty());
		
		gc.resetGame();
		for (String category : gc.UsedList.keySet()) {
			assertTrue(gc.WordBank.containsKey(category), "initializedUsedWord: usedWord not contains correct keys");
			assertEquals(0, gc.UsedList.get(category).size(), "initialziedUsedWord: list corresponding to category is not empty");
		}
	}
	
	@Test
	void testGetWord1() {		
		// first round
		gc.category = "animal";
		gc.getWord();
		ArrayList<String> expected = new ArrayList<>();
		expected.add(gc.word);
		assertTrue(gc.WordBank.get("animal").contains(gc.word));
		assertTrue(gc.UsedList.get("animal").equals(expected));
		assertTrue(checkUnique(gc.UsedList.get("animal")));
		
		// second round
		gc.category = "animal";
		gc.getWord();
		expected.add(gc.word);
		assertTrue(gc.WordBank.get("animal").contains(gc.word));
		assertTrue(gc.UsedList.get("animal").equals(expected));
		assertTrue(checkUnique(gc.UsedList.get("animal")));

		
		// third round
		gc.category = "animal";
		gc.getWord();
		expected.add(gc.word);
		assertTrue(gc.WordBank.get("animal").contains(gc.word)); 
		assertTrue(gc.UsedList.get("animal").equals(expected));
		assertTrue(checkUnique(gc.UsedList.get("animal")));
	}
	
	@Test
	void testGetWord2() {
		ArrayList<String> expectedAnimalList = new ArrayList<>();
		ArrayList<String> expectedWeatherList = new ArrayList<>();
		ArrayList<String> expectedToolList = new ArrayList<>();
		
		// first round
		gc.category = "animal";
		gc.getWord();
		expectedAnimalList.add(gc.word);
		assertTrue(gc.WordBank.get("animal").contains(gc.word));
		
		
		// second round
		gc.category = "animal";
		gc.getWord();
		expectedAnimalList.add(gc.word);
		assertTrue(gc.WordBank.get("animal").contains(gc.word));

		// third round
		gc.category = "tools";
		gc.getWord();
		expectedToolList.add(gc.word);
		assertTrue(gc.WordBank.get("tools").contains(gc.word)); 
		
		// check overall
		assertTrue(gc.UsedList.get("animal").equals(expectedAnimalList));
		assertTrue(gc.UsedList.get("tools").equals(expectedToolList));
		assertTrue(gc.UsedList.get("weather").equals(expectedWeatherList));
		assertTrue(checkUnique(gc.UsedList.get("animal")));
		assertTrue(checkUnique(gc.UsedList.get("weather")));
		assertTrue(checkUnique(gc.UsedList.get("tools")));
	}
	
	@Test
	void testCheckGuess1() {
		// set up word
		gc.category = "animal";
		gc.word = "ant";
		gc.numLetter = 3;
		gc.lettersInWord = gc.word.toCharArray();
		
		// first round
		gc.guessLetter = 'a';
		ArrayList<Integer> actualPosition = gc.checkGuess(); // check guess for first correct letter
		ArrayList<Integer> expectedPosition = new ArrayList<>();
		expectedPosition.add(0);
		char expectedLetters[] = {'_', 'n', 't'};
		char actualLetters[] = gc.lettersInWord;
		assertTrue(expectedPosition.equals(actualPosition)); // check output of checkGuess
		assertTrue(Arrays.equals(expectedLetters, actualLetters)); // check crossing
		
		// second round
		gc.guessLetter = 'x';
		ArrayList<Integer> actualPosition2 = gc.checkGuess(); // check guess for wrong letter
		ArrayList<Integer> expectedPosition2 = new ArrayList<>();
		assertTrue(expectedPosition2.equals(actualPosition2)); // check output of checkGuess
		char expectedLetters2[] = {'_', 'n', 't'};
		char actualLetters2[] = gc.lettersInWord;
		assertTrue(Arrays.equals(expectedLetters2, actualLetters2)); // check crossing
		
		// third round
		gc.guessLetter = 't';
		ArrayList<Integer> actualPosition3 = gc.checkGuess(); // check guess for correct second letter
		ArrayList<Integer> expectedPosition3 = new ArrayList<>();
		expectedPosition3.add(2);
		char expectedLetters3[] = {'_', 'n', '_'};
		char actualLetters3[] = gc.lettersInWord;
		assertTrue(expectedPosition3.equals(actualPosition3)); // check output of checkGuess
		assertTrue(Arrays.equals(expectedLetters3, actualLetters3)); // check crossing
	}
	
	@Test
	void testCheckGuess2() {
		// set up word
		gc.category = "animal";
		gc.word = "ant";
		gc.numLetter = 3;
		gc.lettersInWord = gc.word.toCharArray();
		
		// first round
		gc.guessLetter = 'a';
		gc.checkGuess();
		
		// second round
		gc.guessLetter = 'a'; // guess letter that we already guess
		ArrayList<Integer> actualPosition = gc.checkGuess();
		assertTrue(actualPosition.isEmpty());	
	}
}

