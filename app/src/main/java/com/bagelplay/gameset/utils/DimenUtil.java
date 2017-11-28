package com.bagelplay.gameset.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by zhangtianjie on 2017/7/29.
 */

public class DimenUtil {

    /**
     * @param context
     * @param px
     * @return
     */
    public static int px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float sp2Px(Context context, float value) {
        TypedValue typedValue = new TypedValue();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return typedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, metrics);
    }

}
