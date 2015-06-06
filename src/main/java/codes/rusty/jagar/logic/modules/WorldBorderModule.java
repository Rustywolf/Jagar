package codes.rusty.jagar.logic.modules;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.logic.Module;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.out.PacketOutSetBorder;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.players.Player;
import java.awt.Color;

public class WorldBorderModule extends Module {

    private final int width;
    private final int height;
    private final Color color;

    public WorldBorderModule(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public WorldBorderModule(int width, int height) {
        this.width = width;
        this.height = height;
        this.color = Color.BLACK;
    }
    
    @Override
    public void enable() {
        this.setupBorder();
    }
    
    private void setupBorder() {
        for (int x = -1; x <= width; x++) {
            Node above = Core.getNodeHandler().newMassNode();
            above.setMass(22.3f);
            above.setX(x*50 + 25f);
            above.setY(-25f);
            above.setColor(Color.BLACK);
            
            Node below = Core.getNodeHandler().newMassNode();
            below.setMass(22.3f);
            below.setX(x*50 + 25f);
            below.setY(height*50 + 25f);
            below.setColor(Color.BLACK);
        }
        
        for (int y = -1; y <= width; y++) {
            Node above = Core.getNodeHandler().newMassNode();
            above.setMass(22.3f);
            above.setX(-25f);
            above.setY(y*50 + 25f);
            above.setColor(Color.BLACK);
            
            Node below = Core.getNodeHandler().newMassNode();
            below.setMass(22.3f);
            below.setX(width*50 + 25f);
            below.setY(y*50 + 25f);
            below.setColor(Color.BLACK);
        }
    }

    @Override
    public void onPacketInReset(Player player, PacketInReset packet) {
        new PacketOutSetBorder(Core.getGame().getBorder()).write(player.getSocket());
    }
}
