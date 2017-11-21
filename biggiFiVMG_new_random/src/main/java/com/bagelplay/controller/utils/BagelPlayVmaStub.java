package com.bagelplay.controller.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteOrder;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.MotionEvent;


public class BagelPlayVmaStub {

    private static String TAG = "BiggiFiVmaStub";

    public static BagelPlayVmaStub bvfs;

    private Context context;

    private Socket socket;

    private InputStream is;

    private OutputStream os;

    private DatagramSocket udpDS;

    private InetAddress addr;

    private int port;

    private boolean connected;
    private String mac;

    private String tcp_lock = "tcp_lock";

    private String udp_lock = "udp_lock";

    private BagelPlayVmaStub(Context context) {
        this.context = context;

        //	 getMacAddr();
    }

    public static void init(Context context) {
        if (bvfs == null)
            bvfs = new BagelPlayVmaStub(context);
    }


    public static BagelPlayVmaStub getInstance() {
        return bvfs;
    }

    public boolean connect(String ip, int port) {
        try {
            addr = InetAddress.getByName(ip);
            this.port = port;
            InetSocketAddress isa = new InetSocketAddress(ip, port);
            socket = new Socket();
            socket.connect(isa, 5000);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            udpDS = new DatagramSocket();
            connected = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() {
        connected = false;
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }

    public InputStream getDataInputStream() {
        return is;
    }

    public void sendLoginData() {


        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.LOGIN);

        sw.writeInt(Config.widthPixels);
        sw.writeInt(Config.heightPixels);

        sendTcp(sw.getBytes());

	/*	SocketWriter sw	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		sw.writeInt(CmdProtocol.ClientToServer.LOGIN);
		
		sw.writeInt(Config.widthPixels);
		sw.writeInt(Config.heightPixels);
		
		
		SocketWriter sw1	=	new SocketWriter(ByteOrder.LITTLE_ENDIAN);
		
		sw1.writeBytes(sw.getBytes());
	
		try
		{
			
			os.write(sw1.getBytes());
			os.flush();
		}catch(Exception e){
			e.printStackTrace();
		}*/


    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public void sendAppInfoData() {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.APPINFO);
        sendTcp(sw.getBytes());
    }

    public void sendMouseData(int action, int offsetX, int offsetY) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.MOUSE);
        sw.writeInt(action);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeInt(action);
        sw2.writeInt(offsetX);
        sw2.writeInt(offsetY);

        sw.writeBytes(sw2.getBytes());

        sendUdp(sw.getBytes());


    }

    public void sendTouchData(int pointCount, int action, int[] id, float[] x, float[] y) {

        if (action == 5 && id.length == 1)
            action = MotionEvent.ACTION_DOWN;
        else if (action == 6 && id.length == 1)
            action = MotionEvent.ACTION_UP;

        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.TOUCH);
        sw.writeInt(action);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeInt(pointCount);
        sw2.writeInt(action);
        for (int i = 0; i < pointCount; i++) {
            sw2.writeInt(id[i]);
            sw2.writeFloat(x[i]);
            sw2.writeFloat(y[i]);
        }

        sw.writeBytes(sw2.getBytes());
        sendUdp(sw.getBytes());
    }

    public void sendKeyData(int key, int action) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.KEY);
        sw.writeInt(action);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeInt(key);
        sw2.writeInt(action);

        sw.writeBytes(sw2.getBytes());
        sendUdp(sw.getBytes());
    }

    public void sendSensorData(float x, float y, float z) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.SENSOR);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeFloat(x);
        sw2.writeFloat(y);
        sw2.writeFloat(z);

        sw.writeBytes(sw2.getBytes());
        sendUdp(sw.getBytes());


    }

    public void sendSensorSwitchData(int swt) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.SENSOR_SWITCH);
        sw.writeInt(swt);
        sendTcp(sw.getBytes());
    }

    public void sendInputTextData(String text) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.INPUTTEXT);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeBytes(text.getBytes());

        sw.writeBytes(sw2.getBytes());
        sendUdp(sw.getBytes());
    }

    public void sendHeartData() {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.HEART);
        sendTcp(sw.getBytes());
    }

    public void sendMouseHoldData(int hold) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.MOUSE_HOLD);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeInt(hold);

        sw.writeBytes(sw2.getBytes());
        sendTcp(sw.getBytes());
    }

    public void sendGameHandlerHandlerStickData(int id, float x, float y) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.GAMEHANDLER_HANDLER_STICK);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeInt(id);
        sw2.writeFloat(x);
        sw2.writeFloat(y);

        sw.writeBytes(sw2.getBytes());
        sendUdp(sw.getBytes());
    }

    public void sendGameHandlerHandlerButtonData(int value, int event) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.GAMEHANDLER_HANDLER_BUTTON);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeInt(value);
        sw2.writeInt(event);

        sw.writeBytes(sw2.getBytes());
        sendUdp(sw.getBytes());
    }

    public void sendPaymentHeartData(String token, String packageName) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.PAYMENT_HEART);
        sw.writeStr(packageName);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeStr(token);

        sw.writeBytes(sw2.getBytes());
        sendTcp(sw.getBytes());
    }

    public void sendPaymentResultData(String token, String packageName, String resultJson) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.PAYMENT_RESULT);
        sw.writeStr(packageName);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeStr(token);
        sw2.writeStr(resultJson);

        sw.writeBytes(sw2.getBytes());
        sendTcp(sw.getBytes());
    }

    public void sendVoiceData(byte[] data, int index, int len) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.VOICE_DATA);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeBytes(data, index, len);

        sw.writeBytes(sw2.getBytes());
        sendUdp(sw.getBytes());

        Log.v(TAG, "sendVoiceData");
    }

    public void sendVoiceCheckResult(int id, String right, String result) {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.VOICE_CHECK_RESULT);

        SocketWriter sw2 = new SocketWriter(ByteOrder.BIG_ENDIAN);
        sw2.writeInt(id);
        sw2.writeStr(right);
        sw2.writeStr(result);

        sw.writeBytes(sw2.getBytes());
        sendTcp(sw.getBytes());

        Log.v(TAG, "sendVoiceCheckResult");
    }


    public void sendServerClose() {
        SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
        sw.writeInt(CmdProtocol.ClientToServer.SERVER_CLOSE);
        sw.writeBytes(new byte[0]);
        sendTcp(sw.getBytes());

        Log.v(TAG, "sendServerClose");
    }

    private void sendTcp(byte[] datas) {
        synchronized (tcp_lock) {
            SocketWriter sw = new SocketWriter(ByteOrder.LITTLE_ENDIAN);
            sw.writeBytes(datas);
            try {

                os.write(sw.getBytes());
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void sendUdp(byte[] datas) {
        synchronized (udp_lock) {
            DatagramPacket dp = new DatagramPacket(datas, datas.length, addr, port);
            try {
                udpDS.send(dp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//	public void sendRegister(int androidSign)
//	{
//		SocketWriter sw	=	new SocketWriter(ByteOrder.BIG_ENDIAN);	
//		sw.writeInt(CmdProtocol.ClientToTranslateServer.REGISTER);
//		sw.writeStr(mac);
//		sw.writeInt(androidSign);
//		sendTcp2(sw.getBytes());
//	}

//	private void sendTcp2(byte[] datas)
//	{
//		synchronized(tcp_lock)
//		{
//			SocketWriter sw	=	new SocketWriter(ByteOrder.BIG_ENDIAN);	
//			sw.writeBytes(datas);
//			try
//			{
//				os.write(sw.getBytes());
//				os.flush();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		
//	}
//	private void getMacAddr()
//	{
//		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		 
//		WifiInfo info = wifi.getConnectionInfo();
//		 
//		mac	=	info.getMacAddress();
//		
//		
//		Log.v("=--------------------------------------------=", mac + "");
//	}
}
