package alpha.nosleep.neonrush;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.androidgames.framework.Screen;

/**
 * Created by John on 2017-10-10.
 */

public class MainGameScreen extends Screen {
    private Pixmap background;
    Graphics g;

    public MainGameScreen(Game game)
    {
        super(game);
        g = game.getGraphics();

        background = g.newPixmap("gameui.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(background, g.getWidth(), g.getHeight());
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {
        g.drawPixmap(background);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
