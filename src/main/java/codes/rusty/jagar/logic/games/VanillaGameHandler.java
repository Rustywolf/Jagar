package codes.rusty.jagar.logic.games;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.logic.GameHandler;
import codes.rusty.jagar.logic.GameHandler;
import codes.rusty.jagar.net.packets.PacketOut;
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
import codes.rusty.jagar.nodes.PlayerNode;
import codes.rusty.jagar.players.Player;
import com.google.common.collect.Table;
import java.util.Set;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class VanillaGameHandler extends GameHandler {

    @Override
    public void tickGame() {
        this.handleCollisions();
    }
    
    protected void handleCollisions() {
        
    }

    protected void handlePlayerMovement() {
        Core.getPlayerHandler().getPlayers().forEach(player -> {
            Vector2D mouse = new Vector2D(player.getMouseX(), player.getMouseY());
            for (PlayerNode node : player.getNodes()) {
                Vector2D origin = new Vector2D(node.getX(), node.getY());
                Vector2D normalize = mouse.subtract(origin).normalize();
                Vector2D movement = normalize.scalarMultiply(node.getSpeed());

                node.setX((float) (node.getX() + movement.getX()));
                node.setY((float) (node.getY() + movement.getY()));
            }
        });
    }
    
    @Override
    public void render(Table<Integer, Integer, Set<Node>> chunks) {
        
    }

    @Override
    public void destroy() {
        
    }
    
    @Override
    public void onPacketInReset(Player player, PacketInReset packet) {
        if (player.getNodes().isEmpty()) {
            PlayerNode node = Core.getNodeHandler().newPlayerNode(player);
            node.setMass(100);
            node.setX(100.0f);
            node.setY(100.0f);
        }

        PacketOut packetOut = new PacketOutSetBorder(Core.getGame().getBorder());
        packetOut.write(player.getSocket());
    }

    @Override
    public void onPacketInEjectMass(Player player, PacketInEjectMass packet) {
        
    }

    @Override
    public void onPacketInQReleased(Player player, PacketInQReleased packet) {
            
    }

    @Override
    public void onPacketInQPressed(Player player, PacketInQPressed packet) {
        
    }

    @Override
    public void onPacketInSplit(Player player, PacketInSplit packet) {
        
    }

    @Override
    public void onPacketInMouseMove(Player player, PacketInMouseMove packet) {
        player.setMouseX(packet.getMouseX());
        player.setMouseY(packet.getMouseY());
    }

    @Override
    public void onPacketInSpectate(Player player, PacketInSpectate packet) {
        player.setSpectating(true);
    }

    @Override
    public void onPacketInSetNickname(Player player, PacketInSetNickname packet) {
        player.setNickName(packet.getName());
        player.setSpectating(false); // I assume this is how it works?
    }
    
    
}
