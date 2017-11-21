package com.bagelplay.sdk.unity;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.view.WindowManager;

import com.bagelplay.sdk.common.SDKManager;

public class SDKUnityManager extends SDKManager{

	private static SDKUnityManager bfsdkum;
	
	private UnityInputDialog uid;
	
	private SDKUnityManager(Activity activity) {
		super(activity);
		uid	=	new UnityInputDialog(this);
 	}
	
	public static SDKUnityManager getInstance(Activity activity)
	{
		SDKManager.lastActivity	=	new WeakReference<Activity>(activity);
		if(bfsdkum == null)
			bfsdkum	=	new SDKUnityManager(activity);
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		return bfsdkum;
	}
	
	public static SDKUnityManager getInstance()
	{
		return bfsdkum;
	}
	
	public void onWindowFocusChanged(boolean arg0)
	{
		if(!arg0)
		{
			uid.checkShow();
		}
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		bfsdkum	=	null;
	}
	
	
	
	
	

}
