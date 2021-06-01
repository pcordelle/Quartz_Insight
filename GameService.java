package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GameService {
	
	// returns a list of all games
	public List<Game> getAllGames() {

		List<Game> list = new ArrayList<Game>();
		
        try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/qieam","postgres","souris");
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          ResultSet res = stmt.executeQuery("SELECT * FROM games");
	          while(res.next())
	          {
	            Game game = new Game();
	    		game.setId(res.getInt(1));
	    		game.setTitle(res.getString(2));
	    		game.setCover(res.getString(3));
	    		list.add(game);
	          }
	          conn.close();
          }
        }
        catch(Exception e){ 
          System.out.println(e);
          list = null;
        }
   
		return list;
	}
	
	// create game
	public int createGame(String title, String cover) {

		int id = 0;
		List<Game> list = getAllGames();
		if (list != null) {
			for (int i=0; i<list.size(); i++)
			{
				Game game = list.get(i);
				if (game.getId() > id) id = game.getId();
			}
		}
		id ++;

        try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/qieam","postgres","souris");
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          int ret = stmt.executeUpdate("INSERT INTO games VALUES (" + id  + ",'" + title + "','" + cover + "')");
	          if (ret == 0) id = -1;
	          conn.close();
          }
        }
        catch(Exception e){ 
          System.out.println(e);
          id = -1;
        }
        
        return id;
	}
	
	// get game
	public Game getGame(int gameId) {
		
		Game game = new Game();
		
		try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/qieam","postgres","souris");
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          ResultSet res = stmt.executeQuery("SELECT * FROM games where id = '" + gameId + "'" );
	          res.next();
	          
	          game.setId(res.getInt(1));
	    	  game.setTitle(res.getString(2));
	    	  game.setCover(res.getString(3));
	          
	    	  conn.close();
          }
        }
        catch(Exception e){ 
          System.out.println(e);
          game = null;
        }
		
		return game;
	}
	
	// delete game
	public int deleteGame(int gameId) {
		
		int ret = 0;
		
		try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/qieam","postgres","souris");
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          ret = stmt.executeUpdate("DELETE FROM games where id = '" + gameId + "'" );
	    	  conn.close();
          }
        }
        catch(Exception e){ 
          System.out.println(e);
          ret = 0;
        }
		
		return ret;
	}
}
