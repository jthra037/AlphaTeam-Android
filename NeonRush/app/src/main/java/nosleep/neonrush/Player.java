package nosleep.neonrush;

import android.content.SharedPreferences;
import android.graphics.Color;

import nosleep.androidgames.framework.Input;
import nosleep.game.framework.FTuple;

/**
 * Created by Mark- on 17-Oct-17.
 */

public class Player extends Ball
{
    private FTuple accel;       //Current accelerometer values.
    private FTuple lastAccel = new FTuple(0.0f, 0.0f);   //Last frame's accelerometer values.
    private FTuple lastvelocity = new FTuple(0, 0);
    private float speedScalar = 15.0f;
    private FTuple startingAccel;
    private boolean handHeldPlay;
    private boolean notColliding = true;
    private float speed = 600;
    private Input i;
    SharedPreferences settings;
    private World mWorld;
    private float damp = 2;

    public Player(World w)
    {
        super(w, 15);
        mWorld = w;
        settings = w.game.getSharedPreferences();
        i = w.game.getInput();
        startingAccel = new FTuple(i.getAccelX(), i.getAccelY());
        color = Color.WHITE;
        position = new FTuple(mWorld.getWidth() / 2, mWorld.getHeight() / 2);
        tag = "Player";
        handHeldPlay = settings.getBoolean("handHeldPlay", false); //gets the value for handheld play. sets it to false if prefs doesn't exist.
        //img = mWorld.g.newPixmap("filename.png", Graphics.PixmapFormat.RGB565);
        //mWorld.g.resizePixmap(playerImg, xValue, yValue);
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        move(deltaTime);
    }

    public void move(float deltaTime)
    {
        if (handHeldPlay)
            accel = new FTuple(i.getAccelX(), i.getAccelY()).Add(startingAccel.Mul(-1));
        else
            accel = new FTuple(i.getAccelX(), i.getAccelY());

        float Fn = getMass() * -mWorld.getGravity();

        float dx = Fn * (accel.y - lastAccel.y) * speedScalar;
        float dy = Fn * (accel.x - lastAccel.x) * speedScalar;

            //float dx = Fn * accel.y * (accel.y / damp) * (accel.y / Math.abs(accel.y));
            //float dy = Fn * accel.x * (accel.x / damp) * (accel.x / Math.abs(accel.x));

        FTuple Fa = new FTuple(dx, dy);

        if (!collision.isHitOccurred())
        {
            AddForce(Fa); // Impulse plays more fun
            lastvelocity = velocity;
        }
        else
        {
            /*dx = Fn * (accel.y) * speedScalar;
            dy = Fn * (accel.x) * speedScalar;

            setVelocity(new FTuple(dx, dy));*/
            velocity = lastvelocity;

            notColliding = true;
        }

        if (velocity.LengthS() > speed * speed) {
            velocity = velocity.Normalized().Mul(speed);
        }

        lastAccel = accel;



    }


    public void setCollision()
    {
        notColliding = false;
    }

    public FTuple getWorldCoord()
    {
        return position;
    }
}
