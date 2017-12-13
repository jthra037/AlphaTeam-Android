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
    float mindistscalar = 100.0f;
    int maxIterations = 50;

    ///<Summary>
    /// density: scale value which modifies the minimum distance apart obstacles will spawn at.
    ///     Lower values accept denser randomization, large values are stricter and require more distance between obstacles.
    ///</Summary>
    LevelGenerator(World w, int density)
    {
        Random r = new Random();
        ITuple screenSize = new ITuple(w.g.getWidth(), w.g.getHeight());
        FTuple worldSize = new FTuple (w.getWidth(), w.getHeight());
        List<Obstacle> placedObstacles = new ArrayList<>();

        //Add a mock obstacle at player location for the purpose of not spawning proceeding obstacles nearby.
        //Size of slightly smaller than the screen size. Immediately deregister from world.
        placedObstacles.add(new ObRectangle(w.game,w,w.getPlayer().getWorldCoord(), new ITuple(screenSize.x - (screenSize.x / 10),screenSize.y - (screenSize.y / 10))));
        w.unregister(placedObstacles.get(0));
        System.out.println("Initial Player Obstacle: x: " + placedObstacles.get(0).position.x + " y: " + placedObstacles.get(0).position.y);

        int minObstacleCount = 15;
        int maxObstacleCount = 16;
        int rangeObstacleCount = maxObstacleCount - minObstacleCount;
        int minObSize = 50;
        int maxObSize = 151;
        int rangeObSize = maxObSize - minObSize;

        //r.nextInt produces an int between 0 inclusive and (n) EXCLUSIVE, hence the +1 on max arguments.
        int obstacleCount = r.nextInt(rangeObstacleCount) + minObstacleCount;
        System.out.println("Number of obstacles: " + obstacleCount);

        float mindist = density * mindistscalar;
        System.out.println("Minimum distance: " + mindist);

        //Place the determined amount of objects in the world.
        for (int i = 0; i < obstacleCount; i++)
        {
            System.out.println("Placed Obstacle Count: " + placedObstacles.size());
            FTuple pos = new FTuple(0.0f, 0.0f);
            boolean invalidPlacement = true;

            //If the randomized placement is invalid, regenerate a new one and test again.
            while(invalidPlacement)
            {
                //Randomize a new position.
                pos = new FTuple(r.nextFloat() * worldSize.x, r.nextFloat() * worldSize.y);
                invalidPlacement = false;

                //Check the distance to all other obstacles that have been placed.
                for (Obstacle ob : placedObstacles)
                {
                    //If the random position is too close to another obstacle, break and reattempt.
                    float distTo = pos.Distance(ob.position);
                    if (distTo < mindist)
                    {
                        invalidPlacement = true;
                        System.out.println("=== INVALID PLACEMENT, DistTo: " + distTo + " ===");
                        break;
                    }
                }
            }

            //Randomize the size (currently rectangles only) between min and max parameters.
            ITuple size = new ITuple(r.nextInt(rangeObSize) + minObSize, r.nextInt(rangeObSize) + minObSize);

            //Place the obstacle.
            placedObstacles.add(new ObRectangle(w.game, w, pos, size));
            System.out.println("Obstacle " + (i + 1) + "- Size x: " + size.x + ", Size y: " + size.y + ", Pos x: " + pos.x + ", Pos y: " + pos.y);
        }
    }
}
