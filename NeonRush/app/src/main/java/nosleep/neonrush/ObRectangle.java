package nosleep.neonrush;

import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.ITuple;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.LinesCollider;

/**
 * Created by Mark- on 08-Nov-17.
 * Trimmed by Mark on 2018-01-03.
 */

public class ObRectangle extends Obstacle
{
    private ITuple size;

    public ObRectangle(Game game, World world, FTuple pos, ITuple s, int colorIndex)
    {
        super(game, world, pos, colorIndex);
        size = s;

        //Set image.
        img = g.newPixmap(imageRef, Graphics.PixmapFormat.ARGB8888);
        g.resizePixmap(img,size.x,size.y);

        //Set Collider.
        FTuple topLeft = new FTuple(position.x - (size.x / 2), position.y - (size.y / 2));
        FTuple[] myPoints = new FTuple[]{topLeft, topLeft.Add(size.x, 0), topLeft.Add(size.x, size.y), topLeft.Add(0, size.y)};
        collider = new LinesCollider(myPoints, this, world);
    }

    public void present(float deltaTime)
    {
        if (img == null)
        {
            g.drawRect((localCoord.x - size.x / 2), (localCoord.y - size.y / 2), size.x, size.y, color);
        }
        else
        {
            super.present(deltaTime, localCoord);
            //g.drawCircle(localCoord.x, localCoord.y, 5, Color.RED);   //Good for checking obstacle is rendering in the right location. Dot should be at center of obstacle.
        }
    }

    public ITuple getSize()
    {
        return size;
    }

    public void setImg(String imageRef)
    {

    }
    public void setBackupImg(String imageRef)
    {

    }
}
