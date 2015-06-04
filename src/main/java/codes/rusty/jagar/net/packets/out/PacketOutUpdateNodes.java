package codes.rusty.jagar.net.packets.out;

import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.util.StringUtil;
import com.google.common.io.LittleEndianDataOutputStream;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PacketOutUpdateNodes extends PacketOut {

    private final Removed[] consumedData;
    private final Removed[] removedData;
    private final NodeData[] activeData;
    
    public PacketOutUpdateNodes(Collection<Node> active, Map<Integer, Integer> removed) {
        this.activeData = new NodeData[active.size()];
        
        int index = 0;
        for (Node node : active) {
            activeData[index++] = new NodeData(node.getId(), node.getX(), node.getY(), node.getMass(), node.getColor(), node.getFlags(), node.getNickName());
        }
        
        List<Removed> consumedNodes = new ArrayList<>();
        List<Removed> removedNodes = new ArrayList<>();
        for (Entry<Integer, Integer> entry : removed.entrySet()) {
            if (entry.getValue() != -1) {
                consumedNodes.add(new Removed(entry.getKey(), entry.getValue()));
            } else {
                removedNodes.add(new Removed(entry.getKey(), entry.getValue()));
            }
        }
        
        this.consumedData = consumedNodes.toArray(new Removed[consumedNodes.size()]);
        this.removedData = removedNodes.toArray(new Removed[removedNodes.size()]);
    }
    
    @Override
    protected void write(LittleEndianDataOutputStream output) throws IOException {
        output.writeShort(consumedData.length & 0xFFFF);
        for (Removed consumed : consumedData) {
            output.writeInt((int) (((long) consumed.killerId) & 0xFFFFFFFF));
            output.writeInt((int) (((long) consumed.nodeId) & 0xFFFFFFFF));
        }
        
        for (NodeData active : activeData) {
            output.writeInt((int) (((long) active.nodeId) & 0xFFFFFFFF));
            output.writeShort((short) active.x);
            output.writeShort((short) active.y);
            output.writeShort((short) active.size);
            output.write((byte) (active.color.getRed() & 0xFF));
            output.write((byte) (active.color.getGreen() & 0xFF));
            output.write((byte) (active.color.getBlue() & 0xFF));
            output.write((byte) (active.flags & 0xFF));
            output.write(StringUtil.getUTF16Bytes(active.name));
            output.writeShort(0);
        }
        
        output.writeInt((int) (0));
        output.writeInt((int) (((long) removedData.length) & 0xFFFFFFFF));
        for (Removed removed : removedData) {
            output.writeInt((int) (((long) removed.nodeId) & 0xFFFFFFFF));
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
