package codes.rusty.jagar.net.packets.out;

import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.nodes.Node;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public class PacketOutUpdateClient extends PacketOut {

    private final Node node;
    
    public PacketOutUpdateClient(Node node) {
        this.node = node;
    }
    
    @Override
    protected void write(LittleEndianDataOutputStream output) throws IOException {
        output.writeFloat(node.getX());
        output.writeFloat(node.getY());
        output.writeFloat(node.getSize());
    }
    
    
}
