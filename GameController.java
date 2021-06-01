package app;
import static spark.Spark.*;
import static app.JsonUtil.*;
import java.util.List;

public class GameController {
	public GameController(final GameService gameService) {
		
		get("/api/store/games", (req, res) -> {
			List<Game> list = gameService.getAllGames();
			if (list != null)
				return list;
			else {
				res.status(404);
				return new ResponseError("No game found");
			}
		}, json());
		post("/api/store/games", (req, res) -> {
			int id = gameService.createGame(req.queryParams("title"), req.queryParams("cover"));
			if (id != -1)
				return id;
			else {
				res.status(400);
				return new ResponseError("No game created");
			}
		}, json());
		get("/api/store/games/:gameId", (req, res) ->  {
			int gameId = Integer.parseInt(req.params(":gameId"));
			Game game = gameService.getGame(gameId);
			if (game != null)
				return game;
			else {
				res.status(404);
				return new ResponseError("No game with id " + gameId + " found");
			}	
		},json());
		delete("/api/store/games/:gameId", (req, res) -> { 
			int gameId = Integer.parseInt(req.params(":gameId"));
			int ret = gameService.deleteGame(gameId);
			if (ret != 0)
				return ret;
			else {
				res.status(404);
				return new ResponseError("No game with id " + gameId + " found");
			}
		}, json());
		
		exception(IllegalArgumentException.class, (e, req, res) -> {
			res.status(400);
			res.body(toJson(new ResponseError(e)));
		});
		
		after((req, res) -> {
			res.type("application/json");
		});
	}
}

