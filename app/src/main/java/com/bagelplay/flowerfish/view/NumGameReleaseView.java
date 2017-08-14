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

public class NumGameReleaseView extends RelativeLayout {

    ImageView mIvRelease;
    RelativeLayout mRlParent;

    int mIvReleaseWidth,mIvReleaseHeight;

    public NumGameReleaseView(Context context) {
        super(context);
    }

    public NumGameReleaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.num_game_release_view_layout, this, true);

        mRlParent= (RelativeLayout) findViewById(R.id.rl_parent);
        mRlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        int w = getResources().getDimensionPixelSize(R.dimen.ReleaseButtonWidth);

        int h = getResources().getDimensionPixelSize(R.dimen.ReleaseButtonHeight);


        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        mIvReleaseWidth = DimenUtil.dip2px(context, w);
        mIvReleaseHeight = DimenUtil.dip2px(context, h);

        mIvRelease= (ImageView) findViewById(R.id.iv_release);

        mIvRelease.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //   Log.d(Tag, "W~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvRelease.getLayoutParams();


                        para.width = (int) (mIvReleaseWidth * 1.3);

                        para.height = (int) (mIvReleaseHeight * 1.3);

                        mIvRelease.setLayoutParams(para);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "W~up");

                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvRelease.getLayoutParams();


                        para1.width = mIvReleaseWidth;
                        para1.height = mIvReleaseHeight;
                        mIvRelease.setLayoutParams(para1);

                        if (mReleaseButtonViewClickLinstener != null) {
                            mReleaseButtonViewClickLinstener.releaseButtonClick();
                        }

                        break;
                }


                return true;
            }
        });

    }

    public NumGameReleaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }






    ReleaseButtonViewClickLinstener mReleaseButtonViewClickLinstener;

    public void setOnReleaseButtonViewClickLinstener(ReleaseButtonViewClickLinstener mReleaseButtonViewClickLinstener) {
        this.mReleaseButtonViewClickLinstener = mReleaseButtonViewClickLinstener;
    }


    public interface ReleaseButtonViewClickLinstener {
        void releaseButtonClick();


    }


}
