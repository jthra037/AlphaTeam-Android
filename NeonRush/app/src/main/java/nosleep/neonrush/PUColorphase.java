package nosleep.neonrush;

import nosleep.game.framework.FTuple;

/**
 * Created by Mark- on 23-Dec-17.
 */

public class PUColorphase extends Powerup
{
    public PUColorphase(World w, FTuple pos)
    {
        super(w, pos);
        type = PUTYPE.Colorphase;
        duration = 10000;
    }

    @Override
    public void acquire()
    {
        super.acquire();
        player.PUColorphaseCount++;
    }

    public void activate(int colorIndex)
    {
        player.color = player.collision.otherColor;
        player.setImg(world.enemyPalette[colorIndex]);
        player.setPowerupTimer(duration);
    }
}
