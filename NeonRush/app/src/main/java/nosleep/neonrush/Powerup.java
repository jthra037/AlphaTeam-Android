package nosleep.neonrush;

import android.graphics.Color;
import android.graphics.Paint;

import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.CircleCollider;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.ITuple;
import nosleep.game.framework.Object;

/**
 * Created by Mark- on 22-Dec-17.
 */

public abstract class Powerup extends Object
{
    public enum PUTYPE {Colorphase};
    public PUTYPE type;

    protected World world;
    protected Player player;
    protected ITuple localCoord;
    protected int radius = 10;
    protected int color = Color.GREEN;
    protected int duration;

    protected long timespawned;
    protected long lifetime;
    protected int timeout = 20000;

    Powerup(World w, FTuple pos)
    {
        super(w.game);
        world = w;
        player = w.getPlayer();
        position = pos;
        timespawned = System.currentTimeMillis();
        collider = new CircleCollider(radius, this);
        tag = "powerup";

        world.register(this);
    }

    @Override
    public void update(float deltaTime)
    {
        lifetime = System.currentTimeMillis() - timespawned;
        setAlpha(100.0f - (lifetime / timeout * 100.f));

        if(lifetime >= timeout)
        {
            world.unregister(this);
        }

        localCoord = world.toLocalCoord(position);
    }

    @Override
    public void present(float deltaTime)
    {
        if (img == null)
        {
            g.drawCircle(localCoord.x, localCoord.y, radius, color);
        }
        else
        {
            super.present(localCoord.x, localCoord.y, deltaTime);
        }
    }

    public void acquire()
    {
        world.getPlayer().powerups.add(this);
    }

    public abstract void activate();
}
