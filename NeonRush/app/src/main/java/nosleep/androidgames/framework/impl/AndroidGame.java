package nosleep.androidgames.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import nosleep.androidgames.framework.Audio;
import nosleep.androidgames.framework.FileIO;
import nosleep.androidgames.framework.Game;
import nosleep.androidgames.framework.Graphics;
import nosleep.androidgames.framework.Input;
import nosleep.androidgames.framework.Screen;
import nosleep.neonrush.R;


public abstract class AndroidGame extends BaseGameActivity implements Game {
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    Context context;
    SharedPreferences settings;
    private GAMESTATE gamestate;
    static final int REQUEST_LEADERBOARD = 100;
    static final int REQUEST_ACHIEVEMENTS = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 1280 : 800;
        int frameBufferHeight = isLandscape ? 800 : 1280;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
        
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        
        // determine the scale based on our framebuffer and our display sizes
        float scaleX = (float) frameBufferWidth / size.x;
        float scaleY = (float) frameBufferHeight / size.y;

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getStartScreen();
        context = getApplicationContext();
        settings = getSharedPreferences(Settings_Prefs,0);
        setContentView(renderView);

    }



    @Override
    public void onResume() {
        super.onResume();
        screen.resume();
        renderView.resume();
    }

    @Override
    public Context getContext()
    {
       return context;
    }

    @Override
    public void onPause() {
        super.onPause();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(!hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            screen.focusChanged(hasFocus);
        }
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        screen.onBackButton();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        screen.restart();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        screen.destroy();
    }



    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen is null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Activity getActivity()
    {
        return this;
    }
    
    public Screen getCurrentScreen() {
        return screen;
    }

    @Override
    public GAMESTATE getGameState(){return gamestate;}

    @Override
    public void setGameState(GAMESTATE newGameState){gamestate = newGameState;}

    @Override
    public SharedPreferences getSharedPreferences(){return settings;}

   @Override
    public boolean isSignedIn()
    {
        return getGameHelper().isSignedIn();
    }

    @Override
    public void signIn()
    {
        getGameHelper().beginUserInitiatedSignIn();
    }

    @Override
    public void submitScore(int score)
    {
        Games.Leaderboards.submitScore(getGameHelper().getApiClient(),getString(R.string.Leaderboard_top_score),score);
    }

    @Override
    public void showLeaderboard()
    {
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(),getString(R.string.Leaderboard_top_score)),REQUEST_LEADERBOARD);
    }

    @Override
    public void showAchievements()
    {
        startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),REQUEST_ACHIEVEMENTS);
    }

    @Override
    public void unlockAchievement(int achievmentToUnlock)
    {
        Games.Achievements.unlock(getApiClient(),getString(achievmentToUnlock));
    }

    @Override
    public void incrementAchievement(int achievement, int value)
    {
        Games.Achievements.increment(getApiClient(),getString(achievement),value);
    }


}