package nosleep.neonrush;

import nosleep.androidgames.framework.Game;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.ITuple;
import nosleep.game.framework.Object;

/**
 * Created by Mark- on 08-Nov-17.
 * Trimmed by Mark on 2018-01-03.
 */

public abstract class Obstacle extends Object
{
    protected World world;
    protected String imageRef;

    //To be used for dynamic obstacles.
    //protected boolean isDynamic = false;
    //protected Callable<Void> action;

    public Obstacle (Game game, World w, FTuple pos, String image, int col)
    {
        super(game);
        tag = "Obstacle";
        world = w;

        //Location Info.
        position = pos;
        localCoord = this.world.toLocalCoord(position);

        //Color Info.
        color = col;
        imageRef = image;

        w.register(this);
    }

    @Override
    public void update(float deltaTime)
    {
        localCoord = world.toLocalCoord(position);

        /*
        if(isDynamic)
        {
            try {
                action.call();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
    }

    @Override
    public void present(float deltaTime, ITuple pos)
    {
        super.present(deltaTime, pos);
    }
}
