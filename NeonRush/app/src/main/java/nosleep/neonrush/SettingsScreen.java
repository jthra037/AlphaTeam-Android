package nosleep.neonrush;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;

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
import nosleep.game.framework.Button;

/**
 * Created by John on 2017-10-10.
 */

public class SettingsScreen extends Screen {

    private Pixmap gameTitle;
    Graphics g;
    Audio a;
    Rect backGround;

    private Pixmap checked;
    private Pixmap unchecked;
    private Pixmap handHeldPlayButton;
    private Pixmap sfxButton;
    private Pixmap musicButton;
    private Pixmap backButton;
    private Sound buttonSound;
    private Paint titleTextPaint;
    private Paint bodyTextPaint;
    Typeface tf;
    SharedPreferences settings; //the location in which all future user settings will be stored
    private List<Button> buttons = new ArrayList<Button>();

    private boolean handheldPlay;
    private boolean enableSFX;
    private boolean enableMusic;


    public SettingsScreen (final Game game)
    {
        super(game);
        g = game.getGraphics();
        a = game.getAudio();
        backGround = new Rect(0,0,g.getWidth()+1,g.getHeight());

        buttonSound = a.newSound("Sounds/SFX/buttonsound.wav");

        settings = game.getSharedPreferences();
        handheldPlay = settings.getBoolean("handHeldPlay",false); //false is the value if shared preferences doesn't exist
        enableSFX = settings.getBoolean("enableSFX",true); //true is the value, if shared preferences doesn't exist
        enableMusic = settings.getBoolean("enableMusic", true);

        handHeldPlayButton = g.newPixmap("emptyimage.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(handHeldPlayButton,125,125);

        sfxButton = g.newPixmap("emptyimage.png", Graphics.PixmapFormat.ARGB4444);


        musicButton = g.newPixmap("emptyimage.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(musicButton,125,125);

        unchecked = g.newPixmap("buttons/unchecked.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(unchecked,125,125);

        checked = g.newPixmap("buttons/checked.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(checked,125,125);

        backButton = g.newPixmap("buttons/backbutton.png", Graphics.PixmapFormat.ARGB4444);

        if (handheldPlay)
            handHeldPlayButton.setBitmap(checked.getBitmap());
        else
            handHeldPlayButton.setBitmap(unchecked.getBitmap());

        if (enableSFX)
            sfxButton.setBitmap(checked.getBitmap());
        else
            sfxButton.setBitmap(unchecked.getBitmap());

        if (enableMusic)
            musicButton.setBitmap(checked.getBitmap());
        else
            musicButton.setBitmap(unchecked.getBitmap());



        buttons.add(new Button(game,handHeldPlayButton, new Callable<Void>(){
            public Void call() {
                handheldPlay = !handheldPlay;
                toggleHandHeldPlay(handheldPlay);
                toggleCheckbox(handHeldPlayButton,handheldPlay);
                if (enableSFX)
                {
                    buttonSound.play();
                }
                game.vibrateForInterval(50);
                //Log.d("Temp boolean", String.valueOf(settings.getBoolean("handHeldPlay",false)));
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(0).resize(125,125);
        buttons.get(0).setPosition(100,200);

        buttons.add(new Button(game,backButton, new Callable<Void>(){
            public Void call() {
                if (enableSFX)
                {
                    buttonSound.play();
                }
                game.vibrateForInterval(50);
                game.setScreen(new MainMenuScreen(game));
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(1).resize(100,100);
        buttons.get(1).setPosition(20, g.getHeight() - backButton.getHeight() - 20);

        buttons.add(new Button(game,sfxButton, new Callable<Void>(){
            public Void call() {
                enableSFX = !enableSFX;
                if (enableSFX)
                {
                    buttonSound.play();
                }
                game.vibrateForInterval(50);
                toggleSFX(enableSFX);
                toggleCheckbox(sfxButton,enableSFX);
                //Log.d("Temp boolean", String.valueOf(settings.getBoolean("handHeldPlay",false)));
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(2).resize(125,125);
        buttons.get(2).setPosition(100,350);

        buttons.add(new Button(game,musicButton, new Callable<Void>(){
            public Void call() {
                enableMusic = !enableMusic;
                if (enableSFX)
                {
                    buttonSound.play();
                }
                game.vibrateForInterval(50);
                toggleMusic(enableMusic);
                toggleCheckbox(musicButton,enableMusic);
                //Log.d("Temp boolean", String.valueOf(settings.getBoolean("handHeldPlay",false)));
                return null;
            }
        }, new Callable<Void>(){
            public Void call()
            {
                return null;
            }
        } ));
        buttons.get(3).resize(125,125);
        buttons.get(3).setPosition(100,500);


        setupTypefaces();//moved to a function so as not to clutter the constructor

    }




    @Override
    public void update(float deltaTime)
    {
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
            else if (event.type == KeyEvent.KEYCODE_BACK)
            {
                game.setScreen(new MainMenuScreen(game));
            }
        }

    }

    @Override
    public void focusChanged(boolean hasFocus) {
        if(!hasFocus) {
            game.setScreen(new MainMenuScreen(game));

        }
    }

    @Override
    public void onBackButton()
    {
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void present(float deltaTime)
    {
        g.drawRect(backGround, Color.BLACK );
        g.drawText("Settings",g.getWidth()/2 - 150, 125, titleTextPaint);

        g.drawText("Hand Held Play: Zeroes Accelerometer ",240,260, bodyTextPaint);
        g.drawText("based on hand position as apposed to laying flat",240,315, bodyTextPaint);

        g.drawText("Sound ",240,400, bodyTextPaint);
        g.drawText("SFX",260,455, bodyTextPaint);
        g.drawText("Music", 260, 605,bodyTextPaint);






        for (Button button : buttons)
        {
            g.drawPixmap(button.getImg(),button.getX(),button.getY());
        }

    }

    @Override
    public void pause() //what is called when you leave/close the app
    {
        toggleHandHeldPlay(handheldPlay);
        toggleSFX(enableSFX);
    }

    @Override
    public void resume() //what is called when you go back to the app

    {

    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void restart()
    {

    }

    @Override
    public void destroy()
    {

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

    private void toggleHandHeldPlay(boolean val)
    {
        SharedPreferences.Editor editor = settings.edit();
        //editor.clear();
        editor.putBoolean("handHeldPlay", val);
        editor.commit();//always has to be last in order for your preferences to be saved
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

    void setupTypefaces()
    {
        tf = Typeface.createFromAsset(g.getAssets(),"fonts/antoniobold.ttf");
        titleTextPaint = new Paint();
        titleTextPaint.setTypeface(tf);
        titleTextPaint.setColor(Color.WHITE);
        titleTextPaint.setTextSize(100);

        bodyTextPaint = new Paint();
        bodyTextPaint.setTypeface(tf);
        bodyTextPaint.setColor(Color.WHITE);
        bodyTextPaint.setTextSize(50);
    }

}
