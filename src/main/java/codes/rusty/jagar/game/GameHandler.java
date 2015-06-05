package codes.rusty.jagar.game;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.Handler;
import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.net.packets.in.PacketInSpectate;
import codes.rusty.jagar.net.packets.in.PacketInSplit;
import codes.rusty.jagar.net.packets.out.PacketOutSetBorder;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.players.Player;
import com.google.common.collect.Table;
import java.util.Set;

public abstract class GameHandler implements Handler {
    
    private Border border = Border.DEFAULT;
    
    public void enable() {
        
    }
    
    public abstract void tickGame();
    
    public abstract void render(Table<Integer, Integer, Set<Node>> chunks);

    public Border getBorder() {
        return border;
    }
    
    public void setBorder(Border border) {
        this.border = border;
        Core.getPlayerHandler().sendToAll(new PacketOutSetBorder(border));
    }
    
    public void onPacketInSetNickname(Player player, PacketInSetNickname packet) {}

    public void onPacketInSpectate(Player player, PacketInSpectate packet) {}

    public void onPacketInMouseMove(Player player, PacketInMouseMove packet) {}

    public void onPacketInSplit(Player player, PacketInSplit packet) {}

    public void onPacketInQPressed(Player player, PacketInQPressed packet) {}

    public void onPacketInQReleased(Player player, PacketInQReleased packet) {}

    public void onPacketInEjectMass(Player player, PacketInEjectMass packet) {}

    public void onPacketInReset(Player player, PacketInReset packet) {}
    
    public void onNodeSpawned(Node node) {}
    
}
