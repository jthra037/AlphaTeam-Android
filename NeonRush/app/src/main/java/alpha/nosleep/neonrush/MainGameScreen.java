package alpha.nosleep.neonrush;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Input;
import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.androidgames.framework.Screen;
import alpha.nosleep.game.framework.Button;

/**
 * Created by John on 2017-10-10.
 */

public class MainGameScreen extends Screen {
    public static enum GAMESTATE {Play,Pause}
    GAMESTATE gamestate;

    private World world;
    private List<Button> buttons = new ArrayList<Button>();
    Graphics g;
    Typeface tf;
    Paint textPaint;
    private Rect screenRect;
    private Pixmap background;
    private Pixmap pauseButton;
    private Pixmap playButton;
    private Pixmap quitButton;


    public MainGameScreen(final Game game)
    {
        super(game);
        g = game.getGraphics();
        world = new World(game, g, 100.0f, 100.0f);

        background = g.newPixmap("gameui.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(background, g.getWidth(), g.getHeight());

        pauseButton = g.newPixmap("buttons/pausebutton.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(pauseButton,48,68);

        buttons.add(new Button(game,pauseButton, new Callable<Void>(){
            public Void call() {

                gamestate = GAMESTATE.Pause;
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

        buttons.add(new Button(game,playButton, new Callable<Void>(){
            public Void call() {
                buttons.get(1).hide(true);
                buttons.get(2).hide(true);
                gamestate = GAMESTATE.Play;


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

        buttons.get(1).hide(true); //hiding the buttons after initialization so they don't show up immediately
        buttons.get(2).hide(true);
        gamestate = GAMESTATE.Play;

        tf = Typeface.createFromAsset(g.getAssets(),"fonts/antoniobold.ttf");
        textPaint = new Paint();
        textPaint.setTypeface(tf);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(100);


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

        world.update();
    }

    @Override
    public void present(float deltaTime)
    {



        switch(gamestate)
        {
            case Play:

                if (!buttons.get(0).isClickable())
                    buttons.get(0).isClickable(true);
                g.drawPixmap(background);
                world.present(deltaTime);

                break;
            case Pause:
                if (buttons.get(0).isClickable())
                    buttons.get(0).isClickable(false);

                world.present(deltaTime);
                createPauseMenu();

                break;
        }

        for (Button button : buttons)
        {
            if(button.isHidden() == false)
                g.drawPixmap(button.getImg(),button.getX(),button.getY());
        }


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

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



}
