package alpha.nosleep.neonrush;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.game.framework.FTuple;
import alpha.nosleep.game.framework.ITuple;

/**
 * Created by Mark- on 17-Oct-17.
 */

public class World
{
    public Game game;
    public Graphics g;
    private ViewableScreen v;
    private float worldWidth;
    private float worldHeight;
    private Player player;

    public World(Game gm, Graphics graphics, float width, float height)
    {
        game = gm;
        g = graphics;
        worldWidth = width;
        worldHeight = height;

        player = new Player(this);
        v = new ViewableScreen(g);
        v.setPosition(player.position);
    }

    public float getWidth()
    {
        return worldWidth;
    }

    public float getHeight()
    {
        return worldHeight;
    }

    public void update()
    {
        player.move();
        v.setPosition(player.position);
    }

    public void present(float deltaTime)
    {
        player.present(deltaTime);
    }

    //Change world coordinate to local on-screen coordinate.
    public ITuple toLocalCoord(FTuple worldCoord)
    {
        int x = (int)worldCoord.x;
        int y = (int)worldCoord.y;

        ITuple local = new ITuple(x, y);
        return local;
    }

    //This represents the viewport into the world that is visible on screen to the player.
    public class ViewableScreen
    {
        Graphics g;
        FTuple worldPosition;
        ITuple viewSize;
        int bufferSize = 50;


        ViewableScreen(Graphics graphics)
        {
            g = graphics;
            viewSize = new ITuple(g.getWidth(), g.getHeight());
        }

        public void setPosition(FTuple player)
        {
            worldPosition.x = player.x - (viewSize.x / 2) - bufferSize;
            worldPosition.y = player.y - (viewSize.y / 2) - bufferSize;
        }
    }
}
