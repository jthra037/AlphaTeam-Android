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
    private FTuple accel;

    private float maxSpeed = 600;
    private Input i;
    private World world;
    private float damp = 2;

    public Player(World w)
    {
        super(w, 15);
        world = w;
        i = w.game.getInput();

        color = Color.WHITE;
        position = new FTuple(world.getWidth() / 2, world.getHeight() / 2);

        //img = world.g.newPixmap("filename.png", Graphics.PixmapFormat.RGB565);
        //world.g.resizePixmap(playerImg, xValue, yValue);
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        move(deltaTime);
    }

    public void move(float deltaTime)
    {
        accel = new FTuple(i.getAccelX(), i.getAccelY());
        float Fn = getMass() * -world.getGravity();
        //float dx = Fn * accel.y * (accel.y / damp) * (accel.y / Math.abs(accel.y));
        //float dy = Fn * accel.x * (accel.x / damp) * (accel.x / Math.abs(accel.x));
        float dx = Fn * accel.y;
        float dy = Fn * accel.x;
        FTuple Fa = new FTuple(dx, dy);

        AddForce(Fa); // Impulse plays more fun
        if (velocity.LengthS() > maxSpeed * maxSpeed)
        {
            velocity = velocity.Normalized().Mul(maxSpeed);
        }
    }


    public FTuple getWorldCoord()
    {
        return position;
    }
}
