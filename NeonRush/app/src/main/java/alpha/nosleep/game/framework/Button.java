package alpha.nosleep.game.framework;

import java.util.concurrent.Callable;

import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.androidgames.framework.Screen;

/**
 * Created by John on 2017-10-11.
 */

public class Button {
    public Pixmap img;
    public ITuple position;
    public ITuple size;
    private Callable<Screen> screen;

    public Button(Pixmap img, int xPos, int yPos, int width, int height,
                  Callable<Screen> screen)
    {
        this.img = img;
        this.position = new ITuple(xPos, yPos);
        this.size = new ITuple(width, height);
        this.screen = screen;
    }

    public Screen click()
    {
        try {
            return screen.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
