package com.bagelplay.sdk.common;

public class Sensor {
	
	private SDKManager bfsdkm;
	
	private OnSensorListener osl;
	
	public Sensor(SDKManager bfsdkm)
	{
		this.bfsdkm	=	bfsdkm;
	}
	
	public void setOnSensorListener(OnSensorListener osl)
	{
		this.osl	=	osl;
	}
	
	public void doSensor(float x,float y,float z,int playerID)
	{
		if(osl != null)
			osl.OnSensor(x, y, z,playerID);
		else
			bfsdkm.getSocketThirdStub().sendSensorData(x, y, z,playerID);
	}
}
