package nosleep.neonrush;

import android.content.SharedPreferences;
import android.graphics.Color;

import java.util.Vector;

import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Input;
import nosleep.androidgames.framework.Pixmap;
import nosleep.game.framework.CircleCollider;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.Hit;

/**
 * Created by Mark- on 17-Oct-17.
 * Trimmed by Mark on 2018-01-03.
 */

public class Player extends Ball
{
    private Input i;
    private SharedPreferences settings;
    private boolean handHeldPlay;

    //Moevement Info.
    private FTuple startingAccel;
    private FTuple lastAccel = new FTuple(0.0f, 0.0f);      //Last frame's accelerometer values.
    private FTuple lastVelocity = new FTuple(0.0f, 0.0f);   //Last frame's velocity values.

    //Powerup Info.
    Vector<Powerup> powerups;
    private long PUTimeActivated;                   //Time powerup is triggered by player.
    private long PUTimeEnd;                         //Time powerup is scheduled to end.
    boolean PUTriggerActive = false;                //Player is pressing on screen.

    //ColorPhase specific Info.
    int PUColorphaseCount = 0;
    private boolean PUColorphaseIsActive = false;   //Colorphase specifically is active.
    private double PUColorphaseRatio;               //For phase back to white effect.
    private Pixmap PUColorPhaseImg;                 //For phase back to white effect.

    public Player(World w, String imageRef)
    {
        super(w, 15);
        tag = "Player";
        i = game.getInput();
        settings = game.getSharedPreferences();
        handHeldPlay = settings.getBoolean("handHeldPlay", false); //gets the value for handheld play. sets it to false if prefs doesn't exist.

        //Location and Movement Info.
        position = new FTuple(world.getWidth() / 2, world.getHeight() / 2);
        startingAccel = new FTuple(i.getAccelX(), i.getAccelY());

        //Presentation Info.
        color = Color.WHITE;
        img = g.newPixmap(imageRef, Graphics.PixmapFormat.ARGB8888);
        int imgScalar = (int)(radius * 2.0f * 1.5f);    //Manually calculated to suit art assets.
        g.resizePixmap(img, imgScalar, imgScalar);
        backupImg = g.newPixmap(imageRef, Graphics.PixmapFormat.ARGB8888);
        PUColorPhaseImg = g.newPixmap(imageRef, Graphics.PixmapFormat.ARGB8888);

        //Powerups List.
        powerups = new Vector(10, 3);
    }

    @Override
    public void update(float deltaTime)
    {
        resolvePhysics(deltaTime);

        //Timer to return back to white while ColorPhased.
        if(PUColorphaseIsActive)
        {
            if(System.currentTimeMillis() >= PUTimeEnd)
            {
                PUColorphaseIsActive = false;
                color = Color.WHITE;
                if (img != null)
                {
                    img.setBitmap(backupImg.getBitmap());
                    int imgScalar = (int)(radius * 2.0f * 1.5f);    //Manually calculated to suit art assets.
                    g.resizePixmap(img, imgScalar, imgScalar);
                }
            }

            //Used to grow the player back to white color over time, starting from the center.
            PUColorphaseRatio = ((double)(System.currentTimeMillis() - PUTimeActivated) / (double)(PUTimeEnd - PUTimeActivated));
        }

        //Move the player.
        world.ConvertToWorldSpace(position);
        localCoord = world.toLocalCoord(position);
        move();
    }

    @Override
    public void present(float deltaTime)
    {
        super.present(deltaTime, localCoord);

        //Colorphase timer effect. Will turn off if we start using proper art, aka img != null.
        if(PUColorphaseIsActive)
        {
            if (backupImg == null)
            {
                g.drawCircle(localCoord.x, localCoord.y, (int)(radius * PUColorphaseRatio), Color.WHITE);
            }
            else
            {
                int imgScalar = (int)(radius * PUColorphaseRatio * 2.0f * 1.5f) + 1;    //Manually calculated to suit art assets.
                PUColorPhaseImg.setBitmap(backupImg.getBitmap());
                g.resizePixmap(PUColorPhaseImg, imgScalar, imgScalar);
                g.drawPixmap(PUColorPhaseImg, (localCoord.x - PUColorPhaseImg.getWidth()/2), (localCoord.y - PUColorPhaseImg.getHeight()/2));
            }
        }
    }

    private void move()
    {
        FTuple accel;   //Current accelerometer values.
        float speed = 600;
        float speedScalar = 15.0f;

        //
        //
        //This setting needs to be revisited, not sure it does anything effectively if on.
        //
        //
        if (handHeldPlay)
            accel = new FTuple(i.getAccelX(), i.getAccelY()).Add(startingAccel.Mul(-1));
        else
            accel = new FTuple(i.getAccelX(), i.getAccelY());

        //Calculate the force to be applied based on accelerometer values.
        float Fn = getMass() * -world.getGravity();
        float dx = Fn * (accel.y - lastAccel.y) * speedScalar;
        float dy = Fn * (accel.x - lastAccel.x) * speedScalar;
        FTuple Fa = new FTuple(dx, dy);

        //If no collision is projected to occur this frame, business as usual.
        if (!collision.isHitOccurred())
        {
            AddForce(Fa); // Impulse plays more fun
            lastVelocity = velocity;
        }
        //If a collision occured, save the previous velocity value to check again next frame... is what I believe is happening here.
        else
        {
            velocity = lastVelocity;
        }

        //Cap velocity at a max.
        if (velocity.LengthS() > speed * speed)
        {
            velocity = velocity.Normalized().Mul(speed);
        }

        //Save this frame's accelerometer values to compare against next frame.
        lastAccel = accel;
    }

    private void resolvePhysics(float deltaTime)
    {
        if (collision.isHitOccurred())
        {
            //If the player is trying to activate a powerup, check to see what powerups the player has.
            boolean colorphasingThisFrame = false;  //Used to ignore a collision if the player decides to phase through. Prevents stutter.
            if (PUTriggerActive)
            {
                for(Powerup pow : powerups)
                {
                    //If they have a colorphase, activate it and allow the player to pass through.
                    if(pow.type == Powerup.PUTYPE.Colorphase)
                    {
                        PUTimeActivated = System.currentTimeMillis();
                        pow.activate(collision.otherColorIndex);
                        powerups.remove(pow);
                        PUColorphaseCount--;

                        //Denote accordingly.
                        PUTriggerActive = false;
                        PUColorphaseIsActive = true;
                        colorphasingThisFrame = true;

                        break;
                    }
                }
            }

            //If the player hasn't chosen to avoid the collision using ColorPhase, proceed.
            if (!colorphasingThisFrame)
            {
                //Move forward until exact collision time (less than 1 frame of movement).
                position = collision.worldSpaceLocation.Add(collision.GetNormal().Mul(radius + 1.0001f)); // Should this really have this here? AKA shouldn't you just solve why the ball sticks to walls instead
                FTuple velocityRelTangent = velocity.ProjectedOnto(collision.GetTangent());
                position = position.Add(velocityRelTangent.Mul(deltaTime - (collision.GetTStep() * deltaTime)));

                //Hit resolved; clear the hit.
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
    }

    //Tweaked to account for Colorphasing.
    @Override
    void Combine(Ball other)
    {
        radius += other.getRadius()/2;
        mass += other.getMass();
        CircleCollider thisCollider = (CircleCollider)collider;
        thisCollider.setRadius(radius);

        if (img != null && !PUColorphaseIsActive)
        {
            int imgScalar = (int)(radius * 2.0f * 1.5f);    //Manually calculated to suit art assets.
            img.setBitmap(backupImg.getBitmap());
            g.resizePixmap(img, imgScalar, imgScalar);
        }
        else if (img!= null)
        {
            int imgScalar = (int)(radius * 2.0f * 1.5f);    //Manually calculated to suit art assets.
            g.resizePixmap(img, imgScalar, imgScalar);
        }

        world.unregister(other);
    }

    //Getters.
    FTuple getWorldCoord()
    {
        return position;
    }

    //Duration set by the powerup, allows for different durations if necessary. Called from the powerup class.
    void setPowerupTimer(int duration)
    {
        PUTimeEnd = PUTimeActivated + duration;
    }
}
