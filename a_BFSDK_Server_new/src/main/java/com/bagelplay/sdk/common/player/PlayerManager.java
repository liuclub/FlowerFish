package com.bagelplay.sdk.common.player;

import com.bagelplay.sdk.common.SDKManager;

public class PlayerManager {
	
	private static PlayerManager pm;
	
	private OnPlayerState ops;
	
	private int[] playerIDs;
	
	private SDKManager bfusm;
		
	private PlayerManager(SDKManager bfusm)
	{
		this.bfusm	=	bfusm;
	}
	
	public static void init(SDKManager bfusm)
	{
		if(pm == null)
			pm	=	new PlayerManager(bfusm);
 	}
 
	public static PlayerManager getInstance()
	{
		return pm;
	}
	
	public void setOnPlayerState(OnPlayerState ops)
	{
		this.ops	=	ops;
	}
	
	public OnPlayerState getOnPlayerState() 
	{
		return ops;
	}
	
	public void destroy()
	{
		ops	=	null;
		playerIDs	=	new int[0];
	}
	
	public void setPlayerIDs(int[] playerIDs)
	{
		this.playerIDs	=	playerIDs;
	}
	
	public int[] getPlayerIDs()
	{
		playerIDs	=	null;
		bfusm.getSocketStub().sendWantPlayerIdData2();
 		while(playerIDs == null)
		{
			
		}
		return playerIDs;
	}
	
	public int getPlayerNum()
	{
		int[] ids	=	getPlayerIDs();
		return ids.length;
	}
	
}
