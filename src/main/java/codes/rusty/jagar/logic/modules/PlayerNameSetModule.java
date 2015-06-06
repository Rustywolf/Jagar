package codes.rusty.jagar.logic.modules;

import codes.rusty.jagar.logic.Module;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.players.Player;

public class PlayerNameSetModule extends Module {

    @Override
    public void onPacketInSetNickname(Player player, PacketInSetNickname packet) {
        player.setNickName(packet.getName());
    }
    
}
