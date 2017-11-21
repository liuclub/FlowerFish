package com.bagelplay.controller.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.bagelplay.controller.utils.Log;

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
	
	public void writeBytes(byte[] datas)
	{
		
		checkforReAllocate(4 + datas.length);
 		bb.putInt(datas.length);
 		bb.put(datas);
	}
	
	public void writeBytes(byte[] datas,int len)
	{
		checkforReAllocate(4 + len);
 		bb.putInt(len);
 		bb.put(datas, 0, len);
	}
	
	public void writeBytes(byte[] datas,int index,int len)
	{
		checkforReAllocate(4 + len);
 		bb.putInt(len);
 		bb.put(datas, index, len);
	}
	
	public void writeBoolean(boolean b)
	{
		checkforReAllocate(1);
		bb.put(b ? (byte)1 : (byte)0);
	}
	
	public void writeStr(String str)
	{
		writeBytes(str.getBytes());
	}
	
	public byte[] getBytes()
	{
		int len	=	bb.position();
		byte[] data	=	new byte[len];
		bb.position(0);
		bb.get(data);
	 
		return data;
	}
	
	private void checkforReAllocate(int leng)
	{
 		if(bb.remaining() < leng)
		{
 			Log.v("=-------------checkforReAllocate-----------------=", leng + " " + size + " " + bb.position());
			size	+=	leng > 1024 ? leng : 1024;
			
			byte[] data	=	getBytes();
			ByteBuffer tbb	=	ByteBuffer.allocate(size);
			tbb.order(endian);
			tbb.put(data,0,data.length);
			
 			bb	=	tbb;
		}
	}
	 
}
