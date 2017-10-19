package alpha.nosleep.neonrush;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.game.framework.FTuple;
import alpha.nosleep.game.framework.ITuple;
import alpha.nosleep.game.framework.Object;

/**
 * Created by Mark- on 17-Oct-17.
 */

public class World
{
    public int[] Palette = {Color.CYAN, Color.GREEN,
        Color.MAGENTA, Color.RED,
        Color.BLUE, Color.YELLOW,
        Color.GRAY
    };
    public Game game;
    public Graphics g;
    private ViewableScreen v;
    private float worldWidth;
    private float worldHeight;
    private Player player;
    private float gravity = -8;
    private List<Object> objects = new ArrayList<Object>();

    public World(Game gm, Graphics graphics, float width, float height)
    {
        game = gm;
        g = graphics;
        worldWidth = width;
        worldHeight = height;

        player = new Player(this);
        v = new ViewableScreen(g);
        v.setPosition(player.position);

        new Enemy(this, 15);
    }

    public float getWidth()
    {
        return worldWidth;
    }

    public float getHeight()
    {
        return worldHeight;
    }

    public float getGravity() { return gravity; }

    public void update(float deltaTime)
    {
        //player.move(deltaTime);
        //player.update(deltaTime);
        for (Object object : objects)
        {
            object.update(deltaTime);
        }
        v.setPosition(player.position);
    }

    public void present(float deltaTime)
    {
        for (Object object : objects)
        {
            object.present(deltaTime);
        }
    }

    //Change world coordinate to local on-screen coordinate.
    public ITuple toLocalCoord(FTuple worldCoord)
    {
        int x = (int)worldCoord.x;
        int y = (int)worldCoord.y;

        ITuple local = new ITuple(x, y);
        return local;
    }

    public void register(Object object)
    {
        objects.add(object);
    }

    public Player getPlayer()
    { return player; }

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
            worldPosition = new FTuple(g.getWidth()/2, g.getHeight()/2);
        }

        public void setPosition(FTuple player)
        {
            worldPosition.x = player.x - (viewSize.x / 2) - bufferSize;
            worldPosition.y = player.y - (viewSize.y / 2) - bufferSize;
        }
    }
}
