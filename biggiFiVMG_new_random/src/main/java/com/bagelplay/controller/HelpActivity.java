package com.bagelplay.controller;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bagelplay.controller.com.R;
import com.bagelplay.controller.domotion.DoMotionView;
import com.bagelplay.controller.domotion.DoMotionViewManager;
import com.bagelplay.controller.widget.BaseActivity;


public class HelpActivity extends BaseActivity {

    private View frameV;

    private ImageView helpIV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help);

        frameV = this.findViewById(R.id.frame);

        helpIV = (ImageView) this.findViewById(R.id.help);

        DoMotionView dmv = DoMotionViewManager.getCurrentDoMotionView();
        if (dmv != null) {
            frameV.setBackgroundDrawable(new BitmapDrawable(dmv.getBg()));
        }
    }

    @Override
    public void orientationChanged(int orientation) {

    }

}
