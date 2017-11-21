package com.bagelplay.sdk.common;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteOrder;

import android.os.Build;

import com.bagelplay.sdk.common.player.PhonePlayer;
import com.bagelplay.sdk.common.util.FileDir;
import com.bagelplay.sdk.common.util.Log;
import com.bagelplay.sdk.common.util.SocketWriter;
import com.bagelplay.sdk.common.util.Utils;

public class SocketStub {
		
	private String TAG	=	this.getClass().getSimpleName();
				
	private Socket mSocket;
	
	private InputStream is;
	
	private OutputStream os;
			
	private SDKManager bfsdkm;
	
	public SocketStub(SDKManager bfsdkm)
	{
		this.bfsdkm		=	bfsdkm;
	}

	
	public boolean connect(int port)
	{
		try
		{
			InetSocketAddress isa	=	new InetSocketAddress("127.0.0.1",port);
			
			mSocket = new Socket();
			
			mSocket.connect(isa, 5000);
			
			is		=	mSocket.getInputStream();
			
			os		=	mSocket.getOutputStream();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public int getLocalPort()
	{
		if(mSocket != null)
			return mSocket.getLocalPort();
		return -1;
	}
	 
	void close()
	{
		
		try {
			is.close();
		} catch (Exception e) {
 			e.printStackTrace();
		}
		
		try {
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			mSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.v(TAG, "close ");
	}
	
	InputStream getInputStream()
	{
		return is;
	}
	
	void sendHeartData(int isRunInForeground)
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.SDKToServer.HEART);
        sw.writeInt(isRunInForeground);
        sendData(sw.getBytes());
        Log.v(TAG, "sendHeartData " + "isRunInForeground:" + isRunInForeground);
	}
	
	void sendLoginData(String packageName,int thirdManagerPort,String id)
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.LOGIN);
		sw.writeBytes(packageName.getBytes());
		sw.writeInt(thirdManagerPort);
		sw.writeStr(id);
		sendData(sw.getBytes());
		Log.v(TAG, "sendLoginData " + "packageName:" + packageName + " thirdManagerPort:" + thirdManagerPort + " id:" + id);
	}
	
	void sendWantPlayerIdData()
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.WANTPLAYERID);
		sendData(sw.getBytes());
		Log.v(TAG, "sendWantPlayerIdData");
	}
	
	public void sendWantPlayerIdData2()
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.WANTPLAYERID2);
		sendData(sw.getBytes());
		Log.v(TAG, "sendWantPlayerIdData2");
	}
	
	void sendShowInputData()
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.SHOWINPUT);
		sendData(sw.getBytes());
		Log.v(TAG, "sendShowInputData");
	}
	
	public void sendPaymentData(String version,String platform,String token,String paramJson)
	{
		String packageName	=	bfsdkm.getApplicationContext().getPackageName();
		String macAddr		=	Utils.getMacAddressStr(bfsdkm.getApplicationContext());
		String brand		=	Build.BRAND + " " + Build.MODEL;
		
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.PAYMENT_REQUEST);
		
		SocketWriter sw2	=	new SocketWriter(ByteOrder.BIG_ENDIAN);
		sw2.writeStr(version);
		sw2.writeStr(platform);
		sw2.writeStr(token);
		sw2.writeStr(packageName);
		sw2.writeStr(macAddr);
		sw2.writeStr(brand);
		sw2.writeStr(paramJson);
		
		byte[] sw2Data	=	sw2.getBytes();
		sw.writeBytes(sw2Data);
		sendData(sw.getBytes());
		
		Log.v(TAG, "sendPaymentData " + "version:" + version + " platform:" + platform + " token:" + token + " packageName:" + packageName + " macAddr:" + macAddr + " brand:" + brand + " paramJson:" + paramJson);
	}
	
	public void sendAppInfoData(String zipName)
	{
		try
		{
 			InputStream is	=	bfsdkm.getApplicationContext().getAssets().open(FileDir.getInstance().ASSETS_DIR + zipName);
			byte[] data		=	Utils.inStreamToBytes(is);	
			is.close();
			
 			
			SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
			sw.writeInt(CmdProtocol.SDKToServer.GAMEINFO);
			
			SocketWriter sw2	=	new SocketWriter(ByteOrder.BIG_ENDIAN);
			sw2.writeStr(zipName);
	  		sw2.writeBytes(data);
	  		
	  		sw.writeBytes(sw2.getBytes());
			
	  		byte[] data2	=	sw.getBytes();
	  		
 	  		
	  		sendData(data2);
	  		
	  		Log.v(TAG, "sendAppInfoData " + "zipName:" + zipName);
	  		
	  		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void sendAppInfoDataZipName(String zipName)
	{
		try
		{
			SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
			sw.writeInt(CmdProtocol.SDKToServer.GAMEINFO_ZIPNAME);
			
			SocketWriter sw2	=	new SocketWriter(ByteOrder.BIG_ENDIAN);
	  		sw2.writeStr(zipName);
	  		
	  		sw.writeBytes(sw2.getBytes());
			
	  		byte[] data2	=	sw.getBytes();
	  		
 	  		
	  		sendData(data2);
	  		
	  		Log.v(TAG, "sendAppInfoDataZipName " + "zipName:" + zipName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void sendVoiceStart(int frequency,int channelConfiguration,int audioEncoding)
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.VOICE_START);
		
		SocketWriter sw2	=	new SocketWriter(ByteOrder.BIG_ENDIAN);
   		sw2.writeInt(frequency);
		sw2.writeInt(channelConfiguration);
		sw2.writeInt(audioEncoding);
		byte[] sw2Data	=	sw2.getBytes();
		
		sw.writeBytes(sw2Data);
		sendData(sw.getBytes());
		Log.v(TAG, "sendVoiceStart");
	}
	
	public void sendVoiceStop()
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.VOICE_STOP);
		sendData(sw.getBytes());
		Log.v(TAG, "sendVoiceStop");
	}
	
	public void sendVoiceCheck(int id,String right)
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.VOICE_CHECK);
		
		SocketWriter sw2	=	new SocketWriter(ByteOrder.BIG_ENDIAN);
   		sw2.writeInt(id);
		sw2.writeStr(right);
 		byte[] sw2Data	=	sw2.getBytes();
		
 		sw.writeBytes(sw2Data);
		sendData(sw.getBytes());
		Log.v(TAG, "sendVoiceCheck");
	}
	
	public void sendVoiceStartByPlayerID(int playerID,int frequency,int channelConfiguration,int audioEncoding)
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.VOICE_START_PLAYERID);
		sw.writeInt(playerID - PhonePlayer.PLAYORDER_PROFIX);
		
		SocketWriter sw2	=	new SocketWriter(ByteOrder.BIG_ENDIAN);
   		sw2.writeInt(frequency);
		sw2.writeInt(channelConfiguration);
		sw2.writeInt(audioEncoding);
		byte[] sw2Data	=	sw2.getBytes();
		
		sw.writeBytes(sw2Data);
		sendData(sw.getBytes());
		Log.v(TAG, "sendVoiceStartByPlayerID");
	}
	
	public void sendVoiceStopByPlayerID(int playerID)
	{
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.SDKToServer.VOICE_STOP_PLAYERID);
		sw.writeInt(playerID - PhonePlayer.PLAYORDER_PROFIX);
		sendData(sw.getBytes());
		Log.v(TAG, "sendVoiceStopByPlayerID");
	}
	
	synchronized void sendData(byte[] data)
	{		
		Log.v(TAG, "sendData" + data.length);
		SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeBytes(data);
		byte[] datas	=	sw.getBytes();
		
		try
		{
			os.write(datas);
			os.flush();
		}catch(Exception e){
			e.printStackTrace();
		}  			
	}
}
