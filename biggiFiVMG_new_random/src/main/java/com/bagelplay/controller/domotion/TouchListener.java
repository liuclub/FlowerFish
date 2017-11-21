package com.bagelplay.controller.domotion;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.Config;

public class TouchListener implements OnTouchListener{
	
	private Context context;
	
	private BagelPlayVmaStub bfVmaStub;
	
	public TouchListener(Context context)
	{
		this.context	=	context;
		
		bfVmaStub = BagelPlayVmaStub.getInstance();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		 
		int sdk_pointCount	=	event.getPointerCount();
		int sdk_action		=	event.getAction();
		int[] sdk_id		=	new int[sdk_pointCount];
		float[] sdk_x		=	new float[sdk_pointCount];
		float[] sdk_y		=	new float[sdk_pointCount];
		for(int i=0;i<sdk_pointCount;i++)
		{
			sdk_id[i]	=	event.getPointerId(i);
			sdk_x[i]	=	event.getX(i) * Config.tvWidthPixels / Config.widthPixels;
			sdk_y[i]	=	event.getY(i) * Config.tvHeightPixels / Config.heightPixels;
		}
		bfVmaStub.sendTouchData(sdk_pointCount,sdk_action,sdk_id,sdk_x,sdk_y);
		return true;
	}
	
	

}
