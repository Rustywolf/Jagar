package codes.rusty.jagar.net;

import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.net.packets.out.PacketOutAddNode;
import codes.rusty.jagar.net.packets.out.PacketOutClearNodes;
import codes.rusty.jagar.net.packets.out.PacketOutSetBorder;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateClient;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateLeaderboardFFA;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateLeaderboardTeams;
import codes.rusty.jagar.net.packets.out.PacketOutUpdateNodes;
import java.util.HashMap;

public enum Clientbound {
    UPDATE_NODES(16, PacketOutUpdateNodes.class),
    UPDATE_CLIENT(17, PacketOutUpdateClient.class),
    CLEAR_NODES(20, PacketOutClearNodes.class),
    ADD_NODE(32, PacketOutAddNode.class),
    UPDATE_FFA_LEADERBOARD(49, PacketOutUpdateLeaderboardFFA.class),
    UPDATE_TEAM_LEADERBOARD(50, PacketOutUpdateLeaderboardTeams.class),
    SET_BORDER(64, PacketOutSetBorder.class);
        
    private static final HashMap<Integer, Clientbound> byId = new HashMap<>();
    private static final HashMap<Class<? extends PacketOut>, Clientbound> byClass = new HashMap<>();
    static {
        for (Clientbound packet : Clientbound.values()) {
            byId.put(packet.packetId, packet);
            byClass.put(packet.packetClass, packet);
        }
    }
    
    private final int packetId;
    private final Class<? extends PacketOut> packetClass;
    
    private Clientbound(int packetId, Class<? extends PacketOut> packetClass) {
        this.packetId = packetId;
        this.packetClass = packetClass;
    }

    public int getPacketId() {
        return packetId;
    }
    
    public static Clientbound getPacketType(int id) {
        return byId.get(id);
    }
    
    public static int getPacketSize(int id) {
        return byId.get(id).packetId;
    }
    
    public static Clientbound getByClass(Class<? extends PacketOut> packetClass) {
        return byClass.get(packetClass);
    }
}
