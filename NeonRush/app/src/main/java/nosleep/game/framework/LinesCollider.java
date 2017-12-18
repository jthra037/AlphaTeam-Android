package nosleep.game.framework;

import java.util.ArrayList;
import java.util.List;

import nosleep.neonrush.Ball;
import nosleep.neonrush.World;

/**
 * Created by John on 12/9/2017.
 */

public class LinesCollider extends Collider {

    World w;
    private Object parent;
    private FTuple[] points;
    private List<Line> lines = new ArrayList<Line>();

    public LinesCollider(FTuple[] points , Object parent)
    {
        this.points = points;
        this.parent = parent;
        this.format = ColliderFormat.lines;

        for (int i = 0; i < points.length; i++)
        {
            lines.add(new Line(points[i], points[(i + 1) % points.length], false));
        }
    }

    public LinesCollider(FTuple[] points , Object parent, World world)
    {
        this.points = points;
        this.parent = parent;
        this.format = ColliderFormat.lines;
        this.w = world;

        for (int i = 0; i < points.length; i++)
        {
            lines.add(new Line(points[i], points[(i + 1) % points.length], false, world));
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
        return CheckCollision(other);
    }

    public Hit CheckCollision(Object other)
    {
        if (other instanceof Ball)
        {
            return LineOverlap((Ball) other);
        }

        return new Hit();
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

    public Hit LineOverlap(Ball other)
    {
        Hit output = new Hit();

        for (Line line : lines)
        {
            FTuple loi = other.getPosition().Add(line.getNormal().Mul(-other.getRadius()));
            // One day we will need relative velocity here
            Line otherLine = new Line(loi, other.getVelocity().Mul(0.05f), w); // this should be replaced with something other than a hardcoded approximation

            Hit thisHit = line.FindIntersection(otherLine);
            if (thisHit.isHitOccurred() &&
                    thisHit.GetTStep() < output.GetTStep())
                output = thisHit;
        }

        if (!output.isHitOccurred())
        {
            for (FTuple point : points)
            {
                // Find location of impact
                FTuple otherVelNormal = point.Sub(other.position).Normalized();
                FTuple loi = other.getPosition().Add(otherVelNormal.Mul(other.getRadius()));

                FTuple otherVelTangent = new FTuple(otherVelNormal.y, -otherVelNormal.x);

                // make the lines
                Line otherLine = new Line(loi, other.getVelocity().Mul(0.05f), w); // need a real number here too
                FTuple direction = otherVelTangent.Normalized().Mul(other.getRadius());
                Line thisLine = new Line (point.Sub(direction), direction.Mul(2), w);


                Hit thisHit = thisLine.FindIntersection(otherLine);
                if (thisHit.isHitOccurred() &&
                        thisHit.GetTStep() < output.GetTStep())
                    output = thisHit;

            }
        }

        output.SetCollidedWith(parent);
        return output;
    }

}
