package codes.rusty.jagar.logic;

import codes.rusty.jagar.Core;
import codes.rusty.jagar.Handler;
import codes.rusty.jagar.net.PacketReceiver;
import codes.rusty.jagar.net.packets.in.PacketInEjectMass;
import codes.rusty.jagar.net.packets.in.PacketInMouseMove;
import codes.rusty.jagar.net.packets.in.PacketInQPressed;
import codes.rusty.jagar.net.packets.in.PacketInQReleased;
import codes.rusty.jagar.net.packets.in.PacketInReset;
import codes.rusty.jagar.net.packets.in.PacketInSetNickname;
import codes.rusty.jagar.net.packets.in.PacketInSpectate;
import codes.rusty.jagar.net.packets.in.PacketInSplit;
import codes.rusty.jagar.net.packets.out.PacketOutSetBorder;
import codes.rusty.jagar.nodes.Node;
import codes.rusty.jagar.players.Player;
import com.google.common.collect.Table;
import java.util.HashMap;
import java.util.Set;

public abstract class GameHandler implements Handler, PacketReceiver {
    
    private Border border = Border.DEFAULT;
    private HashMap<Class<? extends Module>, Module> modules = new HashMap<>();
    
    public void enable() {
        this.registerModules();
    }
    
    public void tickGame() {}
    
    public void render(Table<Integer, Integer, Set<Node>> chunks) {}

    public final Border getBorder() {
        return border;
    }
    
    public final void setBorder(Border border) {
        this.border = border;
        Core.getPlayerHandler().sendToAll(new PacketOutSetBorder(border));
    }
    
    protected void registerModules() {}
    
    public final<T extends Module> T getModule(Class<T> cls) {
        if (!modules.containsKey(cls)) {
            return null;
        }
        
        return cls.cast(modules.get(cls));
    }
    
    public final void unregisterModule(Module module) {
        if (modules.containsKey(module.getClass())) {
            Module registered = modules.get(module.getClass());
            if (!module.equals(registered)) {
                throw new IllegalArgumentException("Provided module isnt the registered instance!");
            }
            
            registered.disable();
            modules.remove(module.getClass());
        }
    }
    
    public final void registerModule(Module module) {
        if (modules.containsKey(module.getClass())) {
            throw new IllegalArgumentException("Module has already been registered!");
        }
        
        modules.put(module.getClass(), module);
        module.enable();
    }
    
    @Override
    public void onPacketInSetNickname(Player player, PacketInSetNickname packet) {
        modules.values().forEach(module -> { module.onPacketInSetNickname(player, packet); });
    }

    @Override
    public void onPacketInSpectate(Player player, PacketInSpectate packet) {
        modules.values().forEach(module -> { module.onPacketInSpectate(player, packet); });
    }

    @Override
    public void onPacketInMouseMove(Player player, PacketInMouseMove packet) {
        modules.values().forEach(module -> { module.onPacketInMouseMove(player, packet); });
    }

    @Override
    public void onPacketInSplit(Player player, PacketInSplit packet) {
        modules.values().forEach(module -> { module.onPacketInSplit(player, packet); });
    }

    @Override
    public void onPacketInQPressed(Player player, PacketInQPressed packet) {
        modules.values().forEach(module -> { module.onPacketInQPressed(player, packet); });
    }

    @Override
    public void onPacketInQReleased(Player player, PacketInQReleased packet) {
        modules.values().forEach(module -> { module.onPacketInQReleased(player, packet); });
    }

    @Override
    public void onPacketInEjectMass(Player player, PacketInEjectMass packet) {
        modules.values().forEach(module -> { module.onPacketInEjectMass(player, packet); });
    }

    @Override
    public void onPacketInReset(Player player, PacketInReset packet) {
        modules.values().forEach(module -> { module.onPacketInReset(player, packet); });
    }

    @Override
    public void onDisconnect(Player player) {
        modules.values().forEach(module -> { module.onDisconnect(player); });
    }
    
}
