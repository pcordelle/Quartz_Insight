package app;

import static spark.Spark.*;
import static app.JsonUtil.*;
import java.util.List;

public class UserController {
	public UserController(final UserService userService, final GameService gameService) {
		
		get("/api/users", (req, res) -> {
			List<User> list = userService.getAllUsers();
			if (list != null)
				return list;
			else {
				res.status(404);
				return new ResponseError("No user found");
			}
		}, json());
		post("/api/users", (req, res) -> {
			int id = userService.createUser(req.queryParams("username"), req.queryParams("email"));
			if (id != -1)
				return id;
			else {
				res.status(400);
				return new ResponseError("No user created");
			}
		}, json());
		get("/api/users/:userId", (req, res) ->  {
			int userId = Integer.parseInt(req.params(":userId"));
			User user = userService.getUser(userId);
			if (user != null)
				return user;
			else {
				res.status(404);
				return new ResponseError("No user with id " + userId + " found");
			}	
		},json());
		delete("/api/users/:userId", (req, res) -> { 
			int userId = Integer.parseInt(req.params(":userId"));
			int ret = userService.deleteUser(userId);
			if (ret != 0)
				return ret;
			else {
				res.status(404);
				return new ResponseError("No user with id " + userId + " found");
			}
		}, json());
		post("/api/users/:userId/games", (req, res) ->  {
			int userId = Integer.parseInt(req.params(":userId"));
			User user = userService.getUser(userId);
			if (user != null)
			{
				int gameId = Integer.parseInt(req.queryParams("gameId"));
				Game game = gameService.getGame(gameId);
				if (game != null) {
					int ret = userService.addUserGame(userId, gameId);
					if (ret == -1)
					{
						res.status(409);
						return new ResponseError("gameId already exists");
					}
					if (ret != 0)
						return ret;
					else {
						res.status(400);
						return new ResponseError("Internal error");
					}
				}
				else {
					res.status(404);
					return new ResponseError("No game with id " + gameId + " found");
				}
			}
			else {
				res.status(404);
				return new ResponseError("No user with id " + userId + " found");
			}
		},json());
		get("/api/users/:userId/games", (req, res) ->  {
			int userId = Integer.parseInt(req.params(":userId"));
			User user = userService.getUser(userId);
			if (user != null)
			{
				List<Integer> list = userService.getUserGames(userId);
				if (list != null)
					return list;
				else {
					res.status(404);
					return new ResponseError("No game found");
				}
			}
			else {
				res.status(404);
				return new ResponseError("No user with id " + userId + " found");
			}
		},json());
		delete("/api/users/:userId/games/:gameId", (req, res) ->  {
			int userId = Integer.parseInt(req.params(":userId"));
			User user = userService.getUser(userId);
			if (user != null)
			{
				int gameId = Integer.parseInt(req.params(":gameId"));
				Game game = gameService.getGame(gameId);
				if (game != null) 
				{
					int ret = userService.deleteUserGame(userId, gameId);
					if (ret == -1)
					{
						res.status(404);
						return new ResponseError("No game with id " + gameId + " found");
					}
					else
					{
						if (ret != 0)
							return ret;
						else
						{
							res.status(400);
							return new ResponseError("Internal error");
						}
					}
				}
				else {
					res.status(404);
					return new ResponseError("No game found");
				}
			}
			else {
				res.status(404);
				return new ResponseError("No user with id " + userId + " found");
			}
		},json());
		post("/api/users/:userId/friends", (req, res) ->  {
			int userId = Integer.parseInt(req.params(":userId"));
			User user = userService.getUser(userId);
			if (user != null)
			{	
				int friendId = Integer.parseInt(req.queryParams("friendId"));
				List<User> list = userService.getAllUsers();
				
				boolean bFound = false;
				if (userId != friendId)
				{
					for (int i=0; i<list.size(); i++)
					{
						User user1 = list.get(i);
						if (user1.getId() == friendId)
						{
							bFound = true;
							break;
						}
					}
				}
				
				if (bFound == true)
				{
					int ret = userService.addUserFriend(userId, friendId);
					if (ret == -1)
					{
						res.status(409);
						return new ResponseError("friendId already exists");
					}
					if (ret != 0)
						return ret;
					else {
						res.status(400);
						return new ResponseError("Internal error");
					}
				}
				else
				{
					res.status(404);
					return new ResponseError("No friend with id " + friendId + " found");
				}
			}
			else {
				res.status(404);
				return new ResponseError("No user with id " + userId + " found");
			}
		},json());
		get("/api/users/:userId/friends", (req, res) ->  {
			int userId = Integer.parseInt(req.params(":userId"));
			User user = userService.getUser(userId);
			if (user != null)
			{
				List<Integer> list = userService.getUserFriends(userId);
				if (list != null)
					return list;
				else {
					res.status(404);
					return new ResponseError("No friend found");
				}
			}
			else {
				res.status(404);
				return new ResponseError("No user with id " + userId + " found");
			}
		},json());
		delete("/api/users/:userId/friends/:friendId", (req, res) ->  {
			int userId = Integer.parseInt(req.params(":userId"));
			User user = userService.getUser(userId);
			if (user != null)
			{
				int friendId = Integer.parseInt(req.params(":friendId"));
				int ret = userService.deleteUserFriend(userId, friendId);
				if (ret == -1)
				{
					res.status(404);
					return new ResponseError("No friend with id " + friendId + " found");
				}
				else
				{
					if (ret != 0)
						return ret;
					else {
						res.status(400);
						return new ResponseError("Internal error");
					}
				}
			}
			else {
				res.status(404);
				return new ResponseError("No user with id " + userId + " found");
			}
		},json());
		
		exception(IllegalArgumentException.class, (e, req, res) -> {
			res.status(400);
			res.body(toJson(new ResponseError(e)));
		});
		
		after((req, res) -> {
			res.type("application/json");
		});
	}
}
