package com.bagelplay.controller.domotion;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import android.widget.RelativeLayout;

import com.bagelplay.controller.domotion.customize.CView;
import com.bagelplay.controller.utils.BagelPlayVmaStub;

public class CustomizedView extends DoMotionView {

	private List<CView> component = new ArrayList<CView>();

	private boolean isSimulateTouch;

	private CView[] touchedComponent = new CView[10];
	
	private CView[] sendComponent = new CView[touchedComponent.length];

	private int sendTouchedComponentLen;

	private BagelPlayVmaStub bfVmaStub;

	public CustomizedView(Context context, JsonParser jp) {
		super(context, jp);

		this.isSimulateTouch = jp.isSimulateTouch();

		bfVmaStub = BagelPlayVmaStub.getInstance();

		component = jp.getComponents();
		for (int i = 0; i < component.size(); i++) {
			component.get(i).setDoMotionView(this);
			component.get(i).setSimulateTouch(isSimulateTouch);
		}
	}     

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		android.util.Log.v("=-------------------------------------------=", "checkSimulateScreenTouch() dispatchTouchEvent");
		for (int i = 0; i < component.size(); i++) {
			component.get(i).dispatchTouchEvent(ev);
			if (isSimulateTouch)

				checkSimulateScreenTouch(component.get(i));
		}

		return true;
	}

	@Override
	public Bitmap getDefaultBackground() {
		return null;
	}

	public void checkSimulateScreenTouch()
	{
		android.util.Log.v("=-------------------------------------------=", "checkSimulateScreenTouch() ");
		for (int i = 0; i < component.size(); i++) {
 			if (isSimulateTouch)
 			{
 				checkSimulateScreenTouch(component.get(i));
 			}
		}
	}

	private void checkSimulateScreenTouch(CView cv) {
		if (!cv.isSimulateHold()) {
			if (cv.getSimulateTouchAction() == MotionEvent.ACTION_DOWN
					&& putCView(cv) != -1) {
			
				
				toScreenTouch();
				clearTouch();
			} else {
				toScreenTouch();
				clearTouch();
			}
		}
	}

	private int putCView(CView cv) {
		int j = -1;
		for (int i = 0; i < touchedComponent.length; i++) {
			if (touchedComponent[i] == cv)
				return -1;
			if (touchedComponent[i] == null && j == -1)
				j = i;
		}
		touchedComponent[j] = cv;
		cv.setSimulateTouchID(j);
		return j;
	}

	private void toScreenTouch() 
	{
		sendTouchedComponentLen = 0;
		int mainAction = MotionEvent.ACTION_MOVE;
 		for (int i = 0; i < touchedComponent.length; i++) 
		{
			if (touchedComponent[i] != null) 
			{
				if(!touchedComponent[i].isSimulateHold())
				{
					if(touchedComponent[i].getSimulateTouchAction() == MotionEvent.ACTION_DOWN)
					{
						mainAction	=	MotionEvent.ACTION_POINTER_DOWN | (i << 8); 
					}
					else if(touchedComponent[i].getSimulateTouchAction() == MotionEvent.ACTION_UP)
					{
						int k	=	i;
 						for(int j = k - 1;j>=0;j--)
						{
							if(touchedComponent[j] == null)
								k	=	j;
						}
 						mainAction	=	MotionEvent.ACTION_POINTER_UP | (k << 8); 
					}
				}
				sendComponent[sendTouchedComponentLen++]	=	touchedComponent[i];
			}
			
		}
		if (sendTouchedComponentLen == 0)
			return;
		int id[] = new int[sendTouchedComponentLen];
		float x[] = new float[sendTouchedComponentLen];
		float y[] = new float[sendTouchedComponentLen];
		for (int i = 0; i < sendTouchedComponentLen; i++) 
		{
			 
			sendComponent[i].setSimulateHold(true);

			int[] screenXY = sendComponent[i].getTVXY();
			x[i] = screenXY[0];
			y[i] = screenXY[1];
			id[i] = sendComponent[i].getSimulateTouchID();
		}

		
 		
		bfVmaStub.sendTouchData(sendTouchedComponentLen, mainAction, id, x, y);
	}

	private void clearTouch() {
		for (int i = 0; i < touchedComponent.length; i++) {
			if (touchedComponent[i] != null) {
				if (touchedComponent[i].getSimulateTouchAction() == MotionEvent.ACTION_UP) {
					touchedComponent[i].resetSimulate();
					touchedComponent[i] = null;
				}
			}
		}
	}

}
