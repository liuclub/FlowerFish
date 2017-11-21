package com.bagelplay.controller.widget;


import com.bagelplay.controller.domotion.MouseListener;
import com.bagelplay.controller.domotion.SensorListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BagelPlaySharedPreferences {
	
	public static void checkFirstLaunch(Context context)
	{
		SharedPreferences helpInfo = context.getSharedPreferences("help_info", 0);
		Boolean isFirstStart = helpInfo.getBoolean("isFirstLauncher", true);
//		if (isFirstStart) {
//        	Intent intent	=	new Intent(context,GuideActivity.class);
//        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	context.startActivity(intent);
//			helpInfo.edit().putBoolean("isFirstLauncher", false).commit();
//        }
	}
	
	
	
	
	public static class Mouse
	{
		public static int getRatio(Context context)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			int ratio = mPrefs.getInt("mouse_data", 2);
			return ratio;
		}
		 
		public static void setRatio(Context context,int ratio)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = mPrefs.edit(); 
			editor.putInt("mouse_data", ratio); 
			editor.commit(); 
			MouseListener.setRatio(ratio);
		}
		
		public static void setRuntime(Context context)
		{
			int ratio	=	getRatio(context);
			MouseListener.setRatio(ratio);
		}
	}
	
	public static class Volume
	{
		public static int getRatio(Context context)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			int ratio = mPrefs.getInt("volume_data", 5);
			return ratio;
		}
		 
		public static void setRatio(Context context,int ratio)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = mPrefs.edit(); 
			editor.putInt("volume_data", ratio); 
			editor.commit(); 
		
		}
	
	}
	
	
	public static class Vibrate
	{
		public static int getRatio(Context context)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			int ratio = mPrefs.getInt("vibrate_data", 5);
			return ratio;
		}
		 
		public static void setRatio(Context context,int ratio)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = mPrefs.edit(); 
			editor.putInt("vibrate_data", ratio); 
			editor.commit(); 
		
		}
	
	}
	
	
	 
	public static class Sensor
	{
		public static int getRatio(Context context)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			int ratio = mPrefs.getInt("sensor_data", 3);
			return ratio;
		}
		 
		public static boolean isOverturn(Context context)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			boolean overturn = mPrefs.getBoolean("sensor_overturn", false);
 			return overturn;
		}
		 
		public static void setRatio(Context context,int ratio)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = mPrefs.edit(); 
			editor.putInt("sensor_data", ratio); 
			editor.commit(); 
			SensorListener.setRatio(ratio);
 		} 
		 
		public static void setOverturn(Context context,boolean isOverturn)
		{
			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = mPrefs.edit(); 
			editor.putBoolean("sensor_overturn", isOverturn); 
			editor.commit(); 
			SensorListener.setOverturn(isOverturn);
		}
		
		public static void setRuntime(Context context)
		{
			
			int ratio			=	getRatio(context);
			boolean overturn	=	isOverturn(context);
			SensorListener.setRatio(ratio);
			SensorListener.setOverturn(overturn);
		}
	}
}
