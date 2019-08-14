package tk.cavinc.frozerremotecontrol.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import tk.cavinc.frozerremotecontrol.R;

/**
 * Created by cav on 14.08.19.
 */

public class DrawViewSchema2 extends View {

    private Paint mPaint = new Paint();
    private Bitmap mFr1;

    public DrawViewSchema2(Context context) {
        super(context);
    }

    public DrawViewSchema2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mFr1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_fr1);
    }

    //http://www.cyberforum.ru/android-dev/thread2122081.html

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.drawColor(Color.argb(100,20,20,20));
        //Picture picture =
        //canvas.drawPicture(picture);
        // Рисуем зелёный прямоугольник
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(20, 650, 950, 680, mPaint);
        //canvas.drawBitmap(mFr1,10,10,mPaint);
        Rect rect = new Rect();
        rect.set(250, 300, 350, 500);
        canvas.drawRect(rect,mPaint);
    }
}
