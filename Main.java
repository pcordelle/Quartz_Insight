package app;

public class Main {
    public static void main(String[] args) {
    	// Launch service
    	UserService userService = new UserService();
    	GameService gameService = new GameService();
    	new UserController(userService, gameService);
    	new GameController(gameService);
    }
}

