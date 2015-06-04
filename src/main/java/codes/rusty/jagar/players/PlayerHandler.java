package codes.rusty.jagar.players;

import codes.rusty.jagar.Handler;
import codes.rusty.jagar.net.packets.PacketOut;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.java_websocket.WebSocket;

public class PlayerHandler implements Handler {
    
    private final ConcurrentHashMap<Integer, Player> byId;    
    private final ConcurrentLinkedQueue<Integer> freeIds;
    
    public PlayerHandler() {
        this.byId = new ConcurrentHashMap<>();
        this.freeIds = new ConcurrentLinkedQueue<>();
    }
    
    public Player newPlayer(WebSocket socket) {
        int id;
        if (freeIds.isEmpty()) {
            id = byId.size();
        } else {
            id = freeIds.poll();
        }
        
        Player player = new Player(id, socket);
        byId.put(id, player);
        return player;
    }
    
    public Player getPlayer(int id) {
        return byId.get(id);
    }
    
    protected void removePlayer(Player player) {
        player = byId.remove(player.getPlayerId());
        if (player != null) {
            player.getSocket().close();
            freeIds.add(player.getPlayerId());
        }
    }
    
    public void tickPlayers() {
        byId.values().stream().forEach((Player player) -> {
            player.tick();
        });
    }
    
    public void sendToAll(PacketOut packet) {
        packet.write(byId.values().stream().map(player -> player.getSocket()).toArray(WebSocket[]::new));
    }

    @Override
    public void destroy() {
        // Something should probably happen here
    }
}
