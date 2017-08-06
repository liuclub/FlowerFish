package com.bagelplay.flowerfish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bagelplay.flowerfish.R;
import com.bagelplay.flowerfish.utils.DimenUtil;

/**
 * Created by zhangtianjie on 2017/8/5.
 */

public class GamePauseView extends RelativeLayout {

    public GamePauseView(Context context) {
        super(context);
    }

    RelativeLayout mRlParent;
    ImageView mIvPauseRestart, mIvPauseBackHome, mIvPauseBack;

    int mIvPauseRestartWidth, mIvPauseRestartHeight;
    int mIvPauseBackHomeWidth, mIvPauseBackHomeHeight;
    int mIvPauseBackWidth, mIvPauseBackHeight;


    public GamePauseView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.game_pause_view_layout, this, true);
        findView();

        getWidthHeight(context);


        setOnTouchEvent();


    }

    private void getWidthHeight(Context context) {
        //restart
        int w = getResources().getDimensionPixelSize(R.dimen.PauseRestartWidth);

        int h = getResources().getDimensionPixelSize(R.dimen.PauseRestartHeight);


        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        mIvPauseRestartWidth = DimenUtil.dip2px(context, w);
        mIvPauseRestartHeight = DimenUtil.dip2px(context, h);


        //backhome
        w = getResources().getDimensionPixelSize(R.dimen.PauseBackHomeWidth);

        h = getResources().getDimensionPixelSize(R.dimen.PauseBackHomeHeight);


        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        mIvPauseBackHomeWidth = DimenUtil.dip2px(context, w);
        mIvPauseBackHomeHeight = DimenUtil.dip2px(context, h);


        //back
        w = getResources().getDimensionPixelSize(R.dimen.PauseBackWidth);

        h = getResources().getDimensionPixelSize(R.dimen.PauseBackHeight);


        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        mIvPauseBackWidth = DimenUtil.dip2px(context, w);
        mIvPauseBackHeight = DimenUtil.dip2px(context, h);

    }

    private void setOnTouchEvent() {
        mRlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mIvPauseRestart.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //   Log.d(Tag, "W~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvPauseRestart.getLayoutParams();


                        para.width = (int) (mIvPauseRestartWidth * 1.3);

                        para.height = (int) (mIvPauseRestartHeight * 1.3);

                        mIvPauseRestart.setLayoutParams(para);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "W~up");

                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvPauseRestart.getLayoutParams();


                        para1.width = mIvPauseRestartWidth;
                        para1.height = mIvPauseRestartHeight;
                        mIvPauseRestart.setLayoutParams(para1);

                        if (mGamePauseClickLinstener != null) {
                            mGamePauseClickLinstener.pauseRestart();
                        }

                        break;
                }


                return true;

            }
        });


        mIvPauseBackHome.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //   Log.d(Tag, "W~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvPauseBackHome.getLayoutParams();


                        para.width = (int) (mIvPauseBackHomeWidth * 1.3);

                        para.height = (int) (mIvPauseBackHomeHeight * 1.3);

                        mIvPauseBackHome.setLayoutParams(para);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "W~up");

                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvPauseBackHome.getLayoutParams();


                        para1.width = mIvPauseBackHomeWidth;
                        para1.height = mIvPauseBackHomeHeight;
                        mIvPauseBackHome.setLayoutParams(para1);

                        if (mGamePauseClickLinstener != null) {
                            mGamePauseClickLinstener.pauseBackHome();
                        }

                        break;
                }


                return true;

            }
        });


        mIvPauseBack.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //   Log.d(Tag, "W~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvPauseBack.getLayoutParams();


                        para.width = (int) (mIvPauseBackWidth * 1.3);

                        para.height = (int) (mIvPauseBackHeight * 1.3);

                        mIvPauseBack.setLayoutParams(para);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "W~up");

                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvPauseBack.getLayoutParams();


                        para1.width = mIvPauseBackWidth;
                        para1.height = mIvPauseBackHeight;
                        mIvPauseBack.setLayoutParams(para1);

                        if (mGamePauseClickLinstener != null) {
                            mGamePauseClickLinstener.pauseBack();
                        }

                        break;
                }


                return true;

            }
        });

    }


    private void findView() {
        mRlParent = (RelativeLayout) findViewById(R.id.rl_parent);
        mIvPauseRestart = (ImageView) findViewById(R.id.iv_pause_restart);
        mIvPauseBackHome = (ImageView) findViewById(R.id.iv_pause_backhome);
        mIvPauseBack = (ImageView) findViewById(R.id.iv_pause_back);


    }

    public GamePauseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    GamePauseClickLinstener mGamePauseClickLinstener;

    public void setOnGamePauseClickLinstener(GamePauseClickLinstener mGamePauseClickLinstener) {
        this.mGamePauseClickLinstener = mGamePauseClickLinstener;
    }


    public interface GamePauseClickLinstener {
        void pauseRestart();

        void pauseBackHome();

        void pauseBack();
    }
}
