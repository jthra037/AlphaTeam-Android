package alpha.nosleep.game.framework;

/**
 * Created by John on 2017-10-12.
 */

public interface ICollider
{
    public boolean OnOverlap();
    public Hit OnCollision();
}
