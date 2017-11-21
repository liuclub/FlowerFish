package com.bagelplay.sdk.common.ghandle;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.util.FileDir;
import com.bagelplay.sdk.common.util.Utils;

public class JsonParser {
	
	private GHandle gHandler;
	
	private SDKManager bfusm;
	
	private JSONObject jo;
	
	public JsonParser(GHandle gHandler,SDKManager bfusm)
	{
		this.gHandler	=	gHandler;
		this.bfusm		=	bfusm;
	}
	
	public void parse()
	{
		String jStr	=	null;
		try
		{
			InputStream is	=	bfusm.getApplicationContext().getAssets().open(FileDir.getInstance().PHYSIC_GHANDLE_JSON);
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
		
		parseLeftStick();
		parseRightStick();
		parseKeyClick();
		parseKeySlide();
		parseDevScreen();
		gHandler.resetByScreenRate();
		gHandler.simulateTouch();
 	}
	
	private void parseLeftStick()
	{
		try
		{
			boolean leftStickAsSensor	=	!this.jo.has("leftStickAsSensor") ? false : this.jo.getBoolean("leftStickAsSensor");
 			gHandler.setLeftStickAsSensor(leftStickAsSensor);
 			if(!leftStickAsSensor)
 			{
 				JSONObject jo	=	this.jo.getJSONObject("leftStick");
 				int centerX		=	jo.getInt("centerX");
 				int centerY		=	jo.getInt("centerY");
 				int radius		=	jo.getInt("radius");
 				gHandler.setStickLeftCustomized(radius,centerX,centerY);
 			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void parseRightStick()
	{
		try
		{
			boolean rightStickAsMouse	=	!this.jo.has("rightStickAsMouse") ? false : this.jo.getBoolean("rightStickAsMouse");
 			gHandler.setRightStickAsMouse(rightStickAsMouse);
 			
 			boolean alwaysRightStickAsMouse	=	!this.jo.has("alwaysRightStickAsMouse") ? false : this.jo.getBoolean("alwaysRightStickAsMouse");
  			gHandler.setAlwaysRightStickAsMouse(alwaysRightStickAsMouse);
 			
 			
			JSONObject jo	=	this.jo.getJSONObject("rightStick");
			int centerX		=	jo.getInt("centerX");
			int centerY		=	jo.getInt("centerY");
			int radius		=	jo.getInt("radius");
 			gHandler.setStickRightCustomized(radius,centerX,centerY);
 			
 			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void parseKeyClick()
	{
		try
		{
			JSONArray ja	=	this.jo.getJSONArray("key_click");
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
				gHandler.addKeyScreenCustomized(keyCode, x, y,absolute ? 0 : 1);				
			}
 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void parseKeySlide()
	{
		try
		{
			JSONArray ja	=	this.jo.getJSONArray("key_slide");
 			for(int i=0;i<ja.length();i++)
			{
 				JSONObject jo	=	ja.getJSONObject(i);
				String name		=	jo.getString("name");
				int x			=	jo.getInt("x");
				int y			=	jo.getInt("y");
				boolean absolute	=	!jo.has("absolute") ? false : jo.getBoolean("absolute");
				String slideStr	=	jo.getString("slide");
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
				gHandler.addKeySlideCustomized(keyCode, x, y,absolute ? 0 : 1,slide);
			}
 
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
			gHandler.setDevScreenWH(screenWidth, screenHeight);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
