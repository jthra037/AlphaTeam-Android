package alpha.nosleep.game.framework;

import java.util.concurrent.Callable;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.androidgames.framework.Screen;

/**
 * Created by John on 2017-10-11.
 */

public class Button {
    private Pixmap img;
    private ITuple position;
    private ITuple size;
    private Graphics g;
    private Game game;
    private boolean clickable = true;
    private boolean hidden = false;
    private Callable<Void> action;

    public Button(Game game,Pixmap img, int xPos, int yPos, int width, int height,
                  Callable<Void> action)
    {
        this.img = img;
        this.position = new ITuple(xPos, yPos);
        this.size = new ITuple(width, height);
        this.action = action;
        this.game = game;
        this.g = this.game.getGraphics();

    }

    public boolean isClickable(){return clickable;}

    public void isClickable(boolean value) { this.clickable = value; }

    public boolean isHidden(){return hidden;}

    public void hide(boolean value){hidden = value;clickable = !value;}

    public Button(Game game,Pixmap img, int xPos, int yPos,
                  Callable<Void> action)
    {
        this.img = img;
        this.position = new ITuple(xPos, yPos);
        this.size = new ITuple(img.getWidth(), img.getHeight());
        this.game = game;
        this.g = this.game.getGraphics();
        this.action = action;
    }

    public Button(Game game,Pixmap img, Callable<Void> action)
    {
        this.img = img;
        this.position = new ITuple(0, 0);
        this.size = new ITuple(img.getWidth(), img.getHeight());
        this.game = game;
        this.g = this.game.getGraphics();
        this.action = action;
    }

    public void resize(int newWidth, int newHeight)
    {
        this.g.resizePixmap(img,newWidth,newHeight);
        this.size = new ITuple(newWidth,newHeight);
    }

    public void setPosition(int x, int y)
    {
        this.img.setPosition(x,y);
        this.position = new ITuple(x,y);
    }


    public int getX(){ return this.position.x;}

    public int getY(){return this.position.y;}

    public int getWidth(){return this.size.x;}

    public int getHeight(){return this.size.y;}

    public ITuple getPosition() {return this.position;}

    public ITuple getSize(){return this.size;}

    public Pixmap getImg(){return this.img;}

    public Void onClick()
    {
        try {
            if(clickable)
            {
                action.call();
            }
            else
            {
                //System.out.println("Not Clickable.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
