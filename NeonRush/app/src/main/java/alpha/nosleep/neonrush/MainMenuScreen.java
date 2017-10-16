package alpha.nosleep.neonrush;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.androidgames.framework.Screen;
import alpha.nosleep.game.framework.Button;

/**
 * Created by John on 2017-10-10.
 */

public class MainMenuScreen extends Screen {
    private Pixmap background;
    private List<Button> buttons = new ArrayList<Button>();

    public MainMenuScreen(final Game game)
    {
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("gameui.png", Graphics.PixmapFormat.RGB565);

        Pixmap img = g.newPixmap("Temp Logo.png", Graphics.PixmapFormat.RGB565);
        buttons.add(new Button(img, 200, 100, 100, 50, new Callable<Screen>() {
            public Screen call() {
                return new MainMenuScreen(game);
            }
        }
        ));
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(background, 0, 0);
        for (Button button : buttons)
        {
            g.drawPixmap(button.img, button.position.x, button.position.y);
        }
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
