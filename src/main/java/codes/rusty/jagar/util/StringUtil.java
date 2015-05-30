package codes.rusty.jagar.util;

import com.google.common.io.LittleEndianDataInputStream;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static byte[] getUTF16Bytes(String string) {
        try {
            byte[] bytes = string.getBytes("UTF-16");
            byte[] flipped = new byte[bytes.length];
            int delta = 1;
            for (int i = 0; i < bytes.length; i++) {
                int newIndex = i + delta;
                if (newIndex >= bytes.length) {
                    newIndex = i;
                }
                flipped[newIndex] = bytes[i];
                delta *= -1;
            }
            
            return flipped;
        } catch (Exception e) {
            throw new RuntimeException("Unknown Charset!");
        }
    }

    public static String getUTF16String(LittleEndianDataInputStream input) {
        List<Byte> bytes = new ArrayList<>();
        try {
            int ushort = 0;
            while (input.available() >= 2 && (ushort = input.readUnsignedShort()) != 0) {
                bytes.add((byte) ((ushort >> 8) & 0xFF));
                bytes.add((byte) (ushort & 0xFF));
            }
        } catch (Exception e) {

        }

        try {
            return new String(unbox(bytes), "UTF-16");
        } catch (Exception e) {
            throw new RuntimeException("Unknown charset!");
        }
    }

    private static byte[] unbox(List<Byte> list) {
        byte[] bytes = new byte[list.size()];
        int i = 0;
        for (Byte b : list) {
            bytes[i++] = b;
        }

        return bytes;
    }

}
