package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;


import static android.content.ContentValues.TAG;
import static android.view.FrameMetrics.ANIMATION_DURATION;
import static java.lang.Math.abs;
import static java.lang.Math.incrementExact;

/**
 * Created by cav on 13.08.19.
 */

public class DeviceListSchemeFragment extends Fragment implements View.OnClickListener,View.OnTouchListener {
    private DataManager mDataManager;

    private FrameLayout mFrameLayout;
    private int xDelta;
    private int yDelta;
    private boolean modeEdit = false;
    private MenuItem editItem;
    private MenuItem domeItem;

    private GestureDetector mDetector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        setHasOptionsMenu (true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_scheme_fragment, container, false);

        rootView.findViewById(R.id.device_list_bt).setOnClickListener(this);
        mFrameLayout  = rootView.findViewById(R.id.device_schema_map);

        mDetector = new GestureDetector(getActivity(),mGestureListener);

        mFrameLayout.setOnTouchListener(mFrameLayoutTouchListener);

        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.device_shema_menu,menu);
        editItem = menu.getItem(0);
        domeItem = menu.getItem(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_edit) {
            modeEdit = true;
            editItem.setVisible(false);
            domeItem.setVisible(true);
        }
        if (item.getItemId() == R.id.menu_done) {
            modeEdit = false;
            editItem.setVisible(true);
            domeItem.setVisible(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.device_list_bt) {
            ((MainActivity) getActivity()).viewFragment(new DeviceListFragment(),"DEVICELIST");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        ArrayList<DeviceModel> data = mDataManager.getDeviceModels();

        for (DeviceModel l : data) {
            ImageView imgx = setImageView(l.getX(),l.getY(),l.getDirection(),l.getGraphId(),l.getId());
            mFrameLayout.addView(imgx);
        }

    }

    private ImageView setImageView(int x,int y,int rotate,int type,int id){
        ImageView img = new ImageView(getActivity());
        switch (type) {
            case 1:
                Bitmap bitmap1 = getVectorBitmap(R.drawable.ic_fr1);
                img.setImageBitmap(bitmap1);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(50, 50);
                params.leftMargin = x;
                params.topMargin = y;
                img.setLayoutParams(params);
                img.setOnTouchListener(this);
                break;
            case 2:
                Bitmap bitmap2 = getVectorBitmap(R.drawable.ic_fr2);
                img.setImageBitmap(bitmap2);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(120, 50);
                params2.leftMargin = x;
                params2.topMargin = y;
                img.setLayoutParams(params2);
                img.setOnTouchListener(this);
                break;
            case 3:
                Bitmap bitmap3 = getVectorBitmap(R.drawable.ic_fr3);
                img.setImageBitmap(bitmap3);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(100, 50);
                params3.leftMargin = x;
                params3.topMargin = y;
                img.setLayoutParams(params3);
                img.setOnTouchListener(this);
                break;
            case 4:
                Bitmap bitmap4 = getVectorBitmap(R.drawable.ic_fr4);
                img.setImageBitmap(bitmap4);
                FrameLayout.LayoutParams params4 = new FrameLayout.LayoutParams(50, 50);
                params4.leftMargin = x;
                params4.topMargin = y;
                img.setLayoutParams(params4);
                img.setOnTouchListener(this);
                break;

        }
        img.setTag(id);
        rotateImage(img,0,rotate);
        return img;
    }

    private Bitmap getVectorBitmap(int resId){
        Drawable drawable = ContextCompat.getDrawable(getActivity(),resId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void rotateImage(ImageView img,int from,int to){
        RotateAnimation rotateAnimation = new RotateAnimation(from, to,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(ANIMATION_DURATION);
        rotateAnimation.setFillAfter(true);
        img.startAnimation(rotateAnimation);
    }


    View.OnTouchListener mFrameLayoutTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (touchFlag) {

                if (mDetector.onTouchEvent(event)){
                    // повернем элемент
                    int selid =  getIdPositionItem(selected_item);
                    DeviceModel model = mDataManager.getDeviceModels().get(selid);
                    int from = model.getDirection();
                    int to = from + 90 ;
                    if (to > 270) {
                        to = 0;
                    }
                    model.setDirection(to);
                    mDataManager.updateDeviceModels(selid,model);

                    RotateAnimation rotateAnimation = new RotateAnimation(from, to,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    rotateAnimation.setDuration(ANIMATION_DURATION);
                    rotateAnimation.setFillAfter(true);

                    (selected_item).startAnimation(rotateAnimation);
                }


                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        eX = (int) event.getX();
                        eY = (int) event.getY();
                        int x = (int) event.getX() - offset_x;
                        int y = (int) event.getY() - offset_y;

                        int w = mFrameLayout.getWidth()-selected_item.getWidth();
                        int h = mFrameLayout.getHeight()-selected_item.getHeight();

                        if (x > w) x = w;
                        if (y > h) y = h;
                        if (x < 0 ) x = 0;
                        if (y < 0) y = 0;

                        /*
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                new ViewGroup.MarginLayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT));
                        */

                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) selected_item.getLayoutParams();


                        lp.setMargins(x,y,0,0);
                        selected_item.setLayoutParams(lp);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "Закончили перетаскивание");

                        FrameLayout.LayoutParams laparam = (FrameLayout.LayoutParams) selected_item.getLayoutParams();

                        int selid =  getIdPositionItem(selected_item);

                        checkLips(laparam.leftMargin,laparam.topMargin,
                                laparam.leftMargin+selected_item.getWidth(),
                                laparam.topMargin+selected_item.getHeight(),
                                (Integer) selected_item.getTag());

                        if (selid != -1) {
                            DeviceModel model = mDataManager.getDeviceModels().get(selid);
                            model.setX(laparam.leftMargin);
                            model.setY(laparam.topMargin);
                            mDataManager.updateDeviceModels(selid, model);
                        }

                        touchFlag = false;
                        if (dropFlag) {
                            dropFlag = false;
                        } /*else {
                            selected_item.setLayoutParams(imageParams);
                        } */
                        break;
                }
            }
            return true;
        }
    };

    private int offset_x = 0;
    private int offset_y = 0;
    boolean touchFlag = false;
    boolean dropFlag = false;
    private View selected_item = null;
    ViewGroup.LayoutParams imageParams;
    int eX, eY;

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (modeEdit ) {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    touchFlag = true;
                    offset_x = (int) motionEvent.getX();
                    offset_y = (int) motionEvent.getY();
                    selected_item = v;
                    imageParams = v.getLayoutParams();
                    break;
                case MotionEvent.ACTION_UP:
                    selected_item = null;
                    touchFlag = false;
                    break;
                default:
                    break;
            }
        } else {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "Жамкнули");
                int selid =  getIdPositionItem(v);
                if (selid != -1) {
                    DeviceModel record = mDataManager.getDeviceModels().get(selid);

                    // открываем пульт управления
                    mDataManager.setCurrentDevice(record);
                    if (record.getControl() != null) {
                        mDataManager.setDeviceControl(record.getControl());
                    }
                    ((MainActivity) getActivity()).viewFragment(new ControlFragment(), "CONTROL");
                }
            }
            return true;
        }
        return false;

        /*

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        if (modeEdit ) {

            if (mDetector.onTouchEvent(motionEvent)){
                // повернем элемент
                int selid =  getIdPositionItem(v);
                DeviceModel model = mDataManager.getDeviceModels().get(selid);
                int from = model.getDirection();
                int to = from + 90 ;
                if (to > 270) {
                    to = 0;
                }
                model.setDirection(to);
                mDataManager.updateDeviceModels(selid,model);

                RotateAnimation rotateAnimation = new RotateAnimation(from, to,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setDuration(ANIMATION_DURATION);
                rotateAnimation.setFillAfter(true);

                (v).startAnimation(rotateAnimation);
            }

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                    xDelta = x - lParams.leftMargin;
                    yDelta = y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "Закончили перетаскивание");
                    FrameLayout.LayoutParams laoutParamsS = (FrameLayout.LayoutParams) v.getLayoutParams();
                    int selid =  getIdPositionItem(v);

                    if (selid != -1) {
                        DeviceModel model = mDataManager.getDeviceModels().get(selid);
                        model.setX(laoutParamsS.leftMargin);
                        model.setY(laoutParamsS.topMargin);
                        mDataManager.updateDeviceModels(selid, model);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (x - xDelta + v.getWidth() <= mFrameLayout.getWidth()
                            && y - yDelta + v.getHeight() <= mFrameLayout.getHeight()
                            && x - xDelta >= 0
                            && y - yDelta >= 0) {

                        FrameLayout.LayoutParams laoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                        laoutParams.leftMargin = x - xDelta;
                        laoutParams.topMargin = y - yDelta;
                        laoutParams.rightMargin = 0;
                        laoutParams.bottomMargin = 0;

                        v.setLayoutParams(laoutParams);
                    }
                    break;
            }
        } else {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "Жамкнули");
                int selid =  getIdPositionItem(v);
                if (selid != -1) {
                    DeviceModel record = mDataManager.getDeviceModels().get(selid);

                    // открываем пульт управления
                    mDataManager.setCurrentDevice(record);
                    if (record.getControl() != null) {
                        mDataManager.setDeviceControl(record.getControl());
                    }
                    ((MainActivity) getActivity()).viewFragment(new ControlFragment(), "CONTROL");
                }
            }
        }

        mFrameLayout.invalidate();
        return true;
        */
    }

    private void checkLips(int currentX,int currentY,int currentRigth,int currentBottom,int id){
        for (int i=0; i< mFrameLayout.getChildCount(); i++){
            View v = mFrameLayout.getChildAt(i);
            if (id == (int )v.getTag()) continue;

            int x = v.getLeft();
            int y = v.getTop();
            int rigthX = v.getRight();
            int bottomY = v.getBottom();
            int rX = rigthX;
            int bY = bottomY - v.getHeight();
            int x1 = x;
            int y1 = bottomY;

            Log.d(TAG,"Coord : "+x+" "+y+" "+rigthX+" "+bottomY+" id :"+id+" TAG :"+v.getTag());
            Log.d(TAG,"COORDX XX :"+x1+" "+y1+" "+rX+" "+bY);

            // проверяем вершины
            // A = B1  (
            // A = C1
            // A = D1
            // B = A1
            // B = C1
            // B = D1
            // C = A1
            // C = B1
            // C = D1
            // D = A1
            // D = C1
            // D = B1
            Log.d(TAG,"ITEM CURRENT :"+currentX+" "+currentY+" "+currentRigth+" "+currentBottom);
            if (testDelta(currentX,rX) && testDelta(currentY,bY)) {
                // A = B1
                Log.d(TAG,"YES A = B1");
            }
            if (testDelta(currentX,x1) && testDelta(currentY,y1)) {
                Log.d(TAG,"YES A = C1");
            }
            if (testDelta(currentX,rigthX) && testDelta(currentY,bottomY)) {
                Log.d(TAG,"YES A = D1");
            }

        }
    }

    private static final int DELTA_SIZE = 25;

    private boolean testDelta(int d1,int d2){
        int delta = abs(d1-d2);
        if (delta > DELTA_SIZE ) {
            return false;
        }
        return true;
    }

    private int getIdPositionItem(View v) {
        int tagid = (int) v.getTag();
        int selid = mDataManager.getDeviceModels().indexOf(new DeviceModel(tagid, "", "", 0));
        return selid;
    }

    GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG,"DOUBLE TAP");
            return true;
        }
    };
}
