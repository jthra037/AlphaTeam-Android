package nosleep.neonrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nosleep.game.framework.FTuple;
import nosleep.game.framework.ITuple;

/**
 * Created by Mark- on 09-Dec-17.
 */

public class LevelGenerator
{
    LevelGenerator(World w)
    {
        Random r = new Random();
        List<Obstacle> placedObstacles = new ArrayList<>();

        int minObstacleCount = 3;
        int maxObstacleCount = 11;
        int rangeObstacleCount = maxObstacleCount - minObstacleCount;
        int minObSize = 200;
        int maxObSize = 501;
        int rangeObSize = maxObSize - minObSize;

        //r.nextInt produces an int between 0 inclusive and (n) EXCLUSIVE, hence the +1 on max arguments.
        int obstacleCount = r.nextInt(rangeObstacleCount) + minObstacleCount;
        System.out.println("Number of obstacles: " + obstacleCount);

        for (int i = 0; i < obstacleCount; i++)
        {
            ITuple size = new ITuple(r.nextInt(rangeObSize) + minObSize, r.nextInt(rangeObSize) + minObSize);
            FTuple pos = new FTuple(r.nextFloat() * w.getWidth(), r.nextFloat() * w.getHeight());

            placedObstacles.add(new ObRectangle(w.game, w, pos, size));

            System.out.println("Obstacle " + i + "- Size x: " + size.x + ", Size y: " + size.y + ", Pos x: " + pos.x + ", Pos y: " + pos.y);
        }
    }
}
