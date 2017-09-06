package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.MyAnim;
import com.iflytek.cloud.thirdparty.A;
import com.iflytek.cloud.thirdparty.I;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtianjie on 2017/9/6.
 */

public class EvaFruitRotate extends LinearLayout {
    ImageView mIvFruit1, mIvFruit2, mIvFruit3, mIvFruit4,mIvFruit5,mIvFruit6;

    List<ImageView> fruitsArrayIV;

    List<Integer> fruitsArrayImg;

    int maxRotateTimes=7;
    int currentRotateTimes=0;

    private Handler timeHandler;

    Context mContext;

    public EvaFruitRotate(Context context) {
        super(context);
    }

    public EvaFruitRotate(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        LayoutInflater.from(context).inflate(R.layout.evaluation_fruit_rotate_view_layout, this, true);
        timeHandler = new Handler();
        initUI();

    }


    private void initUI() {
        fruitsArrayIV = new ArrayList<>();
        mIvFruit1 = (ImageView) findViewById(R.id.fruit1);
        mIvFruit2 = (ImageView) findViewById(R.id.fruit2);
        mIvFruit3 = (ImageView) findViewById(R.id.fruit3);
        mIvFruit4 = (ImageView) findViewById(R.id.fruit4);
        mIvFruit5 = (ImageView) findViewById(R.id.fruit5);
        mIvFruit6 = (ImageView) findViewById(R.id.fruit6);
        fruitsArrayIV.add(mIvFruit1);
        fruitsArrayIV.add(mIvFruit2);
        fruitsArrayIV.add(mIvFruit3);
        fruitsArrayIV.add(mIvFruit4);
        fruitsArrayIV.add(mIvFruit5);
        fruitsArrayIV.add(mIvFruit6);
        fruitsArrayImg=new ArrayList<>();
        fruitsArrayImg.add(R.mipmap.eva_apple);
        fruitsArrayImg.add(R.mipmap.eva_banana);
        fruitsArrayImg.add(R.mipmap.eva_strawberry);
        fruitsArrayImg.add(R.mipmap.eva_orange);
        fruitsArrayImg.add(R.mipmap.eva_grape);
        fruitsArrayImg.add(R.mipmap.eva_peach);

        //initFruit();


    }

    public void startRotate(){
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentRotateTimes<maxRotateTimes){


                        fruitsArrayIV.get(0).setImageResource(fruitsArrayImg.get((currentRotateTimes+0)%6));
                        fruitsArrayIV.get(1).setImageResource(fruitsArrayImg.get((currentRotateTimes+1)%6));
                        fruitsArrayIV.get(2).setImageResource(fruitsArrayImg.get((currentRotateTimes+2)%6));
                        fruitsArrayIV.get(3).setImageResource(fruitsArrayImg.get((currentRotateTimes+3)%6));
                        fruitsArrayIV.get(4).setImageResource(fruitsArrayImg.get((currentRotateTimes+4)%6));
                        fruitsArrayIV.get(5).setImageResource(fruitsArrayImg.get((currentRotateTimes+5)%6));



                    currentRotateTimes++;

                    startRotate();

                }else{
                    Animation objectAnimationFlash = MyAnim.getInstance(mContext).getFlashThreeTimes();

                    if(fruitsArrayIV!=null){
                        for (int i = 0; i < fruitsArrayIV.size(); i++) {
                            fruitsArrayIV.get(i).startAnimation(objectAnimationFlash);
                        }
                    }

                }


            }
        },500);

    }

    private void initFruit(){

    }

    public EvaFruitRotate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
