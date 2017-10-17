package alpha.nosleep.game.framework;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Pixmap;

/**
 * Created by John on 2017-10-17.
 */

public abstract class Object
{
    protected Collider collider;
    protected Pixmap img;
    public FTuple position;
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

    public abstract void update(float deltaTime);

    public Collider getCollider() {
        return collider;
    }

    protected Game getGame()
    {
        return game;
    }
}
