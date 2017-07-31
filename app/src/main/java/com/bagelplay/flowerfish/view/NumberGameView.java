package com.bagelplay.flowerfish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.flowerfish.R;
import com.bagelplay.flowerfish.utils.DimenUtil;

import static com.bagelplay.flowerfish.utils.DimenUtil.px2Dp;


/**
 * Created by zhangtianjie on 2017/7/28.
 */


public class NumberGameView extends RelativeLayout {


    String Tag = "NumberGameView";

    ImageView mIvNumLeft, mIvNumCenter, mIvNumRight;

    RelativeLayout mLlNumLine;

    RelativeLayout mRlParent;

    int mNumIvWidth, mNumIvHeight;

    public NumberGameView(Context context) {
        super(context);
    }

    public NumberGameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.number_game_layout, this, true);

        findView();

        int w = getResources().getDimensionPixelSize(R.dimen.NumGameNumLineIVWdith);

        int h = getResources().getDimensionPixelSize(R.dimen.NumGameNumLineIVHeight);


        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        mNumIvWidth = DimenUtil.dip2px(context, w);

        mNumIvHeight = DimenUtil.dip2px(context, h);


        mRlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        mIvNumLeft.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(Tag, "left~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvNumLeft.getLayoutParams();


                        para.height = (int) (mNumIvWidth * 1.3);
                        para.width = (int) (mNumIvHeight * 1.3);


                        mIvNumLeft.setLayoutParams(para);


                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d(Tag, "left~up");


                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvNumLeft.getLayoutParams();

                        para1.height = mNumIvWidth;
                        para1.width = mNumIvHeight;

                        mIvNumLeft.setLayoutParams(para1);


                        break;
                }


                return true;
            }
        });





        mIvNumCenter.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(Tag, "center~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvNumCenter.getLayoutParams();


                        para.height = (int) (mNumIvWidth * 1.3);
                        para.width = (int) (mNumIvHeight * 1.3);


                        mIvNumCenter.setLayoutParams(para);


                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d(Tag, "center~up");


                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvNumCenter.getLayoutParams();

                        para1.height = mNumIvWidth;
                        para1.width = mNumIvHeight;

                        mIvNumCenter.setLayoutParams(para1);


                        break;
                }


                return true;
            }
        });




        mIvNumRight.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(Tag, "right~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvNumRight.getLayoutParams();


                        para.height = (int) (mNumIvWidth * 1.3);
                        para.width = (int) (mNumIvHeight * 1.3);


                        mIvNumRight.setLayoutParams(para);



                      //  mRlParent.setVisibility(View.GONE);

                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d(Tag, "right~up");


                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvNumRight.getLayoutParams();

                        para1.height = mNumIvWidth;
                        para1.width = mNumIvHeight;

                        mIvNumRight.setLayoutParams(para1);


                        break;
                }


                return true;
            }
        });

    }




    private void findView() {
        mRlParent = (RelativeLayout) findViewById(R.id.rl_parent);
        mIvNumLeft = (ImageView) findViewById(R.id.iv_num_left);
        mIvNumCenter = (ImageView) findViewById(R.id.iv_num_center);
        mIvNumRight = (ImageView) findViewById(R.id.iv_num_right);
        mLlNumLine = (RelativeLayout) findViewById(R.id.ll_num_line);
    }

    public NumberGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
