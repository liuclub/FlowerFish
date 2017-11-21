package com.bagelplay.controller.widget;

import android.content.Context;
import android.os.Vibrator;

public class BagelPlayVibrator {
	
	private static BagelPlayVibrator bfv;
	
	private Vibrator vibrator;
	
	private BagelPlayVibrator(Context context)
	{
		vibrator	= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	public static void init(Context context)
	{
		if(bfv == null)
			bfv	= new BagelPlayVibrator(context);
	}
	
	public static BagelPlayVibrator getInstance()
	{
		return bfv;
	}
	
	public void vibrate(long milliseconds)
	{
		vibrator.vibrate(milliseconds);
	}
}
