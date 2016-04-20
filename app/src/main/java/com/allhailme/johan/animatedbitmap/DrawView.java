package com.allhailme.johan.animatedbitmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Johan on 2016-03-06.
 */
public class DrawView extends SurfaceView implements Runnable {
    //define the game loop thread
    private Thread mGameloop = null;

    //define the surface holder
    private SurfaceHolder mSurface;

    //define the running variable
    private volatile boolean mRunning = false;

    //the asset manager handles resource loading
    private AssetManager mAssets = null;
    private BitmapFactory.Options mOptions = null;
    private Bitmap mArcherWalkingEast[];
    private int mFrame = 0;
    private float mLeft = 0;

    //this is the DrawView class constructor
    public DrawView(Context context) {
        super(context);

        //get the SurfaceHolder object to supply a context
        mSurface = getHolder();

        //create the asset manager object
        mAssets = context.getAssets();

        //set bitmap color depth option
        mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        //create the bitmap array for animation
        mArcherWalkingEast = new Bitmap[8];

        //load the archer bitmaps
        try {
            for (int i=0; i<8; i++) {
                String filename = "green_archer_walk_east"+Integer.toString(i)+".png";
                InputStream istream = mAssets.open(filename);
                mArcherWalkingEast[i] = BitmapFactory.decodeStream(istream,null,
                        mOptions);
                istream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //custom resume method called by outer class
    public void resume() {
        mRunning = true;
        mGameloop = new Thread(this);
        mGameloop.start();
    }

    //custom pause method called by outer class
    public void pause() {
        mRunning = false;
        while (true) {
            try {
                //just keep doing this until app has focus
                mGameloop.join();
            }
            catch (InterruptedException e) { }
        }
    }

    //this is the threaded method
    @Override public void run() {

        //this is the game loop!
        while (mRunning) {

            //make sure surface is usable (it's asynchronous)
            if (!mSurface.getSurface().isValid())
                continue;

            //request the drawing canvas
            Canvas canvas = mSurface.lockCanvas();

            /**
             We should really make sure canvas is not null
             here before continuing, but if canvas is invalid
             then the game will cease anyway.
             **/

            //draw one frame of animation from the knight array
            canvas.drawColor(Color.rgb(85, 200, 47));
            canvas.drawBitmap(mArcherWalkingEast[mFrame], mLeft, 0, null);

            //draw the knight scaled larger
            Rect dest = new Rect(100,0,300,200);
            canvas.drawBitmap(mArcherWalkingEast[mFrame], null, dest, null);

            //draw the knight scaled REALLY BIG!
            //dest = new Rect(200,0,800,600);
            //canvas.drawBitmap(mArcherWalkingEast[mFrame], null, dest, null);

            //release the canvas
            mSurface.unlockCanvasAndPost(canvas);

            //go to the next frame of animation
            mFrame++;
            mLeft += 10;
            if (mFrame > 7) {
                mFrame = 0;
                mLeft = 0;
            }
            try {
                //slow down the animation
                Thread.sleep(50);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
