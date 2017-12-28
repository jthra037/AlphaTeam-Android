package nosleep.neonrush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.Switch;
import android.graphics.Paint;
import android.util.Log;

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
 */

public class Ball extends Object{
    private World world;
    private ITuple localCoord;
    private int radius = 500;
    private float mass = 1;
    protected int color = Color.BLACK;
    protected Hit collision;
    protected String enemyColor = "enemies/white.png";
    private Paint newPaint = new Paint();
    protected FTuple velocity = new FTuple(0, 0);

    public Ball(World world, int radius)
    {
        super(world.game);
        this.radius = radius;
        this.world = world;
        this.collision = new Hit();

        localCoord = new ITuple(world.g.getWidth() / 2, world.g.getHeight() / 2);
        collider = new CircleCollider(radius, this);
        world.register(this);
    }

    public Ball(Game game, int radius, FTuple pos, int color)
    {
        super(game);
        this.radius = radius;
        this.position = pos;
        this.color = color;
        collider = new CircleCollider(radius, this);
        //world.register(this);
    }

    public Ball(Game game, int radius, FTuple pos, String sColor)
    {
        super(game);
        this.radius = radius;
        this.position = pos;
        this.enemyColor = sColor;
        this.img = game.getGraphics().newPixmap(this.enemyColor, Graphics.PixmapFormat.ARGB8888);
        game.getGraphics().resizePixmap(img, radius*2,radius*2);
        collider = new CircleCollider(radius, this);
        //world.register(this);
    }

    public Ball(World world, int radius, String sColor)
    {
        super(world.game);
        this.radius = radius;
        this.world = world;
        this.enemyColor = sColor;
        localCoord = new ITuple(world.g.getWidth() / 2, world.g.getHeight() / 2);
        collider = new CircleCollider(radius, this);
        world.register(this);
    }

    @Override
    public void update(float deltaTime) {
        if (collision.isHitOccurred())
        {
            //Move forward until collision time
            //position = position.Add(velocity.Mul(collision.GetTStep() * deltaTime));
            position = collision.worldSpaceLocation.Add(collision.GetNormal().Mul(radius + 1.0001f)); // Should this really have this here? AKA shouldn't you just solve why the ball sticks to walls instead
            FTuple velocityRelTangent = velocity.ProjectedOnto(collision.GetTangent());
            position = position.Add(velocityRelTangent.Mul(deltaTime - (collision.GetTStep() * deltaTime)));

            // Hit resolved; clear the hit
            collision = new Hit();
        if (world != null)
        {
            position = position.Add(velocity.Mul(deltaTime));
            position.x %= world.getWidth();
            position.y %= world.getHeight();
            if(position.x < 0)
            {
                position.x = world.getWidth();
            }
            if (position.y < 0)
            {
                position.y = world.getHeight();
            }
            localCoord = world.toLocalCoord(position);
        }
        else
        {
            position = position.Add(velocity.Mul(deltaTime));
        }

        world.ConvertToWorldSpace(position);
        localCoord = world.toLocalCoord(position);
            position.x %= getGame().getGraphics().getWidth();
            position.y %= getGame().getGraphics().getHeight();
            if(position.x < 0)
            {
                position.x = getGame().getGraphics().getWidth();
                velocity.x = IMath.getRandomInt(-10,10);
            }
            if (position.y < 0)
            {
                position.y = getGame().getGraphics().getHeight();
                velocity.y = IMath.getRandomInt(-10,10);
            }
        }

    }

    @Override
    public void present(float deltaTime)
    {
        if (img == null && world == null)
        {
            Graphics g = getGame().getGraphics();
            g.drawCircle((int)position.x, (int)position.y, radius, color);

        }
        else if (img == null && world != null)
        {
            Graphics g = getGame().getGraphics();
            g.drawCircle(localCoord.x, localCoord.y, radius, color);
        }
        else if (img != null && world == null)
        {
            super.present((int)position.x,(int)position.y,deltaTime);
        }
        else
        {
            super.present(localCoord.x, localCoord.y, deltaTime);
        }
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public FTuple getVelocity() { return velocity; }
    public void setVelocity(FTuple velocity) {this.velocity = velocity; }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void AddForce(FTuple force, float deltaTime) {
        velocity = velocity.Add(force.Mul(1/mass).Mul(deltaTime));
    }

    public World getWorld()
    {
        return world;
    }

    public void AddForce(FTuple force) {
        velocity = velocity.Add(force.Mul(1/mass));
    }

    public ITuple getLocalCoord(){return localCoord;}

    public void Combine(Ball other)
    {
        if (img == null)
        {
            radius += other.getRadius()/2;
            mass += other.getMass();
            CircleCollider thisCollider = (CircleCollider)collider;
            thisCollider.setRadius(radius);

            world.unregister(other);
        }
        else
        {
            radius += other.getRadius()/2;
            mass += other.getMass();
            CircleCollider thisCollider = (CircleCollider)collider;
            getGame().getGraphics().resizePixmap(img, radius*2,radius*2);
            thisCollider.setRadius(radius);

            world.unregister(other);
        }

    }

    public void CollisionCheck(Object other)
    {
        Collider otherCollider = other.getCollider();

        switch (otherCollider.format)
        {
            case lines:
                SetCollision(otherCollider.OnCollision(this, localCoord));
                break;
        }
    }


    public void SetCollision(Hit collision)
    {
        if (collision.isHitOccurred() &&
                collision.GetTStep() >= 0 &&
                collision.GetTStep() < this.collision.GetTStep())
        {
            this.collision = collision;
        }
    }

    public Hit GetCollision()
    {
        return collision;
    }
}
