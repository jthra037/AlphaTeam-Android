package nosleep.game.framework;

import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Pixmap;

/**
 * Created by John on 2017-10-17.
 */

public abstract class Object
{
    protected Collider collider;
    protected Pixmap img;
    public FTuple position;
    public float rotation;
    public String tag;
    private Game game;

    public Object(Game game)
    {
        this.game = game;
    }

    public void present(float deltaTime)
    {
        Graphics g = game.getGraphics();

        if (img != null)
        {
            g.drawPixmap(img);
        }
    }

    public void present(int x, int y, float deltaTime)
    {
        Graphics g = game.getGraphics();

        if (img != null)
        {
            g.drawPixmap(img, x, y);
        }
    }

    public abstract void update(float deltaTime);

    public Collider getCollider() {
        return collider;
    }

    public FTuple getPosition()
    {
        return position;
    }

    protected Game getGame()
    {
        return game;
    }

    public Pixmap getImg(){return img;}

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


}
