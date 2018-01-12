package nosleep.neonrush;

import nosleep.game.framework.FTuple;

/**
 * Created by Andrew on 11/27/2017.
 */

public class ILine {

    public int x1;
    public int x2;
    public int y1;
    public int y2;

    public ILine(int x1, int y1, int x2, int y2)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public ILine(FTuple a, FTuple b)
    {
        this.x1 = (int)a.x;
        this.y1 = (int)a.y;
        this.x2 = (int)b.x;
        this.y2 = (int)b.y;
    }
}
