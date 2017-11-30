package nosleep.androidgames.framework;


import android.graphics.Bitmap;

import nosleep.androidgames.framework.Graphics.PixmapFormat;
import nosleep.game.framework.FTuple;

public interface Pixmap 
{
    public int getWidth();
    public int getHeight();
    public void setWidth(int width);
    public void setHeight(int height);
    public int getX();
    public int getY();
    public void setPosition(int x, int y);
    public void setPosition(FTuple in);
    public FTuple getPosition();
    public void setX(int x);
    public void setY(int y);
    public Bitmap getBitmap();
    public void setBitmap(Bitmap bitmap);
    public PixmapFormat getFormat();
    public void dispose();
    public float getRotation();
    public void setRotation(float val);
}

