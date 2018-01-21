package nosleep.androidgames.framework.impl;

import android.media.SoundPool;

import nosleep.androidgames.framework.Sound;

public class AndroidSound implements Sound {
    int soundId;
    SoundPool soundPool;

    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void play(){soundPool.play(soundId,100,100,0,0,1);} //defaults to 100% audio. You can also adjust whether orn ot the audio loops, or how fast it plays back

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }
}


