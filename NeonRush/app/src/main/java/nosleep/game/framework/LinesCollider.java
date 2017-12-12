package nosleep.game.framework;

import java.util.ArrayList;
import java.util.List;

import nosleep.neonrush.Ball;

/**
 * Created by John on 12/9/2017.
 */

public class LinesCollider extends Collider {

    private Object parent;
    private FTuple[] points;
    private List<Line> lines = new ArrayList<Line>();

    private LinesCollider(FTuple[] points, Object parent)
    {
        this.points = points;
        this.parent = parent;
        this.format = ColliderFormat.lines;

        for (int i = 0; i < points.length; i++)
        {
            lines.add(new Line(points[i], points[(i + 1) % points.length], false));
        }
    }


    @Override
    public boolean OnOverlap(Object other, ITuple pos) {
        return false;
    }

    @Override
    public boolean OnOverlap(Object other, FTuple pos) {
        return false;
    }

    @Override
    public Hit OnCollision(Object other, ITuple pos) {
        return null;
    }

    public Hit CheckCollision(Object other, FTuple pos)
    {
        if (!(other instanceof Ball))
        {
            return new Hit();
        }

        FTuple relVelocity = ((Ball) other).getVelocity();

        return LineOverlap(new Line(pos, relVelocity));
    }

    public Hit LineOverlap(Line other)
    {
        Hit output = new Hit();

        for (Line line : lines)
        {
            Hit thisHit = line.FindIntersection(other);
            if (thisHit.GetTStep() < output.GetTStep())
                output = thisHit;
        }

        return output;
    }

}
