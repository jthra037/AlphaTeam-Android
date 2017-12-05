package nosleep.neonrush;

import android.graphics.Color;

import java.util.concurrent.Callable;

import nosleep.androidgames.framework.Game;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.ITuple;
import nosleep.game.framework.Object;

/**
 * Created by Mark- on 08-Nov-17.
 */

public class Obstacle extends Object
{
    protected World w;
    protected boolean isDynamic = false;
    protected ITuple localCoord;
    protected int color = Color.WHITE;
    protected int maxSpeed = 0;
    protected Callable<Void> action;

    public Obstacle (Game game, World world, FTuple pos)
    {
        super(game);
        tag = "Obstacle";
        w = world;
        position = pos;
        localCoord = w.toLocalCoord(position);
        world.register(this);
    }

    @Override
    public void update(float deltaTime)
    {
        localCoord = w.toLocalCoord(position);
        if(isDynamic)
        {
            try {
                action.call();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void present(float deltaTime)
    {
        super.present(localCoord.x, localCoord.y, deltaTime);
    }
}
