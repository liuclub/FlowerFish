package com.bagelplay.sdk.common;

import java.util.Hashtable;

public class Voice {
	
	private SDKManager bfsdkm;
	
	private OnVoiceListener ovl;
	
	private Hashtable<Integer,OnVoiceResultLinstener> ovrlHT	=	new Hashtable<Integer,OnVoiceResultLinstener>();
	
	private int ovrlHTID;
	
	public Voice(SDKManager bfsdkm)
	{
		this.bfsdkm	=	bfsdkm;
	}
	
	public void setOnVoiceListener(OnVoiceListener ovl)
	{
		this.ovl	=	ovl;
	}
	
	public void doVoice(byte[] data,int playerID)
	{
		if(ovl != null)
			ovl.OnVoice(data,playerID);
		//else
			//bfsdkm.getSocketThirdStub().sendSensorData(x, y, z,playerID);
	}
	
	public void checkVoice(String right,OnVoiceResultLinstener ovrl)
	{
		ovrlHTID++;
 		ovrlHT.put(ovrlHTID, ovrl);
		bfsdkm.getSocketStub().sendVoiceCheck(ovrlHTID, right);
	}
	
	public void removeVoiceResultLinstener(int id)
	{
		ovrlHT.remove(id);
	}
	
	public void doVoiceResultLinstener(int id,String right,String result)
	{
		OnVoiceResultLinstener ovrl	=	ovrlHT.get(id);
		if(ovrl != null)
		{
			ovrl.OnVoiceResult(right, result);
			ovrlHT.remove(id);
		}
	}
	
	
}
