package codes.rusty.jagar.net.packets.out;

import codes.rusty.jagar.logic.Border;
import codes.rusty.jagar.net.packets.PacketOut;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public class PacketOutSetBorder extends PacketOut {
    
    private final Border border;

    public PacketOutSetBorder(Border border) {
        this.border = border;
    }

    @Override
    protected void write(LittleEndianDataOutputStream output) throws IOException {
        output.writeDouble(border.getMinX());
        output.writeDouble(border.getMinY());
        output.writeDouble(border.getMaxX());
        output.writeDouble(border.getMaxY());
    }
    
}
