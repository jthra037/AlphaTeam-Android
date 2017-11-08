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

    public FTuple Sub(FTuple in){return new FTuple(this.x - in.x,this.y - in.y);}

    public FTuple Div(float d){return new FTuple(this.x/d, this.y/d);}

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

    public float magnitude(){return (float)Math.sqrt(this.x*this.x + this.y*this.y);}

    public float SqrMagnitude(){return this.x * this.x + this.y * this.y;}

    public static FTuple Lerp(FTuple a, FTuple b, float t)
    {
        t = IMath.clamp01(t);
        return new FTuple(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t);
    }

    public static FTuple LerpUnclamped(FTuple a, FTuple b, float t)
    {
        return new FTuple(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t);
    }

    public static FTuple MoveTowards(FTuple current, FTuple target, float maxDistanceDelta)
    {
        FTuple a = target.Sub(current);
        float magnitude = a.magnitude();
        FTuple result;
        if (magnitude <= maxDistanceDelta || magnitude == 0f)
        {
            result = target;
        }
        else
        {
            result = current.Add((a.Div(magnitude)).Mul(maxDistanceDelta));
        }
        return result;
    }


    public static FTuple Scale(FTuple a, FTuple b)
    {
        return new FTuple(a.x * b.x, a.y * b.y);
    }

    public void Scale(FTuple scale)
    {
        this.x *= scale.x;
        this.y *= scale.y;
    }

    public float Distance(FTuple target)
    {
        return this.Sub(target).magnitude();
    }

    public String ToString(){return ("X:" + this.x + ", Y:" + this.y);}




    public FTuple Normalized()
    {
        float len = Length();
        return new FTuple(this.x/len, this.y/len);
    }
}
