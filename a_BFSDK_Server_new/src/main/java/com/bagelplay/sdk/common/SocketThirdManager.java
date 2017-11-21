package com.bagelplay.sdk.common;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteOrder;

import com.bagelplay.sdk.cocos.XSGRControl;
import com.bagelplay.sdk.common.util.Log;
import com.bagelplay.sdk.common.util.SocketReader;


  
public class SocketThirdManager {
				
	private String TAG	=	this.getClass().getSimpleName();
	
	private SDKManager bfsdkm;
	
	private DatagramSocket udpDS;
		
	public SocketThirdManager(SDKManager bfsdkm)
	{
		this.bfsdkm	=	bfsdkm;
 		try {
			udpDS			=	new DatagramSocket(0);
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
	
	public int getPort()
	{
		return udpDS == null ? -1 : udpDS.getLocalPort();
	}
	
	public void start()
	{
		BMThread b	=	new BMThread();
		b.start();
		Log.v(TAG,"start ");
	}
	
	public void close()
	{
		try {
			udpDS.close();
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
	
	private void handle()
	{
 		while(true)
		{
			try
			{			 
				byte[] recvBuf = new byte[100];
		        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
		        udpDS.receive(recvPacket);
		        SocketReader sr	=	new SocketReader(ByteOrder.LITTLE_ENDIAN);
		        sr.setData(recvBuf);
		        int cmd	=	sr.readInt();
		        
		        Log.v(TAG, "handle " + " cmd:" + cmd);
 		        
		        switch(cmd)
		        {
		        	case CmdProtocol.UnityToSDK.CHANGE_DOMOTION_VIEW:
		        		change_domotion_view(sr);
		        	break;
		        	
		        	case CmdProtocol.UnityToSDK.UDP_PORT:
		        		udp_port(sr);
		        	break;
		        	
		        	case CmdProtocol.UnityToSDK.XSG_CONTROL_STICK:
		        		xsg_stick();
		        	break;
		        	
		        	case CmdProtocol.UnityToSDK.CHANGE_MOUSE_XY:
		        		change_mouse_xy(sr);
		        	break;
		        	
		        	case CmdProtocol.UnityToSDK.LOCATE_REMOTECONTROL_JSON:
		        		locate_remoteControl_json(sr);
		        	break;
		        }
			}catch(Exception e){
				e.printStackTrace();
				return;
			}

 		}
	}
	
	private void change_domotion_view(SocketReader sr)
	{
 		 String zip	=	sr.readStr();
 		 bfsdkm.setControlZip(zip); 
 		 Log.v(TAG, "change_domotion_view " + " zip:" + zip);
	}
	
	private void udp_port(SocketReader sr)
	{
 		int port	=	sr.readInt();
 		bfsdkm.getSocketThirdStub().connect(port);
 		Log.v(TAG, "udp_port " + " port:" + port);
	}
	
	private void change_mouse_xy(SocketReader sr)
	{
		int screenWidth	=	sr.readInt();
		int screenHeight	=	sr.readInt();
		int x	=	sr.readInt();
		int y	=	sr.readInt();
		x		=	(int)((bfsdkm.getScreenWidth() + 0f) / screenWidth * x);
		y		=	(int)((bfsdkm.getScreenHeight() + 0f) / screenHeight * y);
		bfsdkm.getMouse().setMouseXY(-1,x, y);
		
	}
	
	private void locate_remoteControl_json(SocketReader sr)
	{
		String json	=	sr.readStr();
		bfsdkm.setLocateRemoteControlJson(json);
	}
	
	
	private void xsg_stick()
	{
		((XSGRControl)bfsdkm.getRControl()).stick();
	}

	class BMThread extends Thread
	{
		@Override
		public void run()
		{
			handle();
		}
	}
	
	
}
