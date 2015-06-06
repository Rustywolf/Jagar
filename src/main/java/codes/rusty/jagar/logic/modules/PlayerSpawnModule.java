package codes.rusty.jagar.logic.modules;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.logic.Module;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.nodes.PlayerNode;
import codes.rusty.jagar.players.Player;

public class PlayerSpawnModule extends Module {

    private final float defaultSize;

    public PlayerSpawnModule(float defaultSize) {
        this.defaultSize = defaultSize;
    }
    
    @Override
    public void onPacketInReset(Player player, PacketInReset packet) {
        if (player.getNodes().isEmpty()) {
            PlayerNode node = Core.getNodeHandler().newPlayerNode(player);
            node.setMass(defaultSize);
            node.setX(0f);
            node.setY(0f);
        }
    }
    
}
