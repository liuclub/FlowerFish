package com.bagelplay.gameset.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;

/**
 * Created by zhangtianjie on 2017/8/1.
 */

public class FinishGameView extends RelativeLayout {

    private String Tag = "FinishGameView";
    RelativeLayout mRlParent, mRlAnim;
    ImageView mIvFish, mIvPetal;

//    ImageView mIvRight, mIvWrong;

    private int TIME = 1000;
    private int MaxTime = 11;
    private int i = 0;
    private Handler timeHandler;

    // int mIvWidth, mIvHeight;

    public FinishGameView(Context context) {
        super(context);
    }

    Animation fishAnimation, petalAnimation;

    Context mContext;

    public FinishGameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.finish_game_view_layout, this, true);


        findView();
        mRlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


//        mIvRight.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.btn_normal_to_large);
//                v.startAnimation(animation);
//
//                if (mRightWrongClickLinstener != null) {
//                    mRightWrongClickLinstener.RightClick();
//                }
//            }
//        });
//
//
//        mIvWrong.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.btn_normal_to_large);
//                v.startAnimation(animation);
//
//                if (mRightWrongClickLinstener != null) {
//
//                    mRightWrongClickLinstener.WrongClick();
//                }
//            }
//        });


    }


    public  void setPetalandBg(int petalImg,int gameBgImg){

        mIvPetal.setImageResource(petalImg);
        mRlParent.setBackgroundDrawable(getResources().getDrawable(gameBgImg));



    }


    private void resetPetal() {

        mIvPetal.setVisibility(GONE);

        mIvPetal.clearAnimation();
    }

    public void startAnimation() {


        resetPetal();

        fishAnimation = AnimationUtils.loadAnimation(mContext, R.anim.flash_three_times);

        fishAnimation.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.d(Tag, "onAnimationStart");


                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.d(Tag, "onAnimationEnd");
                        mIvPetal.setVisibility(VISIBLE);
                        mIvPetal.startAnimation(petalAnimation);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        Log.d(Tag, "onAnimationRepeat");
                    }
                }
        );

        mIvFish.startAnimation(fishAnimation);

        petalAnimation = AnimationUtils.loadAnimation(mContext, R.anim.num_game_finish_petal_anim);


        petalAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                timeHandler = new Handler();

                timeHandler.postDelayed(runnable, TIME);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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


                        if (mFinishTimeLinstener != null) {
                            mFinishTimeLinstener.timeFinish();
                        }

                    }
                }


            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    };


    public interface FinishTimeLinstener {
        void timeFinish();


    }
    FinishTimeLinstener mFinishTimeLinstener;

    public void setOnTimeFinishListener(FinishTimeLinstener mFinishTimeLinstener){
        this.mFinishTimeLinstener=mFinishTimeLinstener;
    }


//
//    RightWrongClickLinstener mRightWrongClickLinstener;
//
//    public void setOnRightWrongClickLinstener(RightWrongClickLinstener mRightWrongClickLinstener) {
//        this.mRightWrongClickLinstener = mRightWrongClickLinstener;
//    }


//    public interface RightWrongClickLinstener {
//        void RightClick();
//
//        void WrongClick();
//    }

    private void findView() {
        mRlParent = (RelativeLayout) findViewById(R.id.rl_parent);
        mRlAnim = (RelativeLayout) findViewById(R.id.rl_anim);
        mIvFish = (ImageView) findViewById(R.id.iv_fish);
        mIvPetal = (ImageView) findViewById(R.id.iv_petal);

//        mIvRight = (ImageView) findViewById(R.id.iv_right);
//        mIvWrong = (ImageView) findViewById(R.id.iv_wrong);

    }

    public FinishGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
