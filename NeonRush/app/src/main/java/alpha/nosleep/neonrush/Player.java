package alpha.nosleep.neonrush;

import android.graphics.Color;

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
    //private float friction = 0.6f;

    public Player(World w)
    {
        super(w.game, 15);
        world = w;
        i = w.game.getInput();

        position = new FTuple(world.getWidth() / 2, world.getHeight() / 2);
        localCoord = new ITuple(world.g.getWidth() / 2, world.g.getHeight() / 2);

        //img = world.g.newPixmap("filename.png", Graphics.PixmapFormat.RGB565);
        //world.g.resizePixmap(playerImg, xValue, yValue);
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        localCoord = world.toLocalCoord(position);
    }

    @Override
    public void present(float deltaTime)
    {
        if (img == null)
        {
            Graphics g = getGame().getGraphics();
            g.drawCircle(localCoord.x, localCoord.y, getRadius(), Color.WHITE);
        }
        else
        {
            super.present(deltaTime);
        }
    }

    public void move(float deltaTime)
    {
        accel = new FTuple(i.getAccelX(), i.getAccelY());
        float Fg = getMass() * world.getGravity();
        float dx = Fg * accel.x;
        float dy = Fg * accel.x;

        AddForce(new FTuple(dx, dy), deltaTime);

        //System.out.println("Accel X: " + accel.x + "    Accel Y: " + accel.y);
        //System.out.println("********************************");
        //System.out.println("Velocity.x: " + velocity.x + "\n Velocity.y: " + velocity.y);
    }


    public FTuple getWorldCoord()
    {
        return position;
    }
}
