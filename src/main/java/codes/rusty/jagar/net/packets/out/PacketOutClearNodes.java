package codes.rusty.jagar.net.packets.out;

import codes.rusty.jagar.net.packets.PacketOut;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public class PacketOutClearNodes extends PacketOut {

    public PacketOutClearNodes() {}

    @Override
    protected void write(LittleEndianDataOutputStream output) throws IOException {}
    
}
