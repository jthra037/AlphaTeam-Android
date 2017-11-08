package alpha.nosleep.neonrush;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Pixmap;
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
    private int worldSize;
    private float worldWidth;
    private float worldHeight;
    private Player player;
    private Ball goal = null;
    private float gravity = -8;
    private Pixmap background;
    private List<Object> objects = new ArrayList<Object>();
    private List<Object> registryList = new ArrayList<Object>();
    private List<Object> deRegistryList = new ArrayList<Object>();
    private long score = 0;
    private long regTime = 0;

    public World(Game gm, Graphics graphics, int ws)
    {

        game = gm;
        g = graphics;
        worldSize = ws;
        worldWidth = worldSize * g.getWidth();
        worldHeight = worldSize * g.getHeight();

        background = g.newPixmap("newbackground.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(background, g.getWidth(), g.getHeight());

        player = new Player(this);
        v = new ViewableScreen(g);
        regTime = System.currentTimeMillis()/1000;

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
        for (int i = 0; i < objects.size() - 1; i++)
        {
            for(int j = i + 1; j < objects.size(); j++)
            {
                Object object = objects.get(i);
                Object other = objects.get(j);

                List<String> tags = Arrays.asList(object.tag, other.tag);

                if (!deRegistryList.contains(object) &&
                        !deRegistryList.contains(other) &&
                        object.getCollider().OnOverlap(other, object.getPosition()))
                {
                    Ball thisBall = (Ball)object;
                    if (object.tag == other.tag)
                    {
                        thisBall.Combine((Ball)other);
                    }
                    else if (tags.contains("Player") && tags.contains("Goal"))
                    {
                        player.Combine(notPlayer(object, other));
                    }
                    else if (tags.contains("Player") && tags.contains("Enemy"))
                    {
                        unregister(object);
                        unregister(other);
                        game.setGameState(Game.GAMESTATE.GameOver);
                    }
                }
            }
        }

        objects.addAll(registryList);
        registryList.removeAll(registryList);

        objects.removeAll(deRegistryList);
        deRegistryList.removeAll(deRegistryList);

        for (Object object : objects)
        {
            object.update(deltaTime);
            if (object == player)
            {
                score += (System.currentTimeMillis()/1000 - regTime) * player.getMass() * player.getMass();
                regTime = System.currentTimeMillis()/1000;
            }
        }

        if (!objects.contains(goal))
        {
            Random r = new Random();
            FTuple pos = new FTuple((float)r.nextInt((int)getWidth()),
                    (float)r.nextInt((int)getHeight()));
            goal = new Goal(this, 15, pos);
        }

        v.setPosition(player.position, deltaTime);

        System.out.println(".............. v.x: " + v.worldPosition.x);

        System.out.println(".............. v.y: " + v.worldPosition.y);
    }

    public void present(float deltaTime)
    {
        for (int i = 0; i < worldSize; i++)
        {
            for (int j = 0; j < worldSize; j++)
            {
                FTuple pos = new FTuple(i * g.getWidth(), j * g.getHeight());
                ITuple p = toLocalCoord(pos);
                background.setPosition(p.x, p.y);
                g.drawPixmap(background);
            }
        }

        for (Object object : objects)
        {
            object.present(deltaTime);
        }
    }

    private Ball notPlayer(Object one, Object two)
    {
        return one == player ? (Ball)two : (Ball)one;
    }

    //Change world coordinate to local on-screen coordinate.
    public ITuple toLocalCoord(FTuple worldCoord)
    {
        int x, y;

        if((getWidth() - v.worldPosition.x < v.viewSize.x) &&
                (worldCoord.x < (v.worldPosition.x + v.viewSize.x - getWidth())))
        {
            x = (int)(worldCoord.x + (getWidth() - v.worldPosition.x));
        }
        else
        {
            x = (int)(worldCoord.x - v.worldPosition.x);
        }

        if((getHeight() - v.worldPosition.y < v.viewSize.y) &&
                (worldCoord.y < (v.worldPosition.y + v.viewSize.y - getHeight())))
        {
            y = (int)(worldCoord.y + (getHeight() - v.worldPosition.y));
        }
        else
        {
            y = (int)(worldCoord.y - v.worldPosition.y);
        }

        ITuple local = new ITuple(x, y);
        return local;
    }

    public void register(Object object)
    {
        registryList.add(object);
    }
    public void unregister(Object object) {
        deRegistryList.add(object);
    }

    public Player getPlayer()
    { return player; }

    public String getScore()
    {
        return String.valueOf(score);
    }

    //This represents the viewport into the world that is visible on screen to the player.
    public class ViewableScreen
    {
        Graphics g;
        FTuple worldPosition;
        ITuple viewSize;
        int bufferSize = 50;
        int maxFollowDistance = 200;
        int camSpeedMaybe = 800;


        ViewableScreen(Graphics graphics)
        {
            g = graphics;
            viewSize = new ITuple(g.getWidth(), g.getHeight());
            worldPosition = new FTuple(player.position.x - viewSize.x / 2, player.position.y - viewSize.y / 2);
        }

        public void setPosition(FTuple p, float dt)
        {
            float follow = toLocalCoord(player.position).Add(-viewSize.x / 2, -viewSize.y / 2).Length() / maxFollowDistance;
            FTuple playerDisplacement = toLocalCoord(player.position).Add(-viewSize.x / 2, -viewSize.y / 2).Normalized().Mul(camSpeedMaybe);
            //worldPosition.x += playerDisplacement.x * 0.032 * follow;
            //worldPosition.y += playerDisplacement.y * 0.032 * follow;
            worldPosition.x += playerDisplacement.x * dt * follow;
            worldPosition.y += playerDisplacement.y * dt * follow;

            worldPosition.x %= getWidth();
            worldPosition.y %= getHeight();

            if(worldPosition.x < 0)
            {
                worldPosition.x = getWidth();
            }
            if (worldPosition.y < 0)
            {
                worldPosition.y = getHeight();
            }
        }
    }
}
