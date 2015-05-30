package codes.rusty.jagar.net.packets.out;

import codes.rusty.jagar.net.packets.PacketOut;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.util.StringUtil;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public class PacketOutUpdateLeaderboardFFA extends PacketOut {
    
    private final NodeEntry[] entries;

    public PacketOutUpdateLeaderboardFFA(Node... nodes) {
        entries = new NodeEntry[nodes.length];
        int index = 0;
        for (Node node : nodes) {
            entries[index++] = new NodeEntry(node.getId(), node.getNickName());
        }
    }
    
    public PacketOutUpdateLeaderboardFFA(NodeEntry... entries) {
        this.entries = entries;
    }

    @Override
    protected void write(LittleEndianDataOutputStream output) throws IOException {
        output.writeInt((int) (((long) entries.length) & 0xFFFFFFFF));
        for (NodeEntry entry : entries) {
            output.writeInt((int) (((long) entry.getId()) & 0xFFFFFFFF));
            output.write(StringUtil.getUTF16Bytes(entry.getName()));
            output.writeShort(0);
        }
    }
    
    public static class NodeEntry {
        
        protected final int id;
        protected final String name;

        public NodeEntry(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
        
    }
    
}
