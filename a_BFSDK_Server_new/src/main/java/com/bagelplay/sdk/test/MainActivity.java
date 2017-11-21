package com.bagelplay.sdk.test;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.bagelplay.sdk.R;
import com.bagelplay.sdk.unity.SDKUnityManager;
import com.unity3d.player.UnityPlayer;

public class MainActivity extends Activity {

	private Button btn1;
	
	private Button btn2;
	
	private SDKUnityManager bfusm;
	
	private int resolutionWidth;
	
	private int resolutionHeight;
	
	private UnityPlayer mUnityPlayer;
	 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      
     
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().takeSurface(null);
		setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
		getWindow().setFormat(PixelFormat.RGB_565);

		/**
		mUnityPlayer = new UnityPlayer(this);
		if (mUnityPlayer.getSettings().getBoolean("hide_status_bar", true))
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

		int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
		boolean trueColor8888 = false;
		mUnityPlayer.init(glesMode, trueColor8888);

		View playerView = mUnityPlayer.getView();
		setContentView(playerView);
		playerView.requestFocus();
		*/
		
		//setContentView(R.layout.activity_main2);
		
		
        bfusm	=	SDKUnityManager.getInstance(this);
        bfusm.addWindowCallBack(this);
        
        setContentView(R.layout.activity_main2);
         
       
    }
    
    
    public boolean dispatchKeyEvent(KeyEvent event)
    {
    	if(com.bagelplay.sdk.unity.SDKUnityManager.getInstance(this).dispatchKeyEvent(event))
    		return true;
     	return super.dispatchKeyEvent(event);
    }
    
    public boolean dispatchGenericMotionEvent(MotionEvent ev)
    {
    	if(com.bagelplay.sdk.unity.SDKUnityManager.getInstance(this).dispatchGenericMotionEvent(ev))
    		return true;
		return super.dispatchGenericMotionEvent(ev);
    	
    }
    
}
