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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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
    String interstitialTestId = "ca-app-pub-3940256099942544/1033173712";
    String bannerTestId = "ca-app-pub-3940256099942544/5224354917";
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    Context context;
    SharedPreferences settings;
    InterstitialAd mInterstitialAd;
    AdView adview;
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
        MobileAds.initialize(this,"ca-app-pub-4948492122982090~9045022299");

        adview = new AdView(this);
        adview.setAdUnitId(bannerTestId);
        adview.setAdSize(AdSize.SMART_BANNER);

        RelativeLayout mainLayout = new RelativeLayout(this);
        mainLayout.addView(renderView);

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_TOP);
        mainLayout.addView(adview,adParams);

        setContentView(mainLayout);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(interstitialTestId);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                //load next interstitial
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        screen = getStartScreen();
        context = getApplicationContext();
        settings = getSharedPreferences(Settings_Prefs,0);





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

    @Override
    public void showInterstitialAd()
    {
        this.runOnUiThread(new Runnable() {
            public void run()
            {
                if (mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
            }
        });
    }

    @Override
    public void showBanner()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adview.setVisibility(View.VISIBLE);
                adview.loadAd(new AdRequest.Builder().build());
            }
        });

        Log.i("showbanner", "showing banner");
    }

    public void hideBanner()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adview.setVisibility(View.GONE);
            }
        });
    }




}