package alpha.nosleep.neonrush;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

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

public class SettingsScreen extends Screen {

    private Pixmap gameTitle;
    Graphics g;
    Rect backGround;

    private Pixmap checked;
    private Pixmap unchecked;
    private Pixmap handHeldPlayButton;
    private Paint textPaint;
    Typeface tf;
    SharedPreferences settings; //the location in which all future user settings will be stored
    private List<Button> buttons = new ArrayList<Button>();

    private boolean handheldPlay;


    public SettingsScreen (final Game game)
    {
        super(game);
        g = game.getGraphics();
        backGround = new Rect(0,0,g.getWidth()+1,g.getHeight());

        handHeldPlayButton = g.newPixmap("emptyimage.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(handHeldPlayButton,125,125);

        settings = game.getSharedPreferences();
        handheldPlay = settings.getBoolean("handHeldPlay",false); //false is the value if shared preferences doesn't exist

        unchecked = g.newPixmap("buttons/unchecked.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(unchecked,125,125);

        checked = g.newPixmap("buttons/checked.png", Graphics.PixmapFormat.ARGB4444);
        g.resizePixmap(checked,125,125);

        if (handheldPlay)
            handHeldPlayButton.setBitmap(checked.getBitmap());
        else
            handHeldPlayButton.setBitmap(unchecked.getBitmap());



        buttons.add(new Button(game,handHeldPlayButton, new Callable<Void>(){
            public Void call() {
                handheldPlay = !handheldPlay;
                toggleHandHeldPlay(handheldPlay);
                toggleCheckbox(handHeldPlayButton,handheldPlay);
                Log.d("Temp boolean", String.valueOf(settings.getBoolean("handHeldPlay",false)));
                return null;
            }
        }
        ));

        buttons.get(0).resize(125,125);
        buttons.get(0).setPosition(200,200);

        tf = Typeface.createFromAsset(g.getAssets(),"fonts/antoniobold.ttf");
        textPaint = new Paint();
        textPaint.setTypeface(tf);
        textPaint.setColor(Color.WHITE);
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

    }

    @Override
    public void present(float deltaTime)
    {
        g.drawRect(backGround, Color.BLACK );
        g.drawText("Settings",g.getWidth()/2 - 150, 125,textPaint);
        for (Button button : buttons)
        {
            g.drawPixmap(button.getImg(),button.getX(),button.getY());
        }

    }

    @Override
    public void pause() {
        toggleHandHeldPlay(handheldPlay);
    }

    @Override
    public void resume() {
        Log.i("state","gained focus");
    }

    @Override
    public void dispose()
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
        editor.clear();
        editor.putBoolean("handHeldPlay", val);
        editor.commit();//always has to be last in order for your preferences to be saved
    }

}
