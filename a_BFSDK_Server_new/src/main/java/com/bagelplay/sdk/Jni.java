package com.bagelplay.sdk;

public class Jni {
	static{
		System.loadLibrary("com_bagelplay_sdk_Jni");//载入本地库
	}
	
	/*public static native void launch(String packageName);
	
	public static native int isLaunch();
	
	public static native void stop();*/

	//public static native byte[] askLocalVMARun();

	public static native byte[][] findSticksFromLocal(String[] sticks);
}
