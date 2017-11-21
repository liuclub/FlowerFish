package com.bagelplay.controller.domotion;



import com.bagelplay.controller.com.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GestureView extends DoMotionView{

	private static final int MESSAGE_WHAT_DISPEARARROW		=	2;
	
	private static final int DISPEARARROWTIME				=	300;
	
	private GestureListenerWidthArrow gestureListen;
	
	private ImageView signIV;
	
	private ImageView circleIV;
	
	private Handler handler	=	new Handler(){
		@Override
		public void handleMessage(Message msg) {   
            switch (msg.what) {   
            
            	 
            	case MESSAGE_WHAT_DISPEARARROW:
            		
            		signIV.setVisibility(View.GONE);
            	
            		break;
            	
            }
		}
	};
	
	public GestureView(Context context, JsonParser jp) {
		super(context,jp);
		
		View view	=	View.inflate(context, R.layout.arrow_mouse_ui_v, null);
		LinearLayout.LayoutParams lllp	=	new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		addView(view,lllp);
		
		signIV	=	(ImageView) findViewById(R.id.arrow);
		
		circleIV	=	(ImageView) findViewById(R.id.circle);
		
		gestureListen	=	new GestureListenerWidthArrow(context);
		
		setOnTouchListener(gestureListen);
	
 	}
	
	
	class GestureListenerWidthArrow	extends GestureListener
	{

		public GestureListenerWidthArrow(Context context) {
			super(context);
 		}

		@Override
		protected void doAfterFling(int gesture) {
			circleIV.setVisibility(View.GONE);
			
			Matrix matrix = new Matrix();  
			Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.rc_touch_arrow_up)).getBitmap();  
			if(gesture == GESTURE_SLIDE_DOWN)
			{
				matrix.setRotate(180);  
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);  
			}
			else if(gesture == GESTURE_SLIDE_LEFT)
			{
				matrix.setRotate(270);  
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);  
			}
			else if(gesture == GESTURE_SLIDE_RIGHT)
			{
				matrix.setRotate(90);  
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);  
			}
			
			signIV.setImageBitmap(bitmap);
			signIV.setVisibility(View.VISIBLE);
			
			handler.removeMessages(MESSAGE_WHAT_DISPEARARROW);
			handler.sendEmptyMessageDelayed(MESSAGE_WHAT_DISPEARARROW, DISPEARARROWTIME);
		}

		@Override
		protected void doAfterClick() {
			
			handler.removeMessages(MESSAGE_WHAT_DISPEARARROW);
			signIV.setVisibility(View.GONE);
			Animation scaleAnimation = new ScaleAnimation(1f, 0.5f, 1f,  0.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
			scaleAnimation.setAnimationListener(new Animation.AnimationListener(){

				@Override
				public void onAnimationStart(Animation animation) {
	 				
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					circleIV.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
	 				
				}
				
			});
			scaleAnimation.setDuration(300);
	 		circleIV.setVisibility(View.VISIBLE);
			circleIV.startAnimation(scaleAnimation);
		}
		
	}
	
	@Override
	public Bitmap getDefaultBackground() {
 		return null;
	}

}
