package nosleep.androidgames.framework;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.api.GoogleApiClient;

public interface Game
{
    enum GAMESTATE{Play,Pause,GameOver}
    public Input getInput();
    public FileIO getFileIO();
    public Graphics getGraphics();
    public SharedPreferences getSharedPreferences();
    public Audio getAudio();
    public void setScreen(Screen screen);
    public Screen getCurrentScreen();
    public Screen getStartScreen();
    public Activity getActivity();
    public Context getContext();
    public GAMESTATE getGameState();
    public void setGameState(GAMESTATE newGameState);
    public boolean isSignedIn();
    public void signIn();
    public void submitScore(int score);
    public void showLeaderboard();
    public void showAchievements();
    public void unlockAchievement(int achievmentToUnlock);
    public void incrementAchievement(int achievement, int value);
    public static final String Settings_Prefs = "Settingsprefsfile";
    public static final String World_Prefs = "WorldPrefsFile";
}

