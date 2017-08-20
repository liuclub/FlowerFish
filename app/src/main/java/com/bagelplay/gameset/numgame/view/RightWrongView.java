package com.bagelplay.gameset.numgame.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.DimenUtil;

/**
 * Created by zhangtianjie on 2017/7/28.
 */

public class RightWrongView extends RelativeLayout {
    ImageView mIvRight, mIvWrong;
    String Tag = "RightWrongView";

    RelativeLayout mRlParent;

    int mIvWidth, mIvHeight;


    public RightWrongView(Context context) {
        super(context);
    }

    public RightWrongView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.right_wrong_layout, this, true);
        mIvRight = (ImageView) findViewById(R.id.iv_right);
        mIvWrong = (ImageView) findViewById(R.id.iv_wrong);

        mRlParent = (RelativeLayout) findViewById(R.id.rl_parent);


        int w = getResources().getDimensionPixelSize(R.dimen.RightWrongBtnWidth);

        int h = getResources().getDimensionPixelSize(R.dimen.RightWrongBtnHeight);


        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        mIvWidth = DimenUtil.dip2px(context, w);
        mIvHeight = DimenUtil.dip2px(context, h);


        mRlParent.setOnTouchListener(new OnTouchListener() {
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

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvRight.getLayoutParams();


                        para.height = (int) (mIvWidth * 1.5);
                        para.width = (int) (mIvHeight * 1.5);


                        mIvRight.setLayoutParams(para);


                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "R~up");


                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvRight.getLayoutParams();


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

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvWrong.getLayoutParams();


                        para.height = (int) (mIvWidth * 1.5);
                        para.width = (int) (mIvHeight * 1.5);


                        mIvWrong.setLayoutParams(para);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //  Log.d(Tag, "W~up");

                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvWrong.getLayoutParams();


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





    RightWrongClickLinstener mRightWrongClickLinstener;

    public void setOnRightWrongClickLinstener(RightWrongClickLinstener mRightWrongClickLinstener) {
        this.mRightWrongClickLinstener = mRightWrongClickLinstener;
    }


    public interface RightWrongClickLinstener {
        void RightClick();

        void WrongClick();
    }
}
