package codes.rusty.jagar.net;

import codes.rusty.jagar.net.packets.PacketIn;
import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.net.packets.in.PacketInSpectate;
import codes.rusty.jagar.net.packets.in.PacketInSplit;

public interface PacketReceiver {
    
    public abstract void onPacketInSetNickname(PacketInSetNickname packet);
    public abstract void onPacketInSpectate(PacketInSpectate packet);
    public abstract void onPacketInMouseMove(PacketInMouseMove packet);
    public abstract void onPacketInSplit(PacketInSplit packet);
    public abstract void onPacketInQPressed(PacketInQPressed packet);
    public abstract void onPacketInQReleased(PacketInQReleased packet);
    public abstract void onPacketInEjectMass(PacketInEjectMass packet);
    public abstract void onPacketInReset(PacketInReset packet); 
    
}
