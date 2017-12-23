package nosleep.game.framework;

import java.util.concurrent.Callable;

import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Pixmap;

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
    private Callable<Void> pressAction;
    private Callable<Void> releaseAction;

    //For buttons without an image / invisible buttons. (Powerup trigger)
    public Button(Game game, int xPos, int yPos, int width, int height, Callable<Void> pressAction, Callable<Void> relAction)
    {
        this.position = new ITuple(xPos, yPos);
        this.size = new ITuple(width, height);
        this.game = game;
        this.g = this.game.getGraphics();
        this.pressAction = pressAction;
        this.releaseAction = relAction;
    }

    //For buttons with an image.
    public Button(Game game,Pixmap img, Callable<Void> pressAction, Callable<Void> relAction)
    {
        this.img = img;
        this.position = new ITuple(0, 0);
        this.size = new ITuple(img.getWidth(), img.getHeight());
        this.game = game;
        this.g = this.game.getGraphics();
        this.pressAction = pressAction;
        this.releaseAction = relAction;
    }

    public boolean isClickable(){return clickable;}

    public void isClickable(boolean value) { this.clickable = value; }

    public boolean isHidden(){return hidden;}

    public void hide(boolean value){hidden = value;clickable = !value;}

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
                pressAction.call();
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

    public void onRelease()
    {
        try {
            if(clickable)
            {
                releaseAction.call();
            }
            else
            {
                //System.out.println("Not Clickable.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
