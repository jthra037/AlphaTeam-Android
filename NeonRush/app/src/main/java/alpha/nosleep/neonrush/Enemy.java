package alpha.nosleep.neonrush;

import java.util.Random;

/**
 * Created by John on 2017-10-19.
 */

public class Enemy extends Ball {

    public Enemy(World world, int radius) {
        super(world, radius);
        Random r = new Random();
        r.nextInt(world.Palette.length);
    }
}
