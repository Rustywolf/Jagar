package codes.rusty.jagar.players;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.net.PacketReceiver;
import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.net.packets.in.PacketInSpectate;
import codes.rusty.jagar.net.packets.in.PacketInSplit;
import codes.rusty.jagar.net.packets.out.PacketOutAddNode;
import codes.rusty.jagar.nodes.PlayerNode;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.java_websocket.WebSocket;

public class Player implements PacketReceiver {

    private static final Random RANDOM = new Random();
    
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

    public void tick() {
        
    }

    public List<PlayerNode> getNodes() {
        return new ArrayList<>(nodes);
    }

    @Override
    public void onPacketInSetNickname(PacketInSetNickname packet) {
        Core.getGame().onPacketInSetNickname(this, packet);
    }

    @Override
    public void onPacketInSpectate(PacketInSpectate packet) {
        Core.getGame().onPacketInSpectate(this, packet);
    }

    @Override
    public void onPacketInMouseMove(PacketInMouseMove packet) {
        Core.getGame().onPacketInMouseMove(this, packet);
    }

    @Override
    public void onPacketInSplit(PacketInSplit packet) {
        Core.getGame().onPacketInSplit(this, packet);
    }

    @Override
    public void onPacketInQPressed(PacketInQPressed packet) {
        Core.getGame().onPacketInQPressed(this, packet);
    }

    @Override
    public void onPacketInQReleased(PacketInQReleased packet) {
        Core.getGame().onPacketInQReleased(this, packet);
    }

    @Override
    public void onPacketInEjectMass(PacketInEjectMass packet) {
        Core.getGame().onPacketInEjectMass(this, packet);
    }

    @Override
    public void onPacketInReset(PacketInReset packet) {
        Core.getGame().onPacketInReset(this, packet);
    }

}
