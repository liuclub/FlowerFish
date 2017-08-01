package com.bagelplay.flowerfish.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bagelplay.flowerfish.R;
import com.bagelplay.flowerfish.utils.DimenUtil;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.bagelplay.flowerfish.utils.DimenUtil.px2Dp;

/**
 * Created by zhangtianjie on 2017/7/28.
 */

public class RightWrongView extends LinearLayout {
    ImageView mIvRight, mIvWrong;
    String Tag = "RightWrongView";

    LinearLayout mLlParent;

    int mIvWidth, mIvHeight;


    public RightWrongView(Context context) {
        super(context);
    }

    public RightWrongView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.right_wrong_layout, this, true);
        mIvRight = (ImageView) findViewById(R.id.iv_right);
        mIvWrong = (ImageView) findViewById(R.id.iv_wrong);

        mLlParent = (LinearLayout) findViewById(R.id.ll_parent);


        int w = getResources().getDimensionPixelSize(R.dimen.RightWrongBtnWidth);

        int h = getResources().getDimensionPixelSize(R.dimen.RightWrongBtnHeight);


        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        mIvWidth = DimenUtil.dip2px(context, w);
        mIvHeight = DimenUtil.dip2px(context, h);


        mLlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return true;
            }
        });

        mIvRight.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Log.d(Tag, "R~down");

                        LayoutParams para = (LayoutParams) mIvRight.getLayoutParams();


                        para.height = (int) (mIvWidth * 1.5);
                        para.width = (int) (mIvHeight * 1.5);


                        mIvRight.setLayoutParams(para);


                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "R~up");


                        LayoutParams para1 = (LayoutParams) mIvRight.getLayoutParams();


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

                        LayoutParams para = (LayoutParams) mIvWrong.getLayoutParams();


                        para.height = (int) (mIvWidth * 1.5);
                        para.width = (int) (mIvHeight * 1.5);


                        mIvWrong.setLayoutParams(para);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "W~up");

                        LayoutParams para1 = (LayoutParams) mIvWrong.getLayoutParams();


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





    public RightWrongView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RightWrongView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    RightWrongClickLinstener mRightWrongClickLinstener;

    public void setOnRightWrongClickLinstener(RightWrongClickLinstener mRightWrongClickLinstener) {
        this.mRightWrongClickLinstener = mRightWrongClickLinstener;
    }


    public interface RightWrongClickLinstener {
        void RightClick();

        void WrongClick();
    }
}
