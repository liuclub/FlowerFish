package com.bagelplay.controller.domotion;

import android.content.Context;
import android.graphics.Bitmap;

public class TouchView extends DoMotionView{

	private TouchListener touchListen;
	
	public TouchView(Context context, JsonParser jp) {
		super(context,jp);
		
		touchListen	=	new TouchListener(context);
		
		setOnTouchListener(touchListen);
		
  	}
	
	@Override
	public Bitmap getDefaultBackground() {
 		return null;
	}
 

}
