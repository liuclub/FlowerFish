package com.bagelplay.flowerfish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.flowerfish.R;
import com.bagelplay.flowerfish.utils.DimenUtil;

/**
 * Created by zhangtianjie on 2017/8/1.
 */

public class FinishGameView extends RelativeLayout {

    private String Tag = "FinishGameView";
    RelativeLayout mRlParent, mRlAnim;
    ImageView mIvFish, mIvPetal;

    ImageView mIvRight, mIvWrong;

    int mIvWidth, mIvHeight;

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





        int w = getResources().getDimensionPixelSize(R.dimen.FinishGameRightWrongBtnWidth);

        int h = getResources().getDimensionPixelSize(R.dimen.FinishGameRightWrongBtnHeight);


        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        mIvWidth = DimenUtil.dip2px(context, w);
        mIvHeight = DimenUtil.dip2px(context, h);


        mIvRight.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Log.d(Tag, "R~down");

                        LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) mIvRight.getLayoutParams();


                        para.height = (int) (mIvWidth * 1.5);
                        para.width = (int) (mIvHeight * 1.5);


                        mIvRight.setLayoutParams(para);


                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                          Log.d(Tag, "R~up");


                        LinearLayout.LayoutParams para1 = (LinearLayout.LayoutParams) mIvRight.getLayoutParams();


                        para1.height = mIvHeight;
                        para1.width = mIvWidth;
                        mIvRight.setLayoutParams(para1);

                        if (mRightWrongClickLinstener != null) {
                            mRightWrongClickLinstener.RightClick();
                        }

                        break;
                }


                return true;
            }
        });

        mIvWrong.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //   Log.d(Tag, "W~down");

                        LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) mIvWrong.getLayoutParams();


                        para.height = (int) (mIvWidth * 1.5);
                        para.width = (int) (mIvHeight * 1.5);


                        mIvWrong.setLayoutParams(para);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                         Log.d(Tag, "W~up");

                        LinearLayout.LayoutParams para1 = (LinearLayout.LayoutParams) mIvWrong.getLayoutParams();


                        para1.height = mIvHeight;
                        para1.width = mIvWidth;
                        mIvWrong.setLayoutParams(para1);
                        if (mRightWrongClickLinstener != null) {
                            mRightWrongClickLinstener.WrongClick();
                        }

                        break;
                }


                return true;
            }
        });


    }

    public void startAnimation() {
        fishAnimation = AnimationUtils.loadAnimation(mContext, R.anim.num_game_finish_fish_anim);

        fishAnimation.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.d(Tag, "onAnimationStart");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.d(Tag, "onAnimationEnd");

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
    }


    RightWrongClickLinstener mRightWrongClickLinstener;

    public void setOnRightWrongClickLinstener(RightWrongClickLinstener mRightWrongClickLinstener) {
        this.mRightWrongClickLinstener = mRightWrongClickLinstener;
    }


    public interface RightWrongClickLinstener {
        void RightClick();

        void WrongClick();
    }

    private void findView() {
        mRlParent = (RelativeLayout) findViewById(R.id.rl_parent);
        mRlAnim = (RelativeLayout) findViewById(R.id.rl_anim);
        mIvFish = (ImageView) findViewById(R.id.iv_fish);
        mIvPetal = (ImageView) findViewById(R.id.iv_petal);

        mIvRight = (ImageView) findViewById(R.id.iv_right);
        mIvWrong = (ImageView) findViewById(R.id.iv_wrong);

    }

    public FinishGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
