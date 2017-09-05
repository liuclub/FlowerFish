package com.bagelplay.gameset.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;

/**
 * Created by zhangtianjie on 2017/8/1.
 */

public class GameCongrationView extends LinearLayout {

    LinearLayout mLlParent;
    RelativeLayout mRlCongrationLine;

    Context mContext;

    private Handler timeHandler;

    private int TIME = 1000;
    private int i = 0;

    private int MaxTime = 9;

    private ImageView mIvCongrationObject, mIvCongrationWord;


    public GameCongrationView(Context context) {
        super(context);

    }

    public GameCongrationView(Context context, @Nullable AttributeSet attrs) {
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

        MaxTime = animationTime;
        i = 0;
        scaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.num_game_congration_anim);


        mRlCongrationLine.startAnimation(scaleAnimation);

        timeHandler = new Handler();

        timeHandler.postDelayed(runnable, TIME);


    }

    private GameCongrationFinishListener mGameCongrationFinishListener;

    public void setOnGameCongrationFinishListener(GameCongrationFinishListener mGameCongrationFinishListener) {
        this.mGameCongrationFinishListener = mGameCongrationFinishListener;
    }

    public interface GameCongrationFinishListener {
        void gameConfigurationFinish();
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
                        if (mGameCongrationFinishListener != null) {
                            mGameCongrationFinishListener.gameConfigurationFinish();
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

        mIvCongrationObject = (ImageView) findViewById(R.id.iv_congration_fish);


        mIvCongrationWord = (ImageView) findViewById(R.id.iv_congration_word);
    }

    public void setCongrationBg(int objectImgId) {

        mIvCongrationObject.setImageResource(objectImgId);

        //mIvCongrationWord.setImageResource(wordImgId);
    }

    public GameCongrationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
