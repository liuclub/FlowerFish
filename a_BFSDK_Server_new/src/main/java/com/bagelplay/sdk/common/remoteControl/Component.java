package com.bagelplay.sdk.common.remoteControl;

import android.view.KeyEvent;

public class Component {

	public static final int ACTION_DOWN	=	KeyEvent.ACTION_DOWN;
	
	public static final int ACTION_UP	=	KeyEvent.ACTION_UP;
	
	public static final int ACTION_NULL	=	-1;
	
	private int keyCode;
	
	private int action;
	
	public Component(int keyCode,int action)
	{
		this.keyCode	=	keyCode;
		this.action	=	action;
	}
	
	public void setAction(int action)
	{
		this.action	=	action;
	}
	
	public int getAction()
	{
		return action;
	}
	
	public int getkeyCode()
	{
		return keyCode;
	}
}
