package tk.cavinc.frozerremotecontrol.ui.helper;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by cav on 14.08.19.
 */

public class SchemaSurfaceCallback implements SurfaceHolder.Callback {

    private DrawTread mDrawTread;

    private static final String TAG = "CALL";

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"SURFACE GREATE");
        mDrawTread = new DrawTread(surfaceHolder);
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

        public DrawTread(SurfaceHolder holder){
            surfaceHolder = holder;
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
                    if (canvas == null) continue;

                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
