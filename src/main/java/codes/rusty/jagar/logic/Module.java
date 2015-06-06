package codes.rusty.jagar.logic;

import codes.rusty.jagar.net.PacketReceiver;
import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.net.packets.in.PacketInSpectate;
import codes.rusty.jagar.net.packets.in.PacketInSplit;
import codes.rusty.jagar.players.Player;

public abstract class Module implements PacketReceiver {
    
    public void enable() {}
    
    public void disable() {}
    
    @Override
    public void onPacketInSetNickname(Player player, PacketInSetNickname packet) {}

    @Override
    public void onPacketInSpectate(Player player, PacketInSpectate packet) {}

    @Override
    public void onPacketInMouseMove(Player player, PacketInMouseMove packet) {}

    @Override
    public void onPacketInSplit(Player player, PacketInSplit packet) {}

    @Override
    public void onPacketInQPressed(Player player, PacketInQPressed packet) {}

    @Override
    public void onPacketInQReleased(Player player, PacketInQReleased packet) {}

    @Override
    public void onPacketInEjectMass(Player player, PacketInEjectMass packet) {}

    @Override
    public void onPacketInReset(Player player, PacketInReset packet) {}

    @Override
    public void onDisconnect(Player player) {}
}
