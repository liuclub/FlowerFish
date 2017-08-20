package com.bagelplay.gameset.numgame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;

/**
 * Created by zhangtianjie on 2017/8/6.
 */

public class PauseButtonView extends RelativeLayout {

   ImageView mIvHome;
    RelativeLayout mRlParent;

    int mIvHomeWidth,mIvHomeHeight;
    Context mContext;
    public PauseButtonView(Context context) {
        super(context);
    }

    public PauseButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        LayoutInflater.from(context).inflate(R.layout.pause_button_view_layout, this, true);
        mRlParent= (RelativeLayout) findViewById(R.id.rl_parent);
        mRlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        int w = getResources().getDimensionPixelSize(R.dimen.PauseHomeWidth);

        int h = getResources().getDimensionPixelSize(R.dimen.PauseHomeHeight);


//        w = DimenUtil.px2Dp(context, w);
//
//        h = DimenUtil.px2Dp(context, h);


//        mIvHomeWidth = DimenUtil.dip2px(context, w);
//        mIvHomeHeight = DimenUtil.dip2px(context, h);
          mIvHomeWidth=w;
          mIvHomeHeight=h;


        mIvHome= (ImageView) findViewById(R.id.iv_home);




     mIvHome.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //   Log.d(Tag, "W~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvHome.getLayoutParams();


                        para.width = (int) (mIvHomeWidth * 1.3);

                        para.height = (int) (mIvHomeHeight * 1.3);

                        mIvHome.setLayoutParams(para);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "W~up");

                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvHome.getLayoutParams();


                        para1.width = mIvHomeWidth;
                        para1.height = mIvHomeHeight;
                        mIvHome.setLayoutParams(para1);

                        if (mPauseButtonViewClickLinstener != null) {
                            mPauseButtonViewClickLinstener.pauseHomeClick();
                        }

                        break;
                }


                return true;
            }
        });

    }


   PauseButtonViewClickLinstener mPauseButtonViewClickLinstener;

    public void setOnPauseButtonViewClickLinstener(PauseButtonViewClickLinstener mPauseButtonViewClickLinstener) {
        this.mPauseButtonViewClickLinstener = mPauseButtonViewClickLinstener;
    }


    public interface PauseButtonViewClickLinstener {
        void pauseHomeClick();


    }

    public PauseButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
