package codes.rusty.jagar;

import codes.rusty.jagar.game.GameHandler;
import codes.rusty.jagar.game.Mechanics;
import codes.rusty.jagar.net.ServerHandler;
import codes.rusty.jagar.nodes.NodeHandler;
import codes.rusty.jagar.players.PlayerHandler;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Server extends TimerTask implements Handler {
    
    private final ServerHandler serverHandler;
    private final NodeHandler nodeHandler;
    private final PlayerHandler playerHandler;
    private GameHandler gameHandler;
    
    private final Timer timer;
    private final ArrayList<Runnable> queuedActions;
    
    public Server() {
        try {
            this.serverHandler = new ServerHandler();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("WebSocket server failed to initialize!");
        }
        
        this.gameHandler = null; // Temp
        this.nodeHandler = new NodeHandler();
        this.playerHandler = new PlayerHandler();
        
        timer = new Timer();
        queuedActions = new ArrayList<>();
    }
    
    public void start() {
        serverHandler.start();
        timer.scheduleAtFixedRate(this, 50, 50);
    }

    @Override
    public void run() {
        queuedActions.stream().forEach((runnable) -> {
            runnable.run();
        });
        queuedActions.clear();
        nodeHandler.updateNodes();
    }
    
    public void queueAction(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable cannot be null!");
        }
        
        this.queuedActions.add(runnable);
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public NodeHandler getNodeHandler() {
        return nodeHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
    
    @Override
    public void destroy() {
        timer.cancel();
        serverHandler.destroy();
        playerHandler.destroy();
        nodeHandler.destroy();
        gameHandler.destroy();
    }
    
}
