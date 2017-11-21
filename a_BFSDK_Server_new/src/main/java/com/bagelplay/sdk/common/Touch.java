package com.bagelplay.sdk.common;


import android.os.Handler;
import android.os.Message;

import com.bagelplay.sdk.common.util.Log;
import com.bagelplay.sdk.test.MainGun;

import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import android.view.Window;

public class Touch {
	
	private static final int MESSAGE_WHAT_TOUCH	=	4;
	
	private Handler handler	=	new Handler(){
		@Override
		public void handleMessage(Message msg) {   
            switch (msg.what) {   
            
            	case MESSAGE_WHAT_TOUCH:
            		
            		MotionEvent me		=	(MotionEvent) msg.obj;
            		Window.Callback	wc	=	bfusm.getWindowCallback(); 
            		            		
            		bfusm.getMouse().debugForTouchShow((int)me.getX(), (int)me.getY());
            		
            		
            		
             		if(wc != null)
            			wc.dispatchTouchEvent(me);
            	break;
            	
            	
            }
		}
	};
	
	private SDKManager bfusm;
	
	private long time;
	
	Touch(SDKManager bfusm)
	{
		this.bfusm	=	bfusm;
	}
	
	
	public void touch(int action,float x,float y)
	{
		MotionEvent me	=	get1PointTouch(action,x,y);
		Message m		=	new Message();
		m.what			=	MESSAGE_WHAT_TOUCH;
		m.obj			=	me;
		handler.sendMessage(m);
	}
	
	public void touch(int action,float x,float y,int delay)
	{
		MotionEvent me	=	get1PointTouch(action,x,y);
		Message m		=	new Message();
		m.what			=	MESSAGE_WHAT_TOUCH;
		m.obj			=	me;
		handler.sendMessageDelayed(m, delay);
		
	}
	
	public void touch(int pointCount,int action,PointerCoords[] pcs,PointerProperties[] pps)
	{
		MotionEvent me	=	getMultiPointTouch(pointCount,action,pcs,pps);
		Message m		=	new Message();
		m.what			=	MESSAGE_WHAT_TOUCH;
		m.obj			=	me;
		handler.sendMessage(m);
	}
	
	public void doTouch(int pointCount,int action,PointerCoords[] pcs,PointerProperties[] pps)
	{
		MotionEvent me	=	getMultiPointTouch(pointCount,action,pcs,pps);
		Window.Callback	wc	=	bfusm.getWindowCallback(); 
		if(wc != null)
			wc.dispatchTouchEvent(me); 
		
		bfusm.getMouse().debugForTouchShow((int)pcs[0].x, (int)pcs[0].y);
 	}
	
	public void doTouch(int action,float x,float y)
	{
		MotionEvent me	=	get1PointTouch(action,x,y);
		Window.Callback	wc	=	bfusm.getWindowCallback(); 
		if(wc != null)
			wc.dispatchTouchEvent(me); 
	}
	
	private MotionEvent get1PointTouch(int action,float x,float y)
	{
		int[] currentWindowLocationXY	=	bfusm.getCurrentWindowLocationXY();
		int currentWindowLocationX		=	currentWindowLocationXY[0];
		int currentWindowLocationY		=	currentWindowLocationXY[1];
 		MotionEvent me	=	MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(),  action, x - currentWindowLocationX, y - currentWindowLocationY, 0);
		int source = InputDevice.SOURCE_TOUCHSCREEN;
		me.setSource(source);
		return me;
	}
	
	private MotionEvent getMultiPointTouch(int pointCount,int action,PointerCoords[] pcs,PointerProperties[] pps)
	{
		int[] currentWindowLocationXY	=	bfusm.getCurrentWindowLocationXY();
		int currentWindowLocationX		=	currentWindowLocationXY[0];
		int currentWindowLocationY		=	currentWindowLocationXY[1];
		for(int i=0;i<pcs.length;i++)
		{
			pcs[i].x	=	pcs[i].x - currentWindowLocationX;
			pcs[i].y	=	pcs[i].y - currentWindowLocationY;
		}
		int source = InputDevice.SOURCE_TOUCHSCREEN;
		MotionEvent me	=	MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), action, pointCount, pps, pcs, 0, 0, 1.0f, 1.0f, 0, 0, source, 0);
		me.setSource(source);
		return me;
	} 
	
	
	 
}
