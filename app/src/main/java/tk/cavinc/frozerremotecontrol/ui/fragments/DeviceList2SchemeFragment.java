package tk.cavinc.frozerremotecontrol.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import tk.cavinc.frozerremotecontrol.R;
import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.CorrectCoordinate;
import tk.cavinc.frozerremotecontrol.data.models.DeviceModel;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity;
import tk.cavinc.frozerremotecontrol.ui.activity.MainActivity2;

import static android.content.ContentValues.TAG;
import static java.lang.Math.abs;

/**
 * Created by cav on 05.11.19.
 */

public class DeviceList2SchemeFragment extends Fragment implements View.OnClickListener,View.OnTouchListener{
    private DataManager mDataManager;

    private FrameLayout mFrameLayout;
    private boolean modeEdit = false;

    private GestureDetector mDetector;

    private ImageView mDone;
    private ImageView mEdit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        setHasOptionsMenu (true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_list2_scheme_fragment, container, false);

        mFrameLayout  = rootView.findViewById(R.id.device_schema_map);

        mDetector = new GestureDetector(getActivity(),mGestureListener);

        mFrameLayout.setOnTouchListener(mFrameLayoutTouchListener);

        mEdit = rootView.findViewById(R.id.scheme_edit);
        mEdit.setOnClickListener(this);

        mDone = rootView.findViewById(R.id.scheme_done);
        mDone.setOnClickListener(this);

        rootView.findViewById(R.id.get_scheme).setOnClickListener(this);
        rootView.findViewById(R.id.get_home).setOnClickListener(this);

        return rootView;
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
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.f1);
                bitmap1 = RotateBitmap(bitmap1,rotate);
                img.setImageBitmap(bitmap1);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(70, 70);
                params.leftMargin = x;
                params.topMargin = y;
                img.setLayoutParams(params);
                img.setOnTouchListener(this);
                break;
            case 2:
                Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.f2);
                bitmap2 = RotateBitmap(bitmap2,rotate);
                img.setImageBitmap(bitmap2);
                FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(50, 100);
                params2.leftMargin = x;
                params2.topMargin = y;
                img.setLayoutParams(params2);
                img.setOnTouchListener(this);
                break;
            case 3:
                Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.f3);
                bitmap3 = RotateBitmap(bitmap3,rotate);
                img.setImageBitmap(bitmap3);
                FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(60, 120);
                params3.leftMargin = x;
                params3.topMargin = y;
                img.setLayoutParams(params3);
                img.setOnTouchListener(this);
                break;
            case 4:
                Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(),R.drawable.angle_f);
                bitmap4 = RotateBitmap(bitmap4,rotate);
                img.setImageBitmap(bitmap4);
                FrameLayout.LayoutParams params4 = new FrameLayout.LayoutParams(60, 60);
                params4.leftMargin = x;
                params4.topMargin = y;
                img.setLayoutParams(params4);
                img.setOnTouchListener(this);
                break;
        }
        img.setTag(id);
        return img;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scheme_edit){
            modeEdit = true;
            mEdit.setEnabled(false);
            mDone.setEnabled(true);
        }
        if (v.getId() == R.id.scheme_done) {
            modeEdit = false;
            mEdit.setEnabled(true);
            mDone.setEnabled(false);
        }
        if (v.getId() == R.id.get_scheme) {
            ((MainActivity2) getActivity()).viewFragment(new DeviceList2Fragment(),"DEVICELIST");
        }
        if (v.getId() == R.id.get_home) {
            ((MainActivity2) getActivity()).viewFragment(new Start2Fragment(),"START");
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void rotateImageBm(ImageView img,int angle){
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) img.getLayoutParams();
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        bmp = RotateBitmap(bmp,angle);
        int w = img.getWidth();
        int h = img.getHeight();
        img.setImageBitmap(bmp);
        FrameLayout.LayoutParams params;
        if (angle == 90 || angle == 270) {
            params = new FrameLayout.LayoutParams(h,w);
        } else {
            params = new FrameLayout.LayoutParams(w,h);
        }
        params.setMargins(lp.leftMargin,lp.topMargin,0,0);
        img.setLayoutParams(params);
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

                    /*
                    RotateAnimation rotateAnimation = new RotateAnimation(from, to,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    rotateAnimation.setDuration(ANIMATION_DURATION);
                    rotateAnimation.setFillAfter(true);

                    (selected_item).startAnimation(rotateAnimation);
                    */
                    rotateImageBm((ImageView) selected_item,to);
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

                        CorrectCoordinate res = checkLips(laparam.leftMargin, laparam.topMargin,
                                laparam.leftMargin + selected_item.getWidth(),
                                laparam.topMargin + selected_item.getHeight(),
                                (Integer) selected_item.getTag());
                        int cX = laparam.leftMargin;
                        int cY = laparam.topMargin;
                        if (res != null && res.isCorrect()) {
                            cX = res.getX();
                            cY = res.getY();
                        }

                        if (selid != -1) {
                            DeviceModel model = mDataManager.getDeviceModels().get(selid);
                            model.setX(cX);
                            model.setY(cY);
                            mDataManager.updateDeviceModels(selid, model);
                        }
                        laparam.setMargins(cX,cY,0,0);
                        selected_item.setLayoutParams(laparam);
                        touchFlag = false;
                        if (dropFlag) {
                            dropFlag = false;
                        } /*else {
                            selected_item.setLayoutParams(imageParams);
                        } */
                        break;
                }
            }
            //mFrameLayout.invalidate();
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
                    ((MainActivity2) getActivity()).viewFragment(new Control2Fragment(), "CONTROL");
                }
            }
            return true;
        }
        return false;
    }

    private CorrectCoordinate checkLips(int currentX, int currentY, int currentRigth, int currentBottom, int id){
        CorrectCoordinate rec = null;
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
            // A =
            if (testDelta(currentX,rX) && testDelta(currentY,bY)) {
                // A = B1
                Log.d(TAG,"YES A = B1");
                rec = new CorrectCoordinate(true,rX,bY);
                return rec;
            }
            if (testDelta(currentX,x1) && testDelta(currentY,y1)) {
                Log.d(TAG,"YES A = C1");
                rec = new CorrectCoordinate(true,x1,y1);
                return rec;
            }
            if (testDelta(currentX,rigthX) && testDelta(currentY,bottomY)) {
                Log.d(TAG,"YES A = D1");
                rec = new CorrectCoordinate(true,rigthX,bottomY);
                return rec;
            }

            // C =
            if (testDelta(currentX,x) && testDelta(currentBottom,y)) {
                Log.d(TAG,"YES C = A1");
                rec = new CorrectCoordinate(true,x,y-(currentBottom-currentY));
                return rec;
            }
            if (testDelta(currentX,rX) && testDelta(currentBottom,bY)) {
                Log.d(TAG,"YES C = B1");
                rec = new CorrectCoordinate(true,rX,bY-(currentBottom-currentY));
                return rec;
            }
            // B =
            if (testDelta(currentRigth,x) && testDelta(currentY,y)) {
                Log.d(TAG,"YES B = A1");
                rec = new CorrectCoordinate(true,x-(currentRigth-currentX),y);
                return rec;
            }
            if (testDelta(currentRigth,x1) && testDelta(currentY,y1)) {
                Log.d(TAG,"YES B = C1");
                rec = new CorrectCoordinate(true,x1-(currentRigth-currentX),y1);
                return rec;
            }
            if (testDelta(currentRigth,rigthX) && testDelta(currentY,bottomY)) {
                Log.d(TAG,"YES B = D1");
                rec = new CorrectCoordinate(true,rigthX,bottomY);
                return rec;
            }
            // D =
            if (testDelta(currentRigth,x) && testDelta(currentBottom,y)){
                Log.d(TAG,"YES D = A1");
                rec = new CorrectCoordinate(true,x-(currentRigth-currentX),y-(currentBottom-currentY));
                return rec;
            }
            if (testDelta(currentRigth,x1) && testDelta(currentBottom,y1)) {
                Log.d(TAG,"YES D = C1");
                rec = new CorrectCoordinate(true,x1-(currentRigth-currentX),y1-(currentBottom-currentY));
                return rec;
            }
        }
        return rec;
    }

    private static final int DELTA_SIZE = 15;

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
