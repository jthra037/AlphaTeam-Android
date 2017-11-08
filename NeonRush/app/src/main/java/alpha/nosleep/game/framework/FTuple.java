package alpha.nosleep.game.framework;

/**
 * Created by Mark- on 17-Oct-17.
 */

public class FTuple
{
    public float x;
    public float y;

    public FTuple(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public FTuple Add(FTuple in)
    {
        return new FTuple(this.x + in.x, this.y + in.y);
    }

    public FTuple Add(float x, float y)
    {
        return new FTuple(this.x + x, this.y + y);
    }

    public FTuple Mul(float x)
    {
        return new FTuple(this.x * x, this.y * x);
    }

    public float LengthS()
    {
        return (this.x * this.x + this.y * this.y);
    }

    public float Length()
    {
        return (float)Math.sqrt(LengthS());
    }

    public FTuple Normalized()
    {
        float len = Length();
        return new FTuple(this.x/len, this.y/len);
    }

    public float Dot (FTuple other)
    {
        return x * other.x + y * other.y;
    }
}
