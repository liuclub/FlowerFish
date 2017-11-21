package com.bagelplay.sdk.common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Random;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Utils {
	
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

	public static String getMacAddressStr(Context context)
	{
		WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wifi.getConnectionInfo();  
        return info.getMacAddress();  
	}
	
	public static String getUnique(Context context)
	{
		String macAddr	=	getMacAddressStr(context);
		String pName	=	context.getPackageName();
		String unique	=	macAddr + pName + System.currentTimeMillis() + new Random().nextInt(10000);
		return Md5.md5Str(unique);
	}
	
	public static String getGoogleAccount(Context context)
	{
		AccountManager accountManager = AccountManager.get(context);  
		Account[] accounts = accountManager.getAccountsByType("com.google");  
		return accounts == null || accounts.length == 0 ? "" : accounts[0].name;
	}
	
	public static float formatFloat(float f,int num)
	{
		BigDecimal b   =   new BigDecimal(f); 
		return b.setScale(num,BigDecimal.ROUND_FLOOR).floatValue();  
 	}
	
}
