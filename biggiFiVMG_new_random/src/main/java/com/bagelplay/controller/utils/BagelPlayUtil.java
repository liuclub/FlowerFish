/**
 * BiggiFiVmaStub.java Create on 9-6-2011
 * 
 * Copyright (c) 2011 - 2012 by Digilink Software Inc.
 * 
 * @author David
 * @email david@digilinksoftware.com
 * @version 1.0
 * @last modify 9-8-2011
 * 
 */

package com.bagelplay.controller.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;


import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.Log;

import android.widget.Toast;

/**
 * Class: BiggiFiUtil
 * 
 * BiggiFi util, check network state, data type convert
 * 
 */
public class BagelPlayUtil {
	public static final String TAG = "BiggiFiVMG";
		
	/** Anything worse than or equal to this will show 0 bars. */
    private static final int MIN_RSSI = -100;
    
    /** Anything better than or equal to this will show the max bars. */
    private static final int MAX_RSSI = -55;
   	
    private static Toast mErrorInfo = null;
	/**
	 * Check network state, if network not work, go to wireless setting activity
	 * @param context
	 * @return network state
	 */
	public static State checkNetworkState(final Context context) {
		WeakReference<Context> contextRef = new WeakReference<Context>(context);
		final Context mContext = contextRef.get();

		ConnectivityManager conMan = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// wifi
		android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		if (wifi != State.CONNECTED) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(R.string.wifi_error);
			builder.setMessage(R.string.wifi_error_msg);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.wifi_error_configure,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							mContext.startActivity(new Intent(
									Settings.ACTION_WIFI_SETTINGS));	//setting wireless network
						}
					});
			builder.setNegativeButton(R.string.wifi_error_exit,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							System.exit(0);
						}
					});
			builder.show();			
		}
		
		return wifi;
	}
	
	public static State isWifiConnected(final Context context) {
		WeakReference<Context> contextRef = new WeakReference<Context>(context);
		final Context mContext = contextRef.get();

		ConnectivityManager conMan = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// wifi
		android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		
		return wifi;
	}
	
	public static int calculateSignalLevel(int rssi, int numLevels) {
        if (rssi <= MIN_RSSI) {
            return 0;
        } else if (rssi >= MAX_RSSI) {
            return numLevels - 1;
        } else {
            int partitionSize = (MAX_RSSI - MIN_RSSI) / (numLevels - 1);
            return (rssi - MIN_RSSI) / partitionSize;
        }
    }
	
	public int getNetworkStrength(Context context) {
		int strength;
		
		WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        strength = mWifiInfo.getRssi();	//get Wifi signal strength
        
        return strength;
	}
	
	/**
	 * convert short to 2 bytes array
	 * @param number which to be converted
	 * @return converted bytes array
	 */
	public static byte[] shortToByte(short number) {
		int temp = number;
		byte[] b = new byte[2];

		for (int i = 0; i < 2; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();
			temp = temp >> 8;
		}

		return b;
	}

	/**
	 * convert 2 bytes to short
	 * @param b, which to be converted
	 * @return, converted short data
	 */
	public static short byteToShort(byte[] b) {
		short s = 0;
		short s0 = (short) (b[0] & 0xff);
		short s1 = (short) (b[1] & 0xff);

		s1 <<= 8;
		s = (short) (s0 | s1);

		return s;
	}
	
	/**
	 * convert int to 4 bytes array
	 * @param number, which to be converted
	 * @return converted bytes array
	 */
	public static byte[] intToByte(int number) {
        int temp = number;
        int len	 = 4;
        byte[] b = new byte[len];
        
        for (int i = 0; i < 4; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();	//save the last byte
            temp = temp >> 8; 		// 
        }
        return b;
    }
	
	/**
	 * convert 4 bytes to int
	 * @param b, which to be converted
	 * @return, converted int data
	 */
	public static int byteToInt(byte[] b){
		int s =0;
		int s0 = b[0]&0xff;
		int s1 = b[1]&0xff;
		int s2 = b[2]&0xff;
		int s3 = b[3]&0xff;

		s3 <<=24;
		s2 <<=16;
		s1 <<=8;
		s = s0 | s1 | s2 | s3;

		return s;
	}
    
	/**
	 * convert float to 4 bytes array
	 * @param number, which to be converted
	 * @return converted bytes array
	 */
    public static byte[] floatToByte(float number) {  
        byte[] b = new byte[4];  
        int tmp = Float.floatToIntBits(number);  
        for (int i = 0; i < 4; i++) {  
            b[i] = new Integer(tmp).byteValue();  
            tmp = tmp >> 8;  
        }  
        
        return b;
    }
    
    public static long ipToLong(String strIp) {
        long[] ip = new long[4];
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1+1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2+1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3+1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] <<  8 ) + ip[3];
    }
   
    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>>  8 ));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }
    
    public static String longToIP2(long longIp) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((longIp & 0x000000FF)));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>>  8 ));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIp >>> 24)));
        return sb.toString();
    }
    
    /**
     * Multiply function, replace '*' with '<<', as  shifting bits more efficiency than multiply
     * @param data
     * @param ratio
     * @return result of multiply
     */
	public static int multiply(int data, int ratio) {
		int ret;

		switch (ratio) {
		case 0:
			ret = 0;
			break;
		case 1:
			ret = data; // no changes
			break;
		case 2:
			ret = data << 1; // == 'data * 2';
			break;
		case 3:
			ret = (data << 1) + data; // == 'data * 3'
			break;
		case 4:
			ret = data << 2; // == 'data * 4'
			break;
		case 5:
			ret = (data << 2) + data; // == 'data * 5'
			break;
		case 6:
			ret = (data << 2) + (data << 1); // == 'data * 6'
			break;
		case 7:
			ret = (data << 2) + (data << 1) + data; // == 'data * 7'
			break;
		case 8:
			ret = data << 3; // == 'data * 8'
			break;
		case 9:
			ret = (data << 3) + data; // == 'data * 9'
			break;
		case 10:
			ret = (data << 3) + (data << 1); // == 'data * 10'
			break;
		default:
			ret = data;
			Log.w(BagelPlayUtil.TAG, "Unsupport ratio: " + ratio);
			break;
		}

		return ret;
	}
	
	/**
     * Get sub bytes of bytes
     * @param b parent bytes
     * @param start start position of sub bytes in parent bytes
     * @param end end position of sub bytes in parent bytes
     * @return sub bytes
     */
    public static byte[] subBytes(byte[] b, int start, int end) {	
    	if (start < 0) start = 0;
    	
    	if (end >= b.length) {
    		end = b.length - 1;
    	}
    	
    	if (start > end) return null;
    	
    	byte[] sb = new byte[end - start + 1];
    	
    	if (end < b.length) {
    		for (int i = start, j = 0; i <= end; i++, j++) {
    			sb[j] = b[i];
    		}
    	}
    	
    	return sb;
    }  

	public static boolean ping(String host) {
		int timeOut = 3000; // I recommend 3 seconds at least
		
		if (host == null) return false;
		
		try {
			boolean status = InetAddress.getByName(host).isReachable(timeOut);
			
			if (!status) {
				Log.w(BagelPlayUtil.TAG, "Host " + host + " can not reachable");
			}
			return status;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean isValidIP(String ipAddress) {
		String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}
	
	public static void errorInfoShow(final Context context, int msg) {
		if (mErrorInfo == null) {		
			mErrorInfo = Toast.makeText(context.getApplicationContext(), "", 
					Toast.LENGTH_SHORT);
			//return;
		}
		if (msg == 0)	return;
		
 		mErrorInfo.setText(msg);
 		mErrorInfo.show(); 		
 	}
	
	public static boolean isTaskRunning(Context context, String PackageName) {
		boolean isRunning = false;
		
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int getTastCnt = 1000;
		List<ActivityManager.RunningTaskInfo> mRunningTask = mActivityManager.getRunningTasks(getTastCnt);
		
		for (ActivityManager.RunningTaskInfo task : mRunningTask) {
			if (0 == task.baseActivity.getPackageName().compareTo(PackageName)) {
				isRunning = true;
				break;
			}
		}
		
		return isRunning;
	}
	
	public static String getMacAddressStr(Context context)
	{
		WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wifi.getConnectionInfo();  
        return info.getMacAddress();  
	}
	
	public static long getMacAddressLong(Context context)
	{
		String macStr	=	getMacAddressStr(context);
		String[] digs	=	macStr.split(":");
		long macLong		=	0;
		int leng		=	digs.length;
		for(int i=0;i<digs.length;i++)
		{
			macLong	|=	(long)Integer.parseInt(digs[i], 16) << ((leng - 1 - i) * 8);
			
		}
		return macLong;
	}
	
	public static boolean isServiceExist(Context context,Intent intent)
	{
		final PackageManager packageManager = context.getPackageManager();
		packageManager.queryIntentServices(intent, PackageManager.GET_SERVICES);
	    List<ResolveInfo> list = packageManager.queryIntentServices(intent,PackageManager.GET_SERVICES);
	    return list.size() > 0;
	}
	
	public static String inStreamToStr(InputStream is) throws Exception
	{
		byte[] data	=	inStreamToBytes(is);
 		return new String(data);
		
	}
	
	public static byte[] inStreamToBytes(InputStream is) throws Exception
	{
		ByteArrayOutputStream baos	=	new ByteArrayOutputStream();
		byte[] buf	=	new byte[1024];
		while(true)
		{
			int len	=	is.read(buf);
			if(len == -1)
				break;
			baos.write(buf,0,len);
		}
		byte[] data	=	baos.toByteArray();
		baos.close();
		return data;
		
	}
	
	public static String getVersionCode(Context context)
	{
 		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode + "";
		} catch (NameNotFoundException e) {
 			e.printStackTrace();
		}  
 		return "";
	}
}
