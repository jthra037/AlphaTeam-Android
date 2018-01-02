package nosleep.neonrush;

import android.graphics.Color;
import android.util.Log;

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

    public ObRectangle(Game game, World world, FTuple pos, ITuple s, int color)
    {
        super(game, world, pos, color);
        size = s;
        pos = pos.Sub(size.x/2f, size.y/2f);
        FTuple[] myPoints = new FTuple[]{pos, pos.Add(size.x, 0), pos.Add(size.x, size.y), pos.Add(0, size.y)};
        collider = new LinesCollider(myPoints, this, w);
        game.getGraphics().resizePixmap(img,size.x,size.y);
    }

    public ObRectangle(Game game, World world, FTuple pos, ITuple s, String color)
    {
        super(game, world, pos, color);
        size = s;
        FTuple topLeft = new FTuple(pos.x - (size.x / 2), pos.y - (size.y / 2));
        FTuple[] myPoints = new FTuple[]{topLeft, topLeft.Add(size.x, 0), topLeft.Add(size.x, size.y), topLeft.Add(0, size.y)};
        collider = new LinesCollider(myPoints, this, w);
        img = game.getGraphics().newPixmap(obColor, Graphics.PixmapFormat.ARGB8888);
        game.getGraphics().resizePixmap(img,size.x,size.y);
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
            g.drawRect((localCoord.x - size.x / 2), (localCoord.y - size.y / 2), size.x, size.y, color);

        }
        else
        {
            // may need to adjust draw location here too, like done above.
            super.present(localCoord.x, localCoord.y, deltaTime);
            //g.drawCircle(localCoord.x, localCoord.y, 5, Color.RED);   //Good for checking obstacle is rendering in the right location.
        }
    }

    public ITuple getSize()
    {
        return size;
    }
}
