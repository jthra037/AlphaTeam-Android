package alpha.nosleep.game.framework;

/**
 * Created by John on 2017-10-11.
 */

public class ITuple {
    public int x;
    public int y;

    public ITuple(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public ITuple Add(ITuple in)
    {
        return new ITuple(this.x + in.x, this.y + in.y);
    }

    public ITuple Add(int x, int y)
    {
        return new ITuple(this.x + x, this.y + y);
    }

    public ITuple Mul(int x)
    {
        return new ITuple(this.x * x, this.y * x);
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
        if (len == 0)
        {
            return new FTuple(0.0f, 0.0f);
        }
        return new FTuple(this.x/len, this.y/len);
    }
}
