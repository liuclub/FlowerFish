package com.bagelplay.gameset.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bagelplay.gameset.R;

/**
 * Created by zhangtianjie on 2017/8/31.
 */

public class MyAnim {

    private static MyAnim mMyAnim;

    private static Context mContext;

    private MyAnim() {

    }

    public static MyAnim getInstance(Context context){
        mContext=context;

        if(mMyAnim==null){
            mMyAnim=new MyAnim();
        }

        return mMyAnim;
    }

    public static Animation getAnimNormalToLittleLarge(){

        return AnimationUtils.loadAnimation(mContext, R.anim.btn_normal_to_little_large);
    }


    //从无到有
    public static Animation getAnimHideToShow(){

        return AnimationUtils.loadAnimation(mContext, R.anim.hide_to_show);
    }
    //闪三下
    public static Animation getFlashThreeTimes(){

        return AnimationUtils.loadAnimation(mContext, R.anim.flash_three_times);
    }
    //水果移到汉堡
    public static Animation getAnimMoveToFood(){

        return AnimationUtils.loadAnimation(mContext, R.anim.eva_game_move_to_hamber_anim);
    }



}
