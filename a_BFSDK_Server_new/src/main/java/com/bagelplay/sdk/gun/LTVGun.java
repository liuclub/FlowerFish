package com.bagelplay.sdk.gun;

import android.content.Context;
import android.os.Handler;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.test.MainGun;

public class LTVGun extends Gun
{
	
	/*
	 * 
	 * right 64
	 * up  2
     * left  8
     * down  4
     * leftup  10
     * rightup   66
     * leftdown   12
     * rightdown   68
	 * 
	 * 
	 */
	
	private int[] stick_direct	=	{
			GunEvent.mKEY_UP,
			GunEvent.mKEY_DOWN,
			GunEvent.mKEY_LEFT,
			GunEvent.mKEY_RIGHT
	};
	
	private ASpeed aSpeed;
	
	private int lastStickEvent;
	
	private float lastRadius;
	
	private Handler handler	=	new Handler();
	
	private boolean isRightStick;
	
	public LTVGun(Context context, SDKManager bfusm) {
		super(context, bfusm);
		aSpeed	=	new ASpeed();
		aSpeed.appendTts(0, 0.5f);
		aSpeed.appendTts(3000, 0.6f);
		aSpeed.appendTts(6000, 0.7f);
 	}
	
	int index;
	public void OnGunEvent(final GunEvent event)
	{
		stickEvent(event);
		
		if((event.key & GunEvent.mKEY_GRENADE) > 0)
			isRightStick	=	!isRightStick;
		
		
		if(isRightStick)
			rightStickEvent(event);
	}
	
 	private void stickEvent(GunEvent event)
	{
		final int stickEvent	=	checkStickEvent(event);
		if(stickEvent == 0)
 		{
 			clearStickEvent();	
 		}
 		else
 		{
 			resetStickEvent(event);
 			doGameHandlerLeftStick(event);
 		}
		
	}

	private int checkStickEvent(GunEvent event)
	{
		int res	=	0;
		for(int i=0;i<stick_direct.length;i++)
		{
			if((event.key & stick_direct[i]) > 0)
			{
				res	|=	stick_direct[i];
 			}
		}
		return res;
	}
	
	private void resetStickEvent(GunEvent event)
	{
		if(lastStickEvent == 0)
		{
			aSpeed.clear();
			lastStickEvent	=	event.key;
			aSpeed.move();
		}
	}
	
	private void clearStickEvent()
	{
		if(lastStickEvent != 0)
		{
			aSpeed.clear();
			lastStickEvent	=	0;
			lastRadius	=	0;
			setLeftStickXY(0,0);
			//showTV(null);
		}
	}
	
	private void doGameHandlerLeftStick(GunEvent event)
	{
		float radius	=	aSpeed.getRadius();
		if(radius > 0)
		{
			if(lastRadius == 0)
			{
				//showTV("key1:" + event.key);
				for(int i=0;i<5;i++)
				{
					setLeftStickXY(radius / 5 * i,event.key);
				}
				lastRadius	=	radius;
				return;
			}
			
			
			if(lastRadius != radius)
			{
				//showTV("key1:" + event.key);
				setLeftStickXY(radius,event.key);
				lastRadius	=	radius;
			}
			
			if(lastStickEvent != event.key)
			{
				//showTV("key1:" + event.key);
				setLeftStickXY(radius,event.key);
				lastRadius	=	radius;
				lastStickEvent	=	event.key;
			}
		}
	}
	
	private void setLeftStickXY(final float radius,final int key)
	{
		//showTV("key2:" + key);
 		float radiusX	=	0;
		float radiusY	=	0;
		if((key & GunEvent.mKEY_UP) > 0)
		{
			radiusY		=	-radius;
		}
		if((key & GunEvent.mKEY_DOWN) > 0)
		{
			radiusY		=	radius;
		}
		if((key & GunEvent.mKEY_LEFT) > 0)
		{
			radiusX		=	-radius;
		}
		if((key & GunEvent.mKEY_RIGHT) > 0)
		{
			radiusX		=	radius;
		}
		bfusm.onLeftStickChangedOnHandler(0,radiusX,radiusY);
	}
	
	private void rightStickEvent(GunEvent event)
	{
		float x	=	(float)((event.pointX - bfusm.getScreenWidth() / 2) / (bfusm.getScreenWidth() / 2));
		float y	=	(float)((event.pointY - bfusm.getScreenHeight() / 2) / (bfusm.getScreenHeight() / 2));
		/*if(++index % 100 == 0)
		{
			showTV("pointX:" + event.pointX + " x:" + x + " pointY:" +  event.pointY + " y:" + y);
		}
		
		if(index >= 3000)
		{
			showTV(null);
			index = 0;
		}*/
		
		bfusm.onRightStickChangedOnHandler(0, x, y);
	}
	
	
	/*private void showTV(final String str)
	{
		handler.post(new Runnable(){
			public void run()
			{
				if(str == null)
					MainGun.mainGun.tv.setText("");
				else
					MainGun.mainGun.tv.setText(str + "\r\n" + MainGun.mainGun.tv.getText());
			}
		});
	}*/
	
}
