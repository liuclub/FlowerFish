package com.bagelplay.sdk;
import com.bagelplay.sdk.OnReceiveListener;

interface SDKDataHandler
{
	boolean register(in String packageName,in OnReceiveListener orl); 
	void unregister(in String packageName);
 	void send(in byte[] data);
 }