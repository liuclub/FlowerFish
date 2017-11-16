package com.bagelplay.gameset.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.DimenUtil;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.max;
import static android.R.attr.scaleType;

/**
 * Created by zhangtianjie on 2017/8/22.
 */


public class GameProgressView extends RelativeLayout {

    RelativeLayout mRlParent;

    Context mContext;

    RelativeLayout mRlBar;
    LinearLayout progress_container;

    int MaxSize = 12;

    int bar_w, bar_h;

    int total_width;

    int marginLeft;


    List<ImageView> imageViews;

    public GameProgressView(Context context) {
        super(context);
    }

    public GameProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.myGameProgress);

        MaxSize = ta.getInteger(R.styleable.myGameProgress_max_size, 0);


        LayoutInflater.from(context).inflate(R.layout.game_progress_view_layout, this, true);


        mRlParent = (RelativeLayout) findViewById(R.id.rl_parent);

        mRlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mRlBar = (RelativeLayout) findViewById(R.id.rl_bar);
        progress_container = (LinearLayout) findViewById(R.id.progress_container);

//        bar_w = getResources().getDimensionPixelSize(R.dimen.GameProgressBarWidth);
//        bar_h = getResources().getDimensionPixelSize(R.dimen.GameProgressBarHeight);
        bar_w = 50;
        bar_h = 50;
        total_width = getResources().getDimensionPixelSize(R.dimen.GameProgressViewWidth) - DimenUtil.dip2px(context, 300);
        marginLeft = (total_width - bar_w * MaxSize) / (MaxSize - 1) + bar_w;

        imageViews = new ArrayList<>();

        for (int i = 0; i < MaxSize; i++) {
            ImageView mBarIV = new ImageView(mContext);

//            mBarIV.setImageResource(R.mipmap.progress_not_active);
            mBarIV.setImageResource(R.drawable.progress_undo);
            mBarIV.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            RelativeLayout.LayoutParams barIVParams = new RelativeLayout.LayoutParams(100, 100);
            barIVParams.addRule(Gravity.CENTER_HORIZONTAL);
            if (i == 0) {
                barIVParams.leftMargin = marginLeft + DimenUtil.dip2px(context, 300);
            } else {
                barIVParams.leftMargin = marginLeft;
            }

            mBarIV.setLayoutParams(barIVParams);

            imageViews.add(mBarIV);
            progress_container.addView(mBarIV);
        }


    }


    public void setProgressCount(int count) {
        MaxSize = count;

        mRlBar.removeAllViews();

        bar_w = getResources().getDimensionPixelSize(R.dimen.GameProgressBarWidth);
        bar_h = getResources().getDimensionPixelSize(R.dimen.GameProgressBarHeight);
        total_width = getResources().getDimensionPixelSize(R.dimen.GameProgressViewWidth);

        marginLeft = (total_width - bar_w * MaxSize) / (MaxSize - 1) + bar_w;

        imageViews = new ArrayList<>();

        for (int i = 0; i < MaxSize; i++) {
            ImageView mBarIV = new ImageView(mContext);

//            mBarIV.setImageResource(R.mipmap.progress_not_active);
            mBarIV.setImageResource(R.drawable.progress_undo);

            RelativeLayout.LayoutParams barIVParams = new RelativeLayout.LayoutParams(bar_w, bar_h);


            barIVParams.leftMargin = i * marginLeft;


            mBarIV.setLayoutParams(barIVParams);


            imageViews.add(mBarIV);
            mRlBar.addView(mBarIV);
        }
    }


    public void setChooseNum(int num) {
        if (imageViews != null) {
            for (int i = 0; i < MaxSize; i++) {
                if (i <= num) {
//                    imageViews.get(i).setImageResource(R.mipmap.progress_active);
                    imageViews.get(i).setImageResource(R.drawable.progress_done);
                } else {
//                    imageViews.get(i).setImageResource(R.mipmap.progress_not_active);
                    imageViews.get(i).setImageResource(R.drawable.progress_undo);
                }
            }

        }
    }

    public GameProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
