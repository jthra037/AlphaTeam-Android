package alpha.nosleep.neonrush;

import java.util.List;

import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Input;
import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.game.framework.FTuple;
import alpha.nosleep.game.framework.ITuple;

/**
 * Created by Mark- on 17-Oct-17.
 */

public class Player extends Ball
{
    private ITuple localCoord;
    private FTuple accel;

    private Input i;
    private World world;

    public Player(World w)
    {
        super(w.game, 5);
        world = w;
        i = w.game.getInput();

        position = new FTuple(world.getWidth() / 2, world.getHeight() / 2);
        localCoord = new ITuple(world.g.getWidth() / 2, world.g.getHeight() / 2);

        //img = world.g.newPixmap("filename.png", Graphics.PixmapFormat.RGB565);
        //world.g.resizePixmap(playerImg, xValue, yValue);
    }

    public void move()
    {
        accel = new FTuple(i.getAccelX(), i.getAccelY());




        System.out.println("Accel X: " + accel.x + "    Accel Y: " + accel.y);
    }


    public FTuple getWorldCoord()
    {
        return position;
    }
}
