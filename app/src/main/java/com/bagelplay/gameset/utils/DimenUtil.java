package com.bagelplay.gameset.utils;

import android.content.Context;

/**
 * Created by zhangtianjie on 2017/7/29.
 */

public class DimenUtil {




    public static int px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
