package com.bagelplay.sdk.cocos;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.view.WindowManager;

import com.bagelplay.sdk.common.SDKManager;

public class SDKCocosManager extends SDKManager{

	private static SDKCocosManager bfsdkcm;
		
	private SDKCocosManager(Activity activity) {
		super(activity);
 	}
	
	public static SDKCocosManager getInstance(Activity activity)
	{
		SDKManager.lastActivity	=	new WeakReference<Activity>(activity);
		if(bfsdkcm == null)
			bfsdkcm	=	new SDKCocosManager(activity);
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		return bfsdkcm;
	}
	
	public static SDKCocosManager getInstance()
	{
  		return bfsdkcm;
	}
		
	@Override
	public void destroy()
	{
		super.destroy();
		bfsdkcm	=	null;
	}

}
