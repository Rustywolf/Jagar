package codes.rusty.jagar.logic.games;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.logic.Border;
import codes.rusty.jagar.logic.GameHandler;
import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.net.packets.in.PacketInSplit;
import codes.rusty.jagar.net.packets.out.PacketOutSetBorder;
import codes.rusty.jagar.nodes.MassNode;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.nodes.PlayerNode;
import codes.rusty.jagar.players.Player;
import java.awt.Color;

public class LifeGameHandler extends GameHandler {

    private static final int GRID_WIDTH = 16;
    private static final int GRID_HEIGHT = 16;
    private static final Border BORDER = new Border(0, 0, GRID_WIDTH*50, GRID_HEIGHT*50);
    
    private Player player;

    private boolean started = false;
    private int tick = 0;
    private boolean qPressed = false;
    private int lastMouseX = -1;
    private int lastMouseY = -1;
    
    private boolean[][] cells;
    private boolean[][] buffer;
    private final Node[][] nodes;

    public LifeGameHandler() {
        this.cells = new boolean[GRID_WIDTH][GRID_HEIGHT];
        this.buffer = new boolean[GRID_WIDTH][GRID_HEIGHT];
        this.nodes = new Node[GRID_WIDTH][GRID_HEIGHT];
    }

    @Override
    public void enable() {
        this.setBorder(BORDER);
        this.setupBorder();
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
    
    private void reset() {
        this.cells = new boolean[GRID_WIDTH][GRID_HEIGHT];
        this.buffer = new boolean[GRID_WIDTH][GRID_HEIGHT];
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                removeNode(x, y);
                nodes[x][y] = null;
            }
        }
    }
    
    @Override
    public void tickGame() {
        if (player != null && started && tick++ % 10 == 0) {
            
            if (started) {
                for (int x = 0; x < GRID_WIDTH; x++) {
                    for (int y = 0; y < GRID_HEIGHT; y++) {
                        int count = getCount(x, y);
                        if (cells[x][y]) {
                            if (count < 2 || count > 3) {
                                buffer[x][y] = false;
                                removeNode(x, y);
                            } else {
                                buffer[x][y] = true;
                            }
                        } else {
                            if (count == 3) {
                                buffer[x][y] = true;
                                spawnNode(x, y);
                            }
                        }
                    }
                }
                
                this.cells = buffer;
                this.buffer = new boolean[GRID_WIDTH][GRID_HEIGHT];
            }
        }
    }
    
    private void spawnNode(int x, int y) {
        Node node = new MassNode(Short.MAX_VALUE + (y * GRID_WIDTH) + x);
        Core.getNodeHandler().addNode(node);
        node.setColor(Color.YELLOW);
        node.setX(x * 50 + 25f);
        node.setY(y * 50 + 25f);
        node.setMass(22.3f);
        nodes[x][y] = node;
    }

    private void removeNode(int x, int y) {
        if (nodes[x][y] != null) {
            nodes[x][y].destroy(null);
        }
    }
    
    private int getCount(int x, int y) {
        int count = 0;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                if (inBounds(x + dx, y + dy) && !(dx == 0 && dy == 0)) {
                    if (cells[x+dx][y+dy]) {
                        count++;
                    }
                }
            }
        }
        
        return count;
    }
    
    private boolean inBounds(int x, int y) {
        return (x >= 0 && x < GRID_WIDTH && y >= 0 && y < GRID_HEIGHT);
    }
    
    @Override
    public void onPacketInSplit(Player player, PacketInSplit packet) {
        if (player.equals(this.player)) {
            started = !started;
        }
    }

    @Override
    public void onPacketInQPressed(Player player, PacketInQPressed packet) {
        if (player.equals(this.player)) {
            qPressed = true;
            if (!started) {
                int mouseGridX = (int) Math.floor(player.getMouseX() / 50);
                int mouseGridY = (int) Math.floor(player.getMouseY() / 50); 
                if (inBounds(mouseGridX, mouseGridY)) {
                    if (mouseGridX != lastMouseX || mouseGridY != lastMouseY) {
                        lastMouseX = mouseGridX;
                        lastMouseY = mouseGridY;

                        cells[lastMouseX][lastMouseY] = !cells[lastMouseX][lastMouseY];
                        if (cells[lastMouseX][lastMouseY]) {
                            spawnNode(lastMouseX, lastMouseY);
                        } else {
                            removeNode(lastMouseX, lastMouseY);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onPacketInQReleased(Player player, PacketInQReleased packet) {
        if (player.equals(this.player)) {
            qPressed = false;
        }
    }

    @Override
    public void onPacketInEjectMass(Player player, PacketInEjectMass packet) {
        if (player.equals(this.player) && !started) {
            reset();
        }
    }
    
    @Override
    public void onPacketInMouseMove(Player player, PacketInMouseMove packet) {
        player.setMouseX(packet.getMouseX());
        player.setMouseY(packet.getMouseY());
        /*player.getNodes().forEach(node -> {
            node.setX((float) packet.getMouseX());
            node.setY((float) packet.getMouseY());
        });*/
        
        if (player.equals(this.player) && qPressed && !started) {
            int mouseGridX = (int) Math.floor(packet.getMouseX() / 50);
            int mouseGridY = (int) Math.floor(packet.getMouseY() / 50); 
            if (inBounds(mouseGridX, mouseGridY)) {
                if (mouseGridX != lastMouseX || mouseGridY != lastMouseY) {
                    lastMouseX = mouseGridX;
                    lastMouseY = mouseGridY;

                    cells[lastMouseX][lastMouseY] = !cells[lastMouseX][lastMouseY];
                    if (cells[lastMouseX][lastMouseY]) {
                        spawnNode(lastMouseX, lastMouseY);
                    } else {
                        removeNode(lastMouseX, lastMouseY);
                    }
                }
            }
        }
    }

    @Override
    public void onPacketInReset(Player player, PacketInReset packet) {
        if (player.getNodes().isEmpty()) {
            PlayerNode node = Core.getNodeHandler().newPlayerNode(player);
            node.setNickName("");
            node.setMass(1f);
            node.setX((float) Math.floor(GRID_WIDTH*50 / 2));
            node.setY((float) Math.floor(GRID_HEIGHT*50 / 2));
        }
        
        if (this.player == null) {
            this.player = player;
        }

        PacketOut packetOut = new PacketOutSetBorder(Core.getGame().getBorder());
        packetOut.write(player.getSocket());
    }

    @Override
    public void onPacketInSetNickname(Player player, PacketInSetNickname packet) {
        player.setNickName("");
    }

    
    @Override
    public void destroy() {
        
    }
    
}
