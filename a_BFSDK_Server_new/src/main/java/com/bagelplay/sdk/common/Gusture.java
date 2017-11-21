package com.bagelplay.sdk.common;


import android.os.Handler;
import android.os.Message;

import com.bagelplay.sdk.common.util.Log;

import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.Window;

class Gusture implements OnGestureListener{

	protected static int MAXSPEED			=	5000;

	protected static int MAXDISTANCE		=	300;

	protected static int FLINGDELAYTIME	=	0;

	private BFSDKManager bfusm;

	private boolean canCheckFling;

	private static final int MESSAGE_WHAT_DELAYFLINGTIME	=	1;

	private Handler handler	=	new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {

				case MESSAGE_WHAT_DELAYFLINGTIME:

					canCheckFling	=	true;

					break;


			}
		}
	};

	Gusture(BFSDKManager bfusm)
	{
		this.bfusm	=	bfusm;
	}

	@Override
	public boolean onDown(MotionEvent e) {

		SocketData sd	=	bfusm.getSocketData();
		sd.sendGesture(6);

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

		SocketData sd	=	bfusm.getSocketData();
		sd.sendGesture(5);
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

		SocketData sd	=	bfusm.getSocketData();
		int gesture		=	0;
		gesture	=	checkDirect2(e1,e2,velocityX,velocityY);
		if(canCheckFling && gesture != 0)
			sd.sendGesture(gesture);
		return false;
	}

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
			return 0;
		}
		int gesture	=	0;
		//向右滑动
		if(velocityX > MAXSPEED && e2.getX() - e1.getX() > MAXDISTANCE)
		{
			gesture	=	4;
		}
		//向左滑动
		else if(velocityX < -MAXSPEED && e2.getX() - e1.getX() < MAXDISTANCE)
		{
			gesture	=	3;
		}
		//向下 滑动
		else if(velocityY > MAXSPEED && e2.getY() - e1.getY() > MAXDISTANCE)
		{
			gesture	=	2;
		}
		//向上 滑动
		else if(velocityY < -MAXSPEED && e2.getY() - e1.getY() < MAXDISTANCE)
		{
			gesture	=	1;
		}

		return gesture;
	}

	private int checkDirect2(MotionEvent e1, MotionEvent e2, float velocityX,
							 float velocityY)
	{
		double speed	=	Math.sqrt(Math.abs(velocityX * velocityX) + Math.abs(velocityY * velocityY));
		if(speed < MAXSPEED)
		{
			return 0;
		}
		int gesture	=	0;
		float x1	=	e1.getX();
		float x2	=	e2.getX();
		float y1	=	e1.getY();
		float y2	=	e2.getY();

		float distanceX	=	Math.abs(x2 - x1);
		float distanceY	=	Math.abs(y2 - y1);
		double distance	=	Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		if(distance < MAXDISTANCE)
			return 0;

		if(Math.abs(x2 - x1) >= Math.abs(y2 - y1))
		{
			if(x2 - x1 < 0)
			{
				gesture	=	3;
			}
			else if(x2 - x1 > 0)
			{
				gesture	=	4;
			}
		}
		else
		{
			if(y2 - y1 < 0)
			{
				gesture	=	1;
			}
			else if(y2 - y1 > 0)
			{
				gesture	=	2;
			}
		}

		return gesture;
	}

}
