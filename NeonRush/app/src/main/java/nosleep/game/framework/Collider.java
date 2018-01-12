package nosleep.game.framework;

/**
 * Created by John on 2017-10-12.
 */

public abstract class Collider
{
    public static enum ColliderFormat
    {
        circle, rect, lines;
    }
    
    public ColliderFormat format = null;

    public abstract boolean OnOverlap(Object other, ITuple pos);
    public abstract boolean OnOverlap(Object other, FTuple pos);
    public abstract Hit OnCollision(Object other, ITuple pos);
}
