package nosleep.neonrush;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.aware.PublishConfig;

import java.util.Vector;

import nosleep.androidgames.framework.Input;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.Hit;

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

    //Powerups
    public Vector<Powerup> powerups;
    public boolean PUTriggerActive = false;         //Player is pressing on screen.
    private boolean PUcolorPhaseIsActive = false;   //Colorphase specifically is active.
    private long PUTimeActivated;                   //Time powerup is triggered by player.
    private long PUTimeEnd;                         //Time powerup is scheduled to end.

    public Player(World w)
    {
        super(w, 15);
        mWorld = w;
        settings = w.game.getSharedPreferences();
        i = w.game.getInput();
        startingAccel = new FTuple(i.getAccelX(), i.getAccelY());
        this.color = Color.WHITE;
        position = new FTuple(mWorld.getWidth() / 2, mWorld.getHeight() / 2);
        tag = "Player";
        handHeldPlay = settings.getBoolean("handHeldPlay", false); //gets the value for handheld play. sets it to false if prefs doesn't exist.
        powerups = new Vector(10, 3);
        //img = mWorld.g.newPixmap("filename.png", Graphics.PixmapFormat.RGB565);
        //mWorld.g.resizePixmap(playerImg, xValue, yValue);
    }

    @Override
    public void update(float deltaTime)
    {
        if (collision.isHitOccurred())
        {
            //If the player is trying to activate a powerup, check to see what powerups the player has.
            //If they have a colorphase, activate it and allow the player to pass through.
            boolean colorphasingThisFrame = false;
            if (PUTriggerActive)
            {
                for(Powerup pow : powerups)
                {
                    if(pow.type == Powerup.PUTYPE.Colorphase)
                    {
                        System.out.println("Powerups Before phase: " + powerups);
                        PUTimeActivated = System.currentTimeMillis();
                        pow.activate();
                        powerups.remove(pow);
                        PUTriggerActive = false;
                        PUcolorPhaseIsActive = true;
                        colorphasingThisFrame = true;
                        System.out.println("Powerups After phase: " + powerups);
                        break;
                    }
                }
            }

            if (!colorphasingThisFrame)
            {
                //Move forward until collision time
                //position = position.Add(velocity.Mul(collision.GetTStep() * deltaTime));
                position = collision.worldSpaceLocation.Add(collision.GetNormal().Mul(radius + 1.0001f)); // Should this really have this here? AKA shouldn't you just solve why the ball sticks to walls instead
                FTuple velocityRelTangent = velocity.ProjectedOnto(collision.GetTangent());
                position = position.Add(velocityRelTangent.Mul(deltaTime - (collision.GetTStep() * deltaTime)));

                // Hit resolved; clear the hit
                collision = new Hit();
            }
            else
            {
                position = position.Add(velocity.Mul(deltaTime));
            }
        }
        else
        {
            position = position.Add(velocity.Mul(deltaTime));
        }

        //Timer to return back to white.
        if(PUcolorPhaseIsActive)
        {
            if(System.currentTimeMillis() >= PUTimeEnd)
            {
                PUcolorPhaseIsActive = false;
                color = Color.WHITE;
            }
        }

        mWorld.ConvertToWorldSpace(position);
        localCoord = mWorld.toLocalCoord(position);
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

    //Duration set by the powerup, allows for different durations if necessary.
    public void setPowerupTimer(int duration)
    {
        PUTimeEnd = PUTimeActivated + duration;
    }
}
