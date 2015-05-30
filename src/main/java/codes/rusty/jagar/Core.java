package codes.rusty.jagar;

import codes.rusty.jagar.game.GameHandler;
import codes.rusty.jagar.logging.CoreLoggingHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Core {
    
    private static final Logger logger;
    static {
        logger = Logger.getLogger("codes.rusty.jagar.Core");
        logger.setUseParentHandlers(false);
        logger.addHandler(new CoreLoggingHandler());
        logger.setLevel(Level.ALL);
    }
    
    private static Server server;
    
    protected static void setServer(Server server) {
        Core.server = server;
    }
    
    public static Server getServer() {
        return server;
    }
    
    public static void setGame(GameHandler handler) {
        Core.server.setGameHandler(handler);
    }
    
    public static GameHandler getGame() {
        return Core.server.getGameHandler();
    }
    
    public static Logger getLogger() {
        return logger;
    }
    
    public static int getDefaultPort() {
        return 443;
    }
    
}
