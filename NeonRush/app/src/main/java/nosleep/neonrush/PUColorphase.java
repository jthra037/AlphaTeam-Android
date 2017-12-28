package nosleep.neonrush;

import nosleep.game.framework.FTuple;

/**
 * Created by Mark- on 23-Dec-17.
 */

public class PUColorphase extends Powerup
{
    private int duration = 10000; //10 Seconds.

    public PUColorphase(World w, FTuple pos)
    {
        super(w, pos);
        type = PUTYPE.Colorphase;
    }

    public void activate()
    {
        player.color = player.collision.otherColor;
        player.setPowerupTimer(duration);
    }
}
