package com.bagelplay.flowerfish.view;

import android.content.Context;
import android.os.Handler;
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

    private Handler timeHandler;

    private int TIME = 1000;
    private int i = 0;

    private int MaxTime = 9;


    public NumGameCongrationView(Context context) {
        super(context);

    }

    public NumGameCongrationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
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

    public void startAnimation(int animationTime) {

        MaxTime= animationTime;
        i = 0;
        scaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.num_game_congration_anim);


        mRlCongrationLine.startAnimation(scaleAnimation);

        timeHandler = new Handler();

        timeHandler.postDelayed(runnable, TIME);


    }

    private NumGameCongrationFinishListener mNumGameCongrationFinishListener;

    public void setOnNumGameCongrationFinishListener(NumGameCongrationFinishListener numGameCongrationFinishListener) {
        this.mNumGameCongrationFinishListener = numGameCongrationFinishListener;
    }

    public interface NumGameCongrationFinishListener {
        void numGameConfigurationFinish();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                i++;
                if (i < MaxTime) {
                    timeHandler.postDelayed(this, TIME);
                } else {
                    if (timeHandler != null) {
                        timeHandler = null;
                        i = 0;
                        if (mNumGameCongrationFinishListener != null) {
                            mNumGameCongrationFinishListener.numGameConfigurationFinish();
                        }

                    }
                }


            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    };


    private void findView() {

        mLlParent = (LinearLayout) findViewById(R.id.ll_parent);

        mRlCongrationLine = (RelativeLayout) findViewById(R.id.rl_congration_line);


    }

    public NumGameCongrationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
