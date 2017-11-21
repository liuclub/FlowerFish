package com.bagelplay.sdk.common;


import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;

public class KeyMotion {
	
	private static final int MESSAGE_WHAT_KEY_MOTION	=	1;
	
 	
	private Handler handler	=	new Handler(){
		@Override
		public void handleMessage(Message msg) {   
            switch (msg.what) {   
            
            	case MESSAGE_WHAT_KEY_MOTION:
            		
            		KeyEvent ke		=	(KeyEvent) msg.obj;
            		Window.Callback	wc	=	bfusm.getWindowCallback();
            		            		
             		if(wc != null)
             		{
             			wc.dispatchKeyEvent(ke);
             		}
            			
            	break;
            	
            	
            }
		}
	};
	
	private SDKManager bfusm;
	
	KeyMotion(SDKManager bfusm)
	{
		this.bfusm	=	bfusm;
	}
	
	void doKey(int action, int keyCode)
	{
		KeyEvent event	=	getKeyEvent(action,keyCode);
		Message m		=	new Message();
		m.what			=	MESSAGE_WHAT_KEY_MOTION;
		m.obj			=	event;
		handler.sendMessage(m);
	}
	
	private KeyEvent getKeyEvent(int action,int keyCode)
	{
		KeyEvent event	=	new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(),  action, keyCode, 0);
 		return event;
	}
}
