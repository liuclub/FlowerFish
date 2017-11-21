package com.bagelplay.sdk.common.ghandle;

import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.util.Log;


public class GHandle{
	
	private static String TAG	=	"GHandle";
	
	public static final int MSG_COMPONENT_OPERATION		=	1;
	
	public static final int MSG_COMPONENT_MOUSEMOVE		=	2;
	
	public static final int MSG_COMPONENT_DELAYMOVE		=	3;
	
	private Component[] components		=	new Component[5];
	
	private Component[] holdComponents	=	new Component[5];
	
	private int holdLen					=	0;
	
	private int[][] keyCustomized		=	new int[KeyConst.PHYSICSKEY.length][]; 
	
	private int[][] keyCustomizedSlide	=	new int[KeyConst.PHYSICSKEY.length][]; 
	
	private int[] stick_left_center_Customized	=	new int[2];
	
	private int stick_left_radius_Customized;
	
	private int[] stick_right_center_Customized	=	new int[2];
	
	private int stick_right_radius_Customized;
		
	private SDKManager bfusm;
	
	private int[] devScreenWH	=	new int[2];
	
	private float[] screenRate	=	new float[2];
	
	private boolean isSimulateTouch;
	
	private boolean isRightStickAsMouse;
	
	private boolean isAlwaysRightStickAsMouse;
	
	private boolean isLeftStickAsSensor;
			
	private Handler handler	=	new Handler(){
		@Override
		public void handleMessage(Message msg) {   
			if(msg.what == MSG_COMPONENT_OPERATION)
			{
				doComponent2Touch();
				handler.sendEmptyMessage(MSG_COMPONENT_OPERATION);
			}
			else if(msg.what == MSG_COMPONENT_MOUSEMOVE)
			{
				doMouseMove();
			}
			else if(msg.what == MSG_COMPONENT_DELAYMOVE)
			{
				int x	=	msg.arg1;
				int y	=	msg.arg2;
				stickMove(Component.COMPONENT_LEFT_STICK,x,y);
	 			doComponent2Touch();
			}
		}
	};
	
	
	
	public GHandle(SDKManager bfusm)
	{
		this.bfusm	=	bfusm;
				
		JsonParser jp	=	new JsonParser(this,bfusm);
		jp.parse();	 
	}
	
	public void addKeyScreenCustomized(int keyCode,int x,int y,int absoulte)
	{
		keyCustomized[keyCode]		=	new int[3];
		keyCustomized[keyCode][0]	=	x;
		keyCustomized[keyCode][1]	=	y;
		keyCustomized[keyCode][2]	=	absoulte;
	}
	
	public void setStickLeftCustomized(int radius,int x,int y)
	{
		stick_left_radius_Customized	=	radius;
		stick_left_center_Customized[0]	=	x;
		stick_left_center_Customized[1]	=	y;
	}
	
	public void setStickRightCustomized(int radius,int x,int y)
	{
		stick_right_radius_Customized		=	radius;
		stick_right_center_Customized[0]	=	x;
		stick_right_center_Customized[1]	=	y;
	}
	
	public void addKeySlideCustomized(int keyCode,int x,int y,int absoulte,int slide)
	{
		keyCustomizedSlide[keyCode]		=	new int[5];
		keyCustomizedSlide[keyCode][0]	=	x;
		keyCustomizedSlide[keyCode][1]	=	y;
		keyCustomizedSlide[keyCode][2]	=	absoulte;
		keyCustomizedSlide[keyCode][3]	=	slide;
		keyCustomizedSlide[keyCode][4]	=	5;
		
		
		Log.v("---------slide----------", "addKeySlideCustomized " + keyCustomizedSlide[keyCode][0] + "  " + keyCustomizedSlide[keyCode][1] + "  " + keyCustomizedSlide[keyCode][2] + "  " + keyCustomizedSlide[keyCode][3] + "  " + keyCustomizedSlide[keyCode][4]);
		
	}
	
	public void setDevScreenWH(int width,int height)
	{
		devScreenWH[0]	=	width;
		devScreenWH[1]	=	height;
	}
	
	public void resetByScreenRate()
	{
		screenRate[0]	=	(bfusm.getScreenWidth() + 0f) / devScreenWH[0]; 
		screenRate[1]	=	(bfusm.getScreenHeight() + 0f) / devScreenWH[1];
		Log.v(TAG, "resetByScreenRate " + "rate x:" + screenRate[0] + " rate y:" + screenRate[1]);
		for(int i=0;i<keyCustomized.length;i++)
		{
			if(keyCustomized[i] != null && keyCustomized[i][2] == 1)
			{
				keyCustomized[i][0]	=	(int)(keyCustomized[i][0] * screenRate[0]);
				keyCustomized[i][1]	=	(int)(keyCustomized[i][1] * screenRate[1]);
			}
			if(keyCustomizedSlide[i] != null && keyCustomizedSlide[i][2] == 1)
			{
				keyCustomizedSlide[i][0]	=	(int)(keyCustomizedSlide[i][0] * screenRate[0]);
				keyCustomizedSlide[i][1]	=	(int)(keyCustomizedSlide[i][1] * screenRate[1]);
				keyCustomizedSlide[i][4]	=	(int)(keyCustomizedSlide[i][4] * screenRate[1]);
			}
		}
		float rate	=	screenRate[0] < screenRate[1] ? screenRate[0] : screenRate[1];
		
		stick_left_center_Customized[0]	=	(int)(stick_left_center_Customized[0] * screenRate[0]);
		stick_left_center_Customized[1]	=	(int)(stick_left_center_Customized[1] * screenRate[1]);
		stick_left_radius_Customized	=	(int)(stick_left_radius_Customized * rate);
		
		stick_right_center_Customized[0]	=	(int)(stick_right_center_Customized[0] * screenRate[0]);
		stick_right_center_Customized[1]	=	(int)(stick_right_center_Customized[1] * screenRate[1]);
		stick_right_radius_Customized		=	(int)(stick_right_radius_Customized * rate);
		
	}
	
	public void destroy()
	{
		
	}
	
	public void simulateTouch()
	{
		isSimulateTouch	=	true;
	}
	
	public void setRightStickAsMouse(boolean isRightStickAsMouse)
	{
		this.isRightStickAsMouse	=	isRightStickAsMouse;
		if(!isRightStickAsMouse)
			handler.removeMessages(MSG_COMPONENT_MOUSEMOVE);
	}
	
	public void setAlwaysRightStickAsMouse(boolean isAlwaysRightStickAsMouse)
	{
		this.isAlwaysRightStickAsMouse	=	isAlwaysRightStickAsMouse; 
	}
	
	public void setLeftStickAsSensor(boolean isLeftStickAsSensor)
	{
		this.isLeftStickAsSensor	=	isLeftStickAsSensor;
	}
	
	private void keyDown(int keyCodePhysics)
	{
		int keyCode		=	KeyConst.translateKeyCodefromPhysicsHandle(keyCodePhysics);
 		if(keyCode == -1 || keyCustomized[keyCode] == null)
			return;
		int spot		=	getKeyFreeSpot(keyCode);
  		if(spot != -1)
		{
			Component c	=	new Component();
			c.createComponentKey(keyCode, Component.STATE_DOWN);
			c.setTouchId(spot);
			components[spot]=	c;
		}
	}
	
	private void keyUp(int keyCodePhysics)
	{
		int keyCode		=	KeyConst.translateKeyCodefromPhysicsHandle(keyCodePhysics);
		if(keyCode == -1 || keyCustomized[keyCode] == null)
			return;
		for(int i=0;i<components.length;i++)
		{
			if(components[i] != null && components[i].getComponent() == Component.COMPONENT_KEY && components[i].getKeyCode() == keyCode)
			{
				components[i].setHold(false);
				components[i].setState(Component.STATE_UP);
				return;
			}
		}
	}
	
	private boolean slideKeyDown(int keyCodePhysics)
	{
		int keyCode		=	KeyConst.translateKeyCodefromPhysicsHandle(keyCodePhysics);
		Log.v("---------slide----------", "slideKeyUp " + keyCode);
		if(keyCode == -1 || keyCustomizedSlide[keyCode] == null)
			return false;
		int speed	=	20;
		int speedX	=	0;
		int speedY	=	0;
		int distanceX	=	0;
		int distanceY	=	0;
		int delay		=	0;
		if(keyCustomizedSlide[keyCode][3] == 1)
		{
			speedY	=	(int)(-speed * screenRate[1]);
		}
		if(keyCustomizedSlide[keyCode][3] == 2)
		{
			speedY	=	(int)(speed * screenRate[1]);
		}
		if(keyCustomizedSlide[keyCode][3] == 3)
		{
			speedX	=	(int)(-speed * screenRate[0]);
		}
		if(keyCustomizedSlide[keyCode][3] == 4)
		{
			speedX	=	(int)(speed * screenRate[0]);
		}
		bfusm.getTouch().touch(MotionEvent.ACTION_DOWN, keyCustomizedSlide[keyCode][0], keyCustomizedSlide[keyCode][1]);
		for(int i=0;i<10;i++)
		{
			distanceX	=	i * speedX;
			distanceY	=	i * speedY;
			delay		=	i * 50;
			bfusm.getTouch().touch(MotionEvent.ACTION_MOVE, keyCustomizedSlide[keyCode][0] + distanceX, keyCustomizedSlide[keyCode][1] + distanceY,delay);
		}
		bfusm.getTouch().touch(MotionEvent.ACTION_UP, keyCustomizedSlide[keyCode][0] + distanceX, keyCustomizedSlide[keyCode][1] + distanceY,delay);
		return true;
	}
	
	private boolean slideKeyUp(int keyCodePhysics)
	{
		int keyCode		=	KeyConst.translateKeyCodefromPhysicsHandle(keyCodePhysics);
		Log.v("---------slide----------", "slideKeyDown" + keyCode);
		if(keyCode == -1 || keyCustomizedSlide[keyCode] == null)
			return false;
		return true;
	}
	
	private void stickMove(int leftOrRight,float x,float y)
	{
		for(int i=0;i<components.length;i++)
		{
			if(components[i] != null && components[i].getComponent() == leftOrRight)
			{
 				
 				components[i].setHold(false);
 				
 				if(x == 0 && y == 0)
 				{
 					components[i].setState(Component.STATE_UP);
 				}
 				else
 				{
 					components[i].setAxisXY(x, y);
 					components[i].setState(Component.STATE_MOVE);
 				}
				return;
			}
		}
		
		int spot	=	getStickFreeSpot(leftOrRight);
 		if(spot != -1)
		{
			Component c	=	new Component();
			c.createComponentStick(leftOrRight, x, y);
			c.setState(Component.STATE_DOWN);
			c.setTouchId(spot);
			components[spot]=	c;
		}
		
	}
	
	private void doComponent2Touch()
	{
		component2Touch();
		checkAndClearComponent();
	}
	
	
	private void component2Touch()
	{
		getHoldComponent();
		if(holdLen == 0)
			return;
		
		int mainActionToSend	=	Component.STATE_MOVE;
		if(holdLen == 1)
		{
			if(!holdComponents[0].isHold())
			{
				mainActionToSend	=	holdComponents[0].getState();
			}
			holdComponents[0].setHold(true);
			
			
 			int[] screenXY	=	getScreenFromComponent(holdComponents[0]);
  			
  			PointerCoords pc	=	new PointerCoords();
			pc.x		=	screenXY[0];
			pc.y		=	screenXY[1];
			PointerProperties pp	=	new PointerProperties();
			pp.id	=	holdComponents[0].getTouchId();
						
   			//bfusm.getTouch().doTouch(mainActionToSend, screenXY[0], screenXY[1]);
  			bfusm.getTouch().doTouch(holdLen, mainActionToSend, new PointerCoords[]{pc}, new PointerProperties[]{pp});
  			Log.v("=---------------------------------------==", "component2Touch1 " + " action:" + mainActionToSend);
  		
  			
			return;
		}
		
 		
		
		PointerCoords[] pcs	=	new PointerCoords[holdLen];
		PointerProperties[] pps	=	new PointerProperties[holdLen];
		for(int i=0;i<holdLen;i++)
		{
			if(!holdComponents[i].isHold() && mainActionToSend == Component.STATE_MOVE)
			{
 				if(holdComponents[i].getState() == Component.STATE_DOWN)
				{
					mainActionToSend	=	MotionEvent.ACTION_POINTER_DOWN | (holdComponents[i].getTouchId() << 8); 
				}
				else if(holdComponents[i].getState() == Component.STATE_UP)
				{
					int k	=	holdComponents[i].getTouchId();
					for(int j=k - 1;j>=0;j--)
					{
						if(holdComponents[j] == null)
							k--;
					}
					mainActionToSend	=	MotionEvent.ACTION_POINTER_UP | (k << 8);
				}
				
				
				
			}
			holdComponents[i].setHold(true);
			
			int[] screenXY	=	getScreenFromComponent(holdComponents[i]);
			pcs[i]		=	new PointerCoords();
 			pcs[i].x	=	screenXY[0];
			pcs[i].y	=	screenXY[1];
			pps[i]		=	new PointerProperties();
			pps[i].id	=	holdComponents[i].getTouchId();
		}
		 
		
		Log.v("=---------------------------------------==", "component2Touch2 " + " action:" + mainActionToSend);
		bfusm.getTouch().doTouch(holdLen, mainActionToSend, pcs, pps);
	}
	
	private void getHoldComponent()
	{
		holdLen	=	0;
  		for(int i=0;i<components.length;i++)
		{
  			holdComponents[i]	=	null;
			if(components[i] != null)
			{
				holdComponents[holdLen++]	=	components[i];
  			}
		}
	}
	
	private void checkAndClearComponent()
	{
		for(int i=0;i<components.length;i++)
		{
			if(components[i] != null)
			{
 				if(components[i].getState() == Component.STATE_UP)
				{
					components[i]	=	null;
				}
			}
		}
	}
	
	private int getKeyFreeSpot(int keyCode)
	{
		boolean isHave	=	false;
		int spot		=	-1;
		for(int i=0;i<components.length;i++)
		{
			if(spot == -1 && components[i] == null)
			{
				spot	=	i;
			}
			else if(components[i] != null && components[i].getComponent() == Component.COMPONENT_KEY && components[i].getKeyCode() == keyCode) 
			{
				isHave	=	true;
			}
 		}
		if(isHave)
			return -1;
		return spot;
	}
	
	private int getStickFreeSpot(int leftOrRight)
	{
		boolean isHave	=	false;
		int spot		=	-1;
		for(int i=0;i<components.length;i++)
		{
			if(spot == -1 && components[i] == null)
			{
				spot	=	i;
			}
			else if(components[i] != null && components[i].getComponent() == leftOrRight) 
			{
				isHave	=	true;
			}
 		}
		if(isHave)
			return -1;
		return spot;
	}
	
	private int[] keyCode2Screen(int keyCode)
	{
		Log.v(TAG, "keyCode2Screen " + "keyCode:" + keyCode + " x:" + keyCustomized[keyCode][0] + " y:" + keyCustomized[keyCode][1]);
		//bfusm.getMouse().debugForTouchShow(keyCustomized[keyCode][0], keyCustomized[keyCode][1]);
		return keyCustomized[keyCode];
	}
	
	private int[] stick2Screen(int component,float[] axis_xy)
	{
		if(component == Component.COMPONENT_LEFT_STICK)
		{
			double radiusRate	=	stick_left_radius_Customized + 0f;
 			double[] screen		=	new double[]{stick_left_center_Customized[0] + axis_xy[0] * radiusRate,stick_left_center_Customized[1] + axis_xy[1] * radiusRate};
 			return new int[]{(int)screen[0],(int)screen[1]};
		}
		else
		{
			double radiusRate	=	stick_right_radius_Customized + 0f;
			double[] screen		=	new double[]{stick_right_center_Customized[0] + axis_xy[0] * radiusRate,stick_right_center_Customized[1] + axis_xy[1] * radiusRate};
			return new int[]{(int)screen[0],(int)screen[1]};
		}
 	}
	
	private int[] getScreenFromComponent(Component c)
	{
		int[] screenXY	=	null;
		if(c.getComponent() == Component.COMPONENT_KEY)
		{
			screenXY	=	keyCode2Screen(c.getKeyCode());
		}
		else if(c.getComponent() == Component.COMPONENT_LEFT_STICK)
		{
			screenXY	=	stick2Screen(Component.COMPONENT_LEFT_STICK,c.getAxisXY());
		}
		else if(c.getComponent() == Component.COMPONENT_RIGHT_STICK)
		{
			screenXY	=	stick2Screen(Component.COMPONENT_RIGHT_STICK,c.getAxisXY());
		}
		return screenXY;
	}

	int mouseSpeedX;
	int mouseSpeedY;
	private void doMouseMove()
	{
		if(mouseSpeedX != 0 || mouseSpeedY != 0)
		{
			bfusm.getMouse().doMouseMove(mouseSpeedX, mouseSpeedY);
			handler.sendEmptyMessage(MSG_COMPONENT_MOUSEMOVE);
		}
		else
		{
			handler.removeMessages(MSG_COMPONENT_MOUSEMOVE);
 		}
 	}
	
 
	//@Override
	public synchronized void onControllerKeyDown(int playerOrder, int keyCode) 
	{
		Log.v(TAG, "onControllerKeyDown " + "playerOrder:" + playerOrder + " keyCode:" + keyCode);
		int bfKeyCode	=	KeyConst.translateKeyCodefromPhysicsHandle(keyCode);
		
		bfusm.getSocketThirdStub().sendPhysicsHandlerHandlerButtonData(playerOrder,bfKeyCode,KeyEvent.ACTION_DOWN);
		
		if(isSimulateTouch)
		{
			if(!slideKeyDown(keyCode))
			{
				keyDown(keyCode);
				doComponent2Touch();
			}
			
		}
	}

	//@Override
	public synchronized void onControllerKeyUp(int playerOrder, int keyCode) 
	{
		Log.v(TAG, "onControllerKeyUp " + "playerOrder:" + playerOrder + " keyCode:" + keyCode);
		int bfKeyCode	=	KeyConst.translateKeyCodefromPhysicsHandle(keyCode);
		
		bfusm.getSocketThirdStub().sendPhysicsHandlerHandlerButtonData(playerOrder,bfKeyCode,KeyEvent.ACTION_UP);
		
		if(isSimulateTouch)
		{
			if((isAlwaysRightStickAsMouse || isRightStickAsMouse) && KeyConst.KEY_RIGHT_STICK == bfKeyCode)
			{
				bfusm.getMouse().doMouseClick(playerOrder);
				return;
			}
			
			if(!slideKeyUp(keyCode))
			{
				keyUp(keyCode);
				doComponent2Touch();
			}
		}
		
	}
	
 	public synchronized void onLeftStickChanged(int playerOrder, float x, float y) 
 	{
 		Log.v(TAG, "onLeftStickChanged " + "playerOrder:" + playerOrder + " x:" + x + " y:" + y);
 		
 		bfusm.getSocketThirdStub().sendPhysicsHandlerHandlerStickData(playerOrder, 1, x, y);
 		
 		if(isSimulateTouch)
		{
 			if(isLeftStickAsSensor)
 			{
 				float sx	=	-x * 10;
 				float sy	=	-y * 10;
 				bfusm.getSocketThirdStub().sendSensorData(sx, sy, 0, playerOrder);
 			}
 			else if(stick_left_radius_Customized > 0)
 			{
 				x	=	com.bagelplay.sdk.common.util.Utils.formatFloat(x,2);
 	 			y	=	com.bagelplay.sdk.common.util.Utils.formatFloat(y,2);
 	 			stickMove(Component.COMPONENT_LEFT_STICK,x,y);
 	 			doComponent2Touch();
 			}
		}
	}

 	public synchronized void onRightStickChanged(int playerOrder, float x, float y) 
 	{
 		Log.v(TAG, "onRightStickChanged " + "playerOrder:" + playerOrder + " x:" + x + " y:" + y);
 		
 		bfusm.getSocketThirdStub().sendPhysicsHandlerHandlerStickData(playerOrder, 2, x, y);
 		
 		if(isSimulateTouch)
 		{
 			if(isAlwaysRightStickAsMouse || isRightStickAsMouse)
 			{
 				if(mouseSpeedX == 0 && mouseSpeedY == 0)
 				{
 					mouseSpeedX	=	(int)(x * 20);
 		 			mouseSpeedY	=	(int)(y * 20);
 					handler.sendEmptyMessage(MSG_COMPONENT_MOUSEMOVE);
 				}
 				else
 				{
 					mouseSpeedX	=	(int)(x * 20);
 		 			mouseSpeedY	=	(int)(y * 20);
 				}
 			}
 			else if(stick_right_radius_Customized > 0)
 			{
 				x	=	com.bagelplay.sdk.common.util.Utils.formatFloat(x,2);
 	 			y	=	com.bagelplay.sdk.common.util.Utils.formatFloat(y,2); 	 			
 	 			
 	 			stickMove(Component.COMPONENT_RIGHT_STICK,x,y);
 	 			doComponent2Touch();
 			}
 			
 		}
 		
	}
 	
 	
 	 
 	 
 
}
