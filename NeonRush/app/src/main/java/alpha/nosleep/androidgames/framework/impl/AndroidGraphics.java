package alpha.nosleep.androidgames.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.game.framework.FTuple;

import static android.R.attr.y;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer; // represents our artificial framebuffer
    Canvas canvas;		// use to present to the artificial framebuffer
    Paint paint;		// needed for drawing
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public Pixmap resizePixmap(Pixmap pixmap, int newWidth, int newHeight) {
        Bitmap resizedBitmap = null;
        int originalWidth = pixmap.getBitmap().getWidth();
        int originalHeight = pixmap.getBitmap().getHeight();

        resizedBitmap = Bitmap.createScaledBitmap(pixmap.getBitmap(), newWidth, newHeight, true);
        pixmap.setBitmap(resizedBitmap);
        return pixmap;
    }

    @Override
    public Pixmap rotatePixmap(Pixmap pixmap, float angle)
    {
        Bitmap newBM = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        matrix.postRotate(angle,(pixmap.getWidth()/2),(pixmap.getHeight()/2)); //rotating around center of object
        matrix.postTranslate(pixmap.getX(),pixmap.getY());
        newBM = Bitmap.createBitmap(pixmap.getBitmap(), 0, 0, pixmap.getWidth(), pixmap.getHeight(), matrix, true);
        pixmap.setBitmap(newBM);

        pixmap.setRotation(angle);

        return pixmap;
    }

    @Override
    public void rotateToPoint(Pixmap pixmap, FTuple point)
    {
        Bitmap newBM = null;
        float angle = (float) Math.atan2(point.y-pixmap.getY(),point.x-pixmap.getX()); //getting angle between the two objects
        angle = (float) (angle * (180.0f/Math.PI));

        if(angle < 0)
        {
            angle = 360 - (-angle);
        }
        rotatePixmap(pixmap,angle);
    }


    @Override
    public AssetManager getAssets()
    {
        return assets;
    }

    @Override
    public void drawText(String text, int x, int y, Paint color)
    {
        canvas.drawText(text, x, y, color);
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }



    @Override
    public void drawARGBRect(Rect rect, int a, int r, int g, int b)
    {

        paint.setColor(Color.rgb(r,g,b));
        paint.setAlpha(a);
        canvas.drawRect(rect.left,rect.top,rect.right+rect.left -1,rect.bottom+ rect.top -1,paint );
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {

        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
    }

    @Override
    public void drawRect(Rect rect, int color)
    {
        paint.setColor(color);
        paint.setStyle((Style.FILL));
        canvas.drawRect(rect.left,rect.top,rect.right + rect.left -1,rect.bottom + rect.top -1,paint);
    }

    @Override
    public Rect setRectPosition(Rect rect,int x, int y)
    {
        rect.set(x,y,rect.right,rect.bottom);
        return rect;
    }

    @Override
    public void drawCircle(int x, int y, int radius, int color)
    {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawCircle(x, y, radius, paint);
    }




    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
    }
    
    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y,null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap)
    {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap,(pixmap).getX() , (pixmap).getY(), null);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }




}