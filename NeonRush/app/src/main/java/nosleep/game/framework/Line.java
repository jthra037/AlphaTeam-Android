package nosleep.game.framework;

/**
 * Created by John on 12/7/2017.
 */

public class Line {
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

    private void CalcNormal()
    {
        normal = new FTuple(direction.y, -direction.x);
    }

    private FTuple getPoint(){return point; }
    private void setPoint(FTuple point){this.point = point; }

    private FTuple getEndpoint(){return endpoint; }
    private void setEndpoint(FTuple endpoint){this.endpoint = endpoint; }

    private FTuple getDirection(){return direction; }
    private void setDirection(FTuple direction){this.direction = direction; }

    private void MakeByPoints(FTuple head, FTuple tail)
    {
        point = head;
        direction = tail.Sub(head);
        endpoint = tail;
        CalcNormal();
    }

    private float FindYAt(float x)
    {
        float t = (x - point.x)/ direction.x;
        return point.y + (t * direction.y);
    }

    private float FindXAt(float y)
    {
        float t = (y - point.y)/ direction.y;
        return point.x + (t * direction.x);
    }

    private FTuple FindPointAt(float t)
    {
        float x = point.x + (t * direction.x);
        float y = point.y + (t * direction.y);

        return new FTuple(x, y);
    }

    private boolean IntersectsWith(Line other)
    {
        return Math.abs((direction.y/direction.x) - (other.direction.y/other.direction.x)) < 0.00001f;
    }

    private Hit FindIntersection(Line other, FTuple segmentEnd)
    {
        Hit output = new Hit();

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

            float u = (bx * (cy - ay) + by * (ax - cx)) / (dx * by - dy * bx);
            float t = (dx * (ay - cy) + dy * (cx - ax)) / (bx * dy - by * dx);

            boolean hitOccurred = 0 <= u && u <= 1 &&
                    0 <= t && t <= 1;

            output = new Hit(hitOccurred, FindPointAt(u), normal, direction, t);
        }

        return output;
    }

}
