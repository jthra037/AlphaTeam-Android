package nosleep.neonrush;

import android.graphics.Color;
import android.graphics.Matrix;

import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.Object;

/**
 * Created by Andrew on 11/27/2017.
 * Trimmed by Mark on 2018-01-03.
 */

public class DirectionalArrow extends Object
{

    DirectionalArrow(World w, FTuple pos)
    {
        super(w.game);
        tag = "dArrow";

        //Location Info.
        position = pos;
        rotation = 0;

        //Presentation Info.
        img = g.newPixmap("directionalarrow.png", Graphics.PixmapFormat.ARGB4444);
        backupImg = g.newPixmap(img.getBitmap(),img.getFormat());

        w.register(this);
    }

    @Override
    public void update(float deltaTime)
    {

    }

    @Override
    public void present(float deltaTime)
    {
        if (img != null)
        {
            Matrix matrix = new Matrix();
            matrix.setRotate(this.rotation,(this.img.getWidth()/2),(this.img.getHeight()/2));
            matrix.postTranslate(this.position.x,this.position.y);
            g.drawPixmap(img,matrix);
        }
        else
        {
            g.drawCircle((int)position.x, (int)position.y,20, Color.RED);
        }
    }
}
