package nosleep.androidgames.framework.impl;

import android.graphics.Bitmap;

import nosleep.androidgames.framework.Graphics.PixmapFormat;
import nosleep.androidgames.framework.Pixmap;
import nosleep.game.framework.FTuple;

public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    PixmapFormat format;
    float rotation;
    int x;
    int y;
    
    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
        this.rotation = 0.0f;
    }

    @Override
    public int getX(){return (x);}

    @Override
    public int getY(){ return (y);}

    @Override
    public void setPosition(int x, int y){this.x = x; this.y = y;}

    @Override
    public void setPosition(FTuple in){this.x = (int)in.x; this.y = (int)in.y;}

    @Override
    public FTuple getPosition(){FTuple f = new FTuple(this.x, this.y); return  f;}

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
    public float getRotation(){return rotation;}

    @Override
    public void setRotation(float val){rotation = val;}


    @Override
    public void dispose() {
        bitmap.recycle();
    }      
}


