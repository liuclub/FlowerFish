package com.bagelplay.controller.domotion.customize;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class CHideButton extends CView{

	private int action;
	
	private int direct;
	
	private int[] TVXY	=	new int[2];
	
	public CHideButton(Context context, RelativeLayout rl) {
		super(context, rl);
 	}

	public void setTVXY(int x,int y)
	{
		TVXY[0]	=	x;
		TVXY[1]	=	y;
	}
	
	public void setAction(int action)
	{
		this.action	=	action;
	}
	
	public void setDirect(int direct)
	{
		this.direct	=	direct;
	}
	
	public int getAction()
	{
		return action;
	}
	
	public int getDirect()
	{
		return direct;
	}
	
	@Override
	public int[] getTVXY() {
 		return TVXY;
	}

	@Override
	public void arrange() {
 		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
 		return false;
	}
	
	

}
