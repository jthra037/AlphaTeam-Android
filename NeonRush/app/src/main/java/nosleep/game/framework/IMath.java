package nosleep.game.framework;

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

    public static float Clamp(float value, float min, float max)
    {
        if (value < min)
        {
            value = min;
        }
        else if (value > max)
        {
            value = max;
        }
        return value;
    }

    public static int Clamp(int value, int min, int max)
    {
        if (value < min)
        {
            value = min;
        }
        else if (value > max)
        {
            value = max;
        }
        return value;
    }

    public static float Repeat(float t, float length)
    {
        return Clamp(t - (float)(Math.floor((t / length) * length)), 0f, length);
    }

    public static float PingPong(float t, float length)
    {
        t = Repeat(t, length * 2f);
        return length - Math.abs(t - length);

    }


}
