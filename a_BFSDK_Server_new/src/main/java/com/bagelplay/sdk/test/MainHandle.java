package com.bagelplay.sdk.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.ghandle.GHandle;
import com.bagelplay.sdk.common.util.Log;

import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainHandle extends Activity{
	
	private SDKManager bfusm;
			
	private GHandle gHandle;
	
	private TextView tv;
	
	private ScrollView sv;
	
	private Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		
 		
 		bfusm	=	SDKManager.getInstance(this);
 		bfusm.addWindowCallBack(this);
 		
 		bfusm.setControlZip("mouse.zip");
 		
 		gHandle	=	bfusm.getGHandle();
 		
 	 
 		
 		/* setContentView(R.layout.main);
 		 
 		 tv	=	(TextView) this.findViewById(R.id.tv);
        
        sv	=	(ScrollView) this.findViewById(R.id.sv);
        
        btn	=	(Button) this.findViewById(R.id.btn);
        
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText(null);
			}
		});*/
        
        
        Log.v("----------MainHandle-----------", " this " + this);
 	}
	
	@Override
	protected void onStart() {
 		super.onStart();
 		
 		 
	}
	
	
	public boolean dispatchTouchEvent2(MotionEvent event)
	{
		String str	=	"";
    	/*if(event.getPointerCount() == 1)
    	{
    		str	=	tv.getText()  + "\r\n" + "action:" + event.getAction() + "      x:" + event.getX() + "      y:" + event.getY() + "   " + event.getPointerCount(); 
    	}
    	else if(event.getPointerCount() == 2)
    	{
    		str	=	tv.getText()  + "\r\n" + "action:" + event.getAction();
    		str	+=	"      x1:" + event.getX(0) + "      y1:" + event.getY(0);
    		str	+=	"      x2:" + event.getX(1) + "      y2:" + event.getY(1) + "   " + event.getPointerCount(); 
    	}*/
		
		str	=	tv.getText()  + "\r\n" + "action:" + event.getAction() + "      x:" + event.getX() + "      y:" + event.getY() + "   " + event.getPointerCount(); 
		
		
    	//tv.setText(str);
    	
    	//sv.fullScroll(ScrollView.FOCUS_DOWN);
		if(event.getAction() == 2)
    		return true;
		Log.v("=---------------------dispatchTouchEvent---------------------", event.getAction() + " " + event.getPointerCount());
		if(event.getPointerCount() >  1)
		{
			for(int i=0;i<event.getPointerCount();i++)
			{
				Log.v("=---------------------dispatchTouchEvent---------------------", event.getPointerId(i) + " " + event.getX(i) + " " + event.getY(i));
			}
		}
		Log.v("=---------------------dispatchTouchEvent---------------------", "=--------------------------------------");
    	
		return super.dispatchTouchEvent(event);
	}
    
    /*public boolean onKeyDown(int keyCode,MotionEvent event)
    {
    	String str	=	tv.getText()  + "\r\n" + "onKeyDown   action:" + event.getAction() + "      keyCode:" + keyCode; 
    	
    	tv.setText(str);
    	
    	sv.fullScroll(ScrollView.FOCUS_DOWN);
    	return true;
    }
    
    public boolean onKeyUp(int keyCode,MotionEvent event)
    {
    	String str	=	tv.getText()  + "\r\n" + "onKeyUp   action:" + event.getAction() + "      keyCode:" + keyCode; 
    	
    	tv.setText(str);
    	
    	sv.fullScroll(ScrollView.FOCUS_DOWN);
    	return true;
    }*/
    
    public boolean dispatchKeyEvent(KeyEvent event)
    {
    	/*String str	=	tv.getText()  + "\r\n" + "dispatchKeyEvent   action:" + event.getAction() + "      keyCode:" + event.getKeyCode(); 
    	 
    	
    	tv.setText(str);
    	
    	sv.fullScroll(ScrollView.FOCUS_DOWN);
    	
      	return true;*/
    	if(bfusm.dispatchKeyEvent(event))
    		return true;
     	return super.dispatchKeyEvent(event);
    }
    
    public boolean dispatchGenericMotionEvent(MotionEvent ev)
    {
    	if(bfusm.dispatchGenericMotionEvent(ev))
    		return true;
    	return super.dispatchGenericMotionEvent(ev);
    }
    
    public void onResume()
    {
    	super.onResume();
    	bfusm.onResume();
    }
    
    
	
	
	
	 
	
}
