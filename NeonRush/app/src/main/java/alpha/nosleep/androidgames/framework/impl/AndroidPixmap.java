package alpha.nosleep.androidgames.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Paint;

import alpha.nosleep.androidgames.framework.Graphics.PixmapFormat;
import alpha.nosleep.androidgames.framework.Pixmap;

public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    PixmapFormat format;
    int x;
    int y;
    
    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getX(){return (x);}

    @Override
    public int getY(){ return (y);}

    @Override
    public void setPosition(int x, int y){this.x = x; this.y = y;}

    @Override
    public void setX(int x) {this.x = x;}

    @Override
    public void setY(int y){this.y = y;}

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public PixmapFormat getFormat() {
        return format;
    }

    @Override
    public void setWidth(int width){ bitmap.setWidth(width);}

    @Override
    public void setHeight(int height){bitmap.setHeight(height);}

    @Override
    public Bitmap getBitmap(){return this.bitmap;}

    @Override
    public void setBitmap(Bitmap bitmap){ this.bitmap = bitmap;}


    @Override
    public void dispose() {
        bitmap.recycle();
    }      
}


