package com.bagelplay.sdk.common.util;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SocketReader {
	
	private ByteOrder endian;
	
	private ByteBuffer bb;
	
	public SocketReader(ByteOrder endian)
	{
		this.endian	=	endian;
	}
	
	public void setData(byte[] data)
	{
		bb	=	ByteBuffer.wrap(data);
		bb.order(endian);
	}
	
	public int readInt()
	{
		return bb.getInt();
 	}
	
	public byte[] readBytes()
	{
		int len	=	bb.getInt();
		byte[] data	=	new byte[len];
		bb.get(data);
		return data;
	}
	
	public String readStr()
	{
		byte[] data	=	readBytes();
		return new String(data);
	}
	
	
	public byte readByte(InputStream is) throws Exception
	{
		return (byte)is.read();
	}
	
	public short readShort(InputStream is) throws Exception
	{
		 
			byte[] buf		=	new byte[2];
			is.read(buf);
			ByteBuffer bb	=	ByteBuffer.wrap(buf);
			bb.order(endian);
			short s			=	bb.getShort();
			bb.clear();
			return s;
	}
	
	public int readInt(InputStream is) throws Exception
	{
		 
			byte[] buf		=	new byte[4];
			is.read(buf);
			ByteBuffer bb	=	ByteBuffer.wrap(buf);
			bb.order(endian);
			int i			=	bb.getInt();
			bb.clear();
			return i;
		 
	}
	
	public float readFloat(InputStream is) throws Exception
	{
		 
		byte[] buf		=	new byte[4];
		is.read(buf);
		ByteBuffer bb	=	ByteBuffer.wrap(buf);
		bb.order(endian);
		float f			=	bb.getFloat();
		bb.clear();
		return f;
	}
	
	public byte[] readBytes(InputStream is) throws Exception
	{
		 
			int leng		=	readInt(is);
			byte[] datas	=	new byte[leng];
			
			int per		=	1024;
			int readNum	=	0;
			while(true)
			{
				int needRead	=	leng - readNum >= per ? per : leng - readNum;
				if(needRead <= 0)
					break;
				int actRead	=	is.read(datas, readNum, needRead);
				readNum	+=	actRead;
			}
			
			return datas;
		 
	}
	
	public String readStr(InputStream is) throws Exception
	{
		byte[] datas	=	readBytes(is);
		String str		=	new String(datas);
		return str;
	}
	
	public boolean readBoolean(InputStream is) throws Exception
	{
		return is.read() == 1 ? true : false;
	}
	 
}
