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
import java.util.HashMap;

public enum Serverbound {
    
    SET_NICKNAME(0, -1, PacketInSetNickname.class),
    SPECTATE(1, 1, PacketInSpectate.class),
    MOUSE_MOVE(16, 21, PacketInMouseMove.class),
    SPLIT(17, 1, PacketInSplit.class),
    Q_PRESSED(18, 1, PacketInQPressed.class),
    Q_RELEASED(19, 1, PacketInQReleased.class),
    EJECT_MASS(21, 1, PacketInEjectMass.class),
    RESET(0xFF, 5, PacketInReset.class);
    
    private static final HashMap<Integer, Serverbound> byId = new HashMap<>();
    private static final HashMap<Class<? extends PacketIn>, Serverbound> byClass = new HashMap<>();
    static {
        for (Serverbound packet : Serverbound.values()) {
            byId.put(packet.packetId, packet);
            byClass.put(packet.packetClass, packet);
        }
    }
    
    private final int packetId;
    private final int packetSize;
    private final Class<? extends PacketIn> packetClass;
    
    private Serverbound(int packetId, int packetSize, Class<? extends PacketIn> packetClass) {
        this.packetId = packetId;
        this.packetSize = packetSize;
        this.packetClass = packetClass;
    }

    public int getPacketId() {
        return packetId;
    }

    public int getPacketSize() {
        return packetSize;
    }
    
    public static Serverbound getPacketType(int id) {
        return byId.get(id);
    }
    
    public static int getPacketSize(int id) {
        return byId.get(id).packetId;
    }
    
    public static Serverbound getByClass(Class<? extends PacketIn> packetClass) {
        return byClass.get(packetClass);
    }
}
