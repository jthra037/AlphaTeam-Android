package alpha.nosleep.game.framework;

import java.util.Vector;

/**
 * Created by John on 2017-10-12.
 */

public class Hit
{
    private boolean hitOccurred;
    private ITuple hitLocation;
    private Runtime runtime;

    public Hit(boolean hitOccurred, ITuple hitLocation)
    {
        this.hitLocation = hitLocation;
        this.hitOccurred = hitOccurred;
        runtime = Runtime.getRuntime();
    }

    public Hit(boolean hitOccurred, int x, int y)
    {
        this.hitLocation = new ITuple(x, y);
        this.hitOccurred = hitOccurred;
        runtime = Runtime.getRuntime();
    }

    public boolean isHitOccurred() {
        return hitOccurred;
    }

    public ITuple getHitLocation() {
        return hitLocation;
    }

    public Runtime getRuntime() {
        return runtime;
    }
}
