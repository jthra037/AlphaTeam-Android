package alpha.nosleep.neonrush;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.game.framework.FTuple;

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
        v = new ViewableScreen(g);
        worldWidth = width;
        worldHeight = height;

        player = new Player(this);
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
    }

    public void draw()
    {
        player.draw();
    }

    //Change world coordinate to local on-screen coordinate.
    public float toLocalCoord(float xLocat, float yLocat)
    {


        return 0.0f;
    }

    //This represents the viewport into the world that is visible on screen to the player.
    public class ViewableScreen
    {
        Graphics g;
        FTuple viewSize;

        ViewableScreen(Graphics graphics)
        {
            g = graphics;
            viewSize = new FTuple(g.getWidth(), getHeight());
        }
    }
}
