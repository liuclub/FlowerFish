package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bagelplay.gameset.R;

import org.w3c.dom.Text;

/**
 * Created by zhangtianjie on 2017/8/28.
 */

public class EvaObjectView extends FrameLayout {
    TextView mTvEvaText;
    ImageView mIvEvaObject;
    Context mContext;

    private Handler timeHandler;

    Animation textAnimation;
    private int TIME = 1000;


    public void setButtonClickable(boolean buttonClickable) {
        this.buttonClickable = buttonClickable;
    }


    public boolean isButtonClickable() {
        return buttonClickable;
    }

    private boolean buttonClickable=true;

    public EvaObjectView(@NonNull Context context) {
        super(context);
    }

    public EvaObjectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        LayoutInflater.from(context).inflate(R.layout.eva_object_view_layout, this, true);

        initUI();

        mIvEvaObject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isButtonClickable())
                    return;

                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.btn_normal_to_middle_large);
                    v.startAnimation(animation);

                    if (mEvaObjectViewListener != null) {
                        mEvaObjectViewListener.objectClick();
                    }



            }
        });



        textAnimation=AnimationUtils.loadAnimation(mContext, R.anim.hide_to_show);

        timeHandler=  new Handler();



    }

    public void initView(){

        mTvEvaText.setVisibility(INVISIBLE);


        timeHandler.postDelayed(runnable, TIME);




    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {

                mTvEvaText.setVisibility(VISIBLE);

                mTvEvaText.startAnimation(textAnimation);
                if(mEvaObjectViewListener!=null) {
                    mEvaObjectViewListener.objectStartPlaySounds();
                }
            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    };

    public void startAnim(){
        mTvEvaText.setVisibility(VISIBLE);
        mTvEvaText.startAnimation(textAnimation);


    }

    //获得要闪三下的食物物体
    public ImageView getFlashObject(){

        return mIvEvaObject;
    }


    private void initUI() {
        mTvEvaText= (TextView) findViewById(R.id.tv_eva_text);
        mIvEvaObject= (ImageView) findViewById(R.id.iv_eva_object);
    }


    private  EvaObjectViewListener mEvaObjectViewListener;

    public void setEvaObjectViewListener(EvaObjectViewListener mEvaObjectViewListener){
        this.mEvaObjectViewListener=mEvaObjectViewListener;
    }

    public void changeObject(String text,int imgId){
        mTvEvaText.setText(text);

        mIvEvaObject.setImageResource(imgId);

        initView();

    }

    public  interface EvaObjectViewListener{
         void  objectClick();

        void   objectStartPlaySounds();
    }


    public EvaObjectView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
