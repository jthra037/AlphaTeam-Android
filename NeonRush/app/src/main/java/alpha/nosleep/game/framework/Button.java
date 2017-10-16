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
    private Callable<Void> action;

    public Button(Pixmap img, int xPos, int yPos, int width, int height,
                  Callable<Void> action)
    {
        this.img = img;
        this.position = new ITuple(xPos, yPos);
        this.size = new ITuple(width, height);
        this.action = action;
    }

    public Button(Pixmap img, int xPos, int yPos,
                  Callable<Void> action)
    {
        this.img = img;
        this.position = new ITuple(xPos, yPos);
        this.size = new ITuple(img.getWidth(), img.getHeight());
        this.action = action;
    }

    public Button(Pixmap img, Callable<Void> action)
    {
        this.img = img;
        this.position = new ITuple(img.getX(), img.getY());
        this.size = new ITuple(img.getWidth(), img.getHeight());
        this.action = action;
    }

    public Void click()
    {
        try {
            action.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
