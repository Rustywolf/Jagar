package codes.rusty.jagar.net.packets.in;

import codes.rusty.jagar.net.packets.PacketIn;

public class PacketInSetNickname extends PacketIn {
    
    private final String name;

    public PacketInSetNickname(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
