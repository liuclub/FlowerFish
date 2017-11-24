package com.bagelplay.sdk.cocos;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.remoteControl.KeyConst;
import com.bagelplay.sdk.common.remoteControl.RControl;
import com.bagelplay.sdk.common.util.Log;

public class XSGRControl extends RControl{

	private static String TAG	=	"XSGRControl";
	
	private boolean isStick;
	
	private long lastTime;
	
	private int state;
	
	public XSGRControl(SDKManager bfusm) {
		super(bfusm);
		setMouseMode(true);
  	}

	protected void onKeyDown(int keyCode)
	{
		super.onKeyDown(keyCode);
		if(keyCode == KeyConst.KEY_CENTER)
		{
			if(!isStick)
				bfusm.getMouse().setHold(1);
			Log.v(TAG, "onKeyLongTouch " + "keyCode:" + keyCode);
			lastTime	=	System.currentTimeMillis();
		}
	}
	
	protected void onKeyUp(int keyCode)
	{
		super.onKeyUp(keyCode);
		if(keyCode == KeyConst.KEY_CENTER)
		{
			bfusm.getMouse().setHold(2);
			isStick	=	false;
 		}
	}

	public void stick()
	{
		bfusm.getMouse().setHold(1);
		bfusm.getMouse().mouseMove(-60, -60);
		isStick	=	true;
	}
	
	/*private void checkClick()
	{
		long t	=	System.currentTimeMillis() - lastTime;
		if(t < 500)
		{
			Log.v(TAG, "do check click " + state);
			if(state != 1)
			{
				
				sdkManager.getMouse().setHold(1);
				sdkManager.getMouse().mouseMove(-60, -60);
				state	=	1;
			}
			else
			{
				sdkManager.getMouse().setHold(2);
				state	=	0;
			}
			
		}
	}
	
	private void checkClick2()
	{
		long t	=	System.currentTimeMillis() - lastTime;
		if(t < 500)
		{
			Log.v(TAG, "do check click " + state);
			if(state != 1)
			{
				
				sdkManager.getMouse().setHold(1);
				sdkManager.getMouse().mouseMove(-60, -60);
				state	=	1;
			}
			else
			{
				sdkManager.getMouse().setHold(2);
				state	=	0;
			}
			
		}
	}
	
	private void checkLongTouch()
	{
		
	}*/
	
	
}
