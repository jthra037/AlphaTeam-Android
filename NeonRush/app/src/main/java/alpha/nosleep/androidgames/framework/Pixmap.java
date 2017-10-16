package alpha.nosleep.androidgames.framework;

import alpha.nosleep.androidgames.framework.Graphics.PixmapFormat;

public interface Pixmap 
{
    public int getWidth();
    public int getHeight();
    public PixmapFormat getFormat();
    public void dispose();
}

