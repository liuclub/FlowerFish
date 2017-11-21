package com.bagelplay.controller.wifiset;


public class StickInfo {
	
	public int VMA_SCRSHOTSC_PORT	=	17891;		// receive the screen shot
	
	public int VMA_CMD_PORT 		= 17892; 		// mouse atcion, sensor and so on
	
	public int VMA_CMD_APP_INFO		=	17893;
	
	public String name;
	
	public String ip;
	
	public boolean isHot;
	
	public String hotType;
	
	public int level	=	1;
	
	public StickInfo(String name,String ip,boolean isHot,String hotType)
	{
		this.name	=	name;
		this.ip		=	ip;
		this.isHot	=	isHot;
		this.hotType	=	hotType;
	}
	
	public StickInfo(String name,String ip,int VMA_SCRSHOTSC_PORT,int VMA_CMD_PORT,int level)
	{
		this.name	=	name;
		this.ip		=	ip;
		this.isHot	=	false;
		this.VMA_SCRSHOTSC_PORT	=	VMA_SCRSHOTSC_PORT;
		this.VMA_CMD_PORT	=	VMA_CMD_PORT;
		this.level		=	level;
 	}
	
	@Override
	public boolean equals(Object o) {
		StickInfo si	=	(StickInfo)o;
		return name.equals(si.name) && ip != null && si.ip != null && ip.equals(si.ip);
	}
}
