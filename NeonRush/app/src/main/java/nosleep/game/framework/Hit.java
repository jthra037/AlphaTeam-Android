package nosleep.game.framework;

/**
 * Created by John on 2017-10-12.
 */

public class Hit
{
    private boolean hitOccurred;
    private int framesUntilHit;
    private Runtime runtime;
    private ITuple screenSpaceLocation;
    private FTuple worldSpaceLocation;
    private FTuple normal;
    private FTuple tangent;


    public Hit(boolean hitOccurred, ITuple hitLocation)
    {
        this.hitOccurred = hitOccurred;
        runtime = Runtime.getRuntime();
    }

    public Hit(boolean hitOccurred, int x, int y)
    {
        this.hitOccurred = hitOccurred;
        runtime = Runtime.getRuntime();
    }

    public boolean isHitOccurred() {
        return hitOccurred;
    }

    public Runtime getRuntime() {
        return runtime;
    }
}
