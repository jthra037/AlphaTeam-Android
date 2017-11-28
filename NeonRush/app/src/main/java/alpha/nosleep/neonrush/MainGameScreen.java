package alpha.nosleep.neonrush;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Input;
import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.androidgames.framework.Screen;
import alpha.nosleep.game.framework.Button;
import alpha.nosleep.game.framework.FTuple;

/**
 * Created by John on 2017-10-10.
 */

public class MainGameScreen extends Screen {


    private World world;
    private List<Button> buttons = new ArrayList<Button>();
    Graphics g;
    Typeface tf;
    Paint textPaint;
    private Rect screenRect;
    private Pixmap pauseButton;
    private Pixmap playButton;
    private Pixmap quitButton;
    private Pixmap replayButton;
    private int worldSize = 10;
    private int spawnWait = 2000;
    private long lastSpawn = 0;
    private Random random;
    SharedPreferences settings;


    public MainGameScreen(final Game game)
    {
        super(game);
        g = game.getGraphics();
        world = new World(game, g, worldSize);

        settings = game.getSharedPreferences();

        pauseButton = g.newPixmap("buttons/pausebutton.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(pauseButton,48,68);

        buttons.add(new Button(game,pauseButton, new Callable<Void>(){
            public Void call() {

                game.setGameState(Game.GAMESTATE.Pause);
                buttons.get(1).hide(false);
                buttons.get(2).hide(false);
                return null;
            }
        }
        ));
        buttons.get(0).resize(48,68);
        buttons.get(0).setPosition((g.getWidth() - pauseButton.getWidth()) - 40,20);

        screenRect = new Rect(0,0,800,400);
        screenRect.set(g.getWidth()/2- screenRect.right/2,g.getHeight()/2-screenRect.bottom/2,screenRect.right,screenRect.bottom);



        playButton = g.newPixmap("buttons/playbutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(playButton,200,80);

        quitButton = g.newPixmap("buttons/quitbutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(quitButton,200,80);

        replayButton = g.newPixmap("buttons/replaybutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(replayButton,200,80);

        buttons.add(new Button(game,playButton, new Callable<Void>(){
            public Void call() {
                buttons.get(1).hide(true);
                buttons.get(2).hide(true);
                game.setGameState(Game.GAMESTATE.Play);


                return null;
            }
        }
        ));
        buttons.get(1).resize(200,80);
        buttons.get(1).setPosition((g.getWidth()/2 - playButton.getWidth()/2) ,g.getHeight()/2 - playButton.getHeight());

        buttons.add(new Button(game,quitButton, new Callable<Void>(){
            public Void call() {

                game.setScreen(new MainMenuScreen(game));

                return null;
            }
        }
        ));
        buttons.get(2).resize(200,80);
        buttons.get(2).setPosition((g.getWidth()/2 - quitButton.getWidth()/2) ,g.getHeight()/2 + quitButton.getHeight());

        buttons.add(new Button(game,replayButton, new Callable<Void>(){
            public Void call() {

                game.setScreen(new MainGameScreen(game));
                return null;
            }
        }
        ));
        buttons.get(3).resize(200,80);
        buttons.get(3).setPosition((g.getWidth()/2 - replayButton.getWidth()/2) ,g.getHeight()/2 - replayButton.getHeight());


        buttons.get(1).hide(true); //hiding the buttons after initialization so they don't show up immediately
        buttons.get(2).hide(true);
        buttons.get(3).hide(true);
        game.setGameState(Game.GAMESTATE.Play);

        tf = Typeface.createFromAsset(g.getAssets(),"fonts/antoniobold.ttf");
        textPaint = new Paint();
        textPaint.setTypeface(tf);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(100);

        lastSpawn = System.currentTimeMillis();
        random = new Random();
    }

    @Override
    public void update(float deltaTime)
    {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                for (Button button : buttons)
                {
                    if(inBounds(event, button.getX(), button.getY(),
                            button.getWidth(), button.getHeight()))
                    {
                        button.onClick();
                    }
                }
            }
        }

        switch(game.getGameState())
        {
            case Play:

                world.update(deltaTime);

                if (System.currentTimeMillis() > lastSpawn + spawnWait)
                {
                    int x = 0;
                    int y = 0;
                    switch (random.nextInt(4))
                    {
                        case 0:
                            x = 0;
                            y = random.nextInt((int)world.getHeight());
                            break;
                        case 1:
                            x = (int)world.getWidth();
                            y = random.nextInt((int)world.getHeight());
                            break;
                        case 2:
                            y = 0;
                            x = random.nextInt((int)world.getWidth());
                            break;
                        case 3:
                            y = (int)world.getHeight();
                            x = random.nextInt((int)world.getWidth());
                            break;
                    }
                    lastSpawn = System.currentTimeMillis() + spawnWait;
                    new Enemy(world, 10, new FTuple(x, y));
                }

                break;
            case Pause:



                break;
            case GameOver:
                break;
        }


    }

    @Override
    public void present(float deltaTime)
    {
        switch(game.getGameState())
        {
            case Play:

                if (!buttons.get(0).isClickable())
                    buttons.get(0).isClickable(true);
                world.present(deltaTime);

                world.getdArrow().rotateToPoint(world.getPlayer().getPosition(),world.getBall().getPosition(),250*deltaTime);








                break;
            case Pause:
                if (buttons.get(0).isClickable())
                    buttons.get(0).isClickable(false);

                //world.present(deltaTime);
                createPauseMenu();

                break;
            case GameOver:
                if (buttons.get(0).isClickable()) //don't want the player to be able to pause the game when they're dead...
                    buttons.get(0).isClickable(false);

                createGameOverScreen();
                buttons.get(3).hide(false);
                buttons.get(2).hide(false);

                break;
        }

        for (Button button : buttons)
        {
            if(button.isHidden() == false)
                g.drawPixmap(button.getImg(),button.getX(),button.getY());
        }

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
        g.drawText(world.getScore(), g.getWidth()/2, 100, paint);

    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void destroy()
    {

    }

    @Override
    public void focusChanged(boolean hasFocus)
    {
        if (!hasFocus)
        {
            game.setGameState(Game.GAMESTATE.Pause); //pause the game if user leaves the screen, or accidentally leaves the game
            buttons.get(1).hide(false);
            buttons.get(2).hide(false);
        }
    }

    @Override
    public void restart()
    {
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void onBackButton()
    {
        game.setGameState(Game.GAMESTATE.Pause); //pause the game if user leaves the screen, or accidentally leaves the game
        buttons.get(1).hide(false);
        buttons.get(2).hide(false);
    }

    private void removeFromList(int i)
    {
        buttons.remove(i);
    }

    private void createPauseMenu()
    {
        g.drawARGBRect(screenRect,255,255,255,255);
        g.drawText("PAUSED",g.getWidth()/2 - 130,g.getHeight()/2 - 100, textPaint);
    }

    private void createGameOverScreen()
    {
        g.drawARGBRect(screenRect,255,255,0,0);
        g.drawText("You are Dead.",g.getWidth()/2 - 250,g.getHeight()/2 - 100, textPaint);
    }



}
