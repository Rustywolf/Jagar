package codes.rusty.jagar.game;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.out.PacketOutSetBorder;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateLeaderboardFFA;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateLeaderboardFFA.NodeEntry;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.nodes.PlayerNode;
import codes.rusty.jagar.players.Player;
import com.google.common.collect.Table;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SnakeGameHandler extends GameHandler {

    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;
    private static final Border BORDER = new Border(0, 0, GRID_WIDTH*50, GRID_HEIGHT*50); // 20x20 grid
    private static final Random RANDOM = new Random();
    
    private int x = RANDOM.nextInt(GRID_WIDTH);
    private int y = RANDOM.nextInt(GRID_HEIGHT);
    private int tick = 0;
    private int score = 0;
    private int lastScore = -1;
    private boolean lost = false;
    
    private Node appleNode;
    private List<Coord> appleCoords;
    private ArrayList<Node> tailNodes;
    private Player player;
    
    @Override
    public void enable() {
        this.setBorder(BORDER);
        this.tailNodes = new ArrayList<>();
        this.appleCoords = new ArrayList<>();
        this.newApple();
        this.setupBorder();
    }
    
    private void newApple() {
        this.appleNode = Core.getNodeHandler().newMassNode();
        this.appleNode.setMass(11.15f);
        this.appleNode.setColor(Color.RED);
        float x;
        float y;
        while (true) {
            x = RANDOM.nextInt(GRID_WIDTH) * 50 + 25f;
            y = RANDOM.nextInt(GRID_HEIGHT) * 50 + 25f;
            
            boolean freeArea = true;
            for (Node node : tailNodes) {
                if (node.getX() == x && node.getY() == y) {
                    freeArea = false;
                }
            }
            
            if (freeArea) {
                break;
            }
        }
        this.appleNode.setX(x);
        this.appleNode.setY(y);
    }
    
    private void setupBorder() {
        for (int x = -1; x <= GRID_WIDTH; x++) {
            Node above = Core.getNodeHandler().newMassNode();
            above.setMass(22.3f);
            above.setX(x*50 + 25f);
            above.setY(-25f);
            above.setColor(Color.BLACK);
            
            Node below = Core.getNodeHandler().newMassNode();
            below.setMass(22.3f);
            below.setX(x*50 + 25f);
            below.setY(GRID_HEIGHT*50 + 25f);
            below.setColor(Color.BLACK);
        }
        
        for (int y = -1; y <= GRID_WIDTH; y++) {
            Node above = Core.getNodeHandler().newMassNode();
            above.setMass(22.3f);
            above.setX(-25f);
            above.setY(y*50 + 25f);
            above.setColor(Color.BLACK);
            
            Node below = Core.getNodeHandler().newMassNode();
            below.setMass(22.3f);
            below.setX(GRID_WIDTH*50 + 25f);
            below.setY(y*50 + 25f);
            below.setColor(Color.BLACK);
        }
    }
    
    @Override
    public void tickGame() {
        if (player != null && tailNodes.size() > 0 && tick++ % 10 == 0 && !lost) {
            
            Node tail = tailNodes.get(tailNodes.size() - 1);
            if (appleCoords.size() > 0) {
                Coord coord = appleCoords.get(0);
                if (tail.getX() == coord.x && tail.getY() == coord.y) {
                    Node newNode = Core.getNodeHandler().newMassNode();
                    newNode.setColor(player.getColor());
                    newNode.setMass(22.3f);
                    tailNodes.add(newNode);
                    appleCoords.remove(0);
                }
            }
            
            for (int i = tailNodes.size() - 1; i > 0; i--) {
                Node base = tailNodes.get(i);
                Node conn = tailNodes.get(i-1);
                base.setX(conn.getX());
                base.setY(conn.getY());
            }
            
            Node head = tailNodes.get(0);
            switch (getDirection()) {
                case 0:
                    head.setY(head.getY() - 50);
                    break;
                    
                case 1:
                    head.setX(head.getX() + 50);
                    break;
                    
                case 2:
                    head.setY(head.getY() + 50);
                    break;
                    
                case 3:
                    head.setX(head.getX() - 50);
                    break;
            }
            
            if (appleNode.getX() == head.getX() && appleNode.getY() == head.getY()) {
                score++;
                appleCoords.add(new Coord(appleNode.getX(), appleNode.getY()));
                appleNode.destroy(head);
                this.newApple();
            }
            
            for (Node node : tailNodes) {
                if (node.getId() != head.getId()) {
                    if (node.getX() == head.getX() && node.getY() == head.getY()) {
                        lost = true;
                        lastScore = -1;
                        break;
                    }
                }
            }
            
            if (head.getX() < 0 || head.getY() < 0 || head.getX() > GRID_WIDTH*50 || head.getY() > GRID_HEIGHT*50) {
                lost = true;
                lastScore = -1;
            }
        }
    }
    
    private int getDirection() {
        Node head = tailNodes.get(0);
        double mouseDistanceX = player.getMouseX() - head.getX();
        double mouseDistanceY = player.getMouseY() - head.getY();
        if (Math.abs(mouseDistanceX) > Math.abs(mouseDistanceY)) {
            if (mouseDistanceX > 0) {
                return 1;
            } else {
                return 3;
            }
        } else {
            if (mouseDistanceY < 0) {
                return 0;
            } else {
                return 2;
            }
        }
    }
    

    @Override
    public void render(Table<Integer, Integer, Set<Node>> chunks) {
        if (score != lastScore) {
            lastScore = score;
            List<NodeEntry> entries = new ArrayList<>();
            if (lost) {
                entries.add(new NodeEntry(Short.MAX_VALUE - 1, "Game Over!"));
            }
            entries.add(new NodeEntry(Short.MAX_VALUE, score + ""));
            
            Core.getPlayerHandler().sendToAll(new PacketOutUpdateLeaderboardFFA(entries.toArray(new NodeEntry[entries.size()])));
        }
    }

    @Override
    public void destroy() {
        
    }
    
    @Override
    public void onPacketInMouseMove(Player player, PacketInMouseMove packet) {
        player.setMouseX(packet.getMouseX());
        player.setMouseY(packet.getMouseY());
    }

    @Override
    public void onPacketInReset(Player player, PacketInReset packet) {
        player.setNickName("");
        if (player.getNodes().isEmpty()) {
            PlayerNode node = Core.getNodeHandler().newPlayerNode(player);
            node.setMass(22.3f);
            node.setX(225f);
            node.setY(225f);
            tailNodes.add(node);
        }

        PacketOut packetOut = new PacketOutSetBorder(Core.getGame().getBorder());
        packetOut.write(player.getSocket());
        this.player = player;
    }
    
    private static final class Coord {
        private final float x;
        private final float y;

        public Coord(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
    
}
