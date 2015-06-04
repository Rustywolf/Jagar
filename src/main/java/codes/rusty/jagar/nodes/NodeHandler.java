package codes.rusty.jagar.nodes;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.Handler;
import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateNodes;
import codes.rusty.jagar.players.Player;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NodeHandler implements Handler {
    
    private final ConcurrentHashMap<Integer, Node> byId;    
    private final ConcurrentLinkedQueue<Integer> freeIds;
    private final HashMap<Integer, Integer> removedQueue;
    
    private static final int chunkWidth = 200;
    private static final int chunkHeight = 200;
    
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
    
    public void tickNodes() {
        byId.values().stream().forEach((node) -> {
            node.tick();
        });
    }
    
    public void render(Table<Integer, Integer, Set<Node>> chunks) {
        PacketOut packet = new PacketOutUpdateNodes(byId.values(), removedQueue);
        Core.getServer().getPlayerHandler().sendToAll(packet);
        removedQueue.clear();
    }
    
    public Table<Integer, Integer, Set<Node>> getNodeChunkMap() {
        Table<Integer, Integer, Set<Node>> nodesByChunks = HashBasedTable.create();
        
        byId.values().stream().forEach((node) -> {
            setNodeChunk(nodesByChunks, node);
        });
        
        return nodesByChunks;
    }
    
    private void setNodeChunk(Table<Integer, Integer, Set<Node>> chunks, Node node) {
        int chunkX = (int) Math.floor(node.getVelX() / chunkWidth);
        int chunkY = (int) Math.floor(node.getVelY() / chunkHeight);
        
        Set<Node> set = chunks.get(chunkX, chunkY);
        if (set == null) {
            set = new HashSet<>();
            chunks.put(chunkX, chunkY, set);
        }
        
        set.add(node);
    }

    @Override
    public void destroy() {
        // Something should probably happen here
    }
}
