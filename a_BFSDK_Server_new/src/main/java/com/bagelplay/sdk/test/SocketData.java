package com.bagelplay.sdk.test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.content.Context;
import android.os.Message;
import com.biggifi.sdk.common.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

import com.biggifi.sdk.common.SDKListner;
import com.biggifi.sdk.common.event.TouchEvent;
import com.biggifi.sdk.common.event.TouchEvent2;
import com.biggifi.sdk.datatransfer.tv.Controller;

public class SocketData {

	private int port;

	DatagramSocket socket;
	InetAddress mCtrlAddress;
	ByteBuffer buffer= ByteBuffer.allocate(1024);
	Controller c;
	Context context;

	DatagramSocket androidUdpSocket;
	boolean stop;


	public SocketData(final Context context)
	{
		this.context	=	context;
		try
		{
			socket		=	new DatagramSocket();
			mCtrlAddress = InetAddress.getByName("127.0.0.1");
		}catch(Exception e){
			e.printStackTrace();
		}

		buffer.order(ByteOrder.LITTLE_ENDIAN);


		new Thread()
		{
			public void run()
			{
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}


				c	=	Controller.getInstance(context);
				c.startListening(new SDKListner(){

					@Override
					public void OnReceiveListner(int playerID, byte[] tdata) {

					}

					@Override
					public void OnConnectedState(int state) {

						Log.v("=-------------------------------------=", state + "  OnConnectedState ");


					}

					@Override
					public void OnGetPlayerIDs(int[] playerIDs, int num) {

					}

					@Override
					public void OnMouseEvent(int action, int offsetX, int offsetY) {



					}


					@Override
					public void OnSensor(float x,float y,float z) {

					}

					@Override
					public void OnTouchEvent(TouchEvent ate) {
						int action	=	ate.getAction();
						int pointCount	=	ate.getPointCount();
						PointerCoords[] pcs	=	new PointerCoords[pointCount];
						PointerProperties[] pps	=	new PointerProperties[pointCount];
						for(int i=0;i<pointCount;i++)
						{
							pcs[i]	=	new PointerCoords();
							pcs[i].x	=	ate.pcs[i].x;
							pcs[i].y	=	ate.pcs[i].y;

							pps[i]	=	new PointerProperties();
						}



						MotionEvent me	=	MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), action, pointCount, pps, pcs, 0, 0, 1.0f, 1.0f, 0, 0, 0x2, 0);
						Message msg	=	new Message();
						msg.what	=	4;
						msg.obj	=	me;
						//context.handler.sendMessage(msg);
					}

					@Override
					public void OnInputFromKeyboard(String msg) {
						// TODO Auto-generated method stub

					}

					@Override
					public void OnKeyEvent(int action, int keyCode) {

					}



					@Override
					public void OnPlayerConnect(int playerID) {
						// TODO Auto-generated method stub

					}

					@Override
					public void OnPlayerDisconnect(int playerID) {
						// TODO Auto-generated method stub

					}

					@Override
					public void OnMouseHold(int hold) {
						// TODO Auto-generated method stub

					}

					@Override
					public String getControlZip() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void OnGameHandlerHandlerStick(int id, int x, int y) {
						// TODO Auto-generated method stub

					}

					@Override
					public void OnGameHandlerHandlerButton(int value, int event) {
						// TODO Auto-generated method stub

					}

					@Override
					public void OnCustomizedUDPData(byte[] data) {
						// TODO Auto-generated method stub

					}

					@Override
					public void OnPaymentHeart(String token) {
						// TODO Auto-generated method stub

					}

					@Override
					public void OnPaymentResult(String token,
												String resultJson) {
						// TODO Auto-generated method stub

					}


				});
			}
		}.start();
	}

	public void stop()
	{
		c.stopListening();
		stop	=	true;
		androidUdpSocket.close();
	}


}
