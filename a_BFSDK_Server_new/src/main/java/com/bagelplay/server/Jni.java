package com.bagelplay.server;

public class Jni {
	
	static
	{
		System.loadLibrary("com_bagelplay_server_Jni");
	}
	
	public static native void start(String pName,int sw,int sh);
}
