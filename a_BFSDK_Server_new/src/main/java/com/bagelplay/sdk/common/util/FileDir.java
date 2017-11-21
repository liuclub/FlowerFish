package com.bagelplay.sdk.common.util;

import android.content.Context;

public class FileDir 
{
	private static FileDir fd;
	
	public static final String ASSETS_DIR		=	"bagelplay/";

	public static final String SERVER_NAME		=	"bagelplay_server";
	
	public final String SERVER_FILE;
	
	public final String ID				=	ASSETS_DIR + "id";
		
	public final String LOGO			=	ASSETS_DIR + "logo.jpg";
	
	public final String HOWTOPLAY_PHONE	=	ASSETS_DIR + "howtoplay_phone.jpg";
	
	public final String HOWTOPLAY_GHANDLE	=	ASSETS_DIR + "howtoplay_ghandle.jpg";
	
	public final String HOWTOPLAY_RCONTROLLER	=	ASSETS_DIR + "howtoplay_rcontroller.jpg";
	
	public final String PHYSIC_GHANDLE_JSON	=	ASSETS_DIR + "gHandler.json";
	
	public final String RCONTROL_JSON	=	ASSETS_DIR + "rControl.json";
	
	public final String MOUSE			=	ASSETS_DIR + "pointer_arrow.png";
		
	public final String CONFIG_JSON	=	ASSETS_DIR + "config.json";
	
	public final String EXTRAL				=	ASSETS_DIR + "extral";
	
	public final String YOUMENG				=	ASSETS_DIR + "youmeng";
	
	public final String COUNT_JSON;
	
	public static final String LOCATERCONTROL_DIR	=	ASSETS_DIR + "locatercontrol/";
	
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
		SERVER_FILE	=	"/data/data/" + context.getPackageName() + "/" + SERVER_NAME;
		COUNT_JSON	=	"/data/data/" + context.getPackageName() + "/bagelplaycount.json";
		
	}
}
