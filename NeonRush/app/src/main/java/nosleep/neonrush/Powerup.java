package nosleep.neonrush;

import android.graphics.Color;

import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.CircleCollider;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.Object;

/**
 * Created by Mark- on 22-Dec-17.
 */

public abstract class Powerup extends Object
{
    public enum PUTYPE {Colorphase};
    public PUTYPE type;

    //External References.
    protected World world;
    protected Player player;

    //Presentation Info.
    protected int radius = 10;
    protected int color = Color.GREEN;

    //Timer Related Info.
    int duration;
    private long timespawned;
    private long lifetime;
    private int timeout = 20000;

    Powerup(World w, FTuple pos)
    {
        super(w.game);
        tag = "powerup";
        world = w;
        player = world.getPlayer();

        position = pos;
        collider = new CircleCollider(radius, this);

        timespawned = System.currentTimeMillis();
        world.register(this);
    }

    @Override
    public void update(float deltaTime)
    {
        lifetime = System.currentTimeMillis() - timespawned;
        //setAlpha(100.0f - (lifetime / timeout * 100.f));    //Not working as intended yet.

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
            super.present(deltaTime, localCoord);
        }
    }

    //All powerups are added to the player's powerup list when acquired.
    public void acquire()
    {
        world.getPlayer().powerups.add(this);
    }

    public abstract void activate(int otherColorIndex);

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
}
