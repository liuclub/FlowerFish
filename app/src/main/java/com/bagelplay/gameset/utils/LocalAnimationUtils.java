package com.bagelplay.gameset.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bagelplay.gameset.R;

/**
 * Created by zhangtianjie on 2017/8/31.
 */

public class LocalAnimationUtils {

    private static LocalAnimationUtils mLocalAnimationUtils;

    private static Context mContext;

    private LocalAnimationUtils() {

    }

    public static LocalAnimationUtils getInstance(Context context) {
        mContext = context;

        if (mLocalAnimationUtils == null) {
            mLocalAnimationUtils = new LocalAnimationUtils();
        }

        return mLocalAnimationUtils;
    }

    public static Animation getAnimNormalToLittleLarge() {
        return AnimationUtils.loadAnimation(mContext, R.anim.btn_normal_to_little_large);
    }


    //从无到有
    public static Animation getAnimHideToShow() {
        return AnimationUtils.loadAnimation(mContext, R.anim.hide_to_show);
    }

    //闪三下
    public static Animation getFlashThreeTimes() {
        return AnimationUtils.loadAnimation(mContext, R.anim.flash_three_times);
    }

    //水果移到汉堡
    public static Animation getAnimMoveToFood() {
        return AnimationUtils.loadAnimation(mContext, R.anim.eva_game_move_to_hamber_anim);
    }


    //汉堡移到汉堡
    public static Animation getAnimhamMoveToFood() {
        return AnimationUtils.loadAnimation(mContext, R.anim.eva_game_ham_move_to_hamber_anim);
    }


    //水果移到盘子
    public static Animation getAnimMoveToPlate() {
        return AnimationUtils.loadAnimation(mContext, R.anim.eva_game_move_to_plate_anim);
    }

    public static Animation getMouseMoveAnimation() {
        return AnimationUtils.loadAnimation(mContext, R.anim.mouse_move);
    }
}
