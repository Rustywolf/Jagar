package codes.rusty.jagar.nodes;

import codes.rusty.jagar.Core;
import java.awt.Color;

public abstract class Node {
    
    private final int id;
    
    private String nickName = "Unnamed";
    private float x = 0;
    private float y = 0;
    private float velX = 0;//(float) Math.random() * 20f;
    private float velY = 0;//(float) Math.random() * 20f;
    private Color color = new Color(0, 0, 0);
    private float mass = 1;
    
    private float speed = 10f;
    private boolean destroyed = false;
    private int killerId = -1;
    
    protected Node(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public void setColor(int red, int green, int blue) {
        this.color = new Color(red, green, blue);
    }

    public float getSize() {
        return (mass*mass)/100;
    }
    
    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
    
    public int getFlags() {
        return 0b0;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public boolean isDestroyed() {
        return this.destroyed;
    }

    public int getKillerId() {
        return killerId;
    }
    
    public void destroy(Node killer) {
        this.killerId = (killer == null) ? -1 : killer.id;
        this.destroyed = true;
        Core.getServer().getNodeHandler().destroyNode(this);
    }
    
    public void tick() {
        this.x += velX;
        this.y += velY;
    }
}
