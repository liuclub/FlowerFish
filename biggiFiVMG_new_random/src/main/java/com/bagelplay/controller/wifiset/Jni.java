package com.bagelplay.controller.wifiset;

public class Jni {
	
	static{
		System.loadLibrary("com_bagelplay_controller_wifiset_Jni");//载入本地库
		}
	
	public static native byte[][] findSticksFromWifi(String[] sticks);
	public static native byte[] getCurrentIP();
	public static native void stop();
}
