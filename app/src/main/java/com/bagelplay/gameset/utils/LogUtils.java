package com.bagelplay.gameset.utils;

import android.util.Log;

/**
 * 个人专用
 *
 * @author liubo
 */
public class LogUtils {
    public static final boolean allow = true;

    public static void lb(String msg) {
        if (allow) {
            Log.i("Demo", "-------------------------------------");
            Log.i("Demo", msg);
        }
    }

    public static void v(String tag, String msg) {
        if (allow) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (allow) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (allow) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (allow) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (allow) {
            Log.e(tag, msg);
        }
    }
}
