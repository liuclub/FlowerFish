package com.bagelplay.controller.domotion.customize;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CButton extends CView {

	private int value;

	private Bitmap pic_normal;
	
	private Bitmap pic_press;

	private int up;

	private int down;

	private boolean isTouched;
	
	private int[] phoneXY	=	new int[2];
	
	private int[] TVXY		=	new int[2];
	
	private int[] wh		=	new int[2];
		
	private ImageView iv;
		
	private int touchID;
	
	public CButton(Context context,RelativeLayout rl) {
		super(context,rl);
		iv = new ImageView(context);
 	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setUp(int up) {
		this.up = up;
	}

	public void setDown(int down) {
		this.down = down;
	}

	public void setPic_normal(Bitmap pic_normal) {
		this.pic_normal = pic_normal;
		iv.setImageBitmap(pic_normal);
	}
	
	public void setPic_press(Bitmap pic_press)
	{
		this.pic_press	=	pic_press;
	}
	
	public void setPhoneXY(int x,int y)
	{
		phoneXY[0]	=	x;
		phoneXY[1]	=	y;
	}
	
	public void setTVXY(int x,int y)
	{
		TVXY[0]	=	x;
		TVXY[1]	=	y;
	}
 
 
	public void arrange()
	{
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		wh[0]		=	pic_normal.getWidth();
		wh[1]		=	pic_normal.getHeight();
		int left	=	phoneXY[0] - wh[0] / 2;
		int top		=	phoneXY[1] - wh[1] / 2;
		
		rllp.leftMargin	=	left;
		rllp.topMargin	=	top;
		
		
		 
		
		rl.addView(iv, rllp);
		
	 
		
		
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (isTouchDownAction(event)) {
			if (isSimulateTouch) {
				simulateTouchAction = MotionEvent.ACTION_DOWN;
				isSimulateHold = false;
			} else {
				bfvs.sendGameHandlerHandlerButtonData(value, down);
			}
			iv.setImageBitmap(pic_press);
		} else if (isTouchUpAction(event)) {
			if (isSimulateTouch) {
				simulateTouchAction = MotionEvent.ACTION_UP;
				isSimulateHold = false;
			} else {
				bfvs.sendGameHandlerHandlerButtonData(value, up);
			}
			iv.setImageBitmap(pic_normal);
		}
		return false;
	}

	private boolean isTouchDownAction(MotionEvent event) {
		if (isTouched)
			return false;

		int action = event.getAction();

		if (action == MotionEvent.ACTION_MOVE) {
			for (int i = 0; i < event.getPointerCount(); i++) {
				if (isInBorder((int) event.getX(i),(int) event.getY(i))) {
					isTouched = true;
					touchID	=	event.getPointerId(i);
 					return true;
				}
			}
		}

		if (action == MotionEvent.ACTION_DOWN
				|| (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
			int index = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

			if (isInBorder((int) event.getX(index),(int) event.getY(index))) {
				isTouched = true;
				touchID	=	event.getPointerId(index);
 				return true;
			}
		}

 		return false;
	}

	private boolean isTouchUpAction(MotionEvent event) {
		if (!isTouched)
			return false;

		int action = event.getAction();
		
		if(action == MotionEvent.ACTION_UP && event.getPointerId(0) == touchID)
		{
			isTouched = false;
			return true;
		}
		else if(action == MotionEvent.ACTION_MOVE)
		{
 			for(int i=0;i<event.getPointerCount();i++)
			{
				if(event.getPointerId(i) == touchID)
				{
					if(!isInBorder((int) event.getX(i),(int) event.getY(i)))
					{
						isTouched = false;
						return true;
					}
				}
			}
		}
		else if((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP)
		{
			int index	=	(action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			if(event.getPointerId(index) == touchID)
			{
				isTouched	=	false;
				return true;
			}
		}
		
		return false;

	}
	
	private boolean isInBorder(int ex,int ey)
	{
		return ex >= phoneXY[0] - wh[0] / 2 && ex <= phoneXY[0] + wh[0] / 2
				&& ey >= phoneXY[1] - wh[1] / 2 && ey <= phoneXY[1] + wh[1] / 2;
	}

	@Override
	public int[] getTVXY() {
 		return TVXY;
	}

}
