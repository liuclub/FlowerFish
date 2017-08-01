package com.bagelplay.flowerfish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.flowerfish.R;
import com.bagelplay.flowerfish.utils.DimenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.media.CamcorderProfile.get;
import static com.bagelplay.flowerfish.R.drawable.num1;
import static com.bagelplay.flowerfish.R.drawable.num2;


/**
 * Created by zhangtianjie on 2017/7/28.
 */


public class NumberGameView extends RelativeLayout {


    String Tag = "NumberGameView";

    ImageView mIvNumLeft, mIvNumCenter, mIvNumRight;

    RelativeLayout mLlNumLine;

    RelativeLayout mRlParent;

    int mNumIvWidth, mNumIvHeight;

    FrameLayout mFlFishParent;

    ImageView mfish1_row, mfish2_row, mfish3_row, mfish4_row, mfish5_row, mfish6_row, mfish7_row, mfish8_row, mfish9_row;


    ImageView mfish1_line, mfish2_line, mfish3_line, mfish4_line, mfish5_line, mfish6_line, mfish7_line, mfish8_line, mfish9_line;


    List<ImageView> mFishsIV_row, mFishsIV_line;


    List<ImageView> mNumIVs;

    int mFishsNumDrawables[];



    LinearLayout mLlFishTemplateRow, mLlFishTemplateLine;

    private int fishNum;  //正确的鱼数量


    public NumberGameView(Context context) {
        super(context);
    }

    public NumberGameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.number_game_layout, this, true);

        findView();

        int w = getResources().getDimensionPixelSize(R.dimen.NumGameNumLineIVWidth);

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
                        //Log.d(Tag, "left~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvNumLeft.getLayoutParams();


                        para.height = (int) (mNumIvWidth * 1.3);
                        para.width = (int) (mNumIvHeight * 1.3);


                        mIvNumLeft.setLayoutParams(para);


                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        //Log.d(Tag, "left~up");


                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvNumLeft.getLayoutParams();

                        para1.height = mNumIvWidth;
                        para1.width = mNumIvHeight;

                        mIvNumLeft.setLayoutParams(para1);

                        interpretationNum(mIvNumLeft);
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
                       // Log.d(Tag, "center~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvNumCenter.getLayoutParams();


                        para.height = (int) (mNumIvWidth * 1.3);
                        para.width = (int) (mNumIvHeight * 1.3);


                        mIvNumCenter.setLayoutParams(para);


                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                       // Log.d(Tag, "center~up");


                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvNumCenter.getLayoutParams();

                        para1.height = mNumIvWidth;
                        para1.width = mNumIvHeight;

                        mIvNumCenter.setLayoutParams(para1);


                        interpretationNum(mIvNumCenter);

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
                        //Log.d(Tag, "right~down");

                        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mIvNumRight.getLayoutParams();


                        para.height = (int) (mNumIvWidth * 1.3);
                        para.width = (int) (mNumIvHeight * 1.3);


                        mIvNumRight.setLayoutParams(para);


                        //  mRlParent.setVisibility(View.GONE);

                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                       // Log.d(Tag, "right~up");


                        RelativeLayout.LayoutParams para1 = (RelativeLayout.LayoutParams) mIvNumRight.getLayoutParams();

                        para1.height = mNumIvWidth;
                        para1.width = mNumIvHeight;

                        mIvNumRight.setLayoutParams(para1);


                        interpretationNum(mIvNumRight);


                        break;
                }


                return true;
            }
        });


        initGame();


    }

    private void interpretationNum(ImageView mNumIV) {
        if ((int) mNumIV.getTag() == fishNum) {

            Log.d(Tag, "duile");
            initGame();

        } else {
            Log.d(Tag, "cuole");

        }
    }


    private void initGame() {


        fishNum = randNumfrom_1_9();

        int numPosition = randNumfrom_0_2();

        //  Log.d(Tag, "======"+fishNum);

        int[] numList = randNumList(fishNum, numPosition);


        for (int i = 0; i < numList.length; i++) {

            mNumIVs.get(i).setImageResource(mFishsNumDrawables[numList[i]]);

            mNumIVs.get(i).setTag(numList[i]);

        }





        Random r = new Random();


        int template = Math.abs(r.nextInt() % 2);

        if (template == 0) { //行模版
            mLlFishTemplateRow.setVisibility(View.GONE);

            mLlFishTemplateLine.setVisibility(View.VISIBLE);
            if (fishNum % 2 == 0) {
                for (int i = 0; i < mFishsIV_line.size(); i++) {

                    if (i < fishNum) {
                        mFishsIV_line.get(i).setVisibility(View.VISIBLE);
                    } else {
                        mFishsIV_line.get(i).setVisibility(View.GONE);
                    }

                }


            } else {

                for (int i = 0; i < mFishsIV_line.size(); i++) {

                    if (i < fishNum) {
                        mFishsIV_line.get(i).setVisibility(View.VISIBLE);
                    } else if (i >= fishNum && i < mFishsIV_line.size()) {
                        mFishsIV_line.get(i).setVisibility(View.GONE);
                    } else {
                        mFishsIV_line.get(i).setVisibility(View.VISIBLE);
                    }


                }

            }

        } else {  //列模版
            mLlFishTemplateRow.setVisibility(View.VISIBLE);

            mLlFishTemplateLine.setVisibility(View.GONE);

            if (fishNum % 2 == 0) {
                for (int i = 0; i < mFishsIV_row.size(); i++) {

                    if (i < fishNum) {
                        mFishsIV_row.get(i).setVisibility(View.VISIBLE);
                    } else {
                        mFishsIV_row.get(i).setVisibility(View.GONE);
                    }

                }


            } else {

                for (int i = 0; i < mFishsIV_row.size(); i++) {

                    if (i < fishNum) {
                        mFishsIV_row.get(i).setVisibility(View.VISIBLE);
                    } else if (i >= fishNum && i < mFishsIV_row.size()) {
                        mFishsIV_row.get(i).setVisibility(View.GONE);
                    } else {
                        mFishsIV_row.get(i).setVisibility(View.VISIBLE);
                    }


                }

            }


        }


    }


    private void findView() {



        mRlParent = (RelativeLayout) findViewById(R.id.rl_parent);
        mIvNumLeft = (ImageView) findViewById(R.id.iv_num_left);
        mIvNumCenter = (ImageView) findViewById(R.id.iv_num_center);
        mIvNumRight = (ImageView) findViewById(R.id.iv_num_right);

        mNumIVs = new ArrayList<ImageView>();
        mNumIVs.add(mIvNumLeft);
        mNumIVs.add(mIvNumCenter);
        mNumIVs.add(mIvNumRight);

        mLlNumLine = (RelativeLayout) findViewById(R.id.ll_num_line);

        mFlFishParent = (FrameLayout) findViewById(R.id.fl_fish_parent);


        mLlFishTemplateRow = (LinearLayout) findViewById(R.id.ll_fish_template_row);

        mLlFishTemplateLine = (LinearLayout) findViewById(R.id.ll_fish_template_line);

        findFishView();

        findFinshNumImage();


    }


    private int[] randNumList(int fishNum, int numPosition) {


        int number[] = new int[3];
        int num1, num2;

        if (numPosition == 0) {
            number[numPosition] = fishNum;


            while (true) {

                num1 = randNumfrom_1_9();

                if (num1 != number[numPosition]) {
                    number[1] = num1;


                    break;
                }
            }
            while (true) {
                num2 = randNumfrom_1_9();
                if (num2 != number[numPosition] && num2 != number[1]) {
                    number[2] = num2;

                    break;
                }

            }
        } else if (numPosition == 1) {


            number[numPosition] = fishNum;


            while (true) {

                num1 = randNumfrom_1_9();

                if (num1 != number[numPosition]) {
                    number[0] = num1;


                    break;
                }
            }
            while (true) {
                num2 = randNumfrom_1_9();
                if (num2 != number[numPosition] && num2 != number[0]) {
                    number[2] = num2;

                    break;
                }

            }

        } else if (numPosition == 2) {
            number[numPosition] = fishNum;


            while (true) {

                num1 = randNumfrom_1_9();

                if (num1 != number[numPosition]) {
                    number[0] = num1;


                    break;
                }
            }
            while (true) {
                num2 = randNumfrom_1_9();
                if (num2 != number[numPosition] && num2 != number[0]) {
                    number[1] = num2;

                    break;
                }

            }


        }


        return number;


    }

    private int randNumfrom_1_9() {
        Random r = new Random();


        return Math.abs(r.nextInt() % 9) + 1;
    }


    private int randNumfrom_0_2() {
        Random r = new Random();


        return Math.abs(r.nextInt() % 3);
    }

    private void findFinshNumImage() {
        mFishsNumDrawables = new int[10];
        mFishsNumDrawables[0] = R.drawable.num0;
        mFishsNumDrawables[1] = num1;
        mFishsNumDrawables[2] = num2;
        mFishsNumDrawables[3] = R.drawable.num3;
        mFishsNumDrawables[4] = R.drawable.num4;
        mFishsNumDrawables[5] = R.drawable.num5;
        mFishsNumDrawables[6] = R.drawable.num6;
        mFishsNumDrawables[7] = R.drawable.num7;
        mFishsNumDrawables[8] = R.drawable.num8;
        mFishsNumDrawables[9] = R.drawable.num9;
    }

    private void findFishView() {
        mfish1_row = (ImageView) findViewById(R.id.row1_fish1);
        mfish2_row = (ImageView) findViewById(R.id.row1_fish2);
        mfish3_row = (ImageView) findViewById(R.id.row2_fish1);
        mfish4_row = (ImageView) findViewById(R.id.row2_fish2);
        mfish5_row = (ImageView) findViewById(R.id.row3_fish1);
        mfish6_row = (ImageView) findViewById(R.id.row3_fish2);
        mfish7_row = (ImageView) findViewById(R.id.row4_fish1);
        mfish8_row = (ImageView) findViewById(R.id.row4_fish2);
        mfish9_row = (ImageView) findViewById(R.id.row5_fish1);

        mFishsIV_row = new ArrayList<ImageView>();
        mFishsIV_row.add(mfish1_row);
        mFishsIV_row.add(mfish2_row);
        mFishsIV_row.add(mfish3_row);
        mFishsIV_row.add(mfish4_row);
        mFishsIV_row.add(mfish5_row);
        mFishsIV_row.add(mfish6_row);
        mFishsIV_row.add(mfish7_row);
        mFishsIV_row.add(mfish8_row);
        mFishsIV_row.add(mfish9_row);


        mfish1_line = (ImageView) findViewById(R.id.line1_fish1);
        mfish2_line = (ImageView) findViewById(R.id.line1_fish2);
        mfish3_line = (ImageView) findViewById(R.id.line2_fish1);
        mfish4_line = (ImageView) findViewById(R.id.line2_fish2);
        mfish5_line = (ImageView) findViewById(R.id.line3_fish1);
        mfish6_line = (ImageView) findViewById(R.id.line3_fish2);
        mfish7_line = (ImageView) findViewById(R.id.line4_fish1);
        mfish8_line = (ImageView) findViewById(R.id.line4_fish2);
        mfish9_line = (ImageView) findViewById(R.id.line5_fish1);

        mFishsIV_line = new ArrayList<ImageView>();
        mFishsIV_line.add(mfish1_line);
        mFishsIV_line.add(mfish2_line);
        mFishsIV_line.add(mfish3_line);
        mFishsIV_line.add(mfish4_line);
        mFishsIV_line.add(mfish5_line);
        mFishsIV_line.add(mfish6_line);
        mFishsIV_line.add(mfish7_line);
        mFishsIV_line.add(mfish8_line);
        mFishsIV_line.add(mfish9_line);


    }

    public NumberGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
