package codes.rusty.jagar.game;

import codes.rusty.jagar.players.Player;

public abstract class Mechanics {
    
    public abstract void onSplit(Player player);
    public abstract void onEjectMass(Player player);
    public abstract void onTick(Player player);
}
