package com.bagelplay.controller.domotion.customize;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CStick extends CView {

	

	private int downX;

	private int downY;

	private int id;

	private int centerX = -1;

	private int centerY = -1;

	private ImageView rockIV;

	private boolean isTouched;

	private int downID = -1;

	
	

	
	
	private int phoneRadius;
	
	private int[] phoneCenterXY	=	new int[2];
	
	private int TVRadius;
	
	private int[] TVCenterXY		=	new int[2];
		
	private int[] TVXY		=	new int[2];
		
	private ImageView bottomIV;
	
	private ImageView stickIV;
	
	private Bitmap bottomPic;
	
	private Bitmap stickPic;
	
	private float offsetX;
	
	private float offsetY;
	
	
	private int[] rangeXY	=	new int[2];
	
	private int[] rangeWH	=	new int[2];
	
	
	public CStick(Context context,RelativeLayout rl) {
		super(context,rl);
		bottomIV		=	new ImageView(context);
		stickIV			=	new ImageView(context);
 	}
	
	public void setPhoneParameter(int radius,int x,int y)
	{
		phoneRadius	=	radius;
		phoneCenterXY[0]	=	x;
		phoneCenterXY[1]	=	y;
	}
	
	public void setTVParameter(int radius,int x,int y)
	{
		TVRadius	=	radius;
		TVCenterXY[0]		=	x;
		TVCenterXY[1]		=	y;
	}
	
	public void setBottomPic(Bitmap bottomPic) 
	{
		this.bottomPic = bottomPic;
 	}
	
	public void setStickPic(Bitmap stickPic)
	{
		this.stickPic	=	stickPic;
	}
	
	public void setRange(int x,int y,int width,int height)
	{
		rangeXY[0]	=	x;
		rangeXY[1]	=	y;
		rangeWH[0]	=	width;
		rangeWH[1]	=	height;
	}
	
	public void arrange()
	{
		int[] bottomWH	=	new int[2];
		bottomWH[0]		=	bottomPic.getWidth();
		bottomWH[1]		=	bottomPic.getHeight();
		int[] stickWH	=	new int[2];
		stickWH[0]		=	stickPic.getWidth();
		stickWH[1]		=	stickPic.getHeight();
		
		bottomIV.setImageBitmap(bottomPic);
		stickIV.setImageBitmap(stickPic);
		
		arrange(bottomIV,phoneCenterXY,bottomWH);
		arrange(stickIV,phoneCenterXY,stickWH);		
	}
	
	public void reArrange(int x,int y)
	{
		rl.removeView(bottomIV);
		rl.removeView(stickIV);
		phoneCenterXY[0]	=	x;
		phoneCenterXY[1]	=	y;
		arrange();
	}
	
	public void setId(int id)
	{
		this.id	=	id;
	}
	
	private void arrange(ImageView iv,int[] xy,int[] wh)
	{
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
 
		int left	=	xy[0] - wh[0] / 2;
		int top		=	xy[1] - wh[1] / 2;
		
		rllp.leftMargin	=	left;
		rllp.topMargin	=	top;
		
		rl.addView(iv, rllp);
	}
	
	private void reArrangeStick(int offsetX,int offsetY)
	{
		RelativeLayout.LayoutParams rllp =	(LayoutParams) stickIV.getLayoutParams();
		rllp.leftMargin	=	phoneCenterXY[0] - stickPic.getWidth() / 2 + offsetX;
		rllp.topMargin	=	phoneCenterXY[1] - stickPic.getHeight() / 2 + offsetY;
		stickIV.setLayoutParams(rllp);
	}
	
	

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int pointAction = action & MotionEvent.ACTION_MASK;
		if (!isTouched
				&& (pointAction == MotionEvent.ACTION_DOWN || pointAction == MotionEvent.ACTION_POINTER_DOWN)) {
			int index = event.getActionIndex();
			downID = event.getPointerId(index);
			
			int ex	=	(int) event.getX(index);
			int ey	=	(int) event.getY(index);
			if(isInRange(ex,ey))
			
				reArrange(ex,ey);
			
			if (isInBorder(ex,ey)) {
				isTouched = true;

				doTouchDown(ex, ey);
			}
		} else if (isTouched && event.getAction() == MotionEvent.ACTION_MOVE) {
			for (int i = 0; i < event.getPointerCount(); i++) {
				int id = event.getPointerId(i);
				if (id == downID) {
					doTouchMove((int) event.getX(i), (int) event.getY(i));
					break;
				}
			}
		} else if (isTouched
				&& (pointAction == MotionEvent.ACTION_UP || pointAction == MotionEvent.ACTION_POINTER_UP)) {
			int id = event.getPointerId(event.getActionIndex());
			if (id == downID) {
				downID = -1;
				isTouched = false;
				doTouchUp();
			}
		}

		return false;
	}

	public void doTouchDown(int pX, int pY) {
		downX = pX;
		downY = pY;
		if (isSimulateTouch) {
			setSimulateXY();
			simulateTouchAction = MotionEvent.ACTION_DOWN;
			isSimulateHold = false;
		}
	}

	public void doTouchMove(int pX, int pY) {

		checkInCircle(pX, pY);
		if (isSimulateTouch) {
			setSimulateXY();
			simulateTouchAction = MotionEvent.ACTION_MOVE;
			isSimulateHold = false;
		} else {
			sendStickData();
		}

	}

	public void doTouchUp() {
		if (isSimulateTouch) {
			offsetX	=	0;
			offsetY	=	0;
			setSimulateXY();
			simulateTouchAction = MotionEvent.ACTION_UP;
			isSimulateHold = false;
		}
		reArrangeStick(0,0);

		if (!isSimulateTouch) {
			sendStickData();
		}

	}

	private void checkInCircle(int pX, int pY) {
		int tR = (int) Math.sqrt(Math.pow(pX - downX, 2)
				+ Math.pow(pY - downY, 2));
		tR = tR > phoneRadius ? phoneRadius : tR;

		int offsetX = 0;
		int offsetY = 0;
		if (pY == downY) {
			if (pX > downX)
				offsetX = tR;
			else
				offsetX = -tR;
		} else if (pX == downX) {
			if (pY > downY)
				offsetY = tR;
			else
				offsetY = -tR;
		} else {
			double degreeX = pX - downX + 0f;
			double degreeY = pY - downY + 0f;
			double rate = Math.abs(degreeY) / Math.abs(degreeX);
			offsetY = (int) (tR * Math.sin(Math.atan(rate)));
			offsetX = (int) (tR * Math.cos(Math.atan(rate)));
			offsetX = degreeX > 0 ? offsetX : -offsetX;
			offsetY = degreeY > 0 ? offsetY : -offsetY;
		}
		this.offsetX	=	offsetX;
		this.offsetY	=	offsetY;
		reArrangeStick(offsetX,offsetY);
	}

	private void sendStickData() {
		float sendX = (float) (offsetX / phoneRadius); 
		float sendY = (float) (offsetY / phoneRadius);

		bfvs.sendGameHandlerHandlerStickData(id, sendX, sendY);
	}

 	public int[] getTVXY() 
 	{
		return TVXY;
	}

	private void setSimulateXY() {
		TVXY[0] = TVCenterXY[0] + (int) (offsetX * TVRadius / phoneRadius);
		TVXY[1] = TVCenterXY[1] + (int) (offsetY * TVRadius / phoneRadius);
	}
 
	
	private boolean isInBorder(int ex,int ey)
	{
		return ex >= phoneCenterXY[0] - phoneRadius && ex <= phoneCenterXY[0] + phoneRadius
				&& ey >= phoneCenterXY[1] - phoneRadius && ey <= phoneCenterXY[1] + phoneRadius;
	}
	
	private boolean isInRange(int ex,int ey)
	{
		return ex >= rangeXY[0] && ex <= rangeXY[0] + rangeWH[0]
				&& ey >= rangeXY[1] && ey <= rangeXY[1] + rangeWH[1];
	}

}
