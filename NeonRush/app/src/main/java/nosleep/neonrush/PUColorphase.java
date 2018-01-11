package nosleep.neonrush;

import android.content.SharedPreferences;

import nosleep.androidgames.framework.Audio;
import nosleep.androidgames.framework.Sound;
import nosleep.game.framework.FTuple;

/**
 * Created by Mark- on 23-Dec-17.
 */



public class PUColorphase extends Powerup
{

    //Audio Components
    private Sound powerupUseSound;
    SharedPreferences settings;
    private boolean shouldSFXPlay;
    Audio a;

    public PUColorphase(World w, FTuple pos)
    {
        super(w, pos);
        type = PUTYPE.Colorphase;
        duration = 10000;

        a = w.game.getAudio();
        settings = w.game.getSharedPreferences();
        shouldSFXPlay = settings.getBoolean("enableSFX", true);
        powerupUseSound = a.newSound("Sounds/SFX/powerupuse.wav");
    }

    @Override
    public void acquire()
    {
        super.acquire();
        player.PUColorphaseCount++;
    }

    public void activate(int colorIndex)
    {
        player.color = player.collisions.get(0).otherColor;
        player.setImg(world.enemyPalette[colorIndex]);
        player.setPowerupTimer(duration);

        if (shouldSFXPlay)
        {
            powerupUseSound.play();
        }
    }
}
