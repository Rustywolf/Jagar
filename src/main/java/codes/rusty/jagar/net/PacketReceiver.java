package codes.rusty.jagar.net;

import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.net.packets.in.PacketInSpectate;
import codes.rusty.jagar.net.packets.in.PacketInSplit;
import codes.rusty.jagar.players.Player;

public interface PacketReceiver {
    
    public abstract void onPacketInSetNickname(Player player, PacketInSetNickname packet);
    public abstract void onPacketInSpectate(Player player, PacketInSpectate packet);
    public abstract void onPacketInMouseMove(Player player, PacketInMouseMove packet);
    public abstract void onPacketInSplit(Player player, PacketInSplit packet);
    public abstract void onPacketInQPressed(Player player, PacketInQPressed packet);
    public abstract void onPacketInQReleased(Player player, PacketInQReleased packet);
    public abstract void onPacketInEjectMass(Player player, PacketInEjectMass packet);
    public abstract void onPacketInReset(Player player, PacketInReset packet); 
    public abstract void onDisconnect(Player player);
    
}
