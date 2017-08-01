package com.bagelplay.flowerfish.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.flowerfish.R;

/**
 * Created by zhangtianjie on 2017/8/1.
 */

public class NumGameCongrationView extends LinearLayout {

    LinearLayout mLlParent;
    RelativeLayout mRlCongrationLine;

    Context mContext;


    public NumGameCongrationView(Context context) {
        super(context);

    }

    public NumGameCongrationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext=context;
        LayoutInflater.from(context).inflate(R.layout.num_game_congration_view_layout, this, true);

        findView();
        mLlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });





    }
    Animation scaleAnimation;

    public void startAnimation(){

        scaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.num_game_congration_anim);



        mRlCongrationLine.startAnimation(scaleAnimation);

    }



    private void findView() {

        mLlParent= (LinearLayout) findViewById(R.id.ll_parent);

        mRlCongrationLine= (RelativeLayout) findViewById(R.id.rl_congration_line);



    }

    public NumGameCongrationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
