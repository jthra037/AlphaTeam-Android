package alpha.nosleep.androidgames.framework;


import android.graphics.Bitmap;

import alpha.nosleep.androidgames.framework.Graphics.PixmapFormat;

public interface Pixmap 
{
    public int getWidth();
    public int getHeight();
    public void setWidth(int width);
    public void setHeight(int height);
    public int getX();
    public int getY();
    public void setPosition(int x, int y);
    public void setX(int x);
    public void setY(int y);
    public Bitmap getBitmap();
    public void setBitmap(Bitmap bitmap);
    public PixmapFormat getFormat();
    public void dispose();
}
