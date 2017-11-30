package nosleep.neonrush;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import nosleep.androidgames.framework.Screen;
import nosleep.androidgames.framework.impl.AndroidGame;

public class MainActivity extends AndroidGame {


    @Override
    public void onSignInFailed()
    {
        Toast t = new Toast(this);
        t.makeText(this, "Signin failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignInSucceeded()
    {
        Toast t = new Toast(this);
        t.makeText(this, "Successfully signed in", Toast.LENGTH_LONG).show();

    }
    @Override
    public Screen getStartScreen()
    {
        return new MainMenuScreen(this);
    }
}
