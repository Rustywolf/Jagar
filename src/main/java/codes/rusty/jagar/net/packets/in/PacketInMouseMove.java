package codes.rusty.jagar.net.packets.in;

import codes.rusty.jagar.net.packets.PacketIn;

public class PacketInMouseMove extends PacketIn {
    
    private final double mouseX;
    private final double mouseY;

    public PacketInMouseMove(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }
    
}
