package nosleep.neonrush;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import nosleep.androidgames.framework.Audio;
import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Music;
import nosleep.androidgames.framework.Pixmap;
import nosleep.androidgames.framework.Sound;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.ITuple;
import nosleep.game.framework.Object;

/**
 * Created by Mark- on 17-Oct-17.
 * Trimmed by Mark on 2018-01-03.
 */

public class World
{
    //
    //IT IS IMPORTANT THAT ALL PALETTES REMAIN IN THE SAME ORDER AS EACH OTHER.
    //

    //Basic color palette for primitives.
    int[] Palette = { Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED,
        Color.BLUE, Color.YELLOW, Color.GRAY };

    //Obstacle art palette.
    String[] obstaclePalette = {"obstacles/obstaclespatch9/cyan.9.png","obstacles/obstaclespatch9/green.9.png","obstacles/obstaclespatch9/magenta.9.png",
        "obstacles/obstaclespatch9/red.9.png","obstacles/obstaclespatch9/blue.9.png","obstacles/obstaclespatch9/yellow.9.png","obstacles/obstaclespatch9/grey.9.png"};

    //Enemy art palette.
    String[] enemyPalette = {"enemies/cyan.png","enemies/green.png","enemies/magenta.png",
            "enemies/red.png","enemies/blue.png","enemies/yellow.png","enemies/grey.png"};

    public Game game;
    public Audio a;
    public Graphics g;
    private ViewableScreen v;
    private SharedPreferences settings;

    //Audio
    private Sound goalPickupSound;
    private Sound powerupPickupSound;
    private Sound deathSound;
    private Sound playerEatsEnemySound;
    private boolean shouldSFXPlay;
    public Music inGameMusic;
    private boolean shouldMusicPlay;

    //World info.
    public int worldSize;
    private float worldWidth;
    private float worldHeight;
    private float gravity = -8;
    private LevelGenerator LevelGenny;
    static public float DT;

    //Gameplay objects.
    private Player player;
    private Ball goal = null;
    private DirectionalArrow dArrow;
    private List<Object> objects = new ArrayList<Object>();
    private Pixmap background;

    //Registry Lists.
    private List<Enemy> inactiveEnemies = new ArrayList<>();
    private List<Object> registryList = new ArrayList<Object>();
    private List<Object> deRegistryList = new ArrayList<Object>();
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Ball> balls = new ArrayList<>();
    private List<Powerup> powerups = new ArrayList<>();

    //Timer and score metrics.
    private float score = 0;
    private long lastEnemySpawn = 0;
    private long lastPuSpawn = 0;
    private Random r;

    // Debugging
    private long timer;

    public World(Game gm, Graphics graphics, Audio audio, int ws) {
        game = gm;
        g = graphics;
        a = audio;
        settings = game.getSharedPreferences();

        //Set world parameters.
        worldSize = ws;
        worldWidth = worldSize * g.getWidth();
        worldHeight = worldSize * g.getHeight();

        //Set background.
        background = g.newPixmap("newbackground.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(background, g.getWidth(), g.getHeight());

        //Set audio
        goalPickupSound = a.newSound("Sounds/SFX/goalpickup.wav");
        powerupPickupSound = a.newSound("Sounds/SFX/poweruppickup.wav");
        deathSound = a.newSound("Sounds/SFX/death.wav");
        playerEatsEnemySound = a.newSound("Sounds/SFX/playereatsenemy.wav");
        shouldSFXPlay = settings.getBoolean("enableSFX", true);
        inGameMusic = a.newMusic("Sounds/Music/ingame.mp3");
        shouldMusicPlay = settings.getBoolean("enableMusic", true);

        //Make the player, the viewport, and generate the level.
        player = new Player(this, "enemies/white.png");
        v = new ViewableScreen(g);
        LevelGenny = new LevelGenerator(this, 5);

        //Set the directional arrow.
        dArrow = new DirectionalArrow(this,new FTuple(g.getWidth()/2 - 63, g.getHeight()/2 - 33)); //hardcoded numbers are image width and height
        dArrow.setAlpha(25);

        //Set timer related systems.
        lastEnemySpawn = System.currentTimeMillis();
        lastPuSpawn = System.currentTimeMillis() - 20000;

        //Set the game music
        if (shouldMusicPlay)
        {
            inGameMusic.play();
            inGameMusic.setLooping(true);
        }

        // Make some enemies
        for(int i = 0; i < 30; i++)
        {
            spawnEnemies();
        }

        game.showBanner();//for ads
        r = new Random();

        //Debugging
        timer = System.currentTimeMillis();
    }

    public void update(float deltaTime)
    {
        //Cascade update to objects based on gamestate.
        DT = deltaTime;

        switch(game.getGameState())
        {
            case Play:


                //Add all objects to the registry that are in queue.
                objects.addAll(registryList);
                registryList.removeAll(registryList);

                //Remove all objects from the registry that are in queue.
                //objects.removeAll(deRegistryList);
                iterativelyClearList(objects);
                //balls.removeAll(deRegistryList);
                iterativelyClearList(balls);
                powerups.removeAll(deRegistryList);
                obstacles.removeAll(deRegistryList);
                deRegistryList.removeAll(deRegistryList);

                /*================::Timer Start::===============*/
                timer = System.currentTimeMillis(); // Timer start
                /*==============================================*/

                resolvePhysics();

                /*=======================::Timer End::==========================*/
                /*
                long timerResult = System.currentTimeMillis() - timer;
                System.out.println("ms: " + timerResult +
                        ", %dt: " + (deltaTime * 1000) +
                ", balls.size(): " + balls.size());
                */
                /*==============================================================*/


			    //Run update for all registered objects.
			    for (Object object : objects)
                {
                   object.update(deltaTime);

                   //Add score to the player.
                    if (object == player)
                    {
                        score += (deltaTime/2.5f) * player.getMass() * player.getMass();
                    }
                }

                placeGoal();
                activateEnemies();
                spawnPowerups();

                //Set the screen position to that of the player.
                v.setPosition(player.position, deltaTime);

			    break;
            case Pause:

                Log.i("gameState", "Paused!");

                break;
            case GameOver:

                break;
        }
    }

    public void present(float deltaTime)
    {
        //Cascade present to objects based on gamestate.
        switch(game.getGameState())
        {
            case Play:

                //Render the tiled background.
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


                //Draw Wrap Debug Lines.
                FTuple zero = new FTuple(0.0f, 0.0f);
                FTuple worldsize = new FTuple(getWidth(), getHeight());
                g.drawLine(toLocalCoord(zero).x, toLocalCoord(zero).y, toLocalCoord(worldsize).x, toLocalCoord(zero).y, Color.RED);
                g.drawLine(toLocalCoord(worldsize).x, toLocalCoord(zero).y, toLocalCoord(worldsize).x, toLocalCoord(worldsize).y, Color.RED);
                g.drawLine(toLocalCoord(worldsize).x, toLocalCoord(worldsize).y, toLocalCoord(zero).x, toLocalCoord(worldsize).y, Color.RED);
                g.drawLine(toLocalCoord(zero).x, toLocalCoord(worldsize).y, toLocalCoord(zero).x, toLocalCoord(zero).y, Color.RED);


                //Render all the objects.
                for (Object object : objects)
                {
                    object.present(deltaTime);
                }

                break;
            case Pause:

                break;
            case GameOver:

                break;
        }
    }

    //Handles all collision interactions.
    //CONTAINS A GAME OVER CONDITION.
    private void resolvePhysics()
    {
        new Thread(new Runnable() {
            public void run()
            {
                int maxDistSquared = 640000;

                // Check all ball on ball action
                for(int i = 0; i < balls.size() - 1; i++)
                {
                    for(int j = i + 1; j < balls.size(); j++)
                    {
                        Ball ballOne = balls.get(i);
                        Ball ballTwo = balls.get(j);
                        List<String> tags = Arrays.asList(ballOne.tag, ballTwo.tag);

                        //Only check collision on objects in proximity to eachother.
                        if (ballOne.getPosition().Sub(ballTwo.getPosition()).LengthS() < maxDistSquared &&
                                !deRegistryList.contains(ballOne) && // neither ball is about to be deregistered
                                !deRegistryList.contains(ballTwo) &&
                                ballOne.getCollider().OnOverlap(ballTwo, ballOne.getPosition())) // balls are touching
                        {
                            // Start checking tags to determine outcome of collision
                            if (ballOne.tag == ballTwo.tag)
                            {
                                ballOne.Combine(ballTwo);
                            }
                            else if (tags.contains("Player") && tags.contains("Goal"))
                            {
                                player.Combine(notPlayer(ballOne, ballTwo));
                                if (shouldSFXPlay)
                                {
                                    goalPickupSound.play();
                                }
                                game.vibrateForInterval(50);
                            }
                            else if (tags.contains("Player") && tags.contains("Enemy"))
                            {
                                if (ballOne.color == ballTwo.color)
                                {
                                    player.Combine(notPlayer(ballOne, ballTwo));
                                    if (shouldSFXPlay)
                                    {
                                        playerEatsEnemySound.play();
                                    }
                                    game.vibrateForInterval(50);
                                }
                                else
                                {
                                    unregister(ballOne);
                                    unregister(ballTwo);
                                    if (shouldSFXPlay)
                                    {
                                        deathSound.play();
                                    }
                                    game.setGameState(Game.GAMESTATE.GameOver);
                                }
                            }
                        }
                    }
                }

                //Check if overlapping any powerups.
                for (Powerup p : powerups)
                {
                    if(player.position.Sub(p.position).LengthS() < maxDistSquared &&
                            !deRegistryList.contains(p) &&
                            player.getCollider().OnOverlap(p, player.position))
                    {
                        p.acquire();
                        if(shouldSFXPlay)
                        {
                            powerupPickupSound.play();
                        }
                        unregister(p);
                    }
                }

                //Check ball on obstacle collisions.
                try {
                    for (Ball ball : balls)
                    {
                        for (Obstacle obstacle : obstacles)
                        {
                            //For all cases except the player.
                            if (ball.tag != "Player" &&
                                ball.tag != "Goal" &&
                                ball.getPosition().Sub(obstacle.getPosition()).LengthS() < maxDistSquared &&
                                ball.color != obstacle.color)
                            {
                                ball.CollisionCheck(obstacle);
                            }
                            //For the player, this accounts for colorphasing.
                            else if (ball.tag != "Goal" &&
                                    ball.getPosition().Sub(obstacle.getPosition()).LengthS() < maxDistSquared &&
                                    ball.color != obstacle.color)
                            {
                                ((Player)ball).phaseCheck(obstacle);
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.e("THREAD NULLREF ERROR", String.valueOf(e));
                }
            }
        }).start();
    }

    //If the player has reached the goal spawn a new one.
    private void placeGoal()
    {
        if (!objects.contains(goal))
        {
            FTuple pos = new FTuple(0.0f, 0.0f);
            int radius = 15;
            boolean inside = true;

            //Check against obstacle list to avoid spawning goals inside obstacles.
            while(inside)
            {
                inside = false;
                pos = new FTuple((float) r.nextInt((int) getWidth()), (float) r.nextInt((int) getHeight()));

                for(Obstacle ob : LevelGenny.placedObstacles)
                {
                    ObRectangle rect = (ObRectangle) ob;

                    if ((pos.x + radius) > (rect.position.x - rect.getSize().x) &&
                            (pos.x - radius) < (rect.position.x + rect.getSize().x) &&
                            (pos.y + radius) > (rect.position.y - rect.getSize().y) &&
                            (pos.y + radius) < (rect.position.y + rect.getSize().y))
                    {
                        inside = true;
                        Log.e("LvlGen Placement error","TRIED TO PLACE GOAL INSIDE OBSTACLE." );
                        break;
                    }
                }
            }

            goal = new Goal(this, radius, pos, "enemies/white.png");
        }
    }

    //Spawn enemies at appropriate time.
    private void spawnEnemies()
    {
        FTuple pos = new FTuple(0.0f, 0.0f);
        int radius = 10;
        new Enemy(this, radius, pos);
    }

    private void activateEnemies()
    {
        int enemySpawnWait = 20000;  //10 seconds.

        if (System.currentTimeMillis() > lastEnemySpawn + enemySpawnWait &&
                !inactiveEnemies.isEmpty())
        {
            int rand = r.nextInt(inactiveEnemies.size());

            lastEnemySpawn = System.currentTimeMillis();

            activate(inactiveEnemies.get(rand));
        }
    }

    //Spawn powerups at the appropriate time.
    private void spawnPowerups()
    {
        int puSpawnWait = 10000;    //20 seconds.
        int puTypeNum = 1;          //Number of different powerup types.

        if(System.currentTimeMillis() > lastPuSpawn + puSpawnWait)
        {
        FTuple pos = new FTuple(0.0f, 0.0f);
        int radius = 10;
        boolean inside = true;

        //Set the impromptu player obstacle to the location of the player so that enemies won't spawn on/near the player.
        ObRectangle playerOb = new ObRectangle(game, this, player.position, v.viewSize, 0);
        unregister(playerOb);
        LevelGenny.placedObstacles.set(0, playerOb);
        //Check against obstacle list to avoid spawning enemies inside obstacles.
        while(inside)
        {
            inside = false;
            pos = new FTuple((float) r.nextInt((int) getWidth()), (float) r.nextInt((int) getHeight()));

            for(Obstacle ob : LevelGenny.placedObstacles)
            {
                ObRectangle rect = (ObRectangle) ob;

                if ((pos.x + radius) > (rect.position.x - (rect.getSize().x / 2)) &&
                        (pos.x - radius) < (rect.position.x + (rect.getSize().x / 2)) &&
                        (pos.y + radius) > (rect.position.y - (rect.getSize().y / 2)) &&
                        (pos.y + radius) < (rect.position.y + (rect.getSize().y / 2)))
                {
                    inside = true;
                    System.out.println("TRIED TO PLACE POWERUP INSIDE OBSTACLE.");
                    break;
                }
            }
        }

        lastPuSpawn = System.currentTimeMillis();

        //Selects a random powerup type to spawn.
        //Value between 0 INCLUSIVE and number of different powerup types EXCLUSIVE.
        int whichPU = r.nextInt(puTypeNum);
        switch(whichPU)
        {
            case 0:
                PUColorphase newP = new PUColorphase(this, pos);
                break;
        }
    }
    }

    //Used to register and unregister objects to be updated, presented etc.
    public void register(Object object)
    {
        registryList.add(object);
        if (object instanceof Ball)
        {
            balls.add((Ball) object);
        }
        else if (object instanceof Obstacle)
        {
            obstacles.add((Obstacle) object);
        }
        else if (object instanceof Powerup)
        {
            powerups.add((Powerup) object);
        }
    }
    public void unregister(Object object)
    {
        deRegistryList.add(object);
    }

    public void activate(Enemy enemy)
    {
        int ctr = 0;
        FTuple pos = new FTuple((float) r.nextInt((int) getWidth()), (float) r.nextInt((int) getHeight()));

        while ((pos.Sub(player.getPosition())).LengthS() < 32000 && ctr++ < 3)
        {
            pos = new FTuple((float) r.nextInt((int) getWidth()), (float) r.nextInt((int) getHeight()));
        }

        enemy.position = pos;

        register(enemy);
        inactiveEnemies.remove(enemy);
    }

    public void deactivate(Enemy enemy)
    {
        inactiveEnemies.add(enemy);
        unregister(enemy);
    }

    private void iterativelyClearList( List listToRemoveFrom)
    {
        Iterator<Object> iter = listToRemoveFrom.iterator();
        while(iter.hasNext() )
        {
            if (deRegistryList.contains(iter.next()))
            {
                iter.remove();
            }
        }
    }

    //Getters.
    public float getWidth()
    {
        return worldWidth;
    }
    public float getHeight()
    {
        return worldHeight;
    }
    public float getGravity() { return gravity; }
    public Player getPlayer(){ return player; }
    public Ball getBall(){return goal;}
    public DirectionalArrow getdArrow(){return dArrow;}
    public String getScore()
    {
        return String.valueOf(score);
    }
    public long getLScore() {return (long)score;}
    public float getFScore(){return score;}

    //Compares two balls and returns the one that isn't the player.
    private Ball notPlayer(Object one, Object two)
    {
        return one == player ? (Ball)two : (Ball)one;
    }

    //Change world coordinate to local on-screen coordinate (FTuple to ITuple).
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

    //Returns true if the point is within the bounds of the world.
    public boolean IsValidPosition(FTuple point)
    {
        return point.x >= 0 && point.x <= worldWidth &&
                point.y >= 0 && point.y <= worldHeight;
    }

    //Modifies point to exist in world space, essential for wrapping.
    public void ConvertToWorldSpace(FTuple point)
    {
        if (IsValidPosition(point))
        {
            return;
        }

        if (point.x < 0)
        {
            point.x = worldWidth + point.x;
            ConvertToWorldSpace(point);
        }
        if (point.x > worldWidth)
        {
            point.x %= worldWidth;
        }
        if (point.y < 0)
        {
            point.y = worldHeight + point.y;
            ConvertToWorldSpace(point);
        }
        if (point.y > worldHeight)
        {
            point.y %= worldHeight;
        }
    }

    //This represents the viewport into the world that is visible on screen to the player.
    public class ViewableScreen
    {
        Graphics g;
        FTuple worldPosition;
        ITuple viewSize;
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
