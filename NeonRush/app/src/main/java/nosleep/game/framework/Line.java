package nosleep.game.framework;

/**
 * Created by John on 12/7/2017.
 */

public class Line {
    private FTuple point;
    private FTuple direction;
    private FTuple normal;

    public Line()
    {
        point = new FTuple(0, 0);
        direction = new FTuple(1, 0);
        CalcNormal();
    }

    public Line(FTuple point, FTuple direction)
    {
        this.point = point;
        this.direction = direction;
        CalcNormal();
    }

    public Line(FTuple first, FTuple second, boolean flag)
    {
        if (flag) {
            point = first;
            direction = second;
            CalcNormal();
        }
        else
        {
            point = first;
            direction = second.Sub(first);
            CalcNormal();
        }
    }

    private void CalcNormal()
    {
        normal = new FTuple(direction.y, -direction.x);
    }

    private FTuple getPoint(){return point; }
    private void setPoint(FTuple point){this.point = point; }

    private FTuple getDirection(){return direction; }
    private void setDirection(FTuple direction){this.direction = direction; }

    private void MakeByPoints(FTuple head, FTuple tail)
    {
        point = head;
        direction = tail.Sub(head);
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

    private boolean IntersectsWith(Line other)
    {
        float minX = Math.min(direction.x, other.direction.x);
        float minY = Math.min(direction.y, other.direction.y);
        float maxX = Math.max(direction.x, other.direction.x);
        float maxY = Math.max(direction.y, other.direction.y);

        return maxX % minX != 0 ||
                maxY % minY != 0;
    }

}
