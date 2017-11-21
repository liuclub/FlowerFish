package com.bagelplay.controller.domotion;

import android.content.Context;
import android.graphics.Bitmap;

public class MouseView extends DoMotionView{

	private MouseListener mouseListen;
	
	public MouseView(Context context, JsonParser jp) {
		super(context,jp);
		
		mouseListen	=	new MouseListener(context);
		
		setOnTouchListener(mouseListen);
		
 	}

	@Override
	public Bitmap getDefaultBackground() {
		
		
 		return null;
	}
 
}
