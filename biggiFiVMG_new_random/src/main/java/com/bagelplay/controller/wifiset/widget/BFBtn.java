package com.bagelplay.controller.wifiset.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.Log;

public class BFBtn extends RelativeLayout{
		
	//private View btn1;
	
	//private View btn2;

	private TextView tv;
	
	public BFBtn(Context context)
	{
		super(context);
		
		//View view	=	View.inflate(context, R.layout.bfbtn, null);
		//addView(view);
		
		//tv	=	(TextView) view.findViewById(R.id.tv2);
		
		//btn1	=	view.findViewById(R.id.btn1);
		
		//btn2	=	view.findViewById(R.id.btn2);
		
		tv	=	new TextView(context);
		
		tv.setTextColor(0xff4cddff);
		
		addView(tv);
	}
	
	/*public BFBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		View view	=	View.inflate(context, R.layout.bfbtn, null);
		addView(view);
		
		tv	=	(TextView) view.findViewById(R.id.tv2);
		
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.bfbtn);
		for(int i=0;i<a.getIndexCount();i++)
		{
			int attr = a.getIndex(i);
			switch(attr)
			{
				case R.styleable.bfbtn_text:
					setText(a);
				break;
			}
		}
		
  	}*/
 
	
	/*public boolean onTouchEvent(MotionEvent event)
	{
		
		Log.v("----------------------------------------------", "onTouchEvent " + event.getAction() + "");
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			btn1.setVisibility(View.VISIBLE);
			tv.setTextColor(0xff4cddff);
		}
		return true;
	}*/

	public boolean dispatchTouchEvent(MotionEvent event) 
	{ 
  		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			//btn1.setVisibility(View.INVISIBLE);
			//btn2.setVisibility(View.INVISIBLE);
			tv.setTextColor(0xff57565f);
		}
  		else
		{
			//btn1.setVisibility(View.VISIBLE);
			tv.setTextColor(0xff4cddff);
		}
 		return super.dispatchTouchEvent(event);
	}
	

	
	public void setText(String text)
	{
		tv.setText(text);
 	}
	
	
	
	private void setText(TypedArray a)
	{
		int resouceId	=	a.getResourceId(R.styleable.bfbtn_text, -1);
		if(resouceId > 0)
			tv.setText(resouceId);
		else
			tv.setText(a.getString(R.styleable.bfbtn_text));
	}

}
