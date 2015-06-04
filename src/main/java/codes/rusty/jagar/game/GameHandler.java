package codes.rusty.jagar.game;

import codes.rusty.jagar.Handler;
import codes.rusty.jagar.net.PacketReceiver;
import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.net.packets.in.PacketInSpectate;
import codes.rusty.jagar.net.packets.in.PacketInSplit;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.players.Player;
import com.google.common.collect.Table;
import java.util.Set;

public abstract class GameHandler implements Handler {
    
    public abstract Mechanics getMechanics();
    
    public abstract void tickGame();
    
    public abstract void render(Table<Integer, Integer, Set<Node>> chunks);

    public boolean onPacketInSetNickname(Player player, PacketInSetNickname packet) {
        return true;
    }

    public boolean onPacketInSpectate(Player player, PacketInSpectate packet) {
        return true;
    }

    public boolean onPacketInMouseMove(Player player, PacketInMouseMove packet) {
        return true;
    }

    public boolean onPacketInSplit(Player player, PacketInSplit packet) {
        return true;
    }

    public boolean onPacketInQPressed(Player player, PacketInQPressed packet) {
        return true;
    }

    public boolean onPacketInQReleased(Player player, PacketInQReleased packet) {
        return true;
    }

    public boolean onPacketInEjectMass(Player player, PacketInEjectMass packet) {
        return true;
    }

    public boolean onPacketInReset(Player player, PacketInReset packet) {
        return true;
    }
    
}
