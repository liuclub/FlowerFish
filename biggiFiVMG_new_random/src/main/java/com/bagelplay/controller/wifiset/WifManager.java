package com.bagelplay.controller.wifiset;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.bagelplay.controller.utils.BagelPlayUtil;
import com.bagelplay.controller.utils.Log;

public class WifManager {
	
	private static WifManager wfm;
	
	private Context context;
	
	private WifiManager wm;
	
	private List<BroadcastReceiver> brStubs;
	
	private WifManager(Context context)
	{
		this.context	=	context;
		wm	=	(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		brStubs	=	new ArrayList<BroadcastReceiver>();
	}
	
	public static WifManager initInstance(Context context)
	{
		if(wfm == null)
			wfm	=	new WifManager(context);
		return wfm;
	}
	
	public static WifManager getInstance()
	{
		return wfm;
	}
	
	public void release()
	{
		for(BroadcastReceiver br : brStubs)
		{
			context.unregisterReceiver(br);
		}
		wfm	=	null;
	}
	
	public List<ScanResult> scanHots()
	{
		wifiOpen();
		Timeout t	=	new Timeout(10 * 1000); 
		final Finish f	=	new Finish();
		BroadcastReceiver br	=	new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				f.finish	=	true;
			}
		};
		registerBroadcastReceiver(br,WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		wm.startScan();
		while(!f.finish)
		{
			if(t.isTimeout())
			{
				f.finish	=	true;
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
 				e.printStackTrace();
			}
		}
		context.unregisterReceiver(br);
		List<ScanResult> srs	=	wm.getScanResults();
		return srs == null ? new ArrayList<ScanResult>() : srs;
	}
	
	public List<ScanResult> scanHots(float second)
	{
		wifiOpen();
		 
		wm.startScan();
		try {
			Thread.sleep((long)(second *  1000));
		} catch (InterruptedException e) {
				e.printStackTrace();
		}
		List<ScanResult> srs	=	wm.getScanResults();
		return srs == null ? new ArrayList<ScanResult>() : srs;
	}
	
	public boolean wifiOpen()
	{
		if(wm.isWifiEnabled())
			return true;
		hotClose();
		wm.setWifiEnabled(true);
		while(!wm.isWifiEnabled())
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
 				e.printStackTrace();
			}
		}
 		return true;
	}
	
	public boolean wifiClose()
	{
		if(wm.getWifiState() == WifiManager.WIFI_STATE_DISABLED)
			return true;
		wm.setWifiEnabled(false);
		while(wm.getWifiState() != WifiManager.WIFI_STATE_DISABLED)
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
 				e.printStackTrace();
			}
		}
		return true;
	}
	
	
	public boolean hotClose()
	{
		try
		{
			Method getWifiApState	=	wm.getClass().getMethod("getWifiApState");
			Method setWifiApEnabled	=	wm.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class,Boolean.TYPE);
			Field WIFI_AP_STATE_DISABLED	=	wm.getClass().getField("WIFI_AP_STATE_DISABLED");
			if((Integer)getWifiApState.invoke(wm) == WIFI_AP_STATE_DISABLED.getInt(null))
			{
				return true;
			}
			
			setWifiApEnabled.invoke(wm, null, false);
			
			while((Integer)getWifiApState.invoke(wm) != WIFI_AP_STATE_DISABLED.getInt(null))
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
	 				e.printStackTrace();
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public int connectToHot(String ssid, String password, String type)
	{
		wifiOpen();
		final Finish f	=	new Finish();
		WifiConfiguration wifiConfiguration	=	getWifiConfiguration(ssid,password,type);
		List<WifiConfiguration> wcs	=	wm.getConfiguredNetworks();
		for(WifiConfiguration wc : wcs)
		{
 			wm.disableNetwork(wc.networkId);
			if(wc.SSID.equals(wifiConfiguration.SSID))
			{				
				wm.removeNetwork(wc.networkId);
			}
		}
		int netId	=	wm.addNetwork(wifiConfiguration);
 		wm.enableNetwork(netId, true);
		boolean isConnected	=	false;
		boolean canCheck	=	false;
		Timeout t	=	new Timeout(30 * 1000);
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	 
		SupplicantState ss	=	null;
		NetworkInfo.DetailedState ds	=	null;
		android.net.NetworkInfo.State wifi	=	null;
		WifiInfo wi	=	null;
 		while(!f.finish)
		{
  			if(t.isTimeout())
 			{
  				Log.v("=-------------------connectToHot------------------", ssid + " time out");
 				f.finish	=	true;
 				f.res	=	1;
 				break;
 			}
  			wi	=	wm.getConnectionInfo();
			ss	=	wi.getSupplicantState();
			ds =	WifiInfo.getDetailedStateOf(ss);
			wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			//Log.v("#####################################", canCheck + " " + ss + " " + ds + " " + wi.getDetailedStateOf(ss) + " " + wi.getIpAddress() + " " + wifi);
			if(canCheck && ss == SupplicantState.COMPLETED)
			{
				//if(wi.getIpAddress() == 0)
					isConnected	=	true;
				if(isConnected && wi.getIpAddress() != 0 && wifi == State.CONNECTED)
				{
					f.finish	=	true;
					f.res	=	0;
				}
					
			}
			else if(ds == DetailedState.CONNECTING || ds == DetailedState.OBTAINING_IPADDR || ds == DetailedState.AUTHENTICATING)
			{
				canCheck	=	true;
			}
			else if(canCheck && (ss == SupplicantState.DISCONNECTED || ss == SupplicantState.INACTIVE))
			{
				f.finish	=	true;
				f.res	=	2;
			}
			 
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
 				e.printStackTrace();
			}
 		}
 		
 		
 		if(wifi == State.CONNECTED && wi.getIpAddress() != 0 && ssid.equals(wi.getSSID()))
 		{
			return 0;
 		}
 		
 		if(f.res == 2 || f.res == 1)
 		{
 			for(int i=0;i<2;i++)
 			{
 				List<ScanResult> srs	=	scanHots();
 	 			for(ScanResult sr : srs)
 	 			{
 	 				if(sr.SSID.equals(ssid))
 	 				{
 	 					f.res = 2;
 	 					return f.res;
 	 				}
 	 			}
 			}	
 			
 			f.res	=	1;
 		}
 	
 		return f.res;
	}
	
 
	
	public String getCurrentSsid()
	{
		WifiInfo wi	=	wm.getConnectionInfo();
		if(wi == null)
			return null;
		return wi.getSSID() == null ? null : wi.getSSID().replace("\"", "");
	}
	
	public String getCurrentIP()
	{
		WifiInfo wi	=	wm.getConnectionInfo();
		if(wi != null && wi.getIpAddress() != 0)
			return BagelPlayUtil.longToIP2(wi.getIpAddress());
		return null;
	}
	
	public String getHotIP()
    {
		DhcpInfo info=wm.getDhcpInfo();
   	 	int hopIP	=	info.serverAddress;
   	 	return Utils.ipLong2String(hopIP);
    }
	
	public boolean isNeedPassword(ScanResult sr)
	{
		String cap	=	sr.capabilities.toLowerCase();
 		return cap.contains("wep") || cap.contains("psk");
	}
	
	private void registerBroadcastReceiver(BroadcastReceiver br,String... Actions)
	{
		IntentFilter iFilter	=	new IntentFilter();
		for(int i=0;i<Actions.length;i++)
		{
			iFilter.addAction(Actions[i]);
		}
		context.registerReceiver(br, iFilter);
	}
	
	private WifiConfiguration getWifiConfiguration(String ssid, String Password, String type)
    {
 		WifiConfiguration config = new WifiConfiguration();  
        
        config.SSID = "\"" + ssid + "\"";  
        if(type == null || type.trim().equals(""))
        {
         	config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
         }else
         if(type.toLowerCase().contains("wep"))
        {
        	 config.allowedKeyManagement.set(KeyMgmt.NONE);
             config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
             config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
              
             
             if (Password != null && Password.length() != 0) {
                 int length = Password.length();
                 String password = Password;
                 // WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
                 if ((length == 10 || length == 26 || length == 58) &&
                         password.matches("[0-9A-Fa-f]*")) {
                     config.wepKeys[0] = password;
                 } else {
                     config.wepKeys[0] = '"' + password + '"';
                 }
             }
     }else
     if(type.toLowerCase().contains("psk"))
     {
    	 config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
         if (Password != null && Password.length() != 0) {
             String password = Password;
             if (password.matches("[0-9A-Fa-f]{64}")) {
                 config.preSharedKey = password;
             } else {
                 config.preSharedKey = '"' + password + '"';
             }
         }
  	   
  	    
     }else
     {
    	 config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
     }
     
     return config;
    }
	
	
	class Finish
	{
		public boolean finish;
		
		public boolean result;
		
		public int res;
	}
	
	class Timeout extends Thread
	{
		private boolean isTimeout;
		
		private int timeout;
		
		public Timeout(int timeout)
		{
			this.timeout	=	timeout;
			start();
		}
		
		@Override
		public void run()
		{
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
 				e.printStackTrace();
			}
			isTimeout	=	true;
		}
		
		public boolean isTimeout()
		{
			return isTimeout;
		}
	}
	
}
