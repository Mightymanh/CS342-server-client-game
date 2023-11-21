import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ClientTest {

	Client client;
	@BeforeEach
	public void init() {
		client=new Client();
		client.word="__llo";


	}


	@Test
	void updateWordTest1() {

		assertEquals("__llo" , client.word);


		ArrayList<Integer> pos = new ArrayList<>();
		pos.add(0);
		pos.add(1);

		client.updateWord('h', pos);
		assertEquals("hhllo" , client.word);


		client.word = "__we__q";
		pos = new ArrayList<>();
		pos.add(0);
		pos.add(4);
		client.updateWord('a', pos);
		assertEquals("a_wea_q" , client.word);

		pos.add(1);
		pos.add(2);
		pos.add(3);
		pos.add(5);
		pos.add(6);
		client.updateWord('_', pos);
		assertEquals("_______" , client.word);


	}

	@Test
	void updateWordTest2() {


		client.word= "hello";

		client.updateWord('_', null);

		assertEquals("hello", client.word);

		ArrayList<Integer> pos = new ArrayList<>();

		client.updateWord('a', pos);
		assertEquals("hello", client.word);
	}


	@Test
	void resetClient() {

		client.categoryWon.replace("animal", 2);
		client.categoryWon.replace("tools", 2);
		client.categoryWon.replace("weather", 2);

		client.reset();

		assertEquals(0, client.categoryWon.get("animal"));
		assertEquals(0, client.categoryWon.get("weather"));
		assertEquals(0, client.categoryWon.get("tools"));
	}

	@Test
	void resetClient2() {

		client.gameInfo = new GameDetail();


		client.reset();
		assertNull(client.gameInfo);
		assertNull(client.word);
	}

}
