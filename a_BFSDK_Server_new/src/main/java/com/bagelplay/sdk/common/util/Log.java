package com.bagelplay.sdk.common.util;

public class Log {
	
	private static boolean isShow	=	true;
	
	public static void showLog(boolean isShow)
	{
		Log.isShow	=	isShow;
	}
	
	public static void v(String tag,String message)
	{
		if(isShow)
			android.util.Log.v(tag, message);
	}
	
	public static void d(String tag,String message)
	{
		if(isShow)
			android.util.Log.d(tag, message);
	}
	
	public static void w(String tag,String message)
	{
		if(isShow)
			android.util.Log.w(tag, message);
	}
	
	public static void i(String tag,String message)
	{
		if(isShow)
			android.util.Log.i(tag, message);
	}
	
	public static void e(String tag,String message)
	{
		if(isShow)
			android.util.Log.e(tag, message);
	}
}
