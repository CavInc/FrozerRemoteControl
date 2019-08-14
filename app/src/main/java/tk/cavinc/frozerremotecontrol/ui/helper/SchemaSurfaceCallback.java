package tk.cavinc.frozerremotecontrol.ui.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

import tk.cavinc.frozerremotecontrol.R;

/**
 * Created by cav on 14.08.19.
 */

public class SchemaSurfaceCallback implements SurfaceHolder.Callback {

    private DrawTread mDrawTread;

    private static final String TAG = "CALL";
    private Context mContext;

    public SchemaSurfaceCallback(Context context) {
        mContext = context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"SURFACE GREATE");
        mDrawTread = new DrawTread(surfaceHolder, mContext.getResources());
        mDrawTread.setRunnung(true);
        mDrawTread.run();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"SURFACE DESROU");
        boolean retry = true;
        mDrawTread.setRunnung(false);
        while (retry) {
            try {
                mDrawTread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class DrawTread extends Thread {
        private boolean runnung = false;
        private SurfaceHolder surfaceHolder;

        private Bitmap picture;

        public DrawTread(SurfaceHolder holder, Resources resources){
            surfaceHolder = holder;
            picture = BitmapFactory.decodeResource(resources, R.drawable.ic_fr1);
        }

        public void setRunnung(boolean runnung) {
            this.runnung = runnung;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (runnung) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        canvas.drawColor(Color.CYAN);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
