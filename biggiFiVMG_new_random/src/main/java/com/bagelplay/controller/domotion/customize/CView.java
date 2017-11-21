package com.bagelplay.controller.domotion.customize;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.bagelplay.controller.domotion.CustomizedView;
import com.bagelplay.controller.utils.BagelPlayVmaStub;

public abstract class CView{

	private String type;

	protected Context context;

	protected CustomizedView cv;
	
	protected BagelPlayVmaStub bfvs;

	protected int simulateTouchID;

	protected int simulateTouchAction = -1;

	protected boolean isSimulateHold;

	protected boolean isSimulateTouch;

	protected RelativeLayout rl;
	
	public CView(Context context,RelativeLayout rl) 
	{
 
		this.context = context;
		
		this.rl	=	rl;

		bfvs = BagelPlayVmaStub.getInstance();
	}

	public void setDoMotionView(CustomizedView cv) 
	{
		this.cv = cv;
	}

	public void setSimulateTouchID(int simulateTouchID) 
	{
		this.simulateTouchID = simulateTouchID;
	}

	public int getSimulateTouchID()
	{
		return simulateTouchID;
	}

	public int getSimulateTouchAction() 
	{
		return simulateTouchAction;
	}
	
	public void setSimulateTouchAction(int simulateTouchAction)
	{
		this.simulateTouchAction	=	simulateTouchAction;
	}

	public void setSimulateHold(boolean isSimulateHold) 
	{
		this.isSimulateHold = isSimulateHold;
	}

	public boolean isSimulateHold() 
	{
		return isSimulateHold;
	}

	public void setSimulateTouch(boolean isSimulateTouch) 
	{
		this.isSimulateTouch = isSimulateTouch;
	}

	public void resetSimulate() 
	{
		simulateTouchAction = -1;
	}

	public void setType(String type) 
	{
		this.type = type;
	}

	protected boolean isInBorder(int ex, int ey,int x,int y,int width,int height) 
	{
 		return ex >= x - width / 2 && ex <= x + width / 2
				&& ey >= y - height / 2 && ey <= y + height / 2;
	}

	public abstract int[] getTVXY();
	
	public abstract void arrange();
	
	public abstract boolean dispatchTouchEvent(MotionEvent event);

}
