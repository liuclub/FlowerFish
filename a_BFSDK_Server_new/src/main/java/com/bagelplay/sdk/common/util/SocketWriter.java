package com.bagelplay.sdk.common.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SocketWriter {
	
	private int size	=	1024;
	
	private ByteOrder endian;
		
	private ByteBuffer bb;
	
	public SocketWriter(ByteOrder endian)
	{
		this.endian	=	endian;
		bb	=	ByteBuffer.allocate(size);
		bb.order(endian);
	}
	
	public void writeShort(short s)
	{
		checkforReAllocate(2);
		bb.putShort(s);
	}
	
	public void writeInt(int i)
	{
 		checkforReAllocate(4);
		bb.putInt(i);
	}
	
	public void writeFloat(float f)
	{
		checkforReAllocate(4);
		bb.putFloat(f);
	}
	
	public void writeDouble(double d)
	{
		checkforReAllocate(8);
		bb.putDouble(d);
	}
	
	public void writeBytes(byte[] datas)
	{
		checkforReAllocate(4 + datas.length);
 		bb.putInt(datas.length);
 		bb.put(datas);
	}
	
	public void writeBoolean(boolean b)
	{
		checkforReAllocate(1);
		bb.put(b ? (byte)1 : (byte)0);
	}
	
	public byte[] getBytes()
	{
		int len	=	bb.position();
		byte[] data	=	new byte[len];
		bb.position(0);
		bb.get(data);
	 
		return data;
	}
	
	public void writeStr(String str)
	{
		writeBytes(str.getBytes());
	}
	
	private void checkforReAllocate(int leng)
	{
 		if(bb.remaining() < leng)
		{
			size	+=	leng > 1024 ? leng : 1024;
			byte[] data	=	getBytes();
			ByteBuffer tbb	=	ByteBuffer.allocate(size);
			tbb.order(endian);
			tbb.put(data,0,data.length);
			
 			bb	=	tbb;
		}
	}
	 
}
