package codes.rusty.jagar.players;

import codes.rusty.jagar.net.packets.out.PacketOutAddNode;
import codes.rusty.jagar.nodes.PlayerNode;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.java_websocket.WebSocket;

public class Player {

    private static final Random RANDOM = new Random();
    
    private final int playerId;
    private final WebSocket socket;
    private final ArrayList<PlayerNode> nodes;
    private final HashMap<String, Object> data;

    private String nickName;
    private Color color;
    private double mouseX = 0;
    private double mouseY = 0;
    private boolean spectating = false;

    public Player(int id, WebSocket socket) {
        this.playerId = id;
        this.socket = socket;
        this.nodes = new ArrayList<>();
        this.data = new HashMap<>();
        this.nickName = "An unnamed cell";
        this.color = new Color(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
    }

    public int getPlayerId() {
        return playerId;
    }

    public WebSocket getSocket() {
        return socket;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        for (PlayerNode node : nodes) {
            node.setNickName(nickName);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        for (PlayerNode node : nodes) {
            node.setColor(color);
        }
    }

    public double getMouseX() {
        return mouseX;
    }
    
    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }
    
    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public boolean isSpectating() {
        return spectating;
    }

    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }

    public void addNode(PlayerNode node) {
        if (!node.getOwner().equals(this)) {
            throw new IllegalArgumentException("Cannot add node of another player!");
        }

        node.setNickName(nickName);
        node.setColor(color);
        nodes.add(node);

        new PacketOutAddNode(node.getId()).write(socket);
    }

    public void destroyNode(PlayerNode node) {
        if (nodes.contains(node)) {
            nodes.remove(node);
            if (!node.isDestroyed()) {
                node.destroy(null);
            }
        }
    }
    
    public void setData(String key, Object data) {
        if (data == null) {
            this.data.remove(key);
        } else {
            this.data.put(key, data);
        }
    }
    
    public Object getData(String key) {
        return data.get(key);
    }
    
    public<T> T getData(String key, Class<T> expected) {
        if (data.containsKey(key)) {
            return expected.cast(data.get(key));
        } else {
            return null;
        }
    }
    
    public<T> T getOrDefault(String key, T defaultValue) {
        if (data.containsKey(key)) {
            return (T) data.get(key);
        } else {
            return defaultValue;
        }
    }

    public void tick() {
        
    }

    public List<PlayerNode> getNodes() {
        return new ArrayList<>(nodes);
    }

}
