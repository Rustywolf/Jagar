package codes.rusty.jagar.logic.games;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.logic.Border;
import codes.rusty.jagar.logic.GameHandler;
import codes.rusty.jagar.logic.modules.MouseMoveModule;
import codes.rusty.jagar.logic.modules.PlayerNameSetModule;
import codes.rusty.jagar.logic.modules.PlayerSpawnModule;
import codes.rusty.jagar.logic.modules.WorldBorderModule;
import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSplit;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateLeaderboardFFA;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateLeaderboardFFA.NodeEntry;
import codes.rusty.jagar.nodes.MassNode;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.players.Player;
import com.google.common.collect.Table;
import java.awt.Color;
import java.util.Random;
import java.util.Set;

public class BlockGameHandler extends GameHandler {

    private static final Random RANDOM = new Random();
    private static final int GRID_WIDTH = 64;
    private static final int GRID_HEIGHT = 64;
    private static final Border BORDER = new Border(0, 0, GRID_WIDTH*50, GRID_HEIGHT*50);
    private static final int updateMod = 5;
    
    private final Node[][] blocks = new Node[GRID_WIDTH][GRID_HEIGHT];
  
    private int tick;
    
    @Override
    public void enable() {
        this.registerModule(new MouseMoveModule());
        this.registerModule(new PlayerNameSetModule());
        this.registerModule(new PlayerSpawnModule(22.3f));
        this.registerModule(new WorldBorderModule(GRID_WIDTH+1, GRID_HEIGHT+1));
        this.setBorder(BORDER);
    }

    @Override
    public void destroy() {
        
    }

    @Override
    public void tickGame() {
        for (Player player : Core.getPlayerHandler().getPlayers()) {
            if (player.getOrDefault("q", false)) {
                if (tick % updateMod == player.getOrDefault("offset", 0)) {
                    move(player);
                }
            }
        }
        
        tick++;
    }

    @Override
    public void render(Table<Integer, Integer, Set<Node>> chunks) {
        NodeEntry[] entries = new NodeEntry[Block.values().length];
        for (Player player : Core.getPlayerHandler().getPlayers()) {
            Block selected = player.getOrDefault("block", Block.ERASER);
            for (Block block : Block.values()) {
                int id = Short.MAX_VALUE;
                if (block == selected && player.getNodes().size() > 0) {
                    id = player.getNodes().get(0).getId();
                } else {
                    id += block.ordinal();
                }
                
                entries[block.ordinal()] = new NodeEntry(id, block.name());
            }
           
            new PacketOutUpdateLeaderboardFFA(entries).write(player.getSocket());
        }
    }
    
    private void placeBlock(Block block, int x, int y) {
        if (!isInBounds(x, y)) {
            return;
        }
        
        Node node = blocks[x][y];
        if (node == null && block == Block.ERASER) {
            return;
        } else if (node == null) {
            node = new MassNode(Short.MAX_VALUE + (y * GRID_WIDTH) + x);
            Core.getNodeHandler().addNode(node);
            node.setX(x*50 + 25f);
            node.setY(y*50 + 25f);
            node.setMass(22.3f);
            blocks[x][y] = node;
        }
        
        block.apply(node);
        if (node.isDestroyed()) {
            blocks[x][y] = null;
        }
    }
    
    private void move(Player player) {
        Node spawn = player.getNodes().get(0);
        int x = (int) Math.floor((spawn.getX() - 25f)/50);
        int y = (int) Math.floor((spawn.getY() - 25f)/50);
        
        switch (getDirection(player)) {
            case 0:
                y--;
                break;
                
            case 1:
                x++;
                break;
                
            case 2:
                y++;
                break;
                
            case 3:
                x--;
                break;
        }
        
        if (isInBounds(x, y)) {
            spawn.setX(x * 50 + 25f);
            spawn.setY(y * 50 + 25f);
        }
    }
    
    private int getDirection(Player player) {
        Node spawn = player.getNodes().get(0);
        double mouseDistanceX = player.getMouseX() - spawn.getX();
        double mouseDistanceY = player.getMouseY() - spawn.getY();
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

    private boolean isInBounds(int x, int y) {
        return (x >= 0 && y >= 0 && x < GRID_WIDTH && y < GRID_HEIGHT);
    }

    @Override
    public void onPacketInSplit(Player player, PacketInSplit packet) {
        Block block = player.getOrDefault("block", Block.ERASER);
        Node spawn = player.getNodes().get(0);
        int x = (int) Math.floor((spawn.getX() - 25f)/50);
        int y = (int) Math.floor((spawn.getY() - 25f)/50);
        
        placeBlock(block, x, y);
    }

    @Override
    public void onPacketInEjectMass(Player player, PacketInEjectMass packet) {
        Block current = player.getOrDefault("block", Block.ERASER);
        Block next = Block.values()[(current.ordinal() + 1) % Block.values().length];
        player.setData("block", next);
    }
    
    @Override
    public void onPacketInQPressed(Player player, PacketInQPressed packet) {
        player.setData("q", true);
        player.setData("offset", tick % updateMod - 1);
        move(player);
    }

    @Override
    public void onPacketInQReleased(Player player, PacketInQReleased packet) {
        player.setData("q", false);
        player.setData("offset", null);
    }

    @Override
    public void onPacketInReset(Player player, PacketInReset packet) {
        super.onPacketInReset(player, packet);
        Node spawn = player.getNodes().get(0);
        spawn.setX(RANDOM.nextInt(GRID_WIDTH) * 50 + 25f);
        spawn.setY(RANDOM.nextInt(GRID_HEIGHT) * 50 + 25f);
        player.setData("block", Block.ERASER);
    }
    
    private static enum Block {
        ERASER(null),
        RED(Color.RED),
        ORANGE(Color.ORANGE),
        YELLOW(Color.YELLOW),
        GREEN(Color.GREEN),
        CYAN(Color.CYAN),
        BLUE(Color.BLUE),
        PURPLE(Color.MAGENTA),
        WHITE(Color.WHITE),
        BLACK(Color.BLACK);
        
        private Color color;
        
        private Block(Color color) {
            this.color = color;
        }
        
        private void apply(Node node) {
            if (color == null) {
                node.destroy(null);
            } else {
                node.setColor(color);
            }
        }
    }
    
}
