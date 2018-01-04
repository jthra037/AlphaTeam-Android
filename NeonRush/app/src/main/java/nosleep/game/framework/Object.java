package nosleep.game.framework;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Pixmap;

/**
 * Created by John on 2017-10-17.
 * Trimmed by Mark on 2018-01-03.
 */

public abstract class Object
{
    protected Graphics g;
    protected Game game;
    public String tag;

    //Location info.
    public FTuple position;
    protected ITuple localCoord;
    protected float rotation;

    //Presentation info.
    protected Pixmap img;
    protected Pixmap backupImg;
    public int color;
    public int colorIndex;
    private int glowColor = 0;
    private float alpha = 100.0f;

    protected Collider collider;

    public Object(Game gm)
    {
        game = gm;
        g = game.getGraphics();
    }

    //The bare minimum all objects require to present and update is deltaTime.
    //The unique implementation of these functions are called on all objects from World.java.
    public abstract void present(float deltaTime);
    public abstract void update(float deltaTime);

    //Img is checked against null at the bottom of the hierarchy.
    //All img nullity should be resolved by the time this is reached.
    public void present(float deltaTime, ITuple pos)
    {
        g.drawPixmap(img, pos.x - img.getWidth()/2, pos.y - img.getHeight()/2);
    }

    //Getters
    public Collider getCollider() {
        return collider;
    }
    public FTuple getPosition()
    {
        return position;
    }

    //Setters
    public abstract void setImg(String imageRef);
    public abstract void setBackupImg(String imageRef);

    public void rotateToPoint(FTuple a, FTuple point, float maxDegreesDelta)
    {
        float targetAngle = (float)(Math.toDegrees((Math.atan2(a.y-point.y,a.x-point.x)))+270)%360;
        float currentRotation = Math.abs(this.rotation%360);
        float angle = targetAngle-currentRotation;

        if(Math.abs(angle)<maxDegreesDelta)
        {
            this.rotation = targetAngle;
        }
        else if(angle<0)
        {
            this.rotation -= maxDegreesDelta;
        }
        else
        {
            this.rotation += maxDegreesDelta;
        }
        //Log.d("positions", "Player: " + a.ToString() + ", Ball: " + point.ToString() + ", angle: " + angle);

    }

    public Pixmap setBackgroundGlow(Pixmap pixmap,int r,int g,int b)
    {
        int mGlowColor = Color.rgb(r, g, b);
        if (glowColor != mGlowColor)
        {
            // An added margin to the initial image
            int margin = 24;
            int halfMargin = margin / 2;
            // the glow radius
            int mGlowRadius = 40;

            // the glow color

            // extract the alpha from the source image
            Bitmap alpha = pixmap.getBitmap().extractAlpha();

            // The output bitmap (with the icon + glow)
            Bitmap bmp =  Bitmap.createBitmap(pixmap.getWidth() + margin, pixmap.getHeight() + margin, Bitmap.Config.ARGB_8888);
            // The canvas to paint on the image
            Canvas canvas = new Canvas(bmp);

            Paint paint = new Paint();
            paint.setColor(mGlowColor);
            glowColor = mGlowColor;
            // outer glow
            paint.setMaskFilter(new BlurMaskFilter(mGlowRadius, BlurMaskFilter.Blur.OUTER));//For Inner glow set Blur.INNER
            canvas.drawBitmap(alpha, halfMargin, halfMargin, paint);

            // original icon
            canvas.drawBitmap(pixmap.getBitmap(), halfMargin, halfMargin, null);
            pixmap.setBitmap(bmp);
            return pixmap;
        }
        else
        {
            return pixmap;
        }
    }

    public Pixmap setBackgroundGlow(Pixmap pixmap,int col)
    {
        if(glowColor != col)
        {
            // An added margin to the initial image
            int margin = 0;
            int halfMargin = margin / 2;
            // the glow radius
            int mGlowRadius = 20;
            glowColor = col;

            // extract the alpha from the source image
            Bitmap alpha = pixmap.getBitmap().extractAlpha();

            // The output bitmap (with the icon + glow)
            Bitmap bmp =  Bitmap.createBitmap(pixmap.getWidth() + margin, pixmap.getHeight() + margin, Bitmap.Config.ARGB_8888);
            // The canvas to paint on the image
            Canvas canvas = new Canvas(bmp);

            Paint paint = new Paint();
            paint.setColor(col);

            // outer glow
            paint.setMaskFilter(new BlurMaskFilter(mGlowRadius, BlurMaskFilter.Blur.OUTER));//For Inner glow set Blur.INNER
            canvas.drawBitmap(alpha, halfMargin, halfMargin, paint);

            // original icon
            canvas.drawBitmap(pixmap.getBitmap(), halfMargin, halfMargin, null);

            pixmap.setBitmap(bmp);
            return pixmap;
        }
        else
        {
            return pixmap;
        }

    }

    public void setAlpha(float newAlpha) //integer between 0-100
    {


        if (newAlpha != alpha && img != null) //to check if the current alpha value of the image is equal to your desired alpha. to avoid always halving you alpha value
        {
            float percent = newAlpha / 100.0f; //converting desired alpha to number between 0-255
            float intValue = percent * 255;
            alpha = intValue;

            Bitmap newBM = Bitmap.createBitmap(backupImg.getBitmap().getWidth(), backupImg.getBitmap().getHeight(), Bitmap.Config.ARGB_8888);
            Canvas cc = new Canvas(newBM);
            cc.drawARGB(0, 0, 0, 0);
            Paint newPaint = new Paint();
            newPaint.setAlpha((int) intValue);
            cc.drawBitmap(backupImg.getBitmap(), 0, 0, newPaint);
            img.setBitmap(newBM);
        }
        else if (newAlpha != alpha && img == null)
        {
            float percent = newAlpha / 100.0f; //converting desired alpha to number between 0-255
            float intValue = percent * 255;
            alpha = intValue;
        }

    }
}
