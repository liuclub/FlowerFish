/*
 *
 * MinimalDisplay.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.bagelplay.gameset.player;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Description:
 */
public class MinimalDisplay implements IMDisplay {

    private SurfaceView surfaceView;

    public MinimalDisplay(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    @Override
    public View getDisplayView() {
        return surfaceView;
    }

    @Override
    public SurfaceHolder getHolder() {
        return surfaceView.getHolder();
    }

    @Override
    public void onStart(IMPlayer player) {

    }

    @Override
    public void onPause(IMPlayer player) {

    }

    @Override
    public void onResume(IMPlayer player) {

    }

    @Override
    public void onComplete(IMPlayer player) {

    }

}
