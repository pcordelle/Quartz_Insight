package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserService {

	private String DB_HOST = "localhost";
	private String DB_PORT = "5432";
	private String DB_NAME = "qieam";
	private String DB_USER = "postgres";
	private String DB_PASS = "postgres";
	
	private Connection GetConnection() throws Exception
	{
		try
		{
			Connection conn = DriverManager.getConnection(
		          "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME , DB_USER, DB_PASS);
			return conn;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public UserService()
	{
		String dbhost = System.getenv("DB_HOST");
		if (dbhost != null)
			DB_HOST=dbhost;
		String dbport = System.getenv("DB_PORT");
		if (dbport != null)
			DB_PORT=dbport;
		String dbname = System.getenv("DB_NAME");
		if (dbname != null)
			DB_NAME=dbname;
		String dbuser = System.getenv("DB_USER");
		if (dbuser != null)
			DB_USER=dbuser;
		String dbpass = System.getenv("DB_PASS");
		if (dbpass != null)
			DB_PASS=dbpass;
	}
	
	// returns a list of all users
	public List<User> getAllUsers() {

		List<User> list = new ArrayList<User>();
		
        try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = GetConnection();
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          ResultSet res = stmt.executeQuery("SELECT * FROM users");
	          while(res.next())
	          { 
	            User user = new User();
	    		user.setId(res.getInt(1));
	    		user.setUsername(res.getString(2));
	    		user.setEmail(res.getString(3));
	    		list.add(user);
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
	
	// create user
	public int createUser(String username, String email) {

		int id = 0;
		List<User> list = getAllUsers();
		if (list != null) {
			for (int i=0; i<list.size(); i++)
			{
				User user = list.get(i);
				if (user.getId() > id) id = user.getId();
			}
		}
		id ++;
		
        try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = GetConnection();
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          int ret = stmt.executeUpdate("INSERT INTO users VALUES (" + id  + ",'" + username + "','" + email + "')");
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
	
	// get user
	public User getUser(int userId) {
		
		User user = new User();
		
		try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = GetConnection();
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          ResultSet res = stmt.executeQuery("SELECT * FROM users WHERE id = '" + userId + "'" );
	          res.next();
	          user.setId(res.getInt(1));
	    	  user.setUsername(res.getString(2));
	    	  user.setEmail(res.getString(3));
	          
	    	  conn.close();
          }
        }
        catch(Exception e){ 
        	System.out.println(e);
        	user = null;
        }
		
		return user;
	}
	
	// delete user
	public int deleteUser(int userId) {
		
		int ret = 0;
		
		try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = GetConnection();
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          ret = stmt.executeUpdate("DELETE FROM users WHERE id = '" + userId + "'" );
	    	  conn.close();
          }
        }
        catch(Exception e){ 
        	System.out.println(e);
        	ret = 0;
        }
		
		return ret;
	}
	
	// add game to user
	public int addUserGame(int userId, int gameId) {
		
		int ret =  0;
        try
        {
          int index = 1;
          List<Integer> list = getUserGames(userId);
          if (list != null)
          {
        	 for (int i=0; i<list.size(); i++)
        	 {
        		 if (list.get(i) == gameId)
        		 {
        			 ret = -1;
        			 break;
        		 }
        	 }
        	 index = list.size()+1;
          }
          
          if (ret != -1)
          {
	          Class.forName("org.postgresql.Driver");
	          
	          Connection conn = GetConnection();
	          
	          if (conn != null) {
		          Statement stmt = conn.createStatement();
		          ret = stmt.executeUpdate("UPDATE users SET games[" + index + "] = "  + gameId  + " WHERE id = " + userId);
			      conn.close();
		      }
          }
		}
	    catch(Exception e){ 
	       System.out.println(e);
	       ret = 0;
	    }
	        
	    return ret;
	}
	
	// returns a list of all games of user
	public List<Integer> getUserGames(int userId) {

		List<Integer> list = null;
		
        try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = GetConnection();
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          ResultSet res = stmt.executeQuery("SELECT games FROM users WHERE id = " + userId);
	          res.next();
	          if (res.getArray(1) != null ) {
	        	  list = new ArrayList<Integer>();
	        	  ResultSet res1 = res.getArray(1).getResultSet();
	        	  while(res1.next())
	        	  {
	        		  list.add(res1.getInt(2));
	        	  }
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
	
	// delete game from user
	public int deleteUserGame(int userId, int gameId) {
		
		int ret =  0;
		
        try
        {
        	List<Integer> list = getUserGames(userId);
        	if (list != null) {
        		boolean bFound = false;
        		for (int i=0; i<list.size(); i++)
        		{
        			if (list.get(i) == gameId)
        			{
        				list.remove(i);
        				bFound = true;
        				break;
        			}
        		}
        		
        		if (bFound == true)
        		{
				    Class.forName("org.postgresql.Driver");
				          
				    Connection conn = GetConnection();
				    
				    if (conn != null) {
					    Statement stmt = conn.createStatement();
					    if (list.size() == 0)
					    	ret = stmt.executeUpdate("UPDATE users SET games = ARRAY[]::integer[] WHERE id = " + userId);
					    else
					    	ret = stmt.executeUpdate("UPDATE users SET games = ARRAY"  + list.toString()  + " WHERE id = " + userId);
						conn.close();
					}
        		}
        		else
        			ret = -1;	
        	}
		}
	    catch(Exception e){ 
	        System.out.println(e);
	        ret = 0;
	    }
	        
	    return ret;
	}
	
	// returns a list of all friends of user
	public List<Integer> getUserFriends(int userId) {

		List<Integer> list = null;
		
        try
        {
          Class.forName("org.postgresql.Driver");
          
          Connection conn = GetConnection();
          
          if (conn != null) {
	          Statement stmt = conn.createStatement();
	          ResultSet res = stmt.executeQuery("SELECT friends FROM users WHERE id = " + userId);
	          res.next();
	          if (res.getArray(1) != null ) {
	        	  list = new ArrayList<Integer>();
		          ResultSet res1 = res.getArray(1).getResultSet();
		          while(res1.next())
		          {
		        	  list.add(res1.getInt(2));
		          }
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
	
	// add friend to user
	public int addUserFriend(int userId, int friendId) {
		
		int ret =  0;
        try
        {
          int index = 1;
          List<Integer> list = getUserFriends(userId);
          if (list != null)
          {
        	 for (int i=0; i<list.size(); i++)
        	 {
        		 if (list.get(i) == friendId)
        		 {
        			 ret = -1;
        			 break;
        		 }
        	 }
        	 index = list.size()+1;
          }
          
          if (ret != -1)
          {
	          Class.forName("org.postgresql.Driver");
	          
	          Connection conn = GetConnection();
	          
	          if (conn != null) {
		          Statement stmt = conn.createStatement();
		          ret = stmt.executeUpdate("UPDATE users SET friends[" + index + "] = "  + friendId  + " WHERE id = " + userId);
			      conn.close();
		      }
          }
		}
	    catch(Exception e){ 
	       System.out.println(e);
	       ret = 0;
	    }
	        
	    return ret;
	}
	
	// delete friend to user
	public int deleteUserFriend(int userId, int friendId) {
		
		int ret =  0;
		
        try
        {
        	List<Integer> list = getUserFriends(userId);
        	if (list != null) {
        		boolean bFound = false;
        		for (int i=0; i<list.size(); i++)
        		{
        			if (list.get(i) == friendId)
        			{
        				list.remove(i);
        				bFound = true;
        				break;
        			}
        		}
        		
        		if (bFound == true)
        		{
				    Class.forName("org.postgresql.Driver");
				          
				    Connection conn = GetConnection();
				    
				    if (conn != null) {
					    Statement stmt = conn.createStatement();
					    if (list.size() == 0)
					    	ret = stmt.executeUpdate("UPDATE users SET friends = ARRAY[]::integer[] WHERE id = " + userId);
					    else
					    	ret = stmt.executeUpdate("UPDATE users SET friends = ARRAY"  + list.toString()  + " WHERE id = " + userId);
						conn.close();
					}
        		}
        		else
        			ret = -1;
        	}
		}
	    catch(Exception e){ 
	        System.out.println(e);
	        ret = 0;
	    }
	        
	    return ret;
	}
}
