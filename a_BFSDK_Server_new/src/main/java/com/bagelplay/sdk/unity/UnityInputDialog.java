package com.bagelplay.sdk.unity;

import java.lang.reflect.Field;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.unity3d.player.UnityPlayer;

class UnityInputDialog {
	
	
	//private static final int MESSAGE_WHAT_POSTDIALOG	=	5;
	
	private static final int MESSAGE_WHAT_CHECKSHOW		=	6;
	
	private Handler handler	=	new Handler(){
		@Override
		public void handleMessage(Message msg) {   
						
            switch (msg.what) {   
            
            	/*
            	case MESSAGE_WHAT_POSTDIALOG:
            		
            		String str	=	(String) msg.obj;
            		doPostDialog(str);
            		 
            	break;
            	*/
            	
            	case MESSAGE_WHAT_CHECKSHOW:
            		
            		doCheckShow();
            		
            		break;
            	
            	
            }
		}
	};
	
	private SDKUnityManager bfsdkum;
	
	private Window.Callback lastWC;
	
	UnityInputDialog(SDKUnityManager bfsdkum)
	{
		this.bfsdkum	=	bfsdkum;
	}
	
	void checkShow()
	{
 		handler.sendEmptyMessageDelayed(MESSAGE_WHAT_CHECKSHOW, 1000);
	}
	
	
	private void doCheckShow()
	{
 		final Dialog dialog	=	getUnityInputDialog(bfsdkum.getWindowCallback());
 		if(dialog != null && dialog.isShowing())
		{
			lastWC	=	bfsdkum.getWindowCallback();
			bfsdkum.addWindowCallBack(dialog);
			EditText et	=	getEditText(dialog);
			if(et != null)
			{
				bfsdkum.showKeyboardForEditTextAndKeepKbdAfterFill(et);
			}
			dialog.setOnDismissListener(new Dialog.OnDismissListener(){

				@Override
				public void onDismiss(DialogInterface di) {
					bfsdkum.removeWindowCallBack(dialog);
				}
				
			});
		}
	}
	
	private EditText getEditText(Dialog dialog)
	{
		View v = ((ViewGroup)dialog.findViewById(android.R.id.content)).getChildAt(0);  
		ViewGroup vg	=	(ViewGroup)v;
		for(int i=0;i<vg.getChildCount();i++)
		{
			View view	=	vg.getChildAt(i);
			if(EditText.class.isAssignableFrom(view.getClass()))
			{
				 EditText et	=	(EditText)view;
				 return et;
			}
		}
		return null;
	}
 
	
	private void doClickOkBtn(Dialog dialog)
	{
		View v = ((ViewGroup)dialog.findViewById(android.R.id.content)).getChildAt(0); 
		ViewGroup vg	=	(ViewGroup)v;
		for(int i=0;i<vg.getChildCount();i++)
		{
			View view	=	vg.getChildAt(i);
			if(view.getClass() == Button.class)
			{
				int source = InputDevice.SOURCE_TOUCHSCREEN;
	 			MotionEvent me3	=	MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(),  MotionEvent.ACTION_DOWN, 0, 0, 0);
 				me3.setSource(source);
 				view.onTouchEvent(me3);
				
 				me3	=	MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(),  MotionEvent.ACTION_UP, 0, 0, 0);
 				me3.setSource(source);
 				view.onTouchEvent(me3);
			}	
		 
		}
	}
	
	private Dialog getUnityInputDialog(Window.Callback wc) 
	{
		try
		{
			Dialog dialog	=	null;
			Class c	= Class.forName("com.unity3d.player.UnityPlayerActivity");
			Field[] fields=c.getDeclaredFields();
			for(int i=0;i<fields.length;i++)
			{
				fields[i].setAccessible(true);
				if(fields[i].getType() == com.unity3d.player.UnityPlayer.class)
				{
 					
					com.unity3d.player.UnityPlayer up	=	(UnityPlayer) fields[i].get(wc);
					Class c2	= Class.forName("com.unity3d.player.UnityPlayer");
					Field[] fields2=c2.getDeclaredFields();
					for(int j=0;j<fields2.length;j++)
					{
						fields2[j].setAccessible(true);
						if(android.app.Dialog.class.isAssignableFrom(fields2[j].getType()))
						{
 							
							dialog	=	(Dialog)fields2[j].get(up);
							return dialog;
						}
						 
					}
			 
				}
				 
			}
		}catch(Exception e){
			e.printStackTrace();
 		}
		
		return null;
		 
	}
}
