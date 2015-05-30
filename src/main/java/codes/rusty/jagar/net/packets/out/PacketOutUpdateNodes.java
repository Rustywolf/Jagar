package codes.rusty.jagar.net.packets.out;

import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.util.StringUtil;
import com.google.common.io.LittleEndianDataOutputStream;
import java.awt.Color;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class PacketOutUpdateNodes extends PacketOut {

    private final Removed[] removedData;
    private final NodeData[] activeData;
    
    public PacketOutUpdateNodes(Collection<Node> active, Map<Integer, Integer> removed) {
        this.activeData = new NodeData[active.size()];
        this.removedData = new Removed[removed.size()];
        
        int index = 0;
        for (Node node : active) {
            activeData[index++] = new NodeData(node.getId(), node.getX(), node.getY(), node.getSize(), node.getColor(), node.getFlags(), node.getNickName());
        }
        
        index = 0;
        for (Entry<Integer, Integer> entry : removed.entrySet()) {
            removedData[index++] = new Removed(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    protected void write(LittleEndianDataOutputStream output) throws IOException {
        output.writeShort(removedData.length & 0xFFFF);
        for (Removed removed : removedData) {
            output.writeInt((int) (((long) removed.killerId) & 0xFFFFFFFF));
            output.writeInt((int) (((long) removed.nodeId) & 0xFFFFFFFF));
        }
        
        for (NodeData active : activeData) {
            output.writeInt((int) (((long) active.nodeId) & 0xFFFFFFFF));
            output.writeFloat(active.x);
            output.writeFloat(active.y);
            output.writeFloat(active.size);
            output.write((byte) (active.color.getRed() & 0xFF));
            output.write((byte) (active.color.getGreen() & 0xFF));
            output.write((byte) (active.color.getBlue() & 0xFF));
            output.write((byte) (active.flags & 0xFF));
            output.write(StringUtil.getUTF16Bytes(active.name));
            output.writeShort(0);
        }
        
        output.writeInt((int) (0));
        output.writeShort((short) (0));
        output.writeInt((int) (((long) activeData.length) & 0xFFFFFFFF));
        for (NodeData active : activeData) {
            output.writeInt((int) (((long) active.nodeId) & 0xFFFFFFFF));
        }
    }
    
    private static class Removed {
        
        int nodeId;
        int killerId;

        public Removed(int nodeId, int killerId) {
            this.nodeId = nodeId;
            this.killerId = killerId;
        }
        
    }
    
    private static class NodeData {
        
        int nodeId;
        float x;
        float y;
        float size;
        Color color;
        int flags;
        String name;

        public NodeData(int nodeId, float x, float y, float size, Color color, int flags, String name) {
            this.nodeId = nodeId;
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.flags = flags;
            this.name = name;
        }
        
    }
    
}
