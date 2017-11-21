package com.bagelplay.controller.wifiset;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

public class StickFinder extends Thread{
	
	private List<StickInfo> sticks	=	new ArrayList<StickInfo>();
	
	private Handler handler	=	new Handler();
	
	private OnAfterFindListener oafs;
	
	private boolean finish;
	
	private String ip;
		
	public StickFinder(List<StickInfo> sticks)
	{
		this.sticks		=	sticks;
	}

	public void startFind(OnAfterFindListener oafs)
	{
		this.oafs	=	oafs;
		finish	=	false;
		start();
	}
	
	public void restartFind(OnAfterFindListener oafs)
	{
		this.ip=null;
		this.oafs	=	oafs;
		finish	=	false;
		start();
	}
	
	public void startFind2(OnAfterFindListener oafs,String ip)
	{
		this.oafs	=	oafs;
		this.ip		=	ip;
		finish	=	false;
		start();
	}
	
	public void run()
	{
		if(ip == null || ip.equals(""))
			findSticksFromRandom();
		else
			try {
				findSticksFromRandomByIP(ip);
			} catch (Exception e) {
 				e.printStackTrace();
			}
	}
	
	public boolean isFinish()
	{
		return finish;
	}
	
	private void findSticksFromRandom()
	{
		List<StickInfo> tSticks			=	Utils.getSticksFromWifiFindRandom(this.sticks);
		if(tSticks != null)
			this.sticks.addAll(tSticks);
		 
		
		handler.post(new Runnable(){
			public void run()
			{
				if(oafs != null)
					oafs.OnAfterFind(sticks);
				finish	=	true;
			}
		});
 	}
	
	private void findSticksFromRandomByIP(String ip) throws Exception
	{
		String str	=	"SCN_RANDOM";
		byte[] send	=	str.getBytes();
		InetAddress addr = InetAddress.getByName(ip);
        int port = 17890;
        DatagramPacket sendPacket = new DatagramPacket(send ,send.length , addr , port);
        DatagramSocket client = new DatagramSocket();
        client.send(sendPacket);
        client.send(sendPacket);
        client.send(sendPacket);
        
        byte[] recvBuf = new byte[500];
        DatagramPacket recvPacket
            = new DatagramPacket(recvBuf , recvBuf.length);
        client.receive(recvPacket);
        
        List<StickInfo> s			=	new ArrayList<StickInfo>();
         
        String stickName	=	new String(recvBuf);
        
        
        
        
		//String[] sns		=	stickName.split("\\|");
		String hostName		=	"";
		int SCRSHOTSC_PORT	=	0;
		int CMD_PORT		=	0;
		int level			=	0;
		int VMA_CMD_APP_INFO	=	0;
			 
		JSONObject jo		=	new JSONObject(stickName);
		hostName		=	jo.getString("HOSTNAME");
		SCRSHOTSC_PORT	=	jo.getInt("SCRSHOTSC_PORT");
		CMD_PORT		=	jo.getInt("CMD_PORT");
		level			=	jo.getInt("LEVEL");
		VMA_CMD_APP_INFO	=	jo.getInt("SDK_APP_INFO_PORT");
		 
			
			
		StickInfo si	=	new StickInfo(hostName, ip,SCRSHOTSC_PORT,CMD_PORT,level);
		si.VMA_CMD_APP_INFO	=	VMA_CMD_APP_INFO;
		s.add(si);
		
		this.sticks.addAll(s);
		
		handler.post(new Runnable(){
			public void run()
			{
				if(oafs != null)
					oafs.OnAfterFind(sticks);
				finish	=	true;
				
				
				
			}
		});
	}
	
	public interface OnAfterFindListener
	{
		public void OnAfterFind(List<StickInfo> sis);
	}
}
