package nosleep.neonrush;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Input;
import nosleep.androidgames.framework.Pixmap;
import nosleep.androidgames.framework.Screen;
import nosleep.game.framework.Button;
import nosleep.game.framework.FTuple;
import nosleep.game.framework.IMath;
import nosleep.game.framework.ITuple;

/**
 * Created by John on 2017-10-10.
 */

public class MainMenuScreen extends Screen {
    private Pixmap gameTitle;
    Graphics g;
    Pixmap backGround;
    Rect inBetween;
    private Random random;
    int mainButtonY = 155;
    int ballSpeed = 10;
    int count = 0;

    public String[] enemyPalette = {"enemies/cyan.png","enemies/green.png","enemies/magenta.png",
            "enemies/red.png","enemies/blue.png","enemies/yellow.png","enemies/grey.png"};

    private List<Button> buttons = new ArrayList<Button>();
    private List<Ball> balls = new ArrayList<Ball>();

    public MainMenuScreen(final Game game)
    {
        super(game);
        g = game.getGraphics();
        backGround = g.newPixmap("mainscreen1.png", Graphics.PixmapFormat.ARGB4444);
        inBetween = new Rect(0,0,g.getWidth()+1,g.getHeight());
        gameTitle = g.newPixmap("title1.png", Graphics.PixmapFormat.RGB565);
        g.resizePixmap(gameTitle,700,500);
        gameTitle.setPosition(500,((g.getHeight()/2) - (gameTitle.getHeight()/2)));



        Pixmap playButton = g.newPixmap("buttons/playbutton.png", Graphics.PixmapFormat.RGB565);
        //g.resizePixmap(playButton, 100, 50);

        buttons.add(new Button(game,playButton, new Callable<Void>(){
            public Void call() {

                game.setScreen(new MainGameScreen(game));

                return null;
            }
        }
        ));
        buttons.get(0).resize(340,100);
        buttons.get(0).setPosition(100,mainButtonY);

        Pixmap achievementsButton = g.newPixmap("buttons/achievementsbutton.png", Graphics.PixmapFormat.RGB565);
        //g.resizePixmap(achievementsButton, 100, 50);

        buttons.add(new Button(game,achievementsButton, new Callable<Void>(){
            public Void call() {

                //game.setScreen(new AchievementsScreen(game));
                //System.out.println("Achievements have yet to be implemented. Please be patient!");
                if (game.isSignedIn())
                    game.showAchievements();

                return null;
            }
        }
        ));
        buttons.get(1).resize(340,100);
        buttons.get(1).setPosition(100,mainButtonY + 130);

        Pixmap settingsButton = g.newPixmap("buttons/settingsbutton.png", Graphics.PixmapFormat.RGB565);
        //g.resizePixmap(settingsButton, 100, 50);

        buttons.add(new Button(game,settingsButton, new Callable<Void>(){
            public Void call() {

                game.setScreen(new SettingsScreen(game));
                //System.out.println("Settings have yet to be implemented. Please be patient!");
                game.showBanner();
                return null;
            }
        }
        ));
        buttons.get(2).resize(340,100);
        buttons.get(2).setPosition(100,mainButtonY + 260);

        Pixmap leaderboardsButton = g.newPixmap("buttons/leaderboardsbutton.png", Graphics.PixmapFormat.RGB565);
        //g.resizePixmap(leaderboardsButton, 100, 50);

        buttons.add(new Button(game,leaderboardsButton, new Callable<Void>(){
            public Void call() {

                //game.setScreen(new LeaderboardsScreen(game));
                //System.out.println("Leaderboards have yet to be implemented. Please be patient!");
                if (game.isSignedIn())
                    game.showLeaderboard();

                return null;
            }
        }
        ));
        buttons.get(3).resize(340,100);
        buttons.get(3).setPosition(100,mainButtonY + 390);

        random = new Random();



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

            if ( count < 1)
            {
                for (int j = 0; j < 20; j++)
                {
                    int x = 0;
                    int y = 0;
                    switch (random.nextInt(4))
                    {
                        case 0:
                            x = 0;
                            y = random.nextInt(g.getHeight() - 50);
                            break;
                        case 1:
                            x = (int)g.getWidth();
                            y = random.nextInt(g.getHeight() - 50);
                            break;
                        case 2:
                            y = 0;
                            x = random.nextInt(g.getWidth() - 50);
                            break;
                        case 3:
                            y = g.getHeight();
                            x = random.nextInt(g.getWidth() - 50);
                            break;
                    }
                    Random r = new Random();
                    balls.add(new Ball(game,50, new FTuple(IMath.getRandomInt(-ballSpeed,ballSpeed),IMath.getRandomInt(-ballSpeed,ballSpeed)), enemyPalette[r.nextInt(enemyPalette.length)]));
                    balls.get(j).setVelocity(new FTuple(x,y));
                    Log.i("Positions", "X: " + x + ", Y: " + y);

                }
                count++;
            }

        for (Ball ball : balls)
        {
            if (!balls.isEmpty())
            {
                ball.update(deltaTime);
            }
        }

    }

    @Override
    public void present(float deltaTime) {
        //g.drawRect(inBetween,Color.GRAY);
        for (Ball ball : balls)
        {
            if (!balls.isEmpty())
            {
                ball.present(deltaTime);
            }
        }
        g.drawPixmap(backGround,-1,0);
        g.drawPixmap(gameTitle);

        for (Button button : buttons)
        {
            if (!buttons.isEmpty())
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
