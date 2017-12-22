package nosleep.neonrush;

import android.graphics.Color;
import android.graphics.Paint;

import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.ITuple;
import nosleep.game.framework.Object;

/**
 * Created by Mark- on 22-Dec-17.
 */

public class Powerup extends Object
{
    public enum PUTYPE {Colorphase};
    public PUTYPE type;

    private World world;
    private ITuple localCoord;
    private int radius = 10;
    protected int color = Color.GREEN;

    private float timespawned;
    private float lifetime;
    private float timeout = 20000.0f;

    Powerup(World w, PUTYPE t)
    {
        super(w.game);
        world = w;
        position = new FTuple(0.0f, 0.0f);
        timespawned = System.currentTimeMillis();

        tag = "powerup";
        type = t;
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
}
