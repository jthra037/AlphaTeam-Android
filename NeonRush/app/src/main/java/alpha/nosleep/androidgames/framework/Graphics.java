package alpha.nosleep.androidgames.framework;

import android.content.res.AssetManager;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import alpha.nosleep.game.framework.FTuple;

public interface Graphics
{
    public static enum PixmapFormat
    {
        ARGB8888, ARGB4444, RGB565
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format);
    public Pixmap resizePixmap(Pixmap pixmap, int width, int height);
    public Pixmap rotatePixmap(Pixmap pixmap, float angle);
    public Pixmap setAlpha(Pixmap pixmap, int alpha);
    public void clear(int color);
    public void drawPixel(int x, int y, int color);
    public void drawLine(int x, int y, int x2, int y2, int color);
    public void drawRect(int x, int y, int width, int height, int color);
    public void drawCircle(int x, int y, int radius, int color);
    public void drawARGBRect(Rect rect, int a, int r, int g, int b);
    public void drawRect(Rect rect, int color);
    public Rect setRectPosition(Rect rect,int x, int y);
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
    public void drawPixmap(Pixmap pixmap, int x, int y);
    public void drawPixmap(Pixmap pixmap);
    public void drawPixmap(Pixmap pixmap, Matrix matrix);
    public int getWidth();
    public int getHeight();
    public void drawText(String text, int x, int y, Paint color);
    public AssetManager getAssets();

}

