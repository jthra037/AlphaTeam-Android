package alpha.nosleep.neonrush;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import alpha.nosleep.androidgames.framework.Screen;
import alpha.nosleep.androidgames.framework.impl.AndroidGame;

public class MainActivity extends AndroidGame {


    @Override
    public Screen getStartScreen()
    {
        return new MainMenuScreen(this);
    }
}
