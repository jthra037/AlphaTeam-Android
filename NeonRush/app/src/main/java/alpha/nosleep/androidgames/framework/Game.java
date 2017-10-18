package alpha.nosleep.androidgames.framework;

import android.app.Activity;
import android.content.Context;

public interface Game
{
    public Input getInput();
    public FileIO getFileIO();
    public Graphics getGraphics();
    public Audio getAudio();
    public void setScreen(Screen screen);
    public Screen getCurrentScreen();
    public Screen getStartScreen();
    public Activity getActivity();
    public Context getContext();
}

