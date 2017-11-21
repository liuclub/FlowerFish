package com.bagelplay.controller.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BagelPlayTouch extends LinearLayout{

	private int downX;
	
	private int screenWidth;
	
	private int lastMenuWidth;
	
	private long t1;
	
	private int lastX;
	
	private Handler handler	=	new Handler();
	
	public BagelPlayTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
 	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		if(ev.getAction() == MotionEvent.ACTION_DOWN)
		{
			downX	=	(int)ev.getRawX();
			lastMenuWidth	=	getWidth();
			t1	=	System.currentTimeMillis();
			lastX	=	getLeft();
		}
		
		if(ev.getAction() == MotionEvent.ACTION_MOVE)
		{
			int dx 	=(int)ev.getRawX() - downX;
			downX	=	(int)ev.getRawX();
			RelativeLayout.LayoutParams tlp	=	(android.widget.RelativeLayout.LayoutParams) getLayoutParams();
 			RelativeLayout.LayoutParams lp	=	new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
 			lp.topMargin	=	tlp.topMargin;
			int marginLeft	=	getLeft() + dx;
				if(marginLeft < 0)
					marginLeft = 0;
				if(marginLeft >=  screenWidth - lastMenuWidth)
					marginLeft	=	screenWidth - lastMenuWidth;
 				lp.leftMargin = marginLeft + 1;
 				lp.rightMargin=screenWidth;
				setLayoutParams(lp);
				
			 
		}
		
		if(ev.getAction() == MotionEvent.ACTION_UP)
		{
			if(!(System.currentTimeMillis() - t1 < 300 && Math.abs(getLeft() - lastX) < 5))
				return true;
		}
		 
		return super.onInterceptTouchEvent(ev);
	}
 
	public void setScreenWidth(int screenWidth)
	{
		this.screenWidth	=	screenWidth;
	}
	
	public void setCenter(int screenWidth)
	{
		this.screenWidth	=	screenWidth;
		int left	=	(screenWidth - getWidth()) / 2;
		RelativeLayout.LayoutParams tlp =	(RelativeLayout.LayoutParams)getLayoutParams();
		RelativeLayout.LayoutParams lp	=	new android.widget.RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.leftMargin	=	left;
		lp.topMargin	=	tlp.topMargin;
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
		setLayoutParams(lp);
		
		
	}
}
