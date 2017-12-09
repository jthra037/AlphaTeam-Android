package nosleep.game.framework;

/**
 * Created by John on 2017-10-12.
 */

public class Hit
{
    private boolean hitOccurred;
    private int framesUntilHit;
    public float tStep;
    private Runtime runtime;
    private ITuple screenSpaceLocation;
    public FTuple worldSpaceLocation;
    private FTuple normal;
    private FTuple tangent;
    private Object collidedWith;


    public Hit()
    {
        hitOccurred = false;
        worldSpaceLocation = new FTuple(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        normal = new FTuple(0, 1);
        tangent = new FTuple(1, 0);
        tStep = Float.POSITIVE_INFINITY;

        runtime = Runtime.getRuntime();
        framesUntilHit = (int)tStep;
    }

    public Hit(boolean hitOccurred, ITuple hitLocation)
    {
        this.hitOccurred = hitOccurred;
        runtime = Runtime.getRuntime();
    }

    public Hit(boolean hitOccurred, FTuple worldSpaceLocation, FTuple normal, FTuple tangent, float tStep)
    {
        this.hitOccurred = hitOccurred;
        this.worldSpaceLocation = worldSpaceLocation;
        this.normal = normal;
        this.tangent = tangent;
        this.tStep = tStep;

        runtime = Runtime.getRuntime();
        framesUntilHit = (int)tStep;
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
