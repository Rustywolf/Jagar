package codes.rusty.jagar.nodes;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.Handler;
import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.net.packets.out.PacketOutAddNode;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateNodes;
import codes.rusty.jagar.players.Player;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NodeHandler implements Handler {
    
    private final ConcurrentHashMap<Integer, Node> byId;    
    private final ConcurrentLinkedQueue<Integer> freeIds;
    private final HashMap<Integer, Integer> removedQueue;
    
    public NodeHandler() {
        this.byId = new ConcurrentHashMap<>();
        this.freeIds = new ConcurrentLinkedQueue<>();
        this.removedQueue = new HashMap<>();
    }
    
    public PlayerNode newPlayerNode(Player player) {
        int id = getFreeId();
        PlayerNode node = new PlayerNode(id, player);
        byId.put(id, node);
        player.addNode(node);
        
        PacketOut packet = new PacketOutAddNode(id);
        //Core.getServer().getPlayerHandler().sendToAll(packet);
        return node;
    }
    
    private int getFreeId() {
        if (freeIds.isEmpty()) {
            return byId.size() + 1;
        } else {
            return freeIds.poll();
        }
    }
    
    public Node getNode(int id) {
        return byId.get(id);
    }
    
    protected void destroyNode(Node node) {
        this.removedQueue.put(node.getId(), node.getKillerId());
        byId.remove(node.getId());
        freeIds.add(node.getId());
    }
    
    public void updateNodes() {
        byId.values().stream().forEach((node) -> {
            node.tick();
        });
        
        PacketOut packet = new PacketOutUpdateNodes(byId.values(), removedQueue);
        Core.getServer().getPlayerHandler().sendToAll(packet);
        removedQueue.clear();
    }
    
    public void sendNodes(Player... players) {
        
    }

    @Override
    public void destroy() {
        // Something should probably happen here
    }
}
