package nosleep.neonrush;

import android.graphics.Color;
import android.graphics.Matrix;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.CircleCollider;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.Object;

/**
 * Created by Andrew on 11/27/2017.
 */

public class DirectionalArrow extends Object
{
    World mWorld;
    Graphics g;
    public DirectionalArrow(World m_World, FTuple position)
    {
        super(m_World.game);
        this.g = m_World.game.getGraphics();
        this.img = g.newPixmap("directionalarrow.png", Graphics.PixmapFormat.ARGB4444);
        this.position = position;
        this.mWorld = m_World;
        this.rotation = 0;
        this.collider = new CircleCollider(20);
        this.tag = "dArrow";
        mWorld.register(this);
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
            g.drawPixmap(this.img, matrix);
        }
        else
        {
            g.drawCircle((int)position.x, (int)position.y,20, Color.RED);
        }
    }

}