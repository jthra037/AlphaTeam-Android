package alpha.nosleep.neonrush;

import android.graphics.Color;

import alpha.nosleep.game.framework.FTuple;

/**
 * Created by John on 10/19/2017.
 */

public class Goal extends Ball {
    public Goal(World world, int radius, FTuple position) {
        super(world, radius);
        this.tag = "Goal";
        this.position = position;
        this.color = Color.WHITE;
    }
}
