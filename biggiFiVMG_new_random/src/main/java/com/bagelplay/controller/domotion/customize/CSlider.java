package com.bagelplay.controller.domotion.customize;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class CSlider extends CView {

	public static final int SLIDE_NULL		=	0;
	
	public static final int SLIDE_UP		=	1;
	
	public static final int SLIDE_DOWN		=	2;
	
	public static final int SLIDE_LEFT		=	3;
	
	public static final int SLIDE_RIGHT		=	4;
	
	public static final int CLICK			=	5;
	
	public static final int ACTION_SLIDE	=	6;
	
	public static final int ACTION_CLICK	=	7;
	
	public static final int ACTION_LONG_TOUCH	=	8;
		
	
	private int MAXSPEED		=	300;
	
	private int MAXDISTANCE		=	150;
	
	private int[] phoneXY	=	new int[2];
	
	private int[] wh		=	new int[2];

	private List<int[]> route	=	new ArrayList<int[]>();
	
	private int touchID;
	
	private boolean isTouched;
	
	private long downTime;
	
	private long upTime;
	
	private List<CHideButton> btn	=	new ArrayList<CHideButton>();
		
	private Handler handler	=	new Handler();
	
	private Runnable clickDownRunable;
	
	private Runnable clickUpRunable;
	
	private boolean isLongTouch;
	
	private float[] firstTouchXY	=	new float[2];
	
	private float[] touchXY	=	new float[2];
	
	private Runnable checkLongTouch	=	new Runnable(){
		public void run()
		{
			if(Math.abs(touchXY[0] - touchXY[0]) <= 10 && Math.abs(touchXY[1] - touchXY[1]) <= 10)
			{
				isLongTouch	=	true;
				doLongTouchDown();
			}
		}
	};
	
	public CSlider(Context context,RelativeLayout rl) {
		super(context,rl);
 	}

	public void setPhoneXY(int x,int y)
	{
		phoneXY[0]	=	x;
		phoneXY[1]	=	y;
	}
	
	public void setPhoneWH(int width,int height)
	{
		wh[0]	=	width;
		wh[1]	=	height;
	}
	 
	public void setSlide(int maxSpeed,int maxDistance)
	{
		this.MAXSPEED	=	maxSpeed;
		this.MAXDISTANCE	=	maxDistance;
	}
	
	@Override
	public int[] getTVXY() {

		return null;
	}

	@Override
	public void arrange() 
	{
		
	}
	
	public void putSlideButton(CHideButton b)
	{
		btn.add(b);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		if(isTouchDownAction(event))
		{
			downTime	=	System.currentTimeMillis();
		}
		else if(isTouchUpAction(event))
		{
			upTime	=	System.currentTimeMillis();
			if(!isLongTouch)
				checkRoute();
			else
				doLongTouchUp();
			route.clear();
		}
		if(!isLongTouch)
			doTouchMoveAction(event);
 		
 		return false;
	}
	
	private boolean isTouchDownAction(MotionEvent event) {
		if (isTouched)
			return false;

		int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN
				|| (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
			int index = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

			if (isInBorder((int) event.getX(index),(int) event.getY(index))) {
				isTouched = true;
				touchID	=	event.getPointerId(index);
				int[] xy	=	new int[]{(int)event.getX(index),(int)event.getY(index)};
				route.add(xy);
				isLongTouch	=	false;
				firstTouchXY[0]	=	event.getX();
				firstTouchXY[1]	=	event.getY();
				handler.postDelayed(checkLongTouch, 300);
 				return true;
			}
		}

 		return false;
	}
	
	private boolean isTouchUpAction(MotionEvent event) {
		if (!isTouched)
			return false;

		int action = event.getAction();

		if (action == MotionEvent.ACTION_UP
				|| (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP) {
			int index = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			if(event.findPointerIndex(touchID) != index)
				return false;
			
			if (isInBorder((int) event.getX(index),(int) event.getY(index))) {
				isTouched = false;
				handler.removeCallbacks(checkLongTouch);
  				return true;
			}
		}

 		return false;
	}
	
	private boolean doTouchMoveAction(MotionEvent event) {
		if (!isTouched)
			return false;

		int action = event.getAction();

		if (action == MotionEvent.ACTION_MOVE) {
			
			int index	=	event.findPointerIndex(touchID);
			if(index == -1)
				return false;
			int[] xy	=	new int[]{(int)event.getX(index),(int)event.getY(index)};
			if(route.size() == 0)
				route.add(xy);
			else
			{
				int[] txy	=	route.get(route.size() - 1);
				if(xy[0] != txy[0] || xy[1] != txy[1])
				{
					route.add(xy);
				}
			}
			
			touchXY[0]	=	event.getX(index);
			touchXY[1]	=	event.getY(index);
		}

 		return false;
	}
	
	private void checkRoute()
	{
 		if(route.size() > 5)
		{
			int[] xy1	=	route.get(0);
			int[] xy2	=	route.get(route.size() - 1);
			int speedX	=	(int)((xy2[0] - xy1[0] + 0f) * 1000 / (upTime - downTime));
			int speedY	=	(int)((xy2[1] - xy1[1] + 0f) * 1000 / (upTime - downTime));
			
 			
			int slide	=	checkDirect(xy1,xy2,speedX,speedY);
			if(slide != SLIDE_NULL)
			{
				doSlide(slide);
			}
		}
		else if(route.size() > 0 && route.size() <= 5 && upTime - downTime <= 500)
		{
			int[] xy1	=	route.get(0);
			int[] xy2	=	route.get(route.size() - 1);
			int distanceX	=	Math.abs(xy2[0] - xy1[0]);
			int distanceY	=	Math.abs(xy2[1] - xy1[1]);
			int distance	=	(int)Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
			if(distance <= 20)
			{
				doClick();
			}
		}
 	}
	
	private void doClick()
	{
		for(int i=0;i<btn.size();i++)
		{
			CHideButton sdb	=	btn.get(i);
			if(sdb.getAction() == ACTION_CLICK)
			{
				doClick(sdb);
			}
		}
 	}
	
	private void doLongTouchDown()
	{
		for(int i=0;i<btn.size();i++)
		{
			CHideButton csb	=	btn.get(i);
			if(csb.getAction() == ACTION_LONG_TOUCH)
			{
				csb.setSimulateTouchAction(MotionEvent.ACTION_DOWN);
				csb.setSimulateHold(false);
				cv.checkSimulateScreenTouch();
 			}
		}
	}
	
	private void doLongTouchUp()
	{
		for(int i=0;i<btn.size();i++)
		{
			CHideButton csb	=	btn.get(i);
			if(csb.getAction() == ACTION_LONG_TOUCH)
			{
				csb.setSimulateTouchAction(MotionEvent.ACTION_UP);
				csb.setSimulateHold(false);
				cv.checkSimulateScreenTouch();
 			}
		}
	}
	
	private void doSlide(int direct)
	{
		for(int i=0;i<btn.size();i++)
		{
			CHideButton sdb	=	btn.get(i);
			if(sdb.getAction() == ACTION_SLIDE)
			{
				if(sdb.getDirect() == direct)
				{
					doClick(sdb);
					break;
				}
			}
		}
 	}
	
	private boolean isInBorder(int ex,int ey)
	{
		return ex >= phoneXY[0] && ex <= phoneXY[0] + wh[0]
				&& ey >= phoneXY[1] && ey <= phoneXY[1] + wh[1];
	}
	
	private int checkDirect(int[] point1, int[] point2, float velocityX,
			float velocityY)
	{
		double speed	=	Math.sqrt(Math.abs(velocityX * velocityX) + Math.abs(velocityY * velocityY));
  		if(speed < MAXSPEED)
		{
			return SLIDE_NULL;
		}
		int gesture	=	SLIDE_NULL;
		float x1	=	point1[0];
		float x2	=	point2[0];
		float y1	=	point1[1];
		float y2	=	point2[1];
		
		float distanceX	=	Math.abs(x2 - x1);
		float distanceY	=	Math.abs(y2 - y1);
		double distance	=	Math.sqrt(distanceX * distanceX + distanceY * distanceY);
 		if(distance < MAXDISTANCE)
			return SLIDE_NULL;
		
		if(Math.abs(x2 - x1) >= Math.abs(y2 - y1))
		{
			if(x2 - x1 < 0)
			{
				gesture	=	SLIDE_LEFT;
			}
			else if(x2 - x1 > 0)
			{
				gesture	=	SLIDE_RIGHT; 
			}
		}
		else
		{
			if(y2 - y1 < 0)
			{
				gesture	=	SLIDE_UP;
			}
			else if(y2 - y1 > 0)
			{
				gesture	=	SLIDE_DOWN;
			}
		}
		 
		return gesture;
	}
	
	private void doClick(final CHideButton csb)
	{
		if(clickDownRunable != null && clickUpRunable != null)
		{
			return;
		}
		clickDownRunable	=	new Runnable()
		{
			public void run()
			{
				csb.setSimulateTouchAction(MotionEvent.ACTION_DOWN);
				csb.setSimulateHold(false);
				cv.checkSimulateScreenTouch();
				clickDownRunable	=	null;
			}
		}; 
		
		clickUpRunable	=	new Runnable()
		{
			public void run()
			{
				csb.setSimulateTouchAction(MotionEvent.ACTION_UP);
				csb.setSimulateHold(false);
				cv.checkSimulateScreenTouch();
				clickUpRunable	=	null;
			}
		}; 
		
		handler.post(clickDownRunable);
		
		handler.postDelayed(clickUpRunable, 100);
	}
	
 
	
	
}
