package app;

public class User {
	private int id;
	private String username;
	private String email;
	private int[] games;
	private int[] friends;
	
	public void setId(int id0) {
		id = id0;
	}
	
	public int getId( ) {
		return id;
	}
	
	public void setUsername(String username0) {
		username = new String(username0);
	}
	
	public String getUsername( ) {
		return username;
	}
	
	public void setEmail(String email0) {
		email = new String(email0);
	}
	
	public String getEmail( ) {
		return email;
	}
	
	public void setGames(int[] games0) {
		games = games0;
	}
	
	public int[] getGames( ) {
		return games;
	}
	
	public void setFriends(int[] friends0) {
		friends = friends0;
	}
	
	public int[] getFriends( ) {
		return friends;
	}
}

