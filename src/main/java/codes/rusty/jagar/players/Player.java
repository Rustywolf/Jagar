package codes.rusty.jagar.players;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.game.Border;
import codes.rusty.jagar.net.PacketReceiver;
import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.net.packets.in.PacketInSpectate;
import codes.rusty.jagar.net.packets.in.PacketInSplit;
import codes.rusty.jagar.net.packets.out.PacketOutSetBorder;
import codes.rusty.jagar.nodes.PlayerNode;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.java_websocket.WebSocket;

public class Player implements PacketReceiver {
    
    private final int playerId;
    private final WebSocket socket;
    private final ArrayList<PlayerNode> nodes;
    
    private String nickName;
    private Color color;
    private double mouseX = 0;
    private double mouseY = 0;
    private boolean spectating = false;
    
    public Player(int id, WebSocket socket) {
        this.playerId = id;
        this.socket = socket;
        this.nodes = new ArrayList<>();
        this.nickName = "An unnamed cell";
        this.color = new Color(0xFF, 0xFF, 0x00);
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

    public double getMouseY() {
        return mouseY;
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
    }
    
    public void destroyNode(PlayerNode node) {
        if (nodes.contains(node)) {
            nodes.remove(node);
            if (!node.isDestroyed()) {
                node.destroy(null);
            }
        }
    }
    
    public void tick() {
        Core.getGame().getMechanics().onTick(this);
    }
    
    public List<PlayerNode> getNodes() {
        return new ArrayList<>(nodes);
    }
    
    @Override
    public void onPacketInSetNickname(PacketInSetNickname packet) {
        this.setNickName(packet.getName());
        this.setSpectating(false); // I assume this is how it works?
    }

    @Override
    public void onPacketInSpectate(PacketInSpectate packet) {
        this.setSpectating(true);
    }

    @Override
    public void onPacketInMouseMove(PacketInMouseMove packet) {
        this.mouseX = packet.getMouseX();
        this.mouseY = packet.getMouseY();
    }

    @Override
    public void onPacketInSplit(PacketInSplit packet) {
        
    }

    @Override
    public void onPacketInQPressed(PacketInQPressed packet) {
        
    }

    @Override
    public void onPacketInQReleased(PacketInQReleased packet) {
        
    }

    @Override
    public void onPacketInEjectMass(PacketInEjectMass packet) {
        
    }

    @Override
    public void onPacketInReset(PacketInReset packet) {
        if (this.nodes.isEmpty()) {
            PlayerNode node = Core.getServer().getNodeHandler().newPlayerNode(this);
            node.setSize(100);
            node.setX(100.0f);
            node.setY(100.0f);
        }
        
        PacketOut packetOut = new PacketOutSetBorder(Border.DEFAULT);
        packetOut.write(socket);
    }
    
    
}
