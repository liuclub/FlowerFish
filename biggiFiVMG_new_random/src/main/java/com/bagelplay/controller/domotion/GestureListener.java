package com.bagelplay.controller.domotion;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.bagelplay.controller.utils.BagelPlayVmaStub;

public abstract class GestureListener implements OnTouchListener,OnGestureListener{

	public static final int MODE_MOUSE				=	1;
	
	public static final int MODE_ARROW				=	2;
	
	protected static final int GESTURE_SLIDE_UP		=	1;
	
	protected static final int GESTURE_SLIDE_DOWN		=	2;
	
	protected static final int GESTURE_SLIDE_LEFT		=	3;
	
	protected static final int GESTURE_SLIDE_RIGHT	=	4;
	
	private static final int GESTURE_HIT			=	5;
	
	private static final int GESTURE_SLIDE_NULL		=	0;
	
	private static final int MESSAGE_WHAT_DELAYFLINGTIME	=	1;
	
	private static int MAXSPEED			=	1000;
	
	private static int MAXDISTANCE		=	100;
	
	private static int FLINGDELAYTIME	=	0;
	
	private boolean canCheckFling;
	
	private Context context;
	
	private BagelPlayVmaStub bfVmaStub;
	
	private GestureDetector gd;
	
	private Handler handler	=	new Handler(){
		@Override
		public void handleMessage(Message msg) {   
            switch (msg.what) {   
            
            	case MESSAGE_WHAT_DELAYFLINGTIME:
            		
            		canCheckFling	=	true;
            		 
            	break;
            }
		}
	};
	
	public GestureListener(Context context)
	{
		this.context	=	context;
		
		bfVmaStub = BagelPlayVmaStub.getInstance();
		
		gd	=	new GestureDetector(context,this);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {		
		
		gd.onTouchEvent(event);
		
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		
		if(FLINGDELAYTIME == 0)
			canCheckFling	=	true;
		else
		{
			canCheckFling	=	false;
			doDelayFlingTime();
		}
		
 		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
 		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		sendToClickKey(KeyEvent.KEYCODE_DPAD_CENTER);
		
		doAfterClick();
 		
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		
 		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
 		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		int gesture		=	GESTURE_SLIDE_NULL;
		gesture	=	checkDirect(e1,e2,velocityX,velocityY);
		if(canCheckFling && gesture != GESTURE_SLIDE_NULL)
		{
			if(gesture == GESTURE_SLIDE_UP)
			{
				sendToClickKey(KeyEvent.KEYCODE_DPAD_UP);
			}
			else if(gesture == GESTURE_SLIDE_DOWN)
			{
				sendToClickKey(KeyEvent.KEYCODE_DPAD_DOWN);
			}
			else if(gesture == GESTURE_SLIDE_LEFT)
			{
				sendToClickKey(KeyEvent.KEYCODE_DPAD_LEFT);
			}
			else if(gesture == GESTURE_SLIDE_RIGHT)
			{
				sendToClickKey(KeyEvent.KEYCODE_DPAD_RIGHT);
			}
			
			doAfterFling(gesture);
		}
 		return false;
	}
	
	protected abstract void doAfterFling(int gesture);
	
	protected abstract void doAfterClick();
	
	private void doDelayFlingTime()
	{
		handler.removeMessages(MESSAGE_WHAT_DELAYFLINGTIME);
		handler.sendEmptyMessageDelayed(MESSAGE_WHAT_DELAYFLINGTIME, FLINGDELAYTIME);
	}
	
	 
	
	private int checkDirect(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		double speed	=	Math.sqrt(Math.abs(velocityX * velocityX) + Math.abs(velocityY * velocityY));
 		if(speed < MAXSPEED)
		{
			return GESTURE_SLIDE_NULL;
		}
		int gesture	=	GESTURE_SLIDE_NULL;
		float x1	=	e1.getX();
		float x2	=	e2.getX();
		float y1	=	e1.getY();
		float y2	=	e2.getY();
		
		float distanceX	=	Math.abs(x2 - x1);
		float distanceY	=	Math.abs(y2 - y1);
		double distance	=	Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		if(distance < MAXDISTANCE)
			return GESTURE_SLIDE_NULL;
		
		if(Math.abs(x2 - x1) >= Math.abs(y2 - y1))
		{
			if(x2 - x1 < 0)
			{
				gesture	=	GESTURE_SLIDE_LEFT;
			}
			else if(x2 - x1 > 0)
			{
				gesture	=	GESTURE_SLIDE_RIGHT; 
			}
		}
		else
		{
			if(y2 - y1 < 0)
			{
				gesture	=	GESTURE_SLIDE_UP;
			}
			else if(y2 - y1 > 0)
			{
				gesture	=	GESTURE_SLIDE_DOWN;
			}
		}
		 
		return gesture;
	}
	
	private void sendToClickKey(int keyCode)
	{
		bfVmaStub.sendKeyData(keyCode,MotionEvent.ACTION_DOWN);
		bfVmaStub.sendKeyData(keyCode,MotionEvent.ACTION_UP);
 	}
}
