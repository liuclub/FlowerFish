package com.bagelplay.sdk.common.ghandle;

public class Component {
	
	public static final int COMPONENT_KEY			=	1;
		
	public static final int COMPONENT_LEFT_STICK	=	2;	
	
	public static final int COMPONENT_RIGHT_STICK	=	3;
	
	public static final int STATE_DOWN		=	0;
	
	public static final int STATE_UP		=	1;
	
	public static final int STATE_MOVE		=	2;
	
	private int keyCode;
	
	private float axisX;
	
	private float axisY;
	
	//private int lastAxisX;
	
	//private int lastAxisY;
	
	private int component;
	
	private int state	=	-1;
	
	private int touchId;
	
	private boolean hold;
		
	public Component()
	{
		
	}
	
	public void createComponentKey(int keyCode,int state)
	{
		this.component	=	COMPONENT_KEY;
		this.keyCode	=	keyCode;
		this.state		=	state;
	}
	
	public void createComponentStick(int component,float axisX,float axisY)
	{
		this.component	=	component;
		this.axisX		=	axisX;
		this.axisY		=	axisY;
	}
	
	public void setState(int state)
	{
		this.state	=	state;
	}
	
	public void setAxisXY(float x,float y)
	{
		axisX	=	x;
		axisY	=	y;
	}
	
	/*public void setLastAxisXY(int x,int y)
	{
		lastAxisX	=	x;
		lastAxisY	=	y;
	}*/
	
	public float[] getAxisXY()
	{
		float[] xy	=	new float[2];
		xy[0]		=	axisX;
		xy[1]		=	axisY;
		return xy;
	}
	
	/*public int[] getLastAxisXY()
	{
		int[] xy	=	new int[2];
		xy[0]		=	lastAxisX;
		xy[1]		=	lastAxisY;
		return xy;
	}*/
	
	public int getComponent()
	{
		return component;
	}
	
	public int getState()
	{
		return state;
	}
	
	public int getKeyCode()
	{
		return keyCode;
	}
	
	public void setTouchId(int touchId)
	{
		this.touchId	=	touchId;
	}
	
	public int getTouchId()
	{
		return touchId;
	}
	
	public void setHold(boolean hold)
	{
		this.hold		=	hold;
	}
	
	public boolean isHold()
	{
		return hold;
	}
	
	
	
	
}
