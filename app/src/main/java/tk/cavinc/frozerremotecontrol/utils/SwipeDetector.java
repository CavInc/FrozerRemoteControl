package tk.cavinc.frozerremotecontrol.utils;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cav on 04.08.19.
 */

public class SwipeDetector implements View.OnTouchListener {

    public static enum Action {
        LR, // Слева направо
        RL, // Справа налево
        TB, // Сверху вниз
        BT, // Снизу вверх
        None // не обнаружено действий
    }

    private static final int HORIZONTAL_MIN_DISTANCE = 60; // Минимальное расстояние для свайпа по горизонтали  было 100 последнее 80
    private static final int VERTICAL_MIN_DISTANCE = 80; // Минимальное расстояние для свайпа по вертикали
    private float downX, downY, upX, upY; // Координаты
    private Action mSwipeDetected = Action.None; // Последнее дейтсвие

    public boolean swipeDetected() {
        return mSwipeDetected != Action.None;
    }

    public Action getAction() {
        return mSwipeDetected;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                mSwipeDetected = Action.None;
                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // Обнаружение горизонтального свайпа
                if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
                    // Слева направо
                    if (deltaX < 0) {
                        mSwipeDetected = Action.LR;
                        return true;
                    }
                    // Справа налево
                    if (deltaX > 0) {
                        mSwipeDetected = Action.RL;
                        return true;
                    }
                } else

                    // Обнаружение вертикального свайпа
                    if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) {
                        // Сверху вниз
                        if (deltaY < 0) {
                            mSwipeDetected = Action.TB;
                            return false;
                        }
                        // Снизу вверх
                        if (deltaY > 0) {
                            mSwipeDetected = Action.BT;
                            return false;
                        }
                    }
                return true;
            }
        }
        return false;
    }



}
