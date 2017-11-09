package alpha.nosleep.neonrush;

import android.content.SharedPreferences;
import android.graphics.Color;

import alpha.nosleep.androidgames.framework.Input;
import alpha.nosleep.game.framework.FTuple;

/**
 * Created by Mark- on 17-Oct-17.
 */

public class Player extends Ball
{
    private FTuple accel;       //Current accelerometer values.
    private FTuple lastAccel = new FTuple(0.0f, 0.0f);   //Last frame's accelerometer values.
    private float speedScalar = 15.0f;
    private FTuple startingAccel;
    private boolean handHeldPlay;
    private float maxSpeed = 600;
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

        AddForce(Fa); // Impulse plays more fun

        /*
        if (velocity.LengthS() > maxSpeed * maxSpeed)
        {
            velocity = velocity.Normalized().Mul(maxSpeed);
        }
        */

        lastAccel = accel;
    }


    public FTuple getWorldCoord()
    {
        return position;
    }
}
