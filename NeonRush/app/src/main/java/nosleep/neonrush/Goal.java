package nosleep.neonrush;

import android.graphics.Color;

import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.FTuple;

/**
 * Created by John on 10/19/2017.
 * Trimmed by Mark on 2018-01-03.
 */

public class Goal extends Ball
{
    //Standard constructor used for gameplay.
    public Goal(World world, int radius, FTuple position, String imageRef)
    {
        super(world, radius);
        this.tag = "Goal";
        this.position = position;

        this.color = Color.WHITE;
        img = g.newPixmap(imageRef, Graphics.PixmapFormat.ARGB8888);
        int imgScalar = (int)(radius * 2.0f * 1.5f);    //Manually calculated to suit art assets.
        g.resizePixmap(img, imgScalar, imgScalar);
    }

    //Alternative constructor used in main menu for color effect. Goals are basically just balls lets be real.
    public Goal(Game gm, int rad, FTuple pos, int col)
    {
        super(gm);

        radius = rad;
        position = pos;
        color = col;
    }

    public void present(float deltaTime)
    {
        super.present(deltaTime, localCoord);
    }
}
