package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liubo on 2017/11/1.
 */

public class TestView extends View implements View.OnTouchListener{
    public TestView(Context context) {
        super(context);
    }

    int lastX,lastY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                layout(getLeft()+offsetX, getTop()+offsetY,getRight()+offsetX , getBottom()+offsetY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }


        return false;
    }
}
