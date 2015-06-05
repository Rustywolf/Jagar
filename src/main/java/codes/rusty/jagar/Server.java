package codes.rusty.jagar;

import codes.rusty.jagar.game.GameHandler;
import codes.rusty.jagar.game.SnakeGameHandler;
import codes.rusty.jagar.net.ServerHandler;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.nodes.NodeHandler;
import codes.rusty.jagar.players.PlayerHandler;
import com.google.common.collect.Table;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server extends TimerTask implements Handler {
    
    private final ServerHandler serverHandler;
    private final NodeHandler nodeHandler;
    private final PlayerHandler playerHandler;
    private GameHandler gameHandler;
    
    private final Timer timer;
    private final ConcurrentLinkedQueue<Runnable> queuedPackets;
    
    public Server() {
        try {
            this.serverHandler = new ServerHandler();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("WebSocket server failed to initialize!");
        }
        
        this.nodeHandler = new NodeHandler();
        this.playerHandler = new PlayerHandler();
        this.gameHandler = new SnakeGameHandler();
        
        timer = new Timer();
        queuedPackets = new ConcurrentLinkedQueue<>();
    }
    
    public void start() {
        serverHandler.start();
        timer.scheduleAtFixedRate(this, 50, 50);
        
        gameHandler.enable();
    }

    @Override
    public void run() {
        queuedPackets.stream().forEach((runnable) -> {
            runnable.run();
        });
        queuedPackets.clear();
        
        playerHandler.tickPlayers();
        nodeHandler.tickNodes();
        gameHandler.tickGame();
        
        Table<Integer, Integer, Set<Node>> chunks = nodeHandler.getNodeChunkMap();
        nodeHandler.render(chunks);
        gameHandler.render(chunks);
    }
    
    public void queuePacket(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable cannot be null!");
        }
        
        this.queuedPackets.add(runnable);
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
