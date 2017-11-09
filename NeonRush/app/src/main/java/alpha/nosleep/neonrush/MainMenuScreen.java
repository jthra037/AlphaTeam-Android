package alpha.nosleep.neonrush;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import alpha.nosleep.androidgames.framework.Game;
import alpha.nosleep.androidgames.framework.Graphics;
import alpha.nosleep.androidgames.framework.Input;
import alpha.nosleep.androidgames.framework.Pixmap;
import alpha.nosleep.androidgames.framework.Screen;
import alpha.nosleep.game.framework.Button;

/**
 * Created by John on 2017-10-10.
 */

public class MainMenuScreen extends Screen {
    private Pixmap gameTitle;
    Graphics g;
    Rect backGround;
    private List<Button> buttons = new ArrayList<Button>();

    public MainMenuScreen(final Game game)
    {
        super(game);
        g = game.getGraphics();
        backGround = new Rect(0,0,g.getWidth()+1,g.getHeight());
        gameTitle = g.newPixmap("title.png", Graphics.PixmapFormat.RGB565);
        gameTitle.setPosition(500,(g.getHeight()/2) - gameTitle.getHeight()/4);
        g.resizePixmap(gameTitle,700,500);


        Pixmap playButton = g.newPixmap("buttons/playbutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(playButton, 100, 50);

        buttons.add(new Button(game,playButton, new Callable<Void>(){
            public Void call() {

                game.setScreen(new MainGameScreen(game));

                return null;
            }
        }
        ));
        buttons.get(0).resize(340,100);
        buttons.get(0).setPosition(100,200);

        Pixmap achievementsButton = g.newPixmap("buttons/achievementsbutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(achievementsButton, 100, 50);

        buttons.add(new Button(game,achievementsButton, new Callable<Void>(){
            public Void call() {

                //game.setScreen(new AchievementsScreen(game));
                System.out.println("Achievements have yet to be implemented. Please be patient!");
                return null;
            }
        }
        ));
        buttons.get(1).resize(340,100);
        buttons.get(1).setPosition(100,330);

        Pixmap settingsButton = g.newPixmap("buttons/settingsbutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(settingsButton, 100, 50);

        buttons.add(new Button(game,settingsButton, new Callable<Void>(){
            public Void call() {

                game.setScreen(new SettingsScreen(game));
                //System.out.println("Settings have yet to be implemented. Please be patient!");

                return null;
            }
        }
        ));
        buttons.get(2).resize(340,100);
        buttons.get(2).setPosition(100,460);

        Pixmap leaderboardsButton = g.newPixmap("buttons/leaderboardsbutton.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(leaderboardsButton, 100, 50);

        buttons.add(new Button(game,leaderboardsButton, new Callable<Void>(){
            public Void call() {

                //game.setScreen(new LeaderboardsScreen(game));
                System.out.println("Leaderboards have yet to be implemented. Please be patient!");

                return null;
            }
        }
        ));
        buttons.get(3).resize(340,100);
        buttons.get(3).setPosition(100,590);



    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                for (Button button : buttons)
                {
                    if(inBounds(event, button.getX(), button.getY(),
                            button.getWidth(), button.getHeight()))
                    {
                        button.onClick();
                    }
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        g.drawRect(backGround, Color.BLACK );
        g.drawPixmap(gameTitle);

        for (Button button : buttons)
        {
            g.drawPixmap(button.getImg(),button.getX(),button.getY());
        }


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void focusChanged(boolean hasFocus)
    {

    }

    @Override
    public void destroy()
    {

    }

    @Override
    public void onBackButton()
    {
        Activity a = game.getActivity();
        a.finish();

    }

    @Override
    public void restart()
    {

    }

    @Override
    public void dispose() {

    }
}
