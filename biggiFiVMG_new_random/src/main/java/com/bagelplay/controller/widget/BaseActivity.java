package com.bagelplay.controller.widget;

import com.bagelplay.controller.utils.Config;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity{
	
	private int screenOrientation	=	-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//disable screen sleep
		
		resetResolution();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		
		
		
		if(newConfig.orientation != screenOrientation)
		{
			screenOrientation	=	newConfig.orientation;
			resetResolution();
			orientationChanged(screenOrientation);
			
			
		}
		super.onConfigurationChanged(newConfig);
	}
	
	public abstract void orientationChanged(int orientation);
	
	private void resetResolution()
	{
		
		
		
		DisplayMetrics mDm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDm);
		Config.widthPixels	=	mDm.widthPixels;
		Config.heightPixels	=	mDm.heightPixels;
		
		
	}
}
