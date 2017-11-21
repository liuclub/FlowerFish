package com.bagelplay.sdk.common.remoteControl;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.util.FileDir;
import com.bagelplay.sdk.common.util.Utils;

public class JsonParser {
	
	private RControl rControl;
	
	private SDKManager bfusm;
	
	private JSONObject jo;
	
	public JsonParser(RControl rControl,SDKManager bfusm)
	{
		this.rControl	=	rControl;
		this.bfusm		=	bfusm;
	}
	
	public void parse()
	{
		String jStr	=	null;
		try
		{
			InputStream is	=	bfusm.getApplicationContext().getAssets().open(FileDir.getInstance().RCONTROL_JSON);
			jStr	=	Utils.inStreamToStr(is);
			is.close();
		}catch(Exception e){
			e.printStackTrace();
			return;
		} 
		
		try {
			jo	=	new JSONObject(jStr);
		} catch (Exception e) {
 			e.printStackTrace();
		}
		
		parseKeySlide();
		parseKeyClick();
		parseDevScreen();
		parseAsMouse();
		rControl.resetByScreenRate();
 	}
	
	 
	private void parseKeySlide()
	{
		try
		{
			JSONArray ja	=	this.jo.getJSONArray("key_slide");
			rControl.simulateTouch();
			for(int i=0;i<ja.length();i++)
			{
				JSONObject jo	=	ja.getJSONObject(i);
				String name		=	jo.getString("name");
				int x			=	jo.getInt("x");
				int y			=	jo.getInt("y");
				boolean absolute	=	!jo.has("absolute") ? false : jo.getBoolean("absolute");
				String slideStr			=	jo.getString("slide");
				int keyCode		=	KeyConst.translateKeyCodefromKeyCodeStr(name);
				if(keyCode == -1)
					return;
				int slide		=	0;
				if("up".equals(slideStr))
					slide	=	1;
				else if("down".equals(slideStr))
					slide	=	2;
				else if("left".equals(slideStr))
					slide	=	3;
				else if("right".equals(slideStr))
					slide	=	4;
				rControl.addKeyScreenCustomizedSlide(keyCode, x, y,absolute ? 0 : 1,slide);			
			}
 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void parseKeyClick()
	{
		try
		{
			JSONArray ja	=	this.jo.getJSONArray("key_click");
			rControl.simulateTouch();
			for(int i=0;i<ja.length();i++)
			{
				JSONObject jo	=	ja.getJSONObject(i);
				String name		=	jo.getString("name");
				int x			=	jo.getInt("x");
				int y			=	jo.getInt("y");
				boolean absolute	=	!jo.has("absolute") ? false : jo.getBoolean("absolute");
 				int keyCode		=	KeyConst.translateKeyCodefromKeyCodeStr(name);
				if(keyCode == -1)
					return;
				rControl.addKeyScreenCustomizedClick(keyCode, x, y,absolute ? 0 : 1);			
			}
 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void parseAsMouse()
	{
		try
		{
 			JSONObject jo	=	this.jo.getJSONObject("asMouse");
 			rControl.setMouseSpeed(jo.getInt("speed"));
 			rControl.setMouseASpeed(jo.getInt("aSpeed"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void parseDevScreen()
	{
		try
		{
			int screenWidth		=	jo.getInt("devScreenWidth");
			int screenHeight	=	jo.getInt("devScreenHeight");
			rControl.setDevScreenWH(screenWidth, screenHeight);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
