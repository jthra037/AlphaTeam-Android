package alpha.nosleep.game.framework;

import static alpha.nosleep.game.framework.Collider.ColliderFormat.rect;

/**
 * Created by John on 2017-10-12.
 */

public class BoxCollider extends Collider {
    private FTuple size;

    public BoxCollider(FTuple size)
    {
        this.size = size;
        format = rect;
    }

    public BoxCollider(float x, float y)
    {
        this.size = new FTuple(x, y);
        format = rect;
    }

    public FTuple getSize() {
        return size;
    }

    public void setSize(FTuple size) {
        this.size = size;
    }

    @Override
    public boolean OnOverlap(Object other, ITuple pos) {
        Collider otherCollider = other.getCollider();

        switch (otherCollider.format) {
            case circle:
                return rectCircleCollision(other, (CircleCollider) otherCollider, pos);
        }

        return false;
    }

    @Override
    public boolean OnOverlap(Object other, FTuple pos) {
        Collider otherCollider = other.getCollider();

        switch (otherCollider.format) {
            case circle:
                return rectCircleCollision(other, (CircleCollider) otherCollider, pos.ToITuple()); // hard cast fuckit
        }

        return false;
    }

    @Override
    public Hit OnCollision(Object other, ITuple pos) {
        return null;
    }

    private boolean rectCircleCollision(Object other, CircleCollider otherCollider, ITuple pos)
    {
        float left = pos.x - otherCollider.getRadius();
        float right = pos.x + size.x  + otherCollider.getRadius();
        float top = pos.y - otherCollider.getRadius();
        float bottom = pos.y + size.y + otherCollider.getRadius();

        return left <= other.position.x &&
                other.position.x <= right &&
                top <= other.position.y &&
                other.position.y <= bottom;
    }
}
