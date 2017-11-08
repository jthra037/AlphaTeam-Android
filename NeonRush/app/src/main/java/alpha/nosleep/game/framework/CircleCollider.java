package alpha.nosleep.game.framework;

import static alpha.nosleep.game.framework.Collider.ColliderFormat.circle;

/**
 * Created by John on 2017-10-12.
 */

public class CircleCollider extends Collider {
    private int radius = 5;
    private ITuple offset = new ITuple(0,0);

    //public final ColliderFormat format = circle;

    public CircleCollider(int radius)
    {
        this.radius = radius;
        format = circle;
    }

    public CircleCollider(int radius, ITuple offset)
    {
        this.radius = radius;
        this.offset = offset;
        format = circle;
    }

    @Override
    public boolean OnOverlap(Object other, ITuple pos) {
        Collider otherCollider = other.getCollider();

        switch (otherCollider.format) {
            case circle:
                return circleCircleCollision(other, (CircleCollider) otherCollider, pos);
        }

        return false;
    }

    @Override
    public boolean OnOverlap(Object other, FTuple pos) {
        Collider otherCollider = other.getCollider();

        /*switch (otherCollider.format) {
            case circle:
                return circleCircleCollision(other, (CircleCollider) otherCollider, pos);
        }*/

        try{
            CircleCollider otherCircle = (CircleCollider)otherCollider;
            return circleCircleCollision(other, otherCircle, pos);
        }catch (Exception e)
        {
            System.out.println(e);
        }

        return false;
    }

    @Override
    public Hit OnCollision(Object other, ITuple pos) {
        return null;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    private boolean circleCircleCollision(Object other, CircleCollider otherCollider, ITuple pos)
    {
        float ds = ((other.position.x - pos.x) * (other.position.x - pos.x)) +
                ((other.position.y - pos.y) * (other.position.y - pos.y));
        float rs = (otherCollider.getRadius() * otherCollider.getRadius()) + (radius * radius);

        return ds < rs;
    }

    private boolean circleCircleCollision(Object other, CircleCollider otherCollider, FTuple pos)
    {
        float ds = ((other.position.x - pos.x) * (other.position.x - pos.x)) +
                ((other.position.y - pos.y) * (other.position.y - pos.y));
        float rs = (otherCollider.getRadius() * otherCollider.getRadius()) + (radius * radius);

        return ds < rs;
    }

    public ITuple getOffset() {
        return offset;
    }

    public void setOffset(ITuple offset) {
        this.offset = offset;
    }
}
