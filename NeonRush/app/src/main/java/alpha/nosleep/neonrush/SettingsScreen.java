package alpha.nosleep.neonrush;

import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

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
    private List<Button> buttons = new ArrayList<Button>();


    public SettingsScreen (Game game)
    {
        super(game);

        g = game.getGraphics();
        backGround = new Rect(0,0,g.getWidth()+1,g.getHeight());
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
        for (Button button : buttons)
        {
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
}
