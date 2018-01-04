package nosleep.neonrush;

import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.CircleCollider;
import nosleep.game.framework.Collider;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.Hit;
import nosleep.game.framework.IMath;
import nosleep.game.framework.ITuple;
import nosleep.game.framework.Object;

/**
 * Created by John on 2017-10-17.
 * Trimmed by Mark on 2018-01-03.
 */

public abstract class Ball extends Object
{
    protected World world;

    //Physics Info.
    protected int radius;
    float mass = 1;
    Hit collision;
    FTuple velocity = new FTuple(0, 0);

    Ball(World w, int rad)
    {
        super(w.game);
        world = w;

        radius = rad;
        collision = new Hit();
        collider = new CircleCollider(radius, this);

        world.register(this);
    }

    //This constructor is used for menu balls (for color effects), which require less functionality than game balls.
    Ball(Game gm)
    {
        super(gm);
    }

    @Override
    public void update(float deltaTime)
    {
        //If a collision is projected to occur this frame.
        if (collision.isHitOccurred())
        {
            //Move forward until exact collision time (less than 1 frame of movement).
            position = collision.worldSpaceLocation.Add(collision.GetNormal().Mul(radius + 1.0001f)); // Should this really have this here? AKA shouldn't you just solve why the ball sticks to walls instead
            FTuple velocityRelTangent = velocity.ProjectedOnto(collision.GetTangent());
            position = position.Add(velocityRelTangent.Mul(deltaTime - (collision.GetTStep() * deltaTime)));

            //Hit resolved; clear the hit.
            collision = new Hit();
        }
        //Otherwise move full velocity for this frame.
        else
        {
            //
            //
            //Something might be off here, doesn't look right, could be outdated, likely cause of weird movement sometimes.
            //
            //

            position = position.Add(velocity.Mul(deltaTime));

            position.x %= world.getWidth();
            position.y %= world.getHeight();

            //This in particular, seem to recall removing it. Jacob?
            if(position.x < 0)
            {
                position.x = world.getWidth();
            }
            if (position.y < 0)
            {
                position.y = world.getHeight();
            }
            //=====================================================

            localCoord = world.toLocalCoord(position);
        }
    }

    @Override
    public void present(float deltaTime, ITuple pos)
    {
        if (img == null)
        {
            g.drawCircle(pos.x, pos.y, radius, color);
        }
        else
        {
            super.present(deltaTime, pos);
        }
    }

    //Called from Main Menu to update background color effect.
    void menuUpdate(float deltaTime)
    {
        position = position.Add(velocity.Mul(deltaTime));
        position.x %= g.getWidth();
        position.y %= g.getHeight();
        if(position.x < 0)
        {
            position.x = g.getWidth();
            velocity.x = IMath.getRandomInt(-10,10);
        }
        if (position.y < 0)
        {
            position.y = g.getHeight();
            velocity.y = IMath.getRandomInt(-10,10);
        }
    }

    //Called from Main Menu to present background color effect.
    void menuPresent()
    {
        g.drawCircle((int)position.x, (int)position.y, radius, color);
    }

    //Getters.
    public FTuple getVelocity() { return velocity; }
    public float getMass() {
        return mass;
    }
    public int getRadius() {
        return radius;
    }
    public Hit GetCollision()
    {
        return collision;
    }

    //Setters.
    void setVelocity(FTuple velocity) {this.velocity = velocity; }
    public void setImg(String imageRef)
    {
        img = g.newPixmap(imageRef, Graphics.PixmapFormat.ARGB8888);
        int imgScalar = (int)(radius * 2.0f * 1.5f);    //Manually calculated to suit art assets.
        g.resizePixmap(img, imgScalar, imgScalar);
    }
    public void setBackupImg(String imageRef)
    {
        backupImg = g.newPixmap(imageRef, Graphics.PixmapFormat.ARGB8888);
        int imgScalar = (int)(radius * 2.0f * 1.5f);    //Manually calculated to suit art assets.
        g.resizePixmap(backupImg, imgScalar, imgScalar);
    }

    void AddForce(FTuple force) {
        velocity = velocity.Add(force.Mul(1/mass));
    }

    void Combine(Ball other)
    {
        radius += other.getRadius()/2;
        mass += other.getMass();
        CircleCollider thisCollider = (CircleCollider)collider;
        thisCollider.setRadius(radius);

        if (img != null)
        {
            int imgScalar = (int)(radius * 2.0f * 1.5f);    //Manually calculated to suit art assets.
            img.setBitmap(backupImg.getBitmap());
            g.resizePixmap(img, imgScalar, imgScalar);
        }

        world.unregister(other);
    }

    void CollisionCheck(Object other)
    {
        Collider otherCollider = other.getCollider();

        switch (otherCollider.format)
        {
            case lines:
                SetCollision(otherCollider.OnCollision(this, localCoord), other.color, other.colorIndex);
                break;
        }
    }


    void SetCollision(Hit collision, int otherColor, int otherColorIdx)
    {
        if (collision.isHitOccurred() &&
                collision.GetTStep() >= 0 &&
                collision.GetTStep() < this.collision.GetTStep())
        {
            this.collision = collision;
            this.collision.otherColor = otherColor;
            this.collision.otherColorIndex = otherColorIdx;
        }
    }
}
