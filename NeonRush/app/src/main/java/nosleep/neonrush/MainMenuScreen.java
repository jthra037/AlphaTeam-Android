package nosleep.neonrush;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import nosleep.androidgames.framework.Audio;
import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Input;
import nosleep.androidgames.framework.Pixmap;
import nosleep.androidgames.framework.Screen;
import nosleep.androidgames.framework.Sound;
import nosleep.androidgames.framework.impl.AndroidAudio;
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
    Audio a;
    SharedPreferences settings;
    private Random random;

    //UI
    private Pixmap gameTitle;
    private Pixmap backGround;
    private List<Button> buttons = new ArrayList<>();

    //Audio
    private Sound playSound;
    private Sound buttonSound;
    private boolean shouldSFXPlay;

    //Background Color Effect.
    private List<Goal> balls = new ArrayList<>();
    int[] palette = { Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED,
                        Color.BLUE, Color.YELLOW, Color.GRAY };

    public MainMenuScreen(final Game game)
    {
        super(game);
        g = game.getGraphics();
        a = game.getAudio();
        settings = game.getSharedPreferences();

        //Audio Loading
        shouldSFXPlay = settings.getBoolean("enableSFX", true);
        playSound = a.newSound("Sounds/SFX/playsound.wav");
        buttonSound = a.newSound("Sounds/SFX/buttonsound.wav");


        //Background Layer.
        backGround = g.newPixmap("mainscreen1.png", Graphics.PixmapFormat.ARGB4444);

        //Neon Rush Title.
        gameTitle = g.newPixmap("title1.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(gameTitle,700,500);
        gameTitle.setPosition(500,((g.getHeight()/2) - (gameTitle.getHeight()/2)));

        random = new Random();
        createButtons();
        spawnBalls();
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
                ball.menuUpdate(deltaTime);
            }
        }
    }

    @Override
    public void present(float deltaTime)
    {
        //Draw balls behind the background.
        for (Goal ball : balls)
        {
            if (!balls.isEmpty())
            {
                ball.menuPresent();
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
                if (shouldSFXPlay)
                {
                    playSound.play();
                }
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
                    if(shouldSFXPlay)
                    {
                        buttonSound.play();
                    }
                    game.showAchievements();
                }
                else
                {
                    Toast t = new Toast(game.getContext());
                    t.makeText(game.getContext(), "Not signed in", Toast.LENGTH_SHORT).show();
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
                if (shouldSFXPlay)
                {
                    buttonSound.play();
                }
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
                    if (shouldSFXPlay)
                    {
                        buttonSound.play();
                    }

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

            balls.add(new Goal(game, 50, new FTuple(IMath.getRandomInt(-ballSpeed,ballSpeed),IMath.getRandomInt(-ballSpeed,ballSpeed)), palette[random.nextInt(palette.length)]));
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
