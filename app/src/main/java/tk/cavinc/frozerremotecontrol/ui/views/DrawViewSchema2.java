package tk.cavinc.frozerremotecontrol.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import tk.cavinc.frozerremotecontrol.R;

/**
 * Created by cav on 14.08.19.
 */

public class DrawViewSchema2 extends View {

    private Paint mPaint = new Paint();
    private Bitmap mFr1;
    private int width;
    private int heigth;

    public DrawViewSchema2(Context context) {
        super(context);
    }

    public DrawViewSchema2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initWidthAndHeigth(context);
        mFr1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_fr1);
        Drawable mFr11 = context.getResources().getDrawable(R.drawable.ic_fr1);

    }

    private void initWidthAndHeigth(Context context) {
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display=windowManager.getDefaultDisplay();
        Point point= new Point();
        display.getSize(point);
        width=point.x;
        heigth=point.y;
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

        mPaint.setColor(Color.argb(100,200,255,255));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        canvas.drawRect(10,200,50,220,mPaint);
        drawFR1(canvas,40,300,90);
    }

    private void drawFR1(Canvas canvas,int x,int y,int arg){
        canvas.drawRect(x,y,x+20,y+20,mPaint);
        canvas.drawRect(x,y,x+15,y+20,mPaint);
    }

    private void drawFR2(Canvas canvas,int x, int y,int arg) {

    }

    private void drawShape(){
        ShapeDrawable x = new ShapeDrawable(new RectShape());
        x.getPaint().setColor(Color.argb(100,200,100,200));
        x.setIntrinsicHeight(5);
        x.setIntrinsicWidth(150);
        //this.setBackground(x);
    }

}
