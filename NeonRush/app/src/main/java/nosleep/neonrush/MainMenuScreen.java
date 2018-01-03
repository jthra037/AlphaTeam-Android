package nosleep.neonrush;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Input;
import nosleep.androidgames.framework.Pixmap;
import nosleep.androidgames.framework.Screen;
import nosleep.game.framework.Button;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.IMath;

/**
 * Created by John on 2017-10-10.
 * Trimmed by Mark on 2018-01-03.
 */

public class MainMenuScreen extends Screen
{
    Graphics g;
    private Random random;

    //UI
    private Pixmap gameTitle;
    private Pixmap backGround;
    private List<Button> buttons = new ArrayList<Button>();

    //Background Color Effect.
    private List<Ball> balls = new ArrayList<Ball>();
    private String[] enemyPalette = {"enemies/cyan.png","enemies/green.png","enemies/magenta.png",
            "enemies/red.png","enemies/blue.png","enemies/yellow.png","enemies/grey.png"};

    public MainMenuScreen(final Game game)
    {
        super(game);
        g = game.getGraphics();

        //Background Layer.
        backGround = g.newPixmap("mainscreen1.png", Graphics.PixmapFormat.ARGB4444);

        //Neon Rush Title.
        gameTitle = g.newPixmap("title1.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(gameTitle,700,500);
        gameTitle.setPosition(500,((g.getHeight()/2) - (gameTitle.getHeight()/2)));

        createButtons();
        spawnBalls();
        random = new Random();
    }

    @Override
    public void update(float deltaTime)
    {
        //Recognize and resolve touch events.
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_DOWN)
            {
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

        //Move the balls in the background (Color Effect).
        for (Ball ball : balls)
        {
            if (!balls.isEmpty())
            {
                ball.update(deltaTime);
            }
        }
    }

    @Override
    public void present(float deltaTime)
    {
        //Draw balls behind the background.
        for (Ball ball : balls)
        {
            if (!balls.isEmpty())
            {
                ball.present(deltaTime);
            }
        }

        //Draw background and title.
        g.drawPixmap(backGround,-1,0);
        g.drawPixmap(gameTitle);

        //Draw buttons on top.
        for (Button button : buttons)
        {
            if (!buttons.isEmpty())
            {
                g.drawPixmap(button.getImg(), button.getX(), button.getY());
            }
        }
    }

    private void createButtons()
    {
        int butTop = 155;
        int butLeft = 100;
        int butSpacing = 130;
        int butWidth = 340;
        int butHeight = 100;
        final int vibrateTime = 100;

        //Play Button.
        Pixmap playButton = g.newPixmap("buttons/playbutton.png", Graphics.PixmapFormat.RGB565);
        buttons.add(new Button(game,playButton, new Callable<Void>(){
            public Void call() {
                game.vibrateForInterval(vibrateTime);
                game.setScreen(new MainGameScreen(game));
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(0).resize(butWidth, butHeight);
        buttons.get(0).setPosition(butLeft, butTop);

        //Achievements Button.
        Pixmap achievementsButton = g.newPixmap("buttons/achievementsbutton.png", Graphics.PixmapFormat.RGB565);
        buttons.add(new Button(game,achievementsButton, new Callable<Void>(){
            public Void call() {
                game.vibrateForInterval(vibrateTime);
                if (game.isSignedIn())
                {
                    game.showAchievements();
                }
                else
                {
                    Toast t = new Toast(game.getContext());
                    t.makeText(game.getContext(), "Not sogned in", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(1).resize(butWidth, butHeight);
        buttons.get(1).setPosition(butLeft,butTop + butSpacing);

        //Settings Button.
        Pixmap settingsButton = g.newPixmap("buttons/settingsbutton.png", Graphics.PixmapFormat.RGB565);
        buttons.add(new Button(game,settingsButton, new Callable<Void>(){
            public Void call() {
                game.vibrateForInterval(vibrateTime);
                game.setScreen(new SettingsScreen(game));
                game.showBanner();
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(2).resize(butWidth,butHeight);
        buttons.get(2).setPosition(butLeft, butTop + (2 * butSpacing));

        //Leaderboards Button/
        Pixmap leaderboardsButton = g.newPixmap("buttons/leaderboardsbutton.png", Graphics.PixmapFormat.RGB565);
        buttons.add(new Button(game,leaderboardsButton, new Callable<Void>(){
            public Void call() {
                game.vibrateForInterval(vibrateTime);
                if (game.isSignedIn())
                {
                    game.showLeaderboard();
                }
                else
                {
                    Toast t = new Toast(game.getContext());
                    t.makeText(game.getContext(), "Not sogned in", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(3).resize(butWidth,butHeight);
        buttons.get(3).setPosition(butLeft, butTop + (3 * butSpacing));
    }

    private void spawnBalls()
    {
        int ballSpeed = 10;

        for (int j = 0; j < 20; j++)
        {
            int x = 0;
            int y = 0;
            switch (random.nextInt(4))
            {
                case 0:
                    x = 0;
                    y = random.nextInt(g.getHeight() - 50);
                    break;
                case 1:
                    x = g.getWidth();
                    y = random.nextInt(g.getHeight() - 50);
                    break;
                case 2:
                    y = 0;
                    x = random.nextInt(g.getWidth() - 50);
                    break;
                case 3:
                    y = g.getHeight();
                    x = random.nextInt(g.getWidth() - 50);
                    break;
            }

            balls.add(new Ball(game,50, new FTuple(IMath.getRandomInt(-ballSpeed,ballSpeed),IMath.getRandomInt(-ballSpeed,ballSpeed)), enemyPalette[random.nextInt(enemyPalette.length)]));
            balls.get(j).setVelocity(new FTuple(x,y));
        }
    }

    @Override
    public void onBackButton()
    {
        Activity a = game.getActivity();
        a.finish();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void focusChanged(boolean hasFocus)
    {

    }

    @Override
    public void destroy()
    {

    }

    @Override
    public void restart()
    {

    }

    @Override
    public void dispose() {

    }
}
