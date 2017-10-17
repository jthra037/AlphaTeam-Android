package alpha.nosleep.neonrush;

import java.util.List;

import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Input;
import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.game.framework.FTuple;

/**
 * Created by Mark- on 17-Oct-17.
 */

public class Player
{
    private FTuple worldCoord;
    private FTuple localCoord;
    private FTuple accel;

    private Input i;
    private World world;
    private Pixmap playerImg;

    public Player(World w)
    {
        world = w;
        i = w.game.getInput();

        worldCoord = new FTuple(world.getWidth() / 2, world.getHeight() / 2);
        localCoord = new FTuple(world.g.getWidth() / 2, world.g.getHeight() / 2);

        //playerImg = world.g.newPixmap("filename.png", Graphics.PixmapFormat.RGB565);
        //world.g.resizePixmap(playerImg, xValue, yValue);
    }

    public void move()
    {
        accel = new FTuple(i.getAccelX(), i.getAccelY());

        System.out.println("Accel X: " + accel.x + "    Accel Y: " + accel.y);
    }

    public void draw()
    {

    }

    public FTuple getWorldCoord()
    {
        return worldCoord;
    }
}
