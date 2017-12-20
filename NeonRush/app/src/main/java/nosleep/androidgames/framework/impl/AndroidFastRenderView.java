package nosleep.androidgames.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    AndroidGame game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    int fps;
    long lastFpsTime;
    
    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();
    }

    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }      
    
    public void run() {
        Rect dstRect = new Rect();
        //long startTime = System.nanoTime();

        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 1;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        while(running) {  
            if(!holder.getSurface().isValid())
                continue;           

            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double deltaTime = updateLength / ((double)OPTIMAL_TIME);

            lastFpsTime += updateLength;
            fps++;

            if (lastFpsTime >= 1000000000)
            {
                Log.i("FPS", "(FPS: " + fps + ") + (deltaTime: " + deltaTime + ")");
                lastFpsTime = 0;
                fps = 0;
            }


            // converted from nanoseconds to seconds
            //float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            //startTime = System.nanoTime();

            game.getCurrentScreen().update((float)deltaTime);
            game.getCurrentScreen().present((float)deltaTime);
            
            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            
            // Draw the artificial framebuffer.
            // The scaling is performed automatically in case the destination 
            // rectangle is smaller or bigger than the framebuffer

            canvas.drawBitmap(framebuffer, null, dstRect, null);

            holder.unlockCanvasAndPost(canvas);

        }
    }

    public void pause() {                        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }        
}


