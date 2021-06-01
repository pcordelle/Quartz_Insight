package app;

public class Game {
	private int id;
	private String title;
	private String cover;
	
	public void setId(int id0) {
		id = id0;
	}
	
	public int getId( ) {
		return id;
	}
	
	public void setTitle(String title0) {
		title = new String(title0);
	}
	
	public String getTitle( ) {
		return title;
	}
	
	public void setCover(String cover0) {
		cover = new String(cover0);
	}
	
	public String getCover( ) {
		return cover;
	}
}

