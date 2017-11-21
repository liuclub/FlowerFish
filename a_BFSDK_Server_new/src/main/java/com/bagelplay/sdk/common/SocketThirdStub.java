package com.bagelplay.sdk.common;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteOrder;

import com.bagelplay.sdk.common.util.Log;
import com.bagelplay.sdk.common.util.SocketWriter;
import com.bagelplay.sdk.gun.GunEvent;

public class SocketThirdStub {
		
	private String TAG	=	this.getClass().getSimpleName();
	
	private int port;
					
	private DatagramSocket udpDS;
	
	private InetAddress addr;
	
	private SDKManager bfsdkm;
	
	public SocketThirdStub(SDKManager bfsdkm)
	{
		this.bfsdkm	=	bfsdkm;
	}
	
	public boolean connect(int port)
	{
		try {
			addr		=	InetAddress.getByName("127.0.0.1");
			udpDS		=	new DatagramSocket();
 		} catch (Exception e) {
 			e.printStackTrace();
		}
		this.port	=	port;
		
		Log.v(TAG, "connect " + "port:" + port);
		
		return true;
	}
	 
	public DatagramSocket getDatagramSocket()
	{
		return udpDS;
	}
	
	void close()
	{
		try {
			udpDS.close();
 		} catch (Exception e) {
 			e.printStackTrace();
		}
		Log.v(TAG, "close ");
  	}
	 
	public void sendSensorData(float x,float y,float z,int playerID)
	{
		if(port <= 0)
			return;
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToUnity.SENSOR);
		sw.writeInt(playerID);
		sw.writeFloat(x);
		sw.writeFloat(y);
		sw.writeFloat(z);
		sendUdp(sw.getBytes());
		Log.v(TAG, "sendSensorData " + " playerID:" + playerID + " x:" + x + " y:" + y + " z:" + z);
	}
	
	public void sendLoginData(int state)
	{
		if(port <= 0)
			return;
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToUnity.LOGIN);
		sw.writeInt(state);
 		sendUdp(sw.getBytes());
 		Log.v(TAG, "sendLoginData " + " state:" + state);
	}
	
	
	public void sendGameHandlerHandlerStickData(int id,float x,float y) {
		if(port <= 0)
			return;
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToUnity.GAMEHANDLER_HANDLER_STICK);
		sw.writeInt(id);
		sw.writeFloat(x);
		sw.writeFloat(y);
		sendUdp(sw.getBytes());
		Log.v(TAG, "sendGameHandlerHandlerStickData " + id + " x:" + x + " y:" + y);
	}
	
	public void sendGameHandlerHandlerButtonData(int value,int event) {
		if(port <= 0)
			return;
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToUnity.GAMEHANDLER_HANDLER_BUTTON);
		sw.writeInt(value);
		sw.writeInt(event);
		sendUdp(sw.getBytes());
		Log.v(TAG, "sendGameHandlerHandlerButtonData " + "value:" + value + " event:" + event);
	}
	
	public void sendWantPlayerIDData(int[] playerIDs, int num)
	{
		if(port <= 0)
			return;
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToUnity.WANTPLAYERID);
		sw.writeInt(num);
		for(int i=0;i<num;i++)
		{
			sw.writeInt(playerIDs[i]);
		}
 		sendUdp(sw.getBytes());
 		Log.v(TAG, "sendWantPlayerIDData " + "num:" + num);
	}
	
	public void sendPhysicsHandlerHandlerStickData(int playerOrder,int id,float x,float y) {
		if(port <= 0)
			return;
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToUnity.PHYSICSHANDLER_HANDLER_STICK);
		sw.writeInt(playerOrder);
		sw.writeInt(id);
		sw.writeFloat(x);
		sw.writeFloat(y);
		sendUdp(sw.getBytes());
		Log.v(TAG, "sendPhysicsHandlerHandlerStickData " + "playerOrder:" + playerOrder + " id:" + id + " x:" + x + " y:" + y);
	}
	
	public void sendPhysicsHandlerHandlerButtonData(int playerOrder,int value,int event) {
		if(port <= 0)
			return;
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToUnity.PHYSICSHANDLER_HANDLER_BUTTON);
		sw.writeInt(playerOrder);
		sw.writeInt(value);
		sw.writeInt(event);
		sendUdp(sw.getBytes());
		Log.v(TAG, "sendPhysicsHandlerHandlerButtonData " + "playerOrder:" + playerOrder + " value:" + value + " event:" + event);
	}
	
	public void sendGunEventData(GunEvent gunEvent) {
		if(port <= 0)
			return;
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKTOCP.GUNEVENT);
		sw.writeDouble(gunEvent.pointX);
		sw.writeDouble(gunEvent.pointY);
		sw.writeDouble(gunEvent.positiontX);
		sw.writeDouble(gunEvent.positiontY);
		sw.writeDouble(gunEvent.positiontZ);
		sw.writeDouble(gunEvent.positiontAngle);
		sw.writeInt(gunEvent.dataFlag);
		sw.writeInt(gunEvent.key);
		for(int i=0;i<16;i++)
		{
			sw.writeInt(gunEvent.events[i]);
		}
		sw.writeInt(gunEvent.gunPos);
		sendUdp(sw.getBytes());
		Log.v(TAG, "sendGunEventData " + gunEvent.toString());
	}
	
	public void sendRemoteControlData(int playerOrder,int value,int event) {
		if(port <= 0)
			return;
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToUnity.REMOTECONTROL);
		sw.writeInt(playerOrder);
		sw.writeInt(value);
		sw.writeInt(event);
		sendUdp(sw.getBytes());
		Log.v(TAG, "sendRemoteControlData " + " playerOrder:" + playerOrder + " value:" + value + " event:" + event);
	}
	
	private synchronized void sendUdp(byte[] datas)
	{
		DatagramPacket dp = new DatagramPacket(datas, datas.length, addr, port);
		try
		{
			udpDS.send(dp);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	 
	
}
