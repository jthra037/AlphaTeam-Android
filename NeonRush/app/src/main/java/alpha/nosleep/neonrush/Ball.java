package alpha.nosleep.neonrush;

import android.graphics.Color;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.game.framework.CircleCollider;
import alpha.nosleep.game.framework.IPhysics;
import alpha.nosleep.game.framework.Object;

/**
 * Created by John on 2017-10-17.
 */

public class Ball extends Object implements IPhysics {
    private int radius = 5;
    private float mass = 1;

    public Ball(Game game, int radius)
    {
        super(game);
        this.radius = radius;

        collider = new CircleCollider(radius);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime)
    {
        if (img == null)
        {
            Graphics g = getGame().getGraphics();
            g.drawCircle((int)position.x, (int)position.y, radius, Color.BLUE);
        }
        else
        {
            super.present(deltaTime);
        }
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
}
