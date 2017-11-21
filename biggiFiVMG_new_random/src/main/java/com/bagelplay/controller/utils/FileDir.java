package com.bagelplay.controller.utils;

import android.content.Context;

public class FileDir 
{
	private static FileDir fd;
	
	public final String COUNT_JSON;
	
	public static FileDir init(Context context)
	{
		if(fd == null)
			fd	=	new FileDir(context);
		return fd;
	}
	
	public static FileDir getInstance()
	{
		return fd;
	}
	
	private FileDir(Context context)
	{
		COUNT_JSON	=	"/data/data/" + context.getPackageName() + "/biggificount.json";
		
	}
}
