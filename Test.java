/**
 * 
 */
package app;

import static spark.Spark.*;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;

/**
 * @author 
 *
 */
class Test {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Main.main(null);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		stop();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@org.junit.jupiter.api.Test
	void test() {
		
		// create user1
		TestResponse res = request("POST", "/api/users?username=user1&email=user1@test");
		assertEquals(200, res.status);
		System.out.println("user1 created");
		
		// get user1
		res = request("GET", "/api/users/1");
		assertEquals(200, res.status);
		Map<String, String> json = res.json();
		assertEquals("user1", json.get("username"));
		assertEquals("user1@test", json.get("email"));
		assertNotNull(json.get("id"));
		System.out.println("get user1 succeeded");
		
		// get wrong user
		res = request("GET", "/api/users/10");
		assertEquals(404, res.status);
		
		// create user2
		res = request("POST", "/api/users?username=user2&email=user2@test");
		assertEquals(200, res.status);
		System.out.println("user2 created");
		
		// get users
		res = request("GET", "/api/users");
		assertEquals(200, res.status);
				
		// create game 1
		res = request("POST", "/api/store/games?title=title1&cover=cover1");
		assertEquals(200, res.status);
		System.out.println("game 1 created");

		// create game 2
		res = request("POST", "/api/store/games?title=title2&cover=cover2");
		assertEquals(200, res.status);
		System.out.println("game 2 created");
		
		// get games
		res = request("GET", "/api/store/games");
		assertEquals(200, res.status);
				
		// get game 1
		res = request("GET", "/api/store/games/1");
		assertEquals(200, res.status);
		json = res.json();
		assertEquals("title1", json.get("title"));
		assertEquals("cover1", json.get("cover"));
		assertNotNull(json.get("id"));
		System.out.println("get game1 succeeded");
		
		// get wrong game
		res = request("GET", "/api/store/games/10");
		assertEquals(404, res.status);
		
		// add game 1 to user 1
		res = request("POST", "/api/users/1/games?gameId=1");
		assertEquals(200, res.status);
		System.out.println("add game1 to user1 succeeded");
		
		// add game 2 to user 1
		res = request("POST", "/api/users/1/games?gameId=2");
		assertEquals(200, res.status);
		System.out.println("add game2 to user1 succeeded");
		
		// add wrong game to user 1
		res = request("POST", "/api/users/1/games?gameId=20");
		assertEquals(404, res.status);
		
		// get games from user 1
		res = request("GET", "/api/users/1/games");
		assertEquals(200, res.status);
		System.out.println("get games from user1 succeeded");
		
		// get games from wrong user
		res = request("GET", "/api/users/10/games");
		assertEquals(404, res.status);
		
		// add friend 2 to user 1
		res = request("POST", "/api/users/1/friends?friendId=2");
		assertEquals(200, res.status);
		System.out.println("add friend 2 to user1 succeeded");
		
		// add wrong friend to user 1
		res = request("POST", "/api/users/1/friends?friendId=20");
		assertEquals(404, res.status);
		
		// get friends from user 1
		res = request("GET", "/api/users/1/friends");
		assertEquals(200, res.status);
		System.out.println("get friends from user1 succeeded");
		
		// get friends from wrong user
		res = request("GET", "/api/users/10/friends");
		assertEquals(404, res.status);
		
		// delete friend 2 from user 1
		res = request("DELETE", "/api/users/1/friends/2");
		assertEquals(200, res.status);
		System.out.println("delete friend 2 from user1 succeeded");
		
		// delete wrong friend 2 from user 1
		res = request("DELETE", "/api/users/1/friends/20");
		assertEquals(404, res.status);
		
		// delete game 1 from user 1
		res = request("DELETE", "/api/users/1/games/1");
		assertEquals(200, res.status);
		System.out.println("delete game1 from user1 succeeded");
		
		// delete wrong game from user 1
		res = request("DELETE", "/api/users/1/games/10");
		assertEquals(404, res.status);
		
		// delete game 2
		res = request("DELETE", "/api/store/games/2");
		assertEquals(200, res.status);
		System.out.println("delete game2 succeeded");
						
		// delete user 2
		res = request("DELETE", "/api/users/2");
		assertEquals(200, res.status);
		System.out.println("delete user2 succeeded");
				
		// delete game 1
		res = request("DELETE", "/api/store/games/1");
		assertEquals(200, res.status);
		System.out.println("delete game1 succeeded");
				
		// delete user 1
		res = request("DELETE", "/api/users/1");
		assertEquals(200, res.status);
		System.out.println("delete user1 succeeded");
		
		// delete wrong game
		res = request("DELETE", "/api/store/games/10");
		assertEquals(404, res.status);
				
		// delete wrong user
		res = request("DELETE", "/api/users/10");
		assertEquals(404, res.status);
	}
	
	private TestResponse request(String method, String path) {
		try {
			URL url = new URL("http://localhost:4567" + path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.connect();
			String body = null;
			try {
				body = IOUtils.toString(connection.getInputStream());
			} catch (IOException e) {
				body = null;
			}
			return new TestResponse(connection.getResponseCode(), body);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Sending request failed: " + e.getMessage());
			return null;
		}
	}

	private static class TestResponse {

		public final String body;
		public final int status;

		public TestResponse(int status, String body) {
			this.status = status;
			this.body = body;
		}

		public Map<String,String> json() {
			return new Gson().fromJson(body, HashMap.class);
		}
	}

}
