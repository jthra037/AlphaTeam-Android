package nosleep.game.framework;

import nosleep.neonrush.Ball;

/**
 * Created by John on 2017-10-12.
 */

public class CircleCollider extends Collider {
    private int radius;
    private ITuple offset = new ITuple(0,0);
    private Object object;

    //public final ColliderFormat format = circle;

    public CircleCollider(int radius, Object object)
    {
        this.radius = radius;
        format = ColliderFormat.circle;
        this.object = object;
    }

    public CircleCollider(int radius, ITuple offset)
    {
        this.radius = radius;
        this.offset = offset;
        format = ColliderFormat.circle;
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

        switch (otherCollider.format) {
            case circle:
                return circleCircleCollision(other, (CircleCollider) otherCollider, pos);
            case rect:
                return rectCircleCollision(other, (BoxCollider) otherCollider, pos.ToITuple()); // hard cast fuckit
        }

        if (otherCollider instanceof CircleCollider)
        {
            CircleCollider otherCircle = (CircleCollider) otherCollider;
            return circleCircleCollision(other, otherCircle, pos);
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

    private boolean rectCircleCollision(Object other, BoxCollider otherCollider, ITuple pos)
    {
        Ball thisBall = (Ball) object;

        if (thisBall == null)
            return false;

        float left = other.getPosition().x - radius - thisBall.getVelocity().x;
        float right = other.getPosition().x + otherCollider.getSize().x + radius + thisBall.getVelocity().x;
        float top = other.getPosition().y - radius - thisBall.getVelocity().y;
        float bottom = other.getPosition().y + otherCollider.getSize().y + radius + thisBall.getVelocity().y;

        return left <= pos.x &&
                pos.x <= right&&
                top <= pos.y &&
                pos.y <= bottom - getRadius();
    }

    public ITuple getOffset() {
        return offset;
    }

    public void setOffset(ITuple offset) {
        this.offset = offset;
    }
}
