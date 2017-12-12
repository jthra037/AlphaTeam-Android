package nosleep.neonrush;

import android.graphics.Color;

import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.CircleCollider;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.Hit;
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
    protected FTuple velocity = new FTuple(0, 0);

    public Ball(World world, int radius)
    {
        super(world.game);
        this.radius = radius;
        this.world = world;

        localCoord = new ITuple(world.g.getWidth() / 2, world.g.getHeight() / 2);
        collider = new CircleCollider(radius, this);
        world.register(this);
    }

    @Override
    public void update(float deltaTime) {
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

    @Override
    public void present(float deltaTime)
    {
        if (img == null)
        {
            Graphics g = getGame().getGraphics();
            g.drawCircle(localCoord.x, localCoord.y, radius, color);
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

    public void Combine(Ball other)
    {
        radius += other.getRadius()/2;
        mass += other.getMass();
        CircleCollider thisCollider = (CircleCollider)collider;
        thisCollider.setRadius(radius);

        world.unregister(other);
    }

    public void CollisionCheck(Object other)
    {

    }


    public void SetCollision(Hit collision)
    {
        if (collision.GetTStep() < this.collision.GetTStep())
        {
            this.collision = collision;
        }
    }

    public Hit GetCollision()
    {
        return collision;
    }
}
