package com.bagelplay.sdk.cocos;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.Sensor;

public class CocosSensor extends Sensor{

	private SensorEventListener sel;
	
	private SensorEvent sEvent;
	
	public CocosSensor(SDKManager bfusm) {
		super(bfusm);
 	}
	
	public void setSensorEventListener(SensorEventListener sel)
	{
		this.sel	=	sel;
 	}
	
	public void onSensor(float x,float y,float z)
	{
		if(sel != null)
		{
			
		}
	}
	
	
}
