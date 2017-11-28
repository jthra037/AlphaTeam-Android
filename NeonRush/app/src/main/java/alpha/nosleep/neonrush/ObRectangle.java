package alpha.nosleep.neonrush;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.game.framework.BoxCollider;
import alpha.nosleep.game.framework.FTuple;
import alpha.nosleep.game.framework.ITuple;

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
        collider = new BoxCollider(size.x, size.y);
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
