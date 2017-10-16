package alpha.nosleep.game.framework;

/**
 * Created by John on 2017-10-12.
 */

public class CircleCollider implements ICollider {
    private int radius;

    public CircleCollider(int radius)
    {
        this.radius = radius;
    }

    @Override
    public boolean OnOverlap() {
        return false;
    }

    @Override
    public Hit OnCollision() {
        return null;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
