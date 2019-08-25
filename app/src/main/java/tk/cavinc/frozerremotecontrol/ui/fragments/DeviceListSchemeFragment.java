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

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
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
                    /*
                    int tagid = (int) v.getTag();
                    int selid = mDataManager.getDeviceModels().indexOf(new DeviceModel(tagid, "", "", 0));
                    */
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

                        /*
                        v.animate()
                                .x(motionEvent.getRawX() - xDelta)
                                .y(motionEvent.getRawY() - yDelta)
                                .setDuration(0)
                                .start();
                        */
                    }
                    break;
            }
        } else {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "Жамкнули");
                /*
                FrameLayout.LayoutParams laoutParamsS = (FrameLayout.LayoutParams) v.getLayoutParams();
                int tagid = (int) v.getTag();
                int selid = mDataManager.getDeviceModels().indexOf(new DeviceModel(tagid, "", "", 0));
                */
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
    }

    private void checkLips(int currentX,int currentY,int currentRigth,int currentBottom,int id){
        for (int i=0; i< mFrameLayout.getChildCount(); i++){
            View v = mFrameLayout.getChildAt(i);
            if (id == (int )v.getTag()) continue;

            int x = v.getLeft();
            int y = v.getTop();
            int rigthX = v.getRight();
            int bottomY = v.getBottom();

            // проверяем вершины
            // A = B1
            // A = C1
            //

        }
    }

    private static final int DELTA_SIZE = 5;

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
