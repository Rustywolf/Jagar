package codes.rusty.jagar.net.packets.out;

import codes.rusty.jagar.net.packets.PacketOut;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public class PacketOutAddNode extends PacketOut {

    private final int nodeId;
    
    public PacketOutAddNode(int nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    protected void write(LittleEndianDataOutputStream output) throws IOException {
        output.writeInt(nodeId);
    }
    
}
