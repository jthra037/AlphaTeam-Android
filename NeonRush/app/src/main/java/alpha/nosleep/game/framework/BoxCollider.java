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
        return false;
    }

    @Override
    public Hit OnCollision(Object other, ITuple pos) {
        return null;
    }

    private boolean rectCircleCollision(Object other, CircleCollider otherCollider, ITuple pos)
    {
        float left = pos.x - size.x/2;
        float right = pos.x + size.x/2;
        float top = pos.y - size.y/2;
        float bottom = pos.y + size.y/2;

        return left <= other.position.x + otherCollider.getRadius() &&
                other.position.x <= right - otherCollider.getRadius()&&
                top <= other.position.y + otherCollider.getRadius()&&
                other.position.y <= bottom - otherCollider.getRadius();
    }
}
