package nosleep.neonrush;

import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.BoxCollider;
import nosleep.game.framework.ITuple;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.LinesCollider;

/**
 * Created by Mark- on 08-Nov-17.
 */

public class ObRectangle extends Obstacle
{
    private ITuple size;

    public ObRectangle(Game game, World world, FTuple pos, ITuple s)
    {
        super(game, world, pos);
        size = s;
        FTuple[] myPoints = new FTuple[]{pos, pos.Add(size.x, 0), pos.Add(size.x, size.y), pos.Add(0, size.y)};
        //collider = new BoxCollider(size.x, size.y);
        collider = new LinesCollider(myPoints, this, w);
    }

    /*@Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
    }*/

    public void present(float deltaTime)
    {
        if (img == null)
        {
            Graphics g = getGame().getGraphics();
            g.drawRect(localCoord.x, localCoord.y, size.x, size.y, color);
        }
        else
        {
            super.present(localCoord.x, localCoord.y, deltaTime);
        }
    }

    public ITuple getSize()
    {
        return size;
    }
}
