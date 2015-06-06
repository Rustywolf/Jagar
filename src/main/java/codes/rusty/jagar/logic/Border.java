package codes.rusty.jagar.logic;

public final class Border {

    public static Border DEFAULT = new Border(0, 0, 10000, 10000);
    
    private final float minX;
    private final float minY;
    private final float maxX;
    private final float maxY;

    public Border(float minX, float minY, float maxX, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxZ;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }
    
}
