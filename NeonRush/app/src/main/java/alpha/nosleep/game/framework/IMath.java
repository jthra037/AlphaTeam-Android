package alpha.nosleep.game.framework;

/**
 * Created by Andrew on 11/8/2017.
 */

public class IMath {

    public static float clamp(float val, float min, float max){return Math.max(min, Math.min(max, val));}

    /**
     *
     * @param value
     * @return clamps a given value between 0, and 1.
     */
    public static float clamp01(float value)
    {
        float result;
        if (value < 0)
        {
            result = 0f;
        }
        else if (value > 1)
        {
            result = 1f;
        }
        else
        {
            result = value;
        }

        return result;

    }
}
