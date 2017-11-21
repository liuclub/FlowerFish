package com.bagelplay.sdk.common.remoteControl;

import android.os.Handler;
import android.os.Message;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.util.Log;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;


public class RControl {

	private static String TAG	=	"RControl";
	
	private static final int MESSAGE_WHAT_MOUSE_MOVE		=	1;
	
	private static final int BASE_SCREEN_WIDTH	=	1920;
	
	private static final int BASE_SCREEN_HEIGHT	=	1080;
			
	private int keyCode;
	
	private long downTime;
	
	private int action	=	Component.ACTION_NULL;
	
	private int playerOrder;
	
	protected SDKManager bfusm;
	
	private boolean isMouseMode;
	
	private Handler handler	=	new Handler(){
		@Override
		public void handleMessage(Message msg) {   
            switch (msg.what) {   
            
            	case MESSAGE_WHAT_MOUSE_MOVE:
            		keyAsMouseMove();
            	break;
            	
            	
            }
		}
	};
	
	private int speedX;
	
	private int speedY;
	
	private int speed	=	2;
	
	private int aSpeed	=	5;
	
	private int maxSpeed	=	200;
	
	private int speedAX;
	
	private int speedAY;
	
	private int cSpeedX;
	
	private int cSpeedY;
		
	private boolean isMoved;
	
	private long speedStart;
	
	private OnControlListen ocl;
	
	private int[][] keyCustomized		=	new int[KeyConst.PHYSICSKEY.length][]; 
	
	private int[] devScreenWH	=	new int[2];
	
	private float[] screenRate	=	new float[2];
	
	private boolean isSimulateTouch;
	
	public RControl(SDKManager bfusm)
	{
		this.bfusm	=	bfusm;
		
		JsonParser jp	=	new JsonParser(this,bfusm);
		jp.parse();	 
		
		resetSpeed();
	}
	
	public void setOnControlListen(OnControlListen ocl)
	{
		this.ocl	=	ocl;
	}
	
	public boolean setKeyEvent(int playerOrder,int keyCodePhysics,int action)
	{
		int keyCode	=	KeyConst.translateKeyCodefromPhysicsKey(keyCodePhysics);
		
		Log.v(TAG, "setKeyEvent " + "keyCodePhysics:" + keyCodePhysics + " keyCode:" + keyCode);
		
		if(keyCode == -1)
			return false;
		this.playerOrder	=	playerOrder;
		if(this.action == Component.ACTION_NULL)
		{
			if(action == Component.ACTION_DOWN)
			{
				setKeyDown(keyCode,action); 
				doKeyEvent();
				onKeyDown(keyCode);
				doKeyAsMouse();
			}
		}
		else if(this.action == Component.ACTION_DOWN)
		{
			if(action == Component.ACTION_DOWN)
			{
				if(keyCode == this.keyCode)
				{
					doKeyEvent();
 				}
				else
				{
					this.action	=	Component.ACTION_UP;
					onKeyUp(this.keyCode);
					doKeyEvent();
					doKeyAsMouse();
					setKeyDown(keyCode,action);
					doKeyEvent();
					onKeyDown(keyCode);
					doKeyAsMouse();
				}
			}
			else if(action == Component.ACTION_UP && keyCode == this.keyCode)
			{
				this.action	=	Component.ACTION_UP;
				onKeyUp(this.keyCode);
				doKeyEvent();
				doKeyAsMouse();
				this.action		=	Component.ACTION_NULL;
			}
		}
		
		return true;
	}
	
	public void setMouseMode(boolean mouseMode)
	{
		isMouseMode	=	mouseMode;
		if(!mouseMode)
			mouseStop();
	}
	
	public void setMouseSpeed(int speed)
	{
		this.speed	=	speed;
	}
	
	public void setMouseASpeed(int aSpeed)
	{
		this.aSpeed	=	aSpeed;
	}
	
	private void setKeyDown(int keyCode,int action)
	{
		this.keyCode	=	keyCode;
		this.action		=	action;
		this.downTime	=	System.currentTimeMillis();
	}
 
	
	private void doKeyEvent()
	{
		Log.v(TAG, "doKeyEvent " + "playerOrder:" + playerOrder + "keyCode:" + keyCode + " action:" + action);
		bfusm.getSocketThirdStub().sendRemoteControlData(playerOrder,keyCode, action);
 	}
	
	private void doKeyAsMouse()
	{
		if(!isMouseMode)
			return;
		if(action == Component.ACTION_DOWN)
		{
			if(checkSpeed())
				handler.sendEmptyMessage(MESSAGE_WHAT_MOUSE_MOVE);
		}
		else if(action == Component.ACTION_UP)
		{
			if(keyCode == KeyConst.KEY_CENTER)
			{
				bfusm.getMouse().doMouseClick();
			}
			else
			{
				mouseStop(); 
			}
			
		}
	}
	 
	private boolean checkSpeed()
	{
		speedX	=	0;
		speedY	=	0;
		speedAX	=	0;
		speedAY	=	0;
		if(keyCode == KeyConst.KEY_RIGHT)
		{
			speedX	=	speed;
			speedAX	=	aSpeed;
		}
		else if(keyCode == KeyConst.KEY_LEFT)
		{
			speedX	=	-speed;
			speedAX	=	-aSpeed;
		}
		if(keyCode == KeyConst.KEY_DOWN)
		{
			speedY	=	speed;
			speedAY	=	aSpeed;
		}
		else if(keyCode == KeyConst.KEY_UP)
		{
			speedY	=	-speed;
			speedAY	=	-aSpeed;
		}
		return speedX != 0 || speedY != 0;
	}
	
	private void checkASpeed()
	{
		cSpeedX	=	0;
		cSpeedY	=	0;
		if(!isMoved)
		{
			speedStart	=	System.currentTimeMillis();
			isMoved	=	true;
			Log.v(TAG, "checkASpeed " + "start");
		}
		else
		{
			cSpeedX	=	speedX + (int)(speedAX * ((System.currentTimeMillis() - speedStart + 0f) / 1000) * 10);
			cSpeedY	=	speedY + (int)(speedAY * ((System.currentTimeMillis() - speedStart + 0f) / 1000) * 10);
		}
		
		if(Math.abs(cSpeedX) > maxSpeed)
			cSpeedX	=	cSpeedX > 0 ? maxSpeed : -maxSpeed;
		if(Math.abs(cSpeedY) > maxSpeed)
			cSpeedY	=	cSpeedY > 0 ? maxSpeed : -maxSpeed;
	}
	
	private void keyAsMouseMove()
	{
		checkASpeed();
		bfusm.getMouse().doMouseMove(cSpeedX, cSpeedY);
		handler.sendEmptyMessage(MESSAGE_WHAT_MOUSE_MOVE);
	}
	
	private void mouseStop()
	{
		isMoved	=	false;
		handler.removeMessages(MESSAGE_WHAT_MOUSE_MOVE);
	}
	
	protected void onKeyDown(int keyCode)
	{
		Log.v(TAG, "onKeyDown " + "keyCode:" + keyCode);
		if(ocl != null)
			ocl.onKeyDown(keyCode);
		if(isSimulateTouch && !isMouseMode)
			doSimulateTouch(keyCode);
	}
	
	protected void onKeyUp(int keyCode)
	{
		Log.v(TAG, "onKeyUp " + "keyCode:" + keyCode);
		if(ocl != null)
			ocl.onKeyUp(keyCode);
	}
	
	public void addKeyScreenCustomizedSlide(int keyCode,int x,int y,int absoulte,int slide)
	{
		keyCode	-=	100;
		keyCustomized[keyCode]		=	new int[5];
		keyCustomized[keyCode][0]	=	x;
		keyCustomized[keyCode][1]	=	y;
		keyCustomized[keyCode][2]	=	absoulte;
		keyCustomized[keyCode][3]	=	slide;
		keyCustomized[keyCode][4]	=	1;
	}
	
	public void addKeyScreenCustomizedClick(int keyCode,int x,int y,int absoulte)
	{
		keyCode	-=	100;
		keyCustomized[keyCode]		=	new int[5];
		keyCustomized[keyCode][0]	=	x;
		keyCustomized[keyCode][1]	=	y;
		keyCustomized[keyCode][2]	=	absoulte;
		keyCustomized[keyCode][3]	=	0;
		keyCustomized[keyCode][4]	=	2;
	}
	
	public void setDevScreenWH(int width,int height)
	{
		devScreenWH[0]	=	width;
		devScreenWH[1]	=	height;
	}
	
	public void resetByScreenRate()
	{
		screenRate[0]	=	(bfusm.getScreenWidth() + 0f) / devScreenWH[0]; 
		screenRate[1]	=	(bfusm.getScreenHeight() + 0f) / devScreenWH[1];
		Log.v(TAG, "resetByScreenRate " + "rate x:" + screenRate[0] + " rate y:" + screenRate[1]);
		for(int i=0;i<keyCustomized.length;i++)
		{
			if(keyCustomized[i] != null && keyCustomized[i][2] == 1)
			{
				keyCustomized[i][0]	=	(int)(keyCustomized[i][0] * screenRate[0]);
				keyCustomized[i][1]	=	(int)(keyCustomized[i][1] * screenRate[1]);
			}
		}
		 
		
	}
	
	public void simulateTouch()
	{
		isSimulateTouch	=	true;
	}
	
	private void doSimulateTouch(int keyCode)
	{
 		keyCode	-=	100;
		if(keyCustomized[keyCode] == null)
			return;
 		if(keyCustomized[keyCode][4] == 1)
		{
			doSimulateTouchSlide(keyCode);
 		}
		else if(keyCustomized[keyCode][4] == 2)
		{
			doSimulateTouchClick(keyCode);
 		}
	}
	
	private void doSimulateTouchSlide(int keyCode)
	{
		int speed	=	20;
		int speedX	=	0;
		int speedY	=	0;
		int distanceX	=	0;
		int distanceY	=	0;
		int delay		=	0;
		if(keyCustomized[keyCode][3] == 1)
		{
			speedY	=	(int)(-speed * screenRate[1]);
		}
		if(keyCustomized[keyCode][3] == 2)
		{
			speedY	=	(int)(speed * screenRate[1]);
		}
		if(keyCustomized[keyCode][3] == 3)
		{
			speedX	=	(int)(-speed * screenRate[0]);
		}
		if(keyCustomized[keyCode][3] == 4)
		{
			speedX	=	(int)(speed * screenRate[0]);
		}
		bfusm.getTouch().touch(MotionEvent.ACTION_DOWN, keyCustomized[keyCode][0], keyCustomized[keyCode][1]);
		for(int i=0;i<10;i++)
		{
			distanceX	=	i * speedX;
			distanceY	=	i * speedY;
			delay		=	i * 50;
			bfusm.getTouch().touch(MotionEvent.ACTION_MOVE, keyCustomized[keyCode][0] + distanceX, keyCustomized[keyCode][1] + distanceY,delay);
		}
		bfusm.getTouch().touch(MotionEvent.ACTION_UP, keyCustomized[keyCode][0] + distanceX, keyCustomized[keyCode][1] + distanceY,delay);
	}
	
	private void doSimulateTouchClick(int keyCode)
	{
		bfusm.getTouch().touch(MotionEvent.ACTION_DOWN, keyCustomized[keyCode][0], keyCustomized[keyCode][1]);
		bfusm.getTouch().touch(MotionEvent.ACTION_UP, keyCustomized[keyCode][0], keyCustomized[keyCode][1],100);
 	}
	
	private void resetSpeed()
	{
		speed	=	(int)((bfusm.getScreenWidth() + 0f) / BASE_SCREEN_WIDTH * speed);
		aSpeed	=	(int)((bfusm.getScreenWidth() + 0f) / BASE_SCREEN_WIDTH * aSpeed);
		maxSpeed	=	(int)((bfusm.getScreenWidth() + 0f) / BASE_SCREEN_WIDTH * maxSpeed);
	}
}
