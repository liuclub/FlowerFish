package com.bagelplay.gameset.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author zhuyan
 * @date 创建时间：2015年6月18日 下午3:26:43
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class VOUtils {
	private VOUtils() {
	}

	/**
	 * 将一个对象转换为JSON格式的串
	 * 
	 * @param vo
	 *            要转换的VO对象
	 * @return 转换后的字符串
	 */
	public static String convertVO2String(Object vo) {
		try {
			Gson gson = new Gson();
			return gson.toJson(vo);
		} catch (Exception e) {
			LogUtils.lb("error = " + Log.getStackTraceString(e));
			return null;
		}
	}

	/**
	 * 将一个JSON格式的字符串转换为Java对象
	 * 
	 * @param jsonStr
	 *            要转换的JSON格式的字符串
	 * @param destClass
	 *            要将这个JSON格式的字符串转换为什么类型的对象
	 * @return 转换之后的Java对象
	 */
	public static <T> T convertString2VO(String jsonStr, Class<T> destClass) {
		try {
			Gson gson = new Gson();
			if (isJson(jsonStr)) {
				return gson.fromJson(jsonStr, destClass);
			} else {
				return null;
			}
		} catch (Exception e) {
			LogUtils.lb("error = " + Log.getStackTraceString(e));
			return null;
		}
	}

	/**
	 * 将一个json转换成一个集合对象
	 * 
	 * @param jsonStr
	 *            要转换的JSON格式的字符串
	 * @param typeToken
	 *            TypeToken<这里指定集合类型和泛型信息>
	 * @return 转换之后的集合对象
	 */
	public static <T> T convertString2Collection(String jsonStr, TypeToken<T> typeToken) {
		try {
			Gson gson = new Gson();
			if (isJson(jsonStr)) {
				T t = gson.fromJson(jsonStr, typeToken.getType());
				return t;
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.e("xxx", "error = " + Log.getStackTraceString(e));
			return null;
		}
	}

	public static boolean isJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			jsonObject = null;
			return true;
		} catch (JSONException e1) {
			e1.printStackTrace();
			return false;
		}
	}

	public static ArrayList<String> getJsonToArray(String str) {
		ArrayList<String> strs = new ArrayList<String>();
		try {
			JSONArray arr = new JSONArray(str);
			for (int i = 0; i < arr.length(); i++) {
				strs.add(arr.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strs;
	}
}
