package com.bagelplay.controller.wifiset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bagelplay.controller.utils.Log;

public class Utils {

    public static final String HOTTAG = "=------------------MyTest------------------=";

    public static final String BROADCAST_SEND_ID = "sendID";

    public static String HOT_SSID = "";

    public static final String HOT_PASSWORD = "biggifipassword";

    public static String HOT_BSSID = "";

    //public static final String HOT_SECURITY						=	"[wpa-psk-ccmp]";
    public static final String HOT_SECURITY = null;

    public static final String BIGGIFIWIFIAPSSID_PREFIX_OLD = "biggifi_";

    public static final String BIGGIFIWIFIAPSSID_PREFIX = "BiggiFi";

    public static final int HOT_SOCKETSERVER_POT = 19999;

    public static final int HOT_SOCKETSERVERMONITOR_POT = 19998;

    public static final int HOT_PROTOCOL_C2S_WIFI_SIIDANDPASSWORD = 1000001;

    public static final int HOT_PROTOCOL_C2S_CONFIGWIFIOK = 1000004;

    public static final int HOT_PROTOCOL_C2S_WIFI_DEVICENAME = 1000005;

    public static final int HOT_PROTOCOL_C2S_WIFI_WANTOTHERHOTS = 1000006;

    public static final int HOT_PROTOCOL_S2C_WIFI_WANTSIIDANDPASSWORD = 2000001;

    public static final int HOT_PROTOCOL_S2C_WIFI_DEVICENAMESUCCESS = 2000005;

    public static final int HOT_PROTOCOL_C2S_WIFI_CHECKISLAUNCHERING = 1000010;

    public static final int HOT_PROTOCOL_S2C_WIFI_CHECKISLAUNCHERING = 2000010;

    public static final int HOT_PROTOCOL_S2C_WIFI_OTHERHOTS = 2000006;

    public static final int HOT_PROTOCOL_C2S_WIFI_RESETWIFISETTING = 1000011;

    public static final String HOT_ACTIVITY_ACTION_NOSHOW = "noShow";

    public static String SERVERSOCKETIP;

    public static final int ERROR_CODE_1 = 1;

    public static final int ERROR_CODE_2 = 2;

    public static final int ERROR_CODE_3 = 3;

    public static final int ERROR_CODE_4 = 4;

    public static final int ERROR_CODE_5 = 5;

    public static final int ERROR_CODE_6 = 6;

    public static final int ERROR_CODE_7 = 7;

    public static final int ERROR_CODE_8 = 8;

    public static final int ERROR_CODE_9 = 9;

    public static final int ERROR_CODE_10 = 10;


    public static byte[] getServerWantWifiSSidAndPassword() {
        byte[] res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(HOT_PROTOCOL_S2C_WIFI_WANTSIIDANDPASSWORD);
            res = baos.toByteArray();
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static byte[] getClientWifiSSidAndPassword(String ssid, String password, String type) {
        byte[] res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(HOT_PROTOCOL_C2S_WIFI_SIIDANDPASSWORD);
            dos.writeUTF(ssid);
            dos.writeUTF(password);
            dos.writeUTF(type);
            res = baos.toByteArray();
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static byte[] getDeviceName() {
        byte[] res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(HOT_PROTOCOL_C2S_WIFI_DEVICENAME);
            byte[] device = android.os.Build.MODEL.getBytes("utf-8");
            dos.writeInt(device.length);
            dos.write(device);
            res = baos.toByteArray();
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static byte[] getConfigWifiOk() {
        byte[] res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(HOT_PROTOCOL_C2S_CONFIGWIFIOK);
            res = baos.toByteArray();
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String ipLong2String(long ip) {
        long first = ip >>> 24 & 0xff;
        long secend = ip >>> 16 & 0xff;
        long third = ip >>> 8 & 0xff;
        long forth = ip & 0xff;
        return forth + "." + third + "." + secend + "." + first;
    }

    public static String ipLong2StringPreference(long ip) {
        long first = ip >>> 24 & 0xff;
        long secend = ip >>> 16 & 0xff;
        long third = ip >>> 8 & 0xff;
        long forth = ip & 0xff;
        return first + "." + secend + "." + third + "." + forth;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void setBold(TextView tv) {
        Locale l = Locale.getDefault();
        if ("zh".equals(l.getLanguage())) {
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(true);
        }
    }

    public static List<StickInfo> getSticksFromWifiFind(List<StickInfo> stickA) {
        String[] sticks = new String[stickA.size()];
        for (int i = 0; i < stickA.size(); i++) {
            StickInfo si = stickA.get(i);
            sticks[i] = si.name + "|" + si.ip + "|";
        }

        byte[][] sticksByte = com.bagelplay.controller.wifiset.Jni.findSticksFromWifi(sticks);


        if (sticksByte != null) {
            List<StickInfo> s = new ArrayList<StickInfo>();
            for (int i = 0; i < sticksByte.length; i++) {
                String stickName = new String(sticksByte[i]);
                String[] sns = stickName.split("\\|");
                StickInfo si = new StickInfo(sns[0], sns[1], false, null);
                s.add(si);
            }
            return s;
        }

        return null;
    }

    public static List<StickInfo> getSticksFromWifiFindRandom(List<StickInfo> stickA) {
        String[] sticks = new String[stickA.size()];
        for (int i = 0; i < stickA.size(); i++) {
            StickInfo si = stickA.get(i);
            sticks[i] = si.name + "|" + si.ip + "|";
        }

        byte[][] sticksByte = com.bagelplay.controller.wifiset.random.Jni.findSticksFromWifi(sticks);


        if (sticksByte != null) {
            List<StickInfo> s = new ArrayList<StickInfo>();
            for (int i = 0; i < sticksByte.length; i++) {
                String stickName = new String(sticksByte[i]);
                Log.v("=-------------------------------=====", stickName);
                String[] sns = stickName.split("\\|");
                String hostName = "";
                int SCRSHOTSC_PORT = 0;
                int CMD_PORT = 0;
                int level = 0;
                int VMA_CMD_APP_INFO = 0;
                try {
                    JSONObject jo = new JSONObject(sns[0]);
                    hostName = jo.getString("HOSTNAME");
                    SCRSHOTSC_PORT = jo.getInt("SCRSHOTSC_PORT");
                    CMD_PORT = jo.getInt("CMD_PORT");
                    level = jo.getInt("LEVEL");
                    VMA_CMD_APP_INFO = jo.getInt("SDK_APP_INFO_PORT");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                StickInfo si = new StickInfo(hostName, sns[1], SCRSHOTSC_PORT, CMD_PORT, level);
                si.VMA_CMD_APP_INFO = VMA_CMD_APP_INFO;
                s.add(si);
            }
            return s;
        }

        return null;
    }


    public static boolean findStickFromWifi(StickInfo si) {
        /*final int timeout	=	30 * 1000;
		final int sleepTime	=	500;
		String msg = "SCN";
		final DatagramSocket ds;
		try {
			ds = new DatagramSocket();
		} catch (SocketException e) {
 			e.printStackTrace();
 			return false;
		}
		
		InetAddress ia;
		try {
			ia = InetAddress.getByName(BiggiFiVmaStub_b.IPS);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return false;
		}
		final DatagramPacket dpSend = new DatagramPacket(msg.getBytes(),
				msg.length(), ia, BiggiFiVmaStub_b.VMA_ALIVE_PORT);
		
		final Finish f	=	new Finish();
		
		new Thread()
		{
			public void run()
			{
				int times	=	0;
				while(true)
				{
					if(times > timeout)
					{
						try {
							ds.close();
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						ds.send(dpSend);
					} catch (Exception e) {
						e.printStackTrace();
						if(f.finish)
						{
							ds.close();
							return;
						}
					}
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
 						e.printStackTrace();
					}
					times	+=	sleepTime;
				}
			}
		}.start();
		
		while(true)
		{
			byte[] recvData = new byte[12];
			DatagramPacket dp = new DatagramPacket(recvData, recvData.length);
			try {
				ds.receive(dp);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			String name	=	new String(recvData,0,recvData.length);
			
			String ip	=	dp.getAddress().toString().substring(1);
			
			if(name.equals(si.name))
			{
				si.ip	=	ip;
				f.finish	=	true;
				ds.close();
				return true;
			}
		}*/

        return false;
    }

    public static List<StickInfo> findStickFromHot() {
        float time = 3;
        List<StickInfo> sticks = new ArrayList<StickInfo>();
        WifManager wm = WifManager.getInstance();
        List<ScanResult> srs = wm.scanHots(time);
        for (ScanResult sr : srs) {
            Log.v("=--------------findStickFromHot----------------", sr.SSID);
            boolean isHave = false;
            for (StickInfo tsr : sticks) {
                if (tsr.name.equals(sr.SSID)) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave && isSsidBiggiFiHot(sr.SSID))
                addStick(sticks, new StickInfo(sr.SSID, null, true, sr.capabilities));
        }
        return sticks;
    }

    public static boolean isSsidBiggiFiHot(String ssid) {
        if (ssid == null)
            return false;
        if (ssid.startsWith(Utils.BIGGIFIWIFIAPSSID_PREFIX_OLD))
            return true;
        return false;

    }

    private static boolean isOldStick(String ssid) {
        return ssid.startsWith(Utils.BIGGIFIWIFIAPSSID_PREFIX_OLD) && ssid.length() > 12;
    }
	
	/*public static int isStickLaunchering(final Context context,String ip,String ssid)
	{
		Sock sock	=	null;
		try
		{
			if(isOldStick(ssid))
				return 1;
			sock	=	new Sock(ip,HOT_SOCKETSERVERMONITOR_POT);
	 		sock.send(getAskStickLaunchering());
 	 		byte[] datas	=	sock.receive();
 	 		Log.v("=--------------addStick---------fff-------", datas.length + "   ");
 	 		sock.close();
	 		DataInputStream dis	=	new DataInputStream(new ByteArrayInputStream(datas));
	 		int cmd		=	dis.readInt();
	 		boolean res	=	false;
	 		if(cmd == HOT_PROTOCOL_S2C_WIFI_CHECKISLAUNCHERING)
	 		{	
	 			res =	dis.readBoolean();
 	 		}
	 		dis.close();
	 		 
	 		if(res)
	 			return 1;
	 		return 2;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 3;
	}*/


    public static byte[] getAskStickLaunchering() {
        byte[] res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(HOT_PROTOCOL_C2S_WIFI_CHECKISLAUNCHERING);
            res = baos.toByteArray();
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static byte[] getResetStickWifisetting() {
        byte[] res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(HOT_PROTOCOL_C2S_WIFI_RESETWIFISETTING);
            res = baos.toByteArray();
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static byte[] getAskStickOtherHots() {
        byte[] res = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(HOT_PROTOCOL_C2S_WIFI_WANTOTHERHOTS);
            res = baos.toByteArray();
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
	
	/*public static boolean resetStickWifisetting(String ip)
	{
		Sock sock	=	null;
		try
		{
			sock	=	new Sock(ip,HOT_SOCKETSERVERMONITOR_POT);
	 		sock.send(getResetStickWifisetting());
	 		sock.close();
	 		return true;
		}catch(Exception e){
			e.printStackTrace();
			 
		}
		return false;
	}*/


    private static void addStick(List<StickInfo> sticks, StickInfo si) {
        if (!si.name.startsWith(Utils.BIGGIFIWIFIAPSSID_PREFIX) && !si.name.startsWith(Utils.BIGGIFIWIFIAPSSID_PREFIX_OLD))
            return;
        for (StickInfo s : sticks) {
            if (s.equals(si))
                return;
        }
        Log.v("=--------------addStick----------------", si.name);
        sticks.add(si);
    }
	
	/*public static boolean connectToHot2(final Context context,final String ssid,String password,String type,final View.OnClickListener btn1Lisn)
	{
		Log.v("=--------------connectToHot----------------", ssid + " " + password + " " + type);
		Handler handler	=	new Handler(context.getMainLooper());
		WifManager wm			=	WifManager.getInstance();
		int res		=	wm.connectToHot(ssid, password, type);
		final Locale l = Locale.getDefault();  
		if(res == 1)  //timeout
		{
			handler.post(new Runnable(){
				public void run()
				{
					if(ssid.startsWith(BIGGIFIWIFIAPSSID_PREFIX) || ssid.startsWith(BIGGIFIWIFIAPSSID_PREFIX_OLD))
					{
						showAlertDialog(context, null, context.getString(R.string.wifi_set_error_cantconnectstick), R.string.ok, btn1Lisn);
					}
					else
					{
						StickInfo stick	=	((BiggiFiApplication)((WifiSetAct)context).getApplication()).stick;
						if("zh".equals(l.getLanguage()))
						{
							showAlertDialog(context, context.getString(R.string.wifi_set_error_canntfindwifititle), context.getString(R.string.wifi_set_error_canntfindwifi,ssid), R.string.cancel, null,R.string.wifi_set_retry,btn1Lisn);
						}
						else
						{
							showAlertDialog(context, context.getString(R.string.wifi_set_error_canntfindwifititle), context.getString(R.string.wifi_set_error_canntfindwifi,stick.name,ssid), R.string.cancel, null,R.string.wifi_set_retry,btn1Lisn);
						}
					}
				}
			});
			return false;
		}
		else if(res == 2)  //wrong password
		{
			handler.post(new Runnable(){
				public void run()
				{
					if(ssid.startsWith(BIGGIFIWIFIAPSSID_PREFIX) || ssid.startsWith(BIGGIFIWIFIAPSSID_PREFIX_OLD))
					{
						Log.v("=---=----------------=========", "7");
					}
 					else
					{
 						StickInfo stick	=	((BiggiFiApplication)((WifiSetAct)context).getApplication()).stick;
 						if("zh".equals(l.getLanguage()))
 						{
 							showAlertDialog(context, context.getString(R.string.wifi_set_error_passworderrormessagetitle), context.getString(R.string.wifi_set_error_passworderrormessage,ssid), R.string.ok, null);
 				
 						}
 						else
 						{
 							showAlertDialog(context, context.getString(R.string.wifi_set_error_passworderrormessagetitle), context.getString(R.string.wifi_set_error_passworderrormessage,stick.name,ssid), R.string.ok, null);
 						}
					}
						
				}
			});
			return false;
		}
		return true;
	}*/
	
	/*public static boolean connectToHot(final Context context,final String ssid,String password,String type,final View.OnClickListener btn1Lisn)
	{
		Log.v("=--------------connectToHot----------------", ssid + " " + password + " " + type);
		Handler handler	=	new Handler(context.getMainLooper());
		WifManager wm			=	WifManager.getInstance();
		int res		=	wm.connectToHot(ssid, password, type);
		final Locale l = Locale.getDefault();  
		if(res == 1)  //timeout
		{
			handler.post(new Runnable(){
				public void run()
				{
					if(ssid.startsWith(BIGGIFIWIFIAPSSID_PREFIX) || ssid.startsWith(BIGGIFIWIFIAPSSID_PREFIX_OLD))
					{
						showAlertDialog(context, null, context.getString(R.string.wifi_set_error_cantconnectstick), R.string.ok, btn1Lisn);
					}
					else
					{
						StickInfo stick	=	((BiggiFiApplication)((WifiSetAct)context).getApplication()).stick;
						if("zh".equals(l.getLanguage()))
						{
							showAlertDialog(context, context.getString(R.string.wifi_set_error_canntfindwifititle), context.getString(R.string.wifi_set_error_canntfindwifi,ssid), R.string.cancel, null,R.string.wifi_set_retry,btn1Lisn);
						}
						else
						{
							showAlertDialog(context, context.getString(R.string.wifi_set_error_canntfindwifititle), context.getString(R.string.wifi_set_error_canntfindwifi,stick.name,ssid), R.string.cancel, null,R.string.wifi_set_retry,btn1Lisn);
						}
					}
				}
			});
			return false;
		}
		else if(res == 2)  //wrong password
		{
			handler.post(new Runnable(){
				public void run()
				{
					if(ssid.startsWith(BIGGIFIWIFIAPSSID_PREFIX) || ssid.startsWith(BIGGIFIWIFIAPSSID_PREFIX_OLD))
					{
						Log.v("=---=----------------=========", "7");
					}
 					else
					{
 						StickInfo stick	=	((BiggiFiApplication)((WifiSetAct)context).getApplication()).stick;
 						if("zh".equals(l.getLanguage()))
 						{
 							showAlertDialog(context, context.getString(R.string.wifi_set_error_passworderrormessagetitle), context.getString(R.string.wifi_set_error_passworderrormessage,ssid), R.string.ok, btn1Lisn);
 				
 						}
 						else
 						{
 							showAlertDialog(context, context.getString(R.string.wifi_set_error_passworderrormessagetitle), context.getString(R.string.wifi_set_error_passworderrormessage,stick.name,ssid), R.string.ok, btn1Lisn);
 						}
					}
						
				}
			});
			return false;
		}
		return true;
	}*/


    public static boolean connectToHot(final Context context, final String ssid, String password, String type) {
        Log.v("=--------------connectToHot----------------", ssid + " " + password + " " + type);
        Handler handler = new Handler(context.getMainLooper());
        WifManager wm = WifManager.getInstance();
        int res = wm.connectToHot(ssid, password, type);
        return res != 1 && res != 2;
    }

    public static int connectToHot2(final Context context, final String ssid, String password, String type) {
        Log.v("=--------------connectToHot----------------", ssid + " " + password + " " + type);
        Handler handler = new Handler(context.getMainLooper());
        WifManager wm = WifManager.getInstance();
        int res = wm.connectToHot(ssid, password, type);
        return res;
    }
	
	
	/*public static boolean connectToWifiHuiLian(final Context context,final String ssid,String password,String type,final View.OnClickListener btn1Lisn,final View.OnClickListener btn1Lisn2)
	{
		Log.v("=--------------connectToHot----------------", ssid + " " + password + " " + type);
		Handler handler	=	new Handler(context.getMainLooper());
		WifManager wm			=	WifManager.getInstance();
		final int res		=	wm.connectToHot(ssid, password, type);
		final Locale l = Locale.getDefault();  
		if(res != 0)
		{
			handler.post(new Runnable(){
				public void run()
				{
					if(res == 1)
					{
						showAlertDialog(context, context.getString(R.string.wifi_set_error_canntfindwifititle), context.getString(R.string.wifi_set_error_canntfindwifi,ssid), R.string.cancel, null,R.string.wifi_set_retry,btn1Lisn2);
					}
					else
					{
						showAlertDialog(context, context.getString(R.string.wifi_set_error_huilianerrortitle), context.getString(R.string.wifi_set_error_huilianerrormessage),R.string.wifi_set_retry, btn1Lisn,R.string.exit,new View.OnClickListener(){
							@Override
							public void onClick(View v) {
								((WifiSetAct)context).suddenExit();
							}
							 
						});
					}
					
				}
			});
			return false;
		}
		return true;
	}*/


    public static boolean connectToVma(String ip, final Context context, final boolean showToast) {
		/*Handler handler	=	new Handler(context.getMainLooper());
		BiggiFiVmaStub_b biggiFiVmaStub	=	BiggiFiVmaStub_b.getInstance();
		if (biggiFiVmaStub.initVmaStub(BiggiFiUtil.ipToLong(ip)) != 0)
		{
			Log.v("BiggiFiVMG", "VMG Login activity, init vma stub error");
			if(!showToast)
				return false;
			handler.post(new Runnable(){
				public void run()
				{
					BiggiFiUtil.errorInfoShow(context, R.string.ip_error_timeout);
				}
			});
			return false;
		}
		int ret = 1101;
		ret	=	biggiFiVmaStub.loginVma(BiggiFiVmaStub_b.loginName, BiggiFiVmaStub_b.loginCode);
		if(ret == BiggiFiVmaStub_b.ERRNO_SUEECSS)
		{
			int vmaVer = biggiFiVmaStub.getVmaHostVer();
			saveToSharedPreferences("vmaVer","ver",vmaVer,context);
			int vmaType = biggiFiVmaStub.getVmaHostType();
			saveToSharedPreferences("vmaType","type",vmaType,context);
			long vmaip	=	BiggiFiUtil.ipToLong(ip);
			saveToSharedPreferences("vmaIP","ip",vmaip,context);
			notifyVmaSetupFinish(ip);
			Log.v("BiggiFiVMG", "VMG Login SUEECSS");
			return true;
		}
		else
		{
			biggiFiVmaStub.unInitVmaStub();
			if(!showToast)
				return false;
			final int res	=	ret;
			handler.post(new Runnable(){
				public void run()
				{
					switch(res)
					{
						case BiggiFiVmaStub_b.ERRNO_ACCOUNT_EXISTED:
							BiggiFiUtil.errorInfoShow(context, R.string.account_existed_error);
						break;
						
						case BiggiFiVmaStub_b.ERRNO_CODE_WRONG:
							BiggiFiUtil.errorInfoShow(context, R.string.code_error);
							break;
							
						case BiggiFiVmaStub_b.ERRNO_CODE_NOT_SET:
							BiggiFiUtil.errorInfoShow(context, R.string.code_set_error);
							break;
							
						case BiggiFiVmaStub_b.ERRNO_ACCOUNT_HAVE_LOGGED:
							BiggiFiUtil.errorInfoShow(context, R.string.max_user_error);
							break;
							
						case BiggiFiVmaStub_b.ERRNO_MAX_USERS:
							BiggiFiUtil.errorInfoShow(context, R.string.max_user_error);
							break;
							
						case -100:
							BiggiFiUtil.errorInfoShow(context, R.string.disconnect_error);
							break;
							
						default:
							BiggiFiUtil.errorInfoShow(context, R.string.unknown_error);
							break;
					}
				}
			});*/
        return false;
        //}
    }

    public static boolean connectToVma(StickInfo si, final Context context) {
		/*BiggiFiVmaStub_b.VMA_SCRSHOTSC_PORT	=	si.VMA_SCRSHOTSC_PORT;
		BiggiFiVmaStub_b.VMA_CMD_PORT			=	si.VMA_CMD_PORT;
		String ip		=	si.ip;*/

        return false;
    }

    public static boolean connectToVma(String ip, final Context context) {
        int times = 3;
        for (int i = 0; i < times; i++) {
            boolean showToast = i < times - 1 ? false : true;
            boolean res = connectToVma(ip, context, showToast);

            if (res)
                return true;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
	
	/*public static void notifyVmaSetupFinish(String ip)
	{
 		Sock sock	=	null;
		try
		{
 			sock	=	new Sock(ip,Utils.HOT_SOCKETSERVER_POT);
 			sock.send(Utils.getConfigWifiOk());
 	 		sock.close();
		}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public static boolean sendDeviceName(String ip)
	{
		Sock sock	=	null;
		try
		{
			sock	=	new Sock(ip,Utils.HOT_SOCKETSERVER_POT);
	 		sock.send(Utils.getDeviceName());
	 		//byte[] datas	=	sock.receive();
	 		sock.close();
	 		//DataInputStream dis	=	new DataInputStream(new ByteArrayInputStream(datas));
	 		//int cmd		=	dis.readInt();
	 		//dis.close();
	 		//if(cmd == Utils.HOT_PROTOCOL_S2C_WIFI_DEVICENAMESUCCESS)
	 		//{	
	 		//	return true;
	 		//}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean sendSsidAndPassword(String ip,String ssid,String password,String type)
	{
		Sock sock	=	null;
		try
		{
			sock	=	new Sock(ip,Utils.HOT_SOCKETSERVER_POT);
	 		sock.send(Utils.getClientWifiSSidAndPassword(ssid, password, type));
	 		sock.close();
	 		return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/
	
	/*public static List<ScanResult> findNetworks()
	{
		WifManager wm			=	WifManager.getInstance();
		List<ScanResult> srs	=	wm.scanHots(3);
		for(int i=0;i<srs.size();i++)
		{
			ScanResult sr	=	srs.get(i);
			if(sr.SSID.startsWith(Utils.BIGGIFIWIFIAPSSID_PREFIX) || sr.SSID.startsWith(Utils.BIGGIFIWIFIAPSSID_PREFIX_OLD))
			{
				srs.remove(sr);
				i--;
			}
		}
		return srs;
	}*/
	
	/*public static List<WifiList.WifiScanResult> findNetworks(String ip)
	{
		Sock sock	=	null;
		try
		{
			sock	=	new Sock(ip,HOT_SOCKETSERVER_POT);  
	 		sock.send(getAskStickOtherHots());
 	 		byte[] datas	=	sock.receive();
 	 		sock.close();
	 		DataInputStream dis	=	new DataInputStream(new ByteArrayInputStream(datas));
	 		int cmd		=	dis.readInt();
	 		List<WifiList.WifiScanResult> srs	=	new ArrayList<WifiList.WifiScanResult>();
	 		if(cmd == HOT_PROTOCOL_S2C_WIFI_OTHERHOTS)
	 		{	
	 			int hotsNum			=	dis.readInt();
	 			for(int i=0;i<hotsNum;i++)
	 			{
	 				int ssidLeng		=	dis.readInt();
		 			byte[] ssidDatas	=	new byte[ssidLeng];
		 			dis.read(ssidDatas);
		 			int capabilitiesLeng		=	dis.readInt();
		 			byte[] capabilitiesDatas	=	new byte[capabilitiesLeng];
		 			dis.read(capabilitiesDatas);
		 			int level			=	dis.readInt();
		 			WifiList.WifiScanResult sr		=	new WifiList.WifiScanResult();
		 			sr.SSID	=	new String(ssidDatas);
		 			sr.capabilities	=	new String(capabilitiesDatas);
		 			sr.level	=	level;
		 			
		 			if(srs.size() == 0)
		 				srs.add(sr);
		 			else
		 			{
		 				if(sr.level < srs.get(srs.size() - 1).level)
		 					srs.add(sr);
		 				else
		 				{
		 					for(int j=0;j<srs.size();j++)
				 			{
				 				if(sr.level > srs.get(j).level)
				 				{
				 					srs.add(j, sr);
				 					break;
				 				}				 					
				 			}
		 				}
		 			}
	 			}
 	 		}
	 		return srs;
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<WifiList.WifiScanResult>();
	}*/

    public static void showAlertDialog(final Context context, final String title, final String message, final int btnText1, final View.OnClickListener btn1Lisn, final int btnText2, final View.OnClickListener btn2Lisn) {
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                BFDialog2 bfdialog = new BFDialog2((WifiSetAct) context);
                if (title != null)
                    bfdialog.setTitle(title);
                if (message != null)
                    bfdialog.setMessage(message);
                bfdialog.setPositiveButton(btnText1, btn1Lisn);
                bfdialog.setNegativeButton(btnText2, btn2Lisn);
                bfdialog.show();
            }
        });
    }


    public static void showAlertDialog(final Context context, final String title, final String message, final int btnText1, final View.OnClickListener btn1Lisn, final int errorCode) {
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                BFDialog2 bfdialog = new BFDialog2((WifiSetAct) context);
                if (title != null)
                    bfdialog.setTitle(title);
                if (message != null)
                    bfdialog.setMessage(message);
                bfdialog.setPositiveButton(btnText1, btn1Lisn);
                bfdialog.show();
                if (errorCode != -1)
                    Toast.makeText(context, "Error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static boolean checkNetworkState(final Context context) {
        WeakReference<Context> contextRef = new WeakReference<Context>(context);
        final Context mContext = contextRef.get();

        ConnectivityManager conMan = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // wifi
        android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState();

        return wifi == State.CONNECTED;
    }

    public static byte[] readPic(DataInputStream dis, int size) throws Exception {
        byte[] data = new byte[size];
        int per = 1024;
        int readNum = 0;
        while (true) {
            int needRead = size - readNum >= per ? per : size - readNum;
            if (needRead <= 0)
                break;
            int actRead = dis.read(data, readNum, needRead);
            readNum += actRead;
        }
        return data;
    }

    private static void saveToSharedPreferences(String item, String key, int value, Context context) {
        SharedPreferences verPreference = context.getSharedPreferences(
                item, Context.MODE_PRIVATE);
        Editor verEdit = verPreference.edit();
        verEdit.putInt(key, value);
        verEdit.commit();
    }

    private static void saveToSharedPreferences(String item, String key, long value, Context context) {
        SharedPreferences verPreference = context.getSharedPreferences(
                item, Context.MODE_PRIVATE);
        Editor verEdit = verPreference.edit();
        verEdit.putLong(key, value);
        verEdit.commit();
    }


    static class Finish {
        public boolean finish;

        public boolean result;
    }
}
