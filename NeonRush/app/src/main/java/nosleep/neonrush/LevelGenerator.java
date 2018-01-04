package nosleep.neonrush;

import android.graphics.Color;
import android.util.Log;

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
    public List<Obstacle> placedObstacles;

    ///<Summary>
    /// density: Scale value which modifies the minimum distance apart obstacles will spawn at.
    ///     Higher values create a denser level with more obstacles and less distance between them.
    ///     Lower values create a sparcer level with less obstacles and more distance between them.
    ///</Summary>
    LevelGenerator(World w, int density)
    {
        Random r = new Random();
        ITuple screenSize = new ITuple(w.g.getWidth(), w.g.getHeight());
        FTuple worldSize = new FTuple (w.getWidth(), w.getHeight());
        placedObstacles = new ArrayList<>();
        int maxIterations = 50;

        //Add a mock obstacle at player location for the purpose of not spawning proceeding obstacles nearby.
        //Size of slightly smaller than the screen size. Immediately deregister from world.
        placedObstacles.add(new ObRectangle(w.game, w, w.getPlayer().getWorldCoord(), new ITuple(screenSize.x - (screenSize.x / 10),screenSize.y - (screenSize.y / 10)), "obstacles/white.png", Color.TRANSPARENT));
        w.unregister(placedObstacles.get(0));

        int minObstacleCount = w.worldSize * density * 2;
        int maxObstacleCount = (int)(minObstacleCount * 1.5f);
        int rangeObstacleCount = maxObstacleCount - minObstacleCount;
        int minObSize = w.worldSize * 25;
        int maxObSize = (minObSize * 2) + (density * 25);
        int rangeObSize = maxObSize - minObSize;

        //r.nextInt produces an int between 0 inclusive and (n) EXCLUSIVE, hence the +1 on max arguments.
        int obstacleCount = r.nextInt(rangeObstacleCount) + minObstacleCount;
        Log.i("Number of obstacles", "Number of obstacles: " + obstacleCount);

        float mindistscalar = w.worldSize * 100;
        float mindist = mindistscalar * (1.0f / (density * 0.2f));

        Log.i("Minimum Distance" , "Minimum distance: " + String.valueOf(mindist));

        //Place the determined amount of objects in the world.
        for (int i = 0; i < obstacleCount; i++)
        {
            System.out.println("Placed Obstacle Count: " + placedObstacles.size());
            FTuple pos = new FTuple(0.0f, 0.0f);
            boolean invalidPlacement = true;
            int iterations = 0;

            //If the randomized placement is invalid, regenerate a new one and test again.
            while(invalidPlacement)
            {
                //Randomize a new position.
                pos = new FTuple(r.nextFloat() * worldSize.x, r.nextFloat() * worldSize.y);
                invalidPlacement = false;

                //Check the distance to all other obstacles that have been placed.
                for (Obstacle ob : placedObstacles)
                {
                    //Increases density after enough iterations being unable to place an obstacle.
                    if (iterations > maxIterations)
                    {
                        iterations = 0;
                        density++;
                        mindist = mindistscalar * (1.0f / (density * 0.2f));
                        System.out.println("Density increased after " + maxIterations + " iterations without successful placement.");
                    }

                    //If the random position is too close to another obstacle, break and reattempt.
                    float distTo = pos.Distance(ob.position);
                    if (distTo < mindist)
                    {
                        invalidPlacement = true;
                        Log.e("LvlGen InvalidPlacement", "INVALID PLACEMENT, DistTo: " + distTo );
                        iterations++;
                        break;
                    }

                    iterations = 0;
                }
            }

            //Randomize the size (currently rectangles only) between min and max parameters.
            ITuple size = new ITuple(r.nextInt(rangeObSize) + minObSize, r.nextInt(rangeObSize) + minObSize);

            //Place the obstacle.
            int color = r.nextInt(w.Palette.length);
            placedObstacles.add(new ObRectangle(w.game, w, pos, size, w.obstaclePalette[color], color));
            Log.i("Obstacle Placement", "Obstacle " + (i + 1) + "- Size x: " + size.x +
                    ", Size y: " + size.y + ", Pos x: " + pos.x + ", Pos y: " + pos.y + ", Color: " + w.obstaclePalette[color]);
        }
    }
}
