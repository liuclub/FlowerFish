package com.bagelplay.sdk.common;

import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteOrder;

import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

import com.bagelplay.sdk.common.player.OnPlayerState;
import com.bagelplay.sdk.common.player.PhonePlayer;
import com.bagelplay.sdk.common.player.PlayerManager;
import com.bagelplay.sdk.common.util.Log;
import com.bagelplay.sdk.common.util.SocketReader;
import com.bagelplay.sdk.payment.Payment;

  
public class SocketManager {
	
	private String TAG	=	"SocketManager";
	
	private SocketReader socketReader;
							
	private DatagramSocket udpSocket;
		
	private SDKManager bfsdkm;
		
	SocketManager(SDKManager bfsdkm)
	{
		this.bfsdkm		=	bfsdkm;
		socketReader	=	new SocketReader(ByteOrder.LITTLE_ENDIAN);
	}
	
	
	
	
	void start()
	{
		BMTCPThread bt	=	new BMTCPThread();
		bt.start();
		Log.v(TAG,"start ");
	}
	
	void close()
	{
		try
		{
			udpSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void handleTCP()
	{
 		while(true)
		{
			try
			{		
				InputStream is	=	bfsdkm.getSocketStub().getInputStream();
				int len		=	socketReader.readInt(is);
				int cmd		=	socketReader.readInt(is);
				
				Log.v(TAG,"receive tcp " + "len:" + len + " cmd:" + cmd);
				if(len <= 0)
				{
					bfsdkm.getBase().stop();
					return;
				}
					
				if(cmd == CmdProtocol.ServerToSDK.LOGIN)
				{
					login(is);
				}
				else if(cmd == CmdProtocol.ServerToSDK.APPINFO)
				{
					appInfo();
				}
				else if(cmd == CmdProtocol.ServerToSDK.MOUSE)
				{
					int playerID	=	socketReader.readInt(is);
					playerID	+=	PhonePlayer.PLAYORDER_PROFIX;
					mouse(is,playerID);
 				}
				else if(cmd == CmdProtocol.ServerToSDK.TOUCH)
				{
					touch(is);
				}
				else if(cmd == CmdProtocol.ServerToSDK.KEY)
				{
					key(is);
				}
				else if(cmd == CmdProtocol.ServerToSDK.SENSOR)
				{
					int playerID	=	socketReader.readInt(is);
					playerID	+=	PhonePlayer.PLAYORDER_PROFIX;
					sensor(is,playerID);
 				}
				else if(cmd == CmdProtocol.ServerToSDK.INPUTTEXT)
				{
					inputText(is);
 				}
				else if(cmd == CmdProtocol.ServerToSDK.WANTPLAYERID)
				{
					wantPlayerID(is);
 				}
				else if(cmd == CmdProtocol.ServerToSDK.WANTPLAYERID2)
				{
					wantPlayerID2(is);
 				}
				else if(cmd == CmdProtocol.ServerToSDK.PLAYERCONNECT)
				{
					playerConnect(is);
				}
				else if(cmd == CmdProtocol.ServerToSDK.MOUSE_HOLD)
				{
					mouseHold(is);
 				}
				else if(cmd == CmdProtocol.ServerToSDK.GAMEHANDLER_HANDLER_STICK)
				{
					gameHandler_handler_stick(is);
 				}
				else if(cmd == CmdProtocol.ServerToSDK.GAMEHANDLER_HANDLER_BUTTON)
				{
					gameHandler_handler_button(is);
				}
				else if(cmd == CmdProtocol.ServerToSDK.PAYMENT_HEART)
				{
					payment_heart(is);
 				}
				else if(cmd == CmdProtocol.ServerToSDK.PAYMENT_RESULT)
				{
					payment_result(is);
  				}
				else if(cmd == CmdProtocol.ServerToSDK.VOICE_DATA)
				{
					int playerID	=	socketReader.readInt(is);
					playerID	+=	PhonePlayer.PLAYORDER_PROFIX;
					voice_data(is,playerID);
  				}
				else if(cmd == CmdProtocol.ServerToSDK.VOICE_CHECK_RESULT)
				{
					voice_checkResult(is);
				}
				else if(cmd == CmdProtocol.ServerToSDK.SERVER_CLOSE)
				{
					Log.v("SocketManager", "SERVER_CLOSE");
					android.os.Process.killProcess(android.os.Process.myPid());
				}
				else
				{
					is.skip(len - 4);
				}	
			}catch(Exception e){
				e.printStackTrace();
				if(udpSocket != null)
					udpSocket.close();
				return;
			}

 		}
	}
	
	private void handleUDP()
	{
		try {
			udpSocket = new DatagramSocket(bfsdkm.getSocketStub().getLocalPort());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		while(true)
		{
			byte[] data	=	new byte[512];
			DatagramPacket recvPacket = new DatagramPacket(data,data.length);
			try {
				udpSocket.receive(recvPacket);
			} catch (Exception e) {
 				e.printStackTrace();
 				return;
			}
		}
	}
	
	private void login(InputStream is) throws Exception
	{
		int result	=	socketReader.readInt(is);
		if(result == 0)
		{
			bfsdkm.getBase().startHeart();
		}
		else
		{
			bfsdkm.destroy();
		}
		Log.v(TAG,"login " + "result:" + result);
	}
	
	private void appInfo() throws Exception
	{
		String zip	=	bfsdkm.getControlZip();
		bfsdkm.getSocketStub().sendAppInfoData(zip);
		Log.v(TAG,"appInfo " + "zip:" + zip);
	}
	
	private void mouse(InputStream is,int playerID) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		int action	=	socketReader.readInt(is);
		int offsetX	=	socketReader.readInt(is);
		int offsetY	=	socketReader.readInt(is);	
		bfsdkm.getMouse().mouseMove(action, offsetX, offsetY,playerID);
		Log.v(TAG,"mouse " + "action:" + action + " offsetX:" + offsetX + " offsetY:" + offsetY);
	}
	
	private void touch(InputStream is) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		int pointCount	=	socketReader.readInt(is);					
		int action		=	socketReader.readInt(is);
		PointerCoords[] pcs	=	new PointerCoords[pointCount];
		PointerProperties[] pps	=	new PointerProperties[pointCount];
		for(int i=0;i<pointCount;i++)
		{
			pps[i]		=	new PointerProperties();
			pps[i].id	=	socketReader.readInt(is);
			pcs[i]		=	new PointerCoords();
			pcs[i].x	=	socketReader.readFloat(is);
			pcs[i].y	=	socketReader.readFloat(is);
			
		}
		bfsdkm.getTouch().touch(pointCount, action, pcs, pps);
		Log.v(TAG,"touch " + "action:" + action + " pointCount:" + pointCount);
	}
	
	private void key(InputStream is) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		int key		=	socketReader.readInt(is);
		int action	=	socketReader.readInt(is);
		bfsdkm.getKeyMotion().doKey(action, key);
		Log.v(TAG,"key " + "action:" + action + " key:" + key);
	}
	
	private void sensor(InputStream is,int playerID) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		float x		=	socketReader.readFloat(is);
		float y		=	socketReader.readFloat(is);
		float z		=	socketReader.readFloat(is);
		bfsdkm.getSensor().doSensor(x, y, z,playerID);
		Log.v(TAG,"sensor " + "playerID:" + playerID + " x:" + x + " y:" + y + " z:" + z);
	}
	
	private void inputText(InputStream is) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		byte[] data	=	socketReader.readBytes(is);
		String str	=	new String(data);
		bfsdkm.getInputEditText().fillEditText(str);
		Log.v(TAG,"inputText " + "str:" + str);
	}
	
	private void wantPlayerID(InputStream is) throws Exception
	{
		final int num		=	socketReader.readInt(is);
		final int[] ids	=	new int[num];
		for(int i=0;i<num;i++)
		{
			ids[i]	=	socketReader.readInt(is);
			ids[i]	+=	PhonePlayer.PLAYORDER_PROFIX;
		}
		bfsdkm.handleWantPlayerIDsDataFromClient(ids,num);
		Log.v(TAG,"wantPlayerID " + "num:" + num);
	}
	
	private void wantPlayerID2(InputStream is) throws Exception
	{
		final int num		=	socketReader.readInt(is);
		final int[] ids	=	new int[num];
		for(int i=0;i<num;i++)
		{
			ids[i]	=	socketReader.readInt(is);
			ids[i]	+=	PhonePlayer.PLAYORDER_PROFIX;
		}
		PlayerManager.getInstance().setPlayerIDs(ids);
		Log.v(TAG,"wantPlayerID2 " + "num:" + num);
	}
	
	private void playerConnect(InputStream is) throws Exception
	{
		int playerID	=	socketReader.readInt(is);
		int connect		=	socketReader.readInt(is);
		playerID		+=	PhonePlayer.PLAYORDER_PROFIX;
		if(connect == 1)		//connect
		{
			//ssl.OnPlayerConnect(playerID);
			OnPlayerState ops	=	PlayerManager.getInstance().getOnPlayerState();
			if(ops != null)
				ops.onPlayerConnect(playerID);
		}
		else
		{
			//ssl.OnPlayerDisconnect(playerID);
			bfsdkm.getMouse().setHold(2);
			OnPlayerState ops	=	PlayerManager.getInstance().getOnPlayerState();
			if(ops != null)
				ops.onPlayerDisconnect(playerID);
		}
		Log.v(TAG,"playerConnect " + "playerID:" + playerID + " connect:" + connect);
	}
	
	private void mouseHold(InputStream is) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		int hold	=	socketReader.readInt(is);
		bfsdkm.getMouse().setHold(hold);
		Log.v(TAG,"mouseHold " + "hold:" + hold);
	}
	
	private void gameHandler_handler_stick(InputStream is) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		int id	=	socketReader.readInt(is);
		float x	=	socketReader.readFloat(is);
		float y	=	socketReader.readFloat(is);
		bfsdkm.getSocketThirdStub().sendGameHandlerHandlerStickData(id,x,y);
		Log.v(TAG,"gameHandler_handler_stick " + "id:" + id + " x:" + x + " y:" + y);
	}
 	
	private void gameHandler_handler_button(InputStream is) throws Exception
	{
 		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		int value	=	socketReader.readInt(is);
		int event	=	socketReader.readInt(is);
		bfsdkm.getSocketThirdStub().sendGameHandlerHandlerButtonData(value,event);
		Log.v(TAG,"gameHandler_handler_button " + "value" + value + " event:" + event);
	}
	
	private void payment_heart(InputStream is) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		String token		=	socketReader.readStr(is);
		Payment payment		=	bfsdkm.getPayment();
		if(payment != null)
			payment.resetHeart(token);
		Log.v(TAG,"payment_heart " + "token" + token + " payment:" + payment);
	}
	
	private void payment_result(InputStream is) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		String token		=	socketReader.readStr(is);
		String json			=	socketReader.readStr(is);
		Payment payment		=	bfsdkm.getPayment();
		if(payment != null)
			payment.result(token,json);
		Log.v(TAG,"payment_result " + "token" + token + " json:" + json + " payment:" + payment);
	}
	
	private void voice_data(InputStream is,int playerID) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		byte[] data		=	socketReader.readBytes(is);
		bfsdkm.getVoice().doVoice(data, playerID);
		Log.v(TAG,"voice_data " + data.length);
	}

	private void voice_checkResult(InputStream is) throws Exception
	{
		SocketReader socketReader	=	new SocketReader(ByteOrder.BIG_ENDIAN);
		int id	=	socketReader.readInt(is);
		String right = socketReader.readStr(is);
		String result = socketReader.readStr(is);
		bfsdkm.getVoice().doVoiceResultLinstener(id,right,result);
		Log.v(TAG,"voice_checkResult " + "id:" + id + " right:" + right + " result:" + result);
	}
	
	class BMTCPThread extends Thread
	{
		@Override
		public void run()
		{
			handleTCP();
		}
	}
	
	class BMUDPThread extends Thread
	{
		@Override
		public void run()
		{
			handleUDP();
		}
	}
	
	
	
	
}
