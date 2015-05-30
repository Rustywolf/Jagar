package codes.rusty.jagar.net.packets.out;

import codes.rusty.jagar.net.packets.PacketOut;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public class PacketOutUpdateLeaderboardTeams extends PacketOut {
    
    private final float[] entries;

    public PacketOutUpdateLeaderboardTeams(float... entries) {
        this.entries = entries;
    }

    @Override
    protected void write(LittleEndianDataOutputStream output) throws IOException {
        output.writeInt((int) (((long) entries.length) & 0xFFFFFFFF));
        for (float entry : entries) {
            output.writeFloat(entry);
        }
    }
    
}
