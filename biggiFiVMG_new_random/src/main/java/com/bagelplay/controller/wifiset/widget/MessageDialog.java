package com.bagelplay.controller.wifiset.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageDialog extends BFDialog{

	private TextView tv;
	
	public MessageDialog(Context context) {
		super(context);
		
		tv	=	new TextView(context);
		//tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setGravity(Gravity.CENTER);
		contentLL.addView(tv,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
 	}
	
	public void setMess(Object text)
	{
		super.setText(tv,text);
	}
 

}
