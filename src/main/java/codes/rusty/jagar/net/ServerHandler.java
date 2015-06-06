package codes.rusty.jagar.net;

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
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.nodes.PlayerNode;
import codes.rusty.jagar.players.Player;
import codes.rusty.jagar.util.StringUtil;
import com.google.common.io.LittleEndianDataInputStream;
import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ServerHandler extends WebSocketServer implements Handler {
    
    private final HashMap<WebSocket, Player> socketBindings = new HashMap<>();
    
    public ServerHandler() throws UnknownHostException {
        super(new InetSocketAddress(Core.getDefaultPort()));
        //WebSocketImpl.DEBUG = true;
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake handshake) { 
        Core.getLogger().log(Level.INFO, "Connection to {0} created!", ws.getRemoteSocketAddress());
        socketBindings.put(ws, Core.getPlayerHandler().newPlayer(ws));
    }

    @Override
    public void onClose(WebSocket ws, int code, String reason, boolean remote) { 
        Core.getLogger().info("Player Disconnected! " + reason);
        Player disconnected = socketBindings.remove(ws);
        if (disconnected != null) {
            for (PlayerNode node : disconnected.getNodes().toArray(new PlayerNode[0])) {
                disconnected.destroyNode(node);
            }
        }
    }

    @Override
    public void onMessage(WebSocket ws, ByteBuffer message) {
        try {
            byte[] bytes = message.array();
            int packetLength = bytes.length;
            if (packetLength <= 0) {
                return;
            }
            
            LittleEndianDataInputStream data = new LittleEndianDataInputStream(new ByteArrayInputStream(bytes));

            int id = data.readUnsignedByte();
            Serverbound packetType = Serverbound.getPacketType(id);

            if (packetType == null) {
                Core.getLogger().log(Level.WARNING, "Unknown PacketID receieved: {0}! Ignoring...", id);
                return;
            }

            if (packetType.getPacketSize() != -1 && packetLength != packetType.getPacketSize()) {
                Core.getLogger().log(Level.WARNING, "Malformed PacketID #{0} receieved! Expected {1} bytes, receieved {2}! Ignoring...", new Object[] { id, packetType.getPacketSize(), packetLength });
                return;
            }

            Player player = socketBindings.get(ws);
            if (player == null) {
                Core.getLogger().warning("Packets received from socket without a Player binding. Closing connection.");
                ws.close();
                return;
            }

            switch (packetType) {
                case SET_NICKNAME:
                    String name = StringUtil.getUTF16String(data);
                    if (name == null) {
                        Core.getLogger().log(Level.WARNING, "Malformed String given from PacketID #0! Ignoring...");
                        return;
                    }

                    Core.getServer().queuePacket(() -> Core.getGame().onPacketInSetNickname(player, new PacketInSetNickname(name)));
                    break;

                case SPECTATE:
                    Core.getServer().queuePacket(() -> Core.getGame().onPacketInSpectate(player, new PacketInSpectate()));
                    break;

                case MOUSE_MOVE:
                    double x = data.readDouble();
                    double y = data.readDouble();
                    Core.getServer().queuePacket(() -> Core.getGame().onPacketInMouseMove(player, new PacketInMouseMove(x, y)));
                    break;

                case SPLIT:
                    Core.getServer().queuePacket(() -> Core.getGame().onPacketInSplit(player, new PacketInSplit()));
                    break;

                case Q_PRESSED:
                    Core.getServer().queuePacket(() -> Core.getGame().onPacketInQPressed(player, new PacketInQPressed()));
                    break;

                case Q_RELEASED:
                    Core.getServer().queuePacket(() -> Core.getGame().onPacketInQReleased(player, new PacketInQReleased()));
                    break;

                case EJECT_MASS:
                    Core.getServer().queuePacket(() -> Core.getGame().onPacketInEjectMass(player, new PacketInEjectMass()));
                    break;

                case RESET:
                    Core.getServer().queuePacket(() -> Core.getGame().onPacketInReset(player, new PacketInReset()));
                    break;

                default:
                    return;
            }
        } catch (Exception e) {
            Core.getLogger().log(Level.SEVERE, "Error reading packet. Ignoring...");
        }
    }
    
    @Override
    public void onMessage(WebSocket ws, String string) {}
    
    @Override
    public void onError(WebSocket ws, Exception e) { 
        e.printStackTrace();
    }
    
    @Override
    public void destroy() {
        try {
            this.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
