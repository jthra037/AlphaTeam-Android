package nosleep.game.framework;

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

    public FTuple (FTuple other)
    {
        this.x = other.x;
        this.y = other.y;
    }

    public FTuple Add(FTuple in)
    {
        return new FTuple(this.x + in.x, this.y + in.y);
    }

    public FTuple Add(float x, float y) { return new FTuple(this.x + x, this.y + y);}

    public FTuple Sub(FTuple in){return new FTuple(this.x - in.x,this.y - in.y);}

    public FTuple Sub(float x, float y){return new FTuple(this.x - x,this.y - y);}

    public FTuple Div(float d){return new FTuple(this.x/d, this.y/d);}

    public FTuple Mul(float m)
    {
        return new FTuple(this.x * m, this.y * m);
    }

    public float LengthS()
    {
        return (this.x * this.x + this.y * this.y);
    }

    public  float Length()
    {
        return (float)Math.sqrt(LengthS());
    }

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
        float magnitude = a.Length();
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
        FTuple diff = this.Sub(target);
        return diff.Length();
    }

    public String ToString(){return ("X:" + this.x + ", Y:" + this.y);}

    public static float Dot(FTuple lhs, FTuple rhs)
    {
        return lhs.x * rhs.x + lhs.y * rhs.y;
    }

    public float Dot(FTuple in)
    {
        return this.x * in.x + this.y * in.y;
    }

    public FTuple ProjectedOnto(FTuple in)
    {
        return in.Mul((Dot(this, in))/(Dot(in, in)));
    }

    public FTuple Normalized()
    {
        float len = Length();
        return new FTuple(this.x/len, this.y/len);
    }

    public ITuple ToITuple()
    {
        return new ITuple((int)x, (int)y);
    }

}
