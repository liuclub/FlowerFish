package com.bagelplay.controller.domotion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.Log;


public class SensorListener implements SensorEventListener{

	private static SensorListener sl;
		
	private SensorManager mSensorMgr;
	
	private Sensor sensor;
	
	private static int ratio;
	
	private static boolean isOverturn;
	
	private BagelPlayVmaStub bfVmaStub;
	
	private boolean isOpened;
	
	private SensorListener(Context context)
	{
		bfVmaStub = BagelPlayVmaStub.getInstance();
		
		mSensorMgr = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		sensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	public static void init(Context context)
	{
		if(sl == null)
			sl	=	new SensorListener(context);
	}
	
	public static SensorListener getInstance()
	{
		return sl;
	}
	
	public void open()
	{
		if(isOpened)
			return;
		mSensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
		bfVmaStub.sendSensorSwitchData(1);
		isOpened	=	true;
		Log.v("=-------------------------------------=", "sensor open");
	}
	
	public void close()
	{
		if(!isOpened)
			return;
		mSensorMgr.unregisterListener(this);
		bfVmaStub.sendSensorSwitchData(0);
		isOpened	=	false;
		Log.v("=-------------------------------------=", "sensor close");
	}
	
	public boolean isOpened()
	{
		return isOpened;
	}
	
	public static void setRatio(int ratio)
	{
		SensorListener.ratio	=	ratio;
	}
	
	public static void setOverturn(boolean isOverturn)
	{
		SensorListener.isOverturn	=	isOverturn;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		
		if(!isOpened)
			return;
		float x, y, z;
		if(!isOverturn)
		{
			x = event.values[SensorManager.DATA_X];
			y = event.values[SensorManager.DATA_Y];
			z = event.values[SensorManager.DATA_Z];
			
 		}
		else
		{
			x = -event.values[SensorManager.DATA_Y];
			y = event.values[SensorManager.DATA_X];
			z = event.values[SensorManager.DATA_Z];
		}
		
		float ratio	=	0f;
		if(SensorListener.ratio > 5){
			ratio = SensorListener.ratio + 0f - 5;
		}else{
			ratio = (SensorListener.ratio + 0f) / 5;
		}
		
		
		x = ratio * ratio * x;
		y = ratio * ratio * y;
		z = ratio * ratio * z;
		
		bfVmaStub.sendSensorData(x, y, z);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
 		
	}
	
	
}
