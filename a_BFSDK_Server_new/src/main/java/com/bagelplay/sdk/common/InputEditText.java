package com.bagelplay.sdk.common;

import java.lang.ref.WeakReference;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.bagelplay.sdk.common.util.Log;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

class InputEditText {

	private static String TAG	=	"InputEditText";
	
	private static final int MESSAGE_WHAT_SET_FILL_EDITTEXT	=	1;
	
	private static final int MESSAGE_WHAT_HIDE_IME	=	2;
	
	private Handler handler	=	new Handler(){
		@Override
		public void handleMessage(Message msg) {   
						
            switch (msg.what) {   
            
            	case MESSAGE_WHAT_SET_FILL_EDITTEXT:
            		
            		String str	=	(String) msg.obj;
            		int keepKbdAfterFill	=	msg.arg1;
            		doFillEdtiText(str,keepKbdAfterFill);
            		
            	break;
            	
            	case MESSAGE_WHAT_HIDE_IME:
            		EditText et	=	(EditText)msg.obj;
            		doHideSystemKeyBoard(et);
            	break;
            	 
            	
            }
		}
	};
	
	private WeakReference<EditText> tempEditText;
	
	private SDKManager bfusm;
	
	//1 keep 0 don't keep
	private int keepKbdAfterFill;
	
	public InputEditText(SDKManager bfusm)
	{
		this.bfusm	=	bfusm;
	}

	void showKeyboardForEditText(EditText et)
	{
		this.tempEditText	=	new WeakReference<EditText>(et);
		bfusm.getSocketStub().sendShowInputData();
		keepKbdAfterFill	=	0;
	}
	
	void showKeyboardForEditTextAndKeepKbdAfterFill(EditText et)
	{
		showKeyboardForEditText(et);
		keepKbdAfterFill	=	1;
	}
	
	void fillEditText(String str)
	{
		Message m	=	new Message();
		m.what		=	MESSAGE_WHAT_SET_FILL_EDITTEXT;
		m.arg1		=	keepKbdAfterFill;
		m.obj		=	str;
		handler.sendMessage(m);
	}
	
	private void doFillEdtiText(String str,int keepKbdAfterFill)
	{
		Log.v(TAG, "doFillEdtiText " + "str:" + str + " keepKbdAfterFill:" + keepKbdAfterFill + " tempEditText:" + tempEditText + " EditText:" + (tempEditText == null ? "null" : tempEditText.get()));
		if(tempEditText != null)
		{
			EditText et	=	tempEditText.get();
			if(et != null)
			{
				et.setText(str);
				tempEditText.clear();
				if(keepKbdAfterFill == 0)
				{
					hideSystemKeyBoard(et);
				}
					
			}
		}
	}
	
	public void hideSystemKeyBoard(EditText et)
	{
		Message m	=	new Message();
		m.what		=	MESSAGE_WHAT_HIDE_IME;
 		m.obj		=	et;
		handler.sendMessageDelayed(m, 0);
	}
	
	private void doHideSystemKeyBoard(EditText et)
	{
		Log.v(TAG, "doHideSystemKeyBoard " + "et:" + et);
		Context context	=	bfusm.getApplicationContext();
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}
	
	void clear()
	{
		if(tempEditText != null)
			tempEditText.clear();
		tempEditText	=	null;
	}
}
