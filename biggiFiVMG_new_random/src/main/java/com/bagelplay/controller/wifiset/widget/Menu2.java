package com.bagelplay.controller.wifiset.widget;

import com.bagelplay.controller.com.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;


public class Menu2 extends LinearLayout {

    private static final int MSG_WHAT_SHOW = 1;

    private View moreV;

    private View more2V;

    private View touchV;

    private View mouseV;

    private View slideV;

    private View handlerV;

    private View remoteV;

    private View frameV;


    public Menu2(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.menu2, null);
        addView(view);

        moreV = findViewById(R.id.more);
        more2V = findViewById(R.id.more2);
        touchV = findViewById(R.id.touch);
        mouseV = findViewById(R.id.mouse);
        slideV = findViewById(R.id.slide);
        handlerV = findViewById(R.id.handler);
        remoteV = findViewById(R.id.remote);
        frameV = findViewById(R.id.frame);


        moreV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                show();
            }
        });

        more2V.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hide();
            }
        });

        touchV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v("=-----------=", "homeV click");
            }
        });

        mouseV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v("=------------=", "settingsV click");
            }
        });

    }

    public void show() {
        moreV.setVisibility(View.GONE);
        frameV.setVisibility(View.VISIBLE);
    }

    public void hide() {
        moreV.setVisibility(View.VISIBLE);
        frameV.setVisibility(View.GONE);
    }

}
