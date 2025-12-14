package ua.lab3.maze.ui;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeController extends GestureDetector.SimpleOnGestureListener {

    public interface OnSwipeListener {
        void onSwipeUp();
        void onSwipeRight();
        void onSwipeDown();
        void onSwipeLeft();
    }

    private final OnSwipeListener listener;

    private static final int SWIPE_THRESHOLD = 80;
    private static final int SWIPE_VELOCITY_THRESHOLD = 80;

    public SwipeController(OnSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true; // must return true to receive further events
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) return false;

        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) listener.onSwipeRight();
                else listener.onSwipeLeft();
                return true;
            }
        } else {
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) listener.onSwipeDown();
                else listener.onSwipeUp();
                return true;
            }
        }
        return false;
    }
}
