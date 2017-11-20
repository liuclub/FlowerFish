package com.bagelplay.gameset.evagame.utils;

import java.io.Serializable;

import android.content.SharedPreferences;
import android.util.Log;

import com.bagelplay.gameset.MyApplication;

/**
 * @ClassName SPUtil
 * @Description SharedPreferences管理类
 */
public class SPUtil {
    /**
     * 保存在手机里面的文件名
     */
    private static SharedPreferences sp = MyApplication.appContext
            .getSharedPreferences("SpForKingergarten", 0);
    private static SharedPreferences.Editor editor;

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key    String
     * @param object instandof String Integer Boolean Float Long
     */
    public static void set(String key, Object object) {

        editor = sp.edit();

        String type = object.getClass().getSimpleName();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static <T extends Serializable> T get(String key, T defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        // return sp.getString(key, null);
        try {
            if ("String".equals(type)) {
                return (T) sp.getString(key, (String) defaultObject);
            } else if ("Integer".equals(type)) {
                return (T) ((Integer) sp.getInt(key, (Integer) defaultObject));
            } else if ("Boolean".equals(type)) {
                return (T) ((Boolean) sp.getBoolean(key,
                        (Boolean) defaultObject));
            } else if ("Float".equals(type)) {
                return (T) ((Float) sp.getFloat(key, (Float) defaultObject));
            } else if ("Long".equals(type)) {
                return (T) ((Long) sp.getLong(key, (Long) defaultObject));
            }
        } catch (Exception e) {
            Log.e("xxx", "error = " + Log.getStackTraceString(e));
        }
        return defaultObject;

    }
}
