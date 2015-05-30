package codes.rusty.jagar.nodes;

import codes.rusty.jagar.players.Player;

public class PlayerNode extends Node {
    
    private final Player owner;
    
    protected PlayerNode(int id, Player player) {
        super(id);
        this.owner = player;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public void destroy(Node killer) {
        super.destroy(killer);
        owner.destroyNode(this);
    }
}
