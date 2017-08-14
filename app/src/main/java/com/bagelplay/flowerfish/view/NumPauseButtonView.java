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
 * Created by zhangtianjie on 2017/8/14.
 */

public class NumPauseButtonView extends RelativeLayout {

    ImageView mIvPause;
    RelativeLayout mRlParent;

    int mIvPauseWidth,mIvPauseHeight;

    public NumPauseButtonView(Context context) {
        super(context);
    }

    public NumPauseButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.num_pause_button_view_layout, this, true);
        mRlParent= (RelativeLayout) findViewById(R.id.rl_parent);
        mRlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        int w = getResources().getDimensionPixelSize(R.dimen.PauseButtonWidth);

        int h = getResources().getDimensionPixelSize(R.dimen.PauseButtonHeight);


        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        mIvPauseWidth = DimenUtil.dip2px(context, w);
        mIvPauseHeight = DimenUtil.dip2px(context, h);

        mIvPause= (ImageView) findViewById(R.id.iv_pause);

        mIvPause.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //   Log.d(Tag, "W~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvPause.getLayoutParams();


                        para.width = (int) (mIvPauseWidth * 1.3);

                        para.height = (int) (mIvPauseHeight * 1.3);

                        mIvPause.setLayoutParams(para);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "W~up");

                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvPause.getLayoutParams();


                        para1.width = mIvPauseWidth;
                        para1.height = mIvPauseHeight;
                        mIvPause.setLayoutParams(para1);

                        if (mPauseButtonViewClickLinstener != null) {
                            mPauseButtonViewClickLinstener.pauseHomeClick();
                        }

                        break;
                }


                return true;
            }
        });
    }

    public NumPauseButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    PauseButtonViewClickLinstener mPauseButtonViewClickLinstener;

    public void setOnPauseButtonViewClickLinstener(PauseButtonViewClickLinstener mPauseButtonViewClickLinstener) {
        this.mPauseButtonViewClickLinstener = mPauseButtonViewClickLinstener;
    }


    public interface PauseButtonViewClickLinstener {
        void pauseHomeClick();


    }


}
