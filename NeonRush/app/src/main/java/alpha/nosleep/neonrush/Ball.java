package alpha.nosleep.neonrush;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.game.framework.IPhysics;
import alpha.nosleep.game.framework.Object;

/**
 * Created by John on 2017-10-17.
 */

public class Ball extends Object implements IPhysics{

    private int radius = 5;


    public Ball(Game game, int radius)
    {
        super(game);
        this.radius = radius;


    }

    @Override
    public void update(float deltaTime) {

    }
}
