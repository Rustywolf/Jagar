package codes.rusty.jagar.logic.modules;

import codes.rusty.jagar.logic.Module;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.players.Player;

public class MouseMoveModule extends Module {

    @Override
    public void onPacketInMouseMove(Player player, PacketInMouseMove packet) {
        player.setMouseX(packet.getMouseX());
        player.setMouseY(packet.getMouseY());
    }
    
}
