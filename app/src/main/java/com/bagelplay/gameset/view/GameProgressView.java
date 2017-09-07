package com.bagelplay.gameset.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.max;

/**
 * Created by zhangtianjie on 2017/8/22.
 */


public class GameProgressView extends RelativeLayout {


    RelativeLayout mRlParent;

    Context mContext;

    RelativeLayout mRlBar;

    int  MaxSize =12;

    int bar_w,bar_h;

    int total_width;

    int marginLeft;


    List<ImageView> imageViews;
    public GameProgressView(Context context) {
        super(context);
    }

    public GameProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.myGameProgress);

        MaxSize = ta.getInteger(R.styleable.myGameProgress_max_size,0);



        LayoutInflater.from(context).inflate(R.layout.game_progress_view_layout, this, true);





        mRlParent= (RelativeLayout) findViewById(R.id.rl_parent);

        mRlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mRlBar= (RelativeLayout) findViewById(R.id.rl_bar);

        bar_w=getResources().getDimensionPixelSize(R.dimen.GameProgressBarWidth);
        bar_h=getResources().getDimensionPixelSize(R.dimen.GameProgressBarHeight);
        total_width=getResources().getDimensionPixelSize(R.dimen.GameProgressViewWidth);

        marginLeft=(total_width-bar_w*MaxSize)/(MaxSize-1)+bar_w;

        imageViews=new ArrayList<>();

        for(int i=0;i<MaxSize;i++){
            ImageView mBarIV=new ImageView(mContext);

            mBarIV.setImageResource(R.mipmap.progress_not_active);

            RelativeLayout.LayoutParams barIVParams = new RelativeLayout.LayoutParams(bar_w,bar_h);



            barIVParams.leftMargin=i*marginLeft;


            mBarIV.setLayoutParams(barIVParams);


            imageViews.add(mBarIV);
            mRlBar.addView(mBarIV);
        }





    }


    public void setProgressCount(int count){
        MaxSize=count;



        mRlBar.removeAllViews();


        bar_w=getResources().getDimensionPixelSize(R.dimen.GameProgressBarWidth);
        bar_h=getResources().getDimensionPixelSize(R.dimen.GameProgressBarHeight);
        total_width=getResources().getDimensionPixelSize(R.dimen.GameProgressViewWidth);

        marginLeft=(total_width-bar_w*MaxSize)/(MaxSize-1)+bar_w;

        imageViews=new ArrayList<>();

        for(int i=0;i<MaxSize;i++){
            ImageView mBarIV=new ImageView(mContext);

            mBarIV.setImageResource(R.mipmap.progress_not_active);

            RelativeLayout.LayoutParams barIVParams = new RelativeLayout.LayoutParams(bar_w,bar_h);



            barIVParams.leftMargin=i*marginLeft;


            mBarIV.setLayoutParams(barIVParams);


            imageViews.add(mBarIV);
            mRlBar.addView(mBarIV);
        }
    }


   public void setChooseNum(int num){
        if(imageViews!=null){
            for(int i=0;i<MaxSize;i++){
                if(i<=num) {
                    imageViews.get(i).setImageResource(R.mipmap.progress_active);
                }else{
                    imageViews.get(i).setImageResource(R.mipmap.progress_not_active);
                }
            }

        }
    }

    public GameProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
