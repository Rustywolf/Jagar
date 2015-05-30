package codes.rusty.jagar.net.packets;

import codes.rusty.jagar.net.Clientbound;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.java_websocket.WebSocket;

public abstract class PacketOut extends Packet {
    
    public void write(WebSocket socket) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        LittleEndianDataOutputStream output = new LittleEndianDataOutputStream(bytes);
        try {
            int packetId = Clientbound.getByClass(this.getClass()).getPacketId() & 0xFF;
            output.write(packetId);
            write(output);
            byte[] byteArray = bytes.toByteArray();
            socket.send(byteArray);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected abstract void write(LittleEndianDataOutputStream output) throws IOException;
    
}
