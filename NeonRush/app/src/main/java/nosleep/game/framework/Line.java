package nosleep.game.framework;

import nosleep.neonrush.World;

/**
 * Created by John on 12/7/2017.
 */

public class Line {
    private World world;
    private FTuple point;
    private FTuple endpoint;
    private FTuple direction;
    private FTuple normal;

    public Line()
    {
        point = new FTuple(0, 0);
        direction = new FTuple(1, 0);
        endpoint = point.Add(direction);
        CalcNormal();
    }

    public Line(FTuple point, FTuple direction)
    {
        this.point = point;
        this.direction = direction;
        endpoint = point.Add(direction);
        CalcNormal();
    }

    public Line(FTuple point, FTuple direction, World world)
    {
        this.point = point;
        this.direction = direction;
        endpoint = point.Add(direction);
        CalcNormal();
        this.world = world;
    }

    public Line(FTuple first, FTuple second, boolean flag)
    {
        if (flag) {
            point = first;
            direction = second;
            endpoint = first.Add(second);
            CalcNormal();
        }
        else
        {
            point = first;
            direction = second.Sub(first);
            endpoint = second;
            CalcNormal();
        }
    }

    public Line(FTuple first, FTuple second, boolean flag, World world)
    {
        this.world = world;

        if (flag) {
            point = first;
            direction = second;
            endpoint = first.Add(second);
            CalcNormal();
        }
        else
        {
            point = first;
            direction = second.Sub(first);
            endpoint = second;
            CalcNormal();
        }
    }

    private void CalcNormal()
    {
        normal = new FTuple(direction.y, -direction.x).Normalized();
    }

    public FTuple getPoint(){return point; }
    public void setPoint(FTuple point)
    {
        this.point = point;
        endpoint = point.Add(direction);
    }

    public FTuple getEndpoint(){return endpoint; }
    public void setEndpoint(FTuple endpoint){this.endpoint = endpoint; }

    public FTuple getDirection(){return direction; }
    public void setDirection(FTuple direction){this.direction = direction; }

    public void MakeByPoints(FTuple head, FTuple tail)
    {
        point = head;
        direction = tail.Sub(head);
        endpoint = tail;
        CalcNormal();
    }

    public float FindYAt(float x)
    {
        float t = (x - point.x)/ direction.x;
        return point.y + (t * direction.y);
    }

    public float FindXAt(float y)
    {
        float t = (y - point.y)/ direction.y;
        return point.x + (t * direction.x);
    }

    public FTuple FindPointAt(float t)
    {
        float x = point.x + (t * direction.x);
        float y = point.y + (t * direction.y);

        return new FTuple(x, y);
    }


    // Checks if the slopes of each line are the same
    public boolean IntersectsWith(Line other)
    {
        if (direction.x == 0 ||
            other.direction.x == 0)
        {
            return false;
        }
        else
        {
            return Math.abs((direction.y / direction.x) - (other.direction.y / other.direction.x)) > 0.00001f; // Should replace with a real/better epsilon
        }
    }


    // Find the intersection of some other line and this line
    /*public Hit FindIntersection(Line other, FTuple segmentEnd)
    {
        Hit output;

        if (IntersectsWith(other))
        {
            float ax = point.x;
            float ay = point.y;
            float bx = direction.x;
            float by = direction.y;
            float cx = other.point.x;
            float cy = other.point.y;
            float dx = other.direction.x;
            float dy = other.direction.y;

            float u = (bx * (cy - ay) + by * (ax - cx)) / (dx * by - dy * bx); // Parameter for this line
            float t = (dx * (ay - cy) + dy * (cx - ax)) / (bx * dy - by * dx); // Parameter for the other line

            // Assumes "direction" of each line brought it from its Start to its End
            boolean hitOccurred = 0 <= u && u <= 1 &&
                    0 <= t && t <= 1;

            float offset = 15/500;

            if (!hitOccurred && 0 <= u && u <= 1)
            {

            }

            output = new Hit(hitOccurred, FindPointAt(u), normal, direction.Normalized(), t);
        }
        else
        {
            output = new Hit();
        }

        return output;
    }*/

    // Find the intersection of some other line and this line
    public Hit FindIntersection(Line other)
    {
        Hit output;

        if (!world.IsValidPosition(other.getEndpoint()))
        {
            world.ConvertToWorldSpace(other.endpoint);
            other.setPoint(other.getEndpoint().Sub(other.getDirection()));
        }

        if (other.getDirection().Dot(normal) < 0)
        {
            float ax = point.x;
            float ay = point.y;
            float bx = direction.x;
            float by = direction.y;
            float cx = other.point.x;
            float cy = other.point.y;
            float dx = other.direction.x;
            float dy = other.direction.y;

            float u = (bx * (cy - ay) + by * (ax - cx)) / (dx * by - dy * bx); // Parameter for other line
            float t = (dx * (ay - cy) + dy * (cx - ax)) / (bx * dy - by * dx); // Parameter for this(?) line

            // Assumes "direction" of each line brought it from its Start to its End
            boolean hitOccurred = 0 <= u && u <= 1.1 && // this should be 1, but works better around here
                    0 <= t && t <= 1;

            output = new Hit(hitOccurred, FindPointAt(t), normal, direction.Normalized(), u, t);
        }
        else
        {
            output = new Hit();
        }

        return output;
    }

    public FTuple getNormal()
    {return normal;}

}
