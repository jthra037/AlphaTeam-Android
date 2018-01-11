package nosleep.neonrush;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import nosleep.androidgames.framework.Audio;
import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Input;
import nosleep.androidgames.framework.Pixmap;
import nosleep.androidgames.framework.Screen;
import nosleep.androidgames.framework.Sound;
import nosleep.androidgames.framework.impl.AndroidMusic;
import nosleep.game.framework.Button;

/**
 * Created by John on 2017-10-10.
 * Trimmed by Mark on 2018-01-03.
 */

public class MainGameScreen extends Screen
{
    private World world;
    private SharedPreferences settings;
    public Graphics g;
    public Audio a;

    //UI.
    private Paint textPaint;
    private Paint bodyPaint;
    private Paint scorePaint;
    private Rect screenRect;
    private List<Button> buttons = new ArrayList<Button>();
    DecimalFormat df = new DecimalFormat("#"); //to format the score
    Pixmap toggleSFXButton;
    Pixmap toggleMusicButton;
    Pixmap unchecked;
    Pixmap checked;



    //Audio
    private Sound achievementUnlockSound;
    private Sound buttonSound;
    private Sound playSound;
    private boolean shouldSFXPlay;
    private boolean shouldMusicPlay;

    //Advertisement counters.
    private int adDisplayCount = 4;
    private int countBeforeAd;



    public MainGameScreen(final Game game)
    {
        super(game);
        g = game.getGraphics();
        a = game.getAudio();

        //Create the whole world.
        int worldSize = 4;
        world = new World(game, g, a, worldSize);
        settings = game.getSharedPreferences();

        //Create Audio
        achievementUnlockSound = a.newSound("Sounds/SFX/achievementunlocked.wav");
        buttonSound = a.newSound("Sounds/SFX/buttonsound.wav");
        playSound = a.newSound("Sounds/SFX/playsound.wav");
        shouldSFXPlay = settings.getBoolean("enableSFX", true);
        shouldMusicPlay = settings.getBoolean("enableMusic", true);

        //Create rect for pause / game over menu, and buttons.
        screenRect = new Rect(0,0,800,400);
        screenRect.set(g.getWidth()/2- screenRect.right/2,g.getHeight()/2-screenRect.bottom/2,screenRect.right,screenRect.bottom);
        unchecked = g.newPixmap("buttons/uncheckedblack.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(unchecked,100,100);
        checked = g.newPixmap("buttons/checkedblack.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(checked,100,100);
        createButtons();
        toggleSoundButtons();

        //Create the title font used in menus.
        Typeface tf = Typeface.createFromAsset(g.getAssets(),"fonts/antoniobold.ttf");
        textPaint = new Paint();
        textPaint.setTypeface(tf);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(100);

        //Create the body font used in game
        bodyPaint = new Paint();
        bodyPaint.setTypeface(tf);
        bodyPaint.setColor(Color.BLACK);
        bodyPaint.setTextSize(50);

        //Create the font used for in game score.
        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setTextSize(50);



        //Hiding the buttons after initialization so they don't show up immediately.
        buttons.get(1).hide(true);
        buttons.get(2).hide(true);
        buttons.get(3).hide(true);
        buttons.get(5).hide(true);
        buttons.get(6).hide(true);

        //Start the game.
        countBeforeAd = settings.getInt("adCount", 0);
        game.setGameState(Game.GAMESTATE.Play);
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
            else if (event.type == Input.TouchEvent.TOUCH_UP)
            {
                for (Button button : buttons)
                {
                    if(inBounds(event, button.getX(), button.getY(),
                            button.getWidth(), button.getHeight()))
                    {
                        button.onRelease();
                    }
                }
            }
        }

        //Cascade update down the hierarchy based on gamestate.
        switch(game.getGameState())
        {
            case Play:

                world.update(deltaTime);

                break;
            case Pause:


                break;
            case GameOver:

                if (adDisplayCount <= countBeforeAd)
                {
                    game.showInterstitialAd();
                    countBeforeAd = 0;
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("adCount", 0);
                    editor.commit();
                }
                break;
        }
    }

    @Override
    public void present(float deltaTime)
    {
        //Cascade present down the hierarchy based on gamestate.
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
                {
                    buttons.get(0).isClickable(false);
                }

                createPauseMenu();

                break;
            case GameOver:

                if (buttons.get(0).isClickable())
                {
                    buttons.get(0).isClickable(false);
                }

                createGameOverScreen();
                buttons.get(3).hide(false);
                buttons.get(2).hide(false);

                break;
        }

        //Draw all necessary buttons.
        for (Button button : buttons)
        {
            if(button.isHidden() == false && button.getImg() != null)
                g.drawPixmap(button.getImg(),button.getX(),button.getY());
        }

        //Draw the score and powerup count UI.
        g.drawText( df.format(world.getFScore()), g.getWidth()/2, 100, scorePaint);
        g.drawText(String.valueOf(world.getPlayer().PUColorphaseCount), 50, 100, scorePaint);
    }

    private void createButtons()
    {
        int butWidth = 200;
        int butHeight = 80;
        final int vibrateTime = 50;

        //Pause Button.
        Pixmap pauseButton = g.newPixmap("buttons/pausebutton.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(pauseButton,48,68);
        buttons.add(new Button(game,pauseButton, new Callable<Void>(){
            public Void call() {
                buttons.get(1).hide(false);
                buttons.get(2).hide(false);
                buttons.get(4).hide(true);
                buttons.get(5).hide(false);
                buttons.get(6).hide(false);
                game.setGameState(Game.GAMESTATE.Pause);
                if (shouldSFXPlay)
                {
                    buttonSound.play();
                }
                if (world.inGameMusic != null)
                {
                    world.inGameMusic.pause();
                }
                game.vibrateForInterval(vibrateTime);
                return null;
            }
        }, new Callable<Void>(){
            public Void call() {
                return null;
            }
        } ));
        buttons.get(0).resize(48,68);
        buttons.get(0).setPosition((g.getWidth() - pauseButton.getWidth()) - 40,20);

        //Resume play button from pause menu.
        Pixmap playButton = g.newPixmap("buttons/playbutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(playButton, butWidth, butHeight);
        buttons.add(new Button(game,playButton, new Callable<Void>(){
            public Void call() {
                buttons.get(1).hide(true);
                buttons.get(2).hide(true);
                buttons.get(4).hide(false);
                buttons.get(5).hide(true);
                buttons.get(6).hide(true);
                game.setGameState(Game.GAMESTATE.Play);
                if (shouldSFXPlay)
                {
                    buttonSound.play();
                }


                if (world.inGameMusic != null)
                {
                    if (shouldMusicPlay)
                    {
                        world.inGameMusic.play();
                    }
                    else
                    {
                        world.inGameMusic.pause();
                    }

                }
                game.vibrateForInterval(vibrateTime);
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(1).resize(butWidth, butHeight);
        buttons.get(1).setPosition((g.getWidth()/2 - playButton.getWidth()/2) ,g.getHeight()/2 - playButton.getHeight());

        //Quit button from pause menu and the game over menu.
        Pixmap quitButton = g.newPixmap("buttons/quitbutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(quitButton, butWidth, butHeight);
        buttons.add(new Button(game,quitButton, new Callable<Void>(){
            public Void call() {
                if (game.isSignedIn())
                {
                    game.submitScore((int)world.getLScore()); //when user quits the game, their score is submitted to be evaluated
                    if (shouldSFXPlay)
                    {
                        buttonSound.play();
                    }
                    if (world.inGameMusic != null)
                    {
                        world.inGameMusic.stop();
                    }
                    game.vibrateForInterval(vibrateTime);
                    milestonecheck(); //to check whether or not play has met criteria for unlocking achievements based on score

                }

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("adCount", countBeforeAd+=1);
                editor.commit();

                game.setScreen(new MainMenuScreen(game));

                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(2).resize(butWidth, butHeight);
        buttons.get(2).setPosition((g.getWidth()/2 - quitButton.getWidth()/2) ,g.getHeight()/2 + quitButton.getHeight());

        //Replay button from game over menu.
        Pixmap replayButton = g.newPixmap("buttons/replaybutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(replayButton, butWidth, butHeight);
        buttons.add(new Button(game,replayButton, new Callable<Void>(){
            public Void call() {
                if (game.isSignedIn())
                {
                    game.submitScore((int)world.getLScore()); //when user quits the game, their score is submitted to be evaluated
                    if (shouldSFXPlay)
                    {
                        playSound.play();
                    }
                    if (world.inGameMusic != null)
                    {
                        world.inGameMusic.stop();
                    }
                    game.vibrateForInterval(vibrateTime);
                    milestonecheck(); //to check whether or not play has met criteria for unlocking achievements based on score
                }

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("adCount", countBeforeAd+=1);
                editor.commit();

                game.setScreen(new MainGameScreen(game));
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(3).resize(butWidth, butHeight);
        buttons.get(3).setPosition((g.getWidth()/2 - replayButton.getWidth()/2) ,g.getHeight()/2 - replayButton.getHeight());

        //Create hitbox for powerup trigger. Invisible overlay. buttons.get(4)
        buttons.add(new Button(game, 0, 88, g.getWidth(), g.getHeight() - 88, new Callable<Void>() {
            public Void call()
            {
                world.getPlayer().PUTriggerActive = true;
                return null;
            }
        }, new Callable<Void>() {
            public Void call()
            {
                world.getPlayer().PUTriggerActive = false;
                return null;
            }
        } ));


        //Toggle SFX on Pause Menu
        toggleSFXButton = g.newPixmap("emptyimage.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(toggleSFXButton,100,100);
        buttons.add(new Button(game,toggleSFXButton, new Callable<Void>(){
            public Void call() {

                shouldSFXPlay = !shouldSFXPlay;
                if (shouldSFXPlay)
                {
                    buttonSound.play();
                }
                game.vibrateForInterval(50);
                toggleSFX(shouldSFXPlay);
                toggleCheckbox(toggleSFXButton,shouldSFXPlay);

                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(5).setPosition(g.getWidth()/2 - 325, g.getHeight()/2 );

        //Toggle Music on Pause Menu
        toggleMusicButton = g.newPixmap("emptyimage.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(toggleSFXButton,100,100);
        buttons.add(new Button(game,toggleMusicButton, new Callable<Void>(){
            public Void call() {

                shouldMusicPlay = !shouldMusicPlay;
                if (shouldSFXPlay)
                {
                    buttonSound.play();
                }
                game.vibrateForInterval(50);
                toggleMusic(shouldMusicPlay);
                toggleCheckbox(toggleMusicButton,shouldMusicPlay);

                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(6).setPosition(g.getWidth()/2 + 200, g.getHeight()/2);


    }

    private void createPauseMenu()
    {
        g.drawARGBRect(screenRect,255,255,255,255);
        g.drawText("PAUSED",g.getWidth()/2 - 130,g.getHeight()/2 - 100, textPaint);
        g.drawText("Toggle SFX", g.getWidth()/2 - 375, g.getHeight()/2 - 25,bodyPaint);
        g.drawText("Toggle Music", g.getWidth()/2 + 125, g.getHeight()/2 - 25,bodyPaint);
    }

    private void createGameOverScreen()
    {
        g.drawARGBRect(screenRect,255,255,0,0);
        g.drawText("You are Dead.",g.getWidth()/2 - 250,g.getHeight()/2 - 100, textPaint);
        if (world.inGameMusic != null)
        {
            world.inGameMusic.stop();
        }

    }

    private void milestonecheck()
    {
        //Milestones to check against for unlocking achievements.
        int milestone0 = 500;
        int milestone1 = 1000;
        int milestone2 = 2000;
        int milestone3 = 3000;
        int milestone4 = 4000;

        if (world.getLScore() >= milestone4) {
            game.unlockAchievement(R.string.a_score_4000);
            game.unlockAchievement(R.string.a_score_3000);
            game.unlockAchievement(R.string.a_score_2000);
            game.unlockAchievement(R.string.a_score_1000);
            game.unlockAchievement(R.string.a_score_500);
            if (shouldSFXPlay)
            {
                achievementUnlockSound.play(100);
            }
            Log.i("Achievement", "Unlocked a_4000!");
        }
        if (world.getLScore() >= milestone3 && world.getLScore() <= milestone4) {
            game.unlockAchievement(R.string.a_score_3000);
            game.unlockAchievement(R.string.a_score_2000);
            game.unlockAchievement(R.string.a_score_1000);
            game.unlockAchievement(R.string.a_score_500);
            if (shouldSFXPlay)
            {
                achievementUnlockSound.play(100);
            }
            Log.i("Achievement", "Unlocked a_3000!");
        }
        if (world.getLScore() >= milestone2 && world.getLScore() <= milestone3) {
            game.unlockAchievement(R.string.a_score_2000);
            game.unlockAchievement(R.string.a_score_1000);
            game.unlockAchievement(R.string.a_score_500);
            if (shouldSFXPlay)
            {
                achievementUnlockSound.play(100);
            }
            Log.i("Achievement", "Unlocked a_2000!");
        }
        if (world.getLScore() >= milestone1 && world.getLScore() <= milestone2) {
            game.unlockAchievement(R.string.a_score_1000);
            game.unlockAchievement(R.string.a_score_500);
            if (shouldSFXPlay)
            {
                achievementUnlockSound.play(100);
            }
            Log.i("Achievement", "Unlocked a_1000!");
        }
        if (world.getLScore() >= milestone0 && world.getLScore() <= milestone1) {
            game.unlockAchievement(R.string.a_score_500);
            if (shouldSFXPlay)
            {
                achievementUnlockSound.play(100);
            }
            Log.i("Achievement", "Unlocked a_500!");
        }
    }

    private void toggleSoundButtons()
    {
        if (shouldSFXPlay)
        {
            toggleSFXButton.setBitmap(checked.getBitmap());
        }
        else
        {
            toggleSFXButton.setBitmap(unchecked.getBitmap());
        }

        if (shouldMusicPlay)
        {
            toggleMusicButton.setBitmap(checked.getBitmap());
        }
        else
        {
            toggleMusicButton.setBitmap(unchecked.getBitmap());
        }
    }

    private void toggleSFX(boolean val)
    {
        SharedPreferences.Editor editor = settings.edit();
        //editor.clear();
        editor.putBoolean("enableSFX", val);
        editor.commit();
    }

    private void toggleMusic(boolean val)
    {
        SharedPreferences.Editor editor = settings.edit();
        //editor.clear();
        editor.putBoolean("enableMusic", val);
        editor.commit();
    }

    private void toggleCheckbox(Pixmap target, boolean val)
    {
        if(val)
        {
            Log.d("toggleCheckbox","bitmap to checked");
            if(checked.getBitmap() != null)
            {
                target.setBitmap(checked.getBitmap());
            }
        }
        else
        {
            Log.d("toggleCheckbox","bitmap to unchecked");
            if (unchecked.getBitmap() != null)
            {
                target.setBitmap(unchecked.getBitmap());
            }
        }
    }

    @Override
    public void focusChanged(boolean hasFocus)
    {
        if (!hasFocus && game.getGameState() != Game.GAMESTATE.GameOver)
        {
            game.setGameState(Game.GAMESTATE.Pause); //pause the game if user leaves the screen, or accidentally leaves the game
            buttons.get(1).hide(false);
            buttons.get(2).hide(false);
        }
    }

    @Override
    public void onBackButton()
    {
        game.setGameState(Game.GAMESTATE.Pause); //pause the game if user leaves the screen, or accidentally leaves the game
        buttons.get(1).hide(false);
        buttons.get(2).hide(false);
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
    public void restart()
    {

    }

    @Override
    public void dispose()
    {

    }
}
