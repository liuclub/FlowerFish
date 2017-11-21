package com.bagelplay.sdk.common.remoteControl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.KeyEvent;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.util.FileDir;
import com.bagelplay.sdk.common.util.Log;
import com.bagelplay.sdk.common.util.Utils;


public class RControl2 {

	private static String TAG	=	"RControl2";
	
	protected SDKManager bfusm;
	
	private int[] devScreenWH	=	new int[2];
		
	private List<LocateButton> lbs	=	new ArrayList<LocateButton>();
	
	private LocateButton currentLb;
	
	private boolean enable;
		
	public RControl2(SDKManager bfusm)
	{
		this.bfusm	=	bfusm;
	}
	
	public void setJson(String fileName)
	{
		if(parseJson(fileName))
		{
			resetByScreenRate();
			currentLb	=	lbs.get(0);
			setMouseXY(-1,currentLb.x,currentLb.y);
			bfusm.getMouse().setAlwaysShow(true);
			enable	=	true;
 		}
		else
		{
			bfusm.getMouse().setAlwaysShow(false);
			bfusm.getMouse().tempHide();
			enable	=	false;
		}
 	}
	
	private boolean parseJson(String fileName)
	{
		try
		{
			InputStream is	=	bfusm.getApplicationContext().getAssets().open(FileDir.getInstance().LOCATERCONTROL_DIR + fileName);
			String jStr	=	Utils.inStreamToStr(is);
			is.close();
			
			JSONObject jo	=	new JSONObject(jStr);
			devScreenWH[0]	=	jo.getInt("devScreenWidth");
			devScreenWH[1]	=	jo.getInt("devScreenHeight");
			
			JSONArray btns	=	jo.getJSONArray("buttons");
			for(int i=0;i<btns.length();i++)
			{
				JSONObject btnJson	=	btns.getJSONObject(i);
				String name	=	btnJson.getString("name");
				int x		=	btnJson.getInt("x");
				int y		=	btnJson.getInt("y");
				LocateButton lb	=	new LocateButton(name,x,y);
				lbs.add(lb);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
 		} 
		return false;
	}
	
	private void resetByScreenRate()
	{
		float[] screenRate	=	new float[2];
		screenRate[0]	=	(bfusm.getScreenWidth() + 0f) / devScreenWH[0]; 
		screenRate[1]	=	(bfusm.getScreenHeight() + 0f) / devScreenWH[1];
		Log.v(TAG, "resetByScreenRate " + "rate x:" + screenRate[0] + " rate y:" + screenRate[1]);
		for(int i=0;i<lbs.size();i++)
		{
			LocateButton lb	=	lbs.get(i);
			lb.x	=	(int)(lb.x * screenRate[0]);
			lb.y	=	(int)(lb.y * screenRate[0]);
		}
	}
	
	public boolean setKeyEvent(int playerOrder,int keyCodePhysics,int action)
	{
		if(!enable)
			return false;
			
		int keyCode	=	KeyConst.translateKeyCodefromPhysicsKey(keyCodePhysics);
		
		Log.v(TAG, "setKeyEvent " + "keyCodePhysics:" + keyCodePhysics + " keyCode:" + keyCode);
		
		if(keyCode == -1)
			return false;
				
		if(action == KeyEvent.ACTION_DOWN)
		{
			LocateButton lb	=	null;
			if(keyCode == KeyConst.KEY_UP)
			{
				lb	=	check_key_up();
			}
			else if(keyCode == KeyConst.KEY_DOWN)
			{
				lb	=	check_key_down();
			}
			else if(keyCode == KeyConst.KEY_LEFT)
			{
				lb	=	check_key_left();
			}
			else if(keyCode == KeyConst.KEY_RIGHT)
			{
				lb	=	check_key_right();
			}
			else if(keyCode == KeyConst.KEY_CENTER)
			{
				bfusm.getMouse().doMouseClick(playerOrder);
			}
			if(lb != null)
			{
				currentLb	=	lb;
				setMouseXY(playerOrder,lb.x,lb.y);
			}
		} 
		return true;
	}
	
	private void setMouseXY(int playerOrder,int x,int y)
	{
		bfusm.getMouse().setMouseXY(playerOrder,x,y);
	}
	
	private LocateButton check_key_up()
	{
		LocateButton tLb	=	null;
		for(int i=0;i<lbs.size();i++)
		{
			LocateButton lb	=	lbs.get(i);
			if(lb == currentLb)
				continue;
			if(tLb == null && lb.y < currentLb.y)
			{
				tLb	=	lb;
			}
			else if(lb.y < currentLb.y && lb.y > tLb.y)
			{
				tLb	=	lb;
			}
			
		}
		return tLb;
	}
	
	private LocateButton check_key_down()
	{
		LocateButton tLb	=	null;
		for(int i=0;i<lbs.size();i++)
		{
			LocateButton lb	=	lbs.get(i);
			if(lb == currentLb)
				continue;
			if(tLb == null && lb.y > currentLb.y)
			{
				tLb	=	lb;
			}
			else if(lb.y > currentLb.y && lb.y < tLb.y)
			{
				tLb	=	lb;
			}
			
		}
		return tLb;
	}
	
	private LocateButton check_key_left()
	{
		LocateButton tLb	=	null;
		for(int i=0;i<lbs.size();i++)
		{
			LocateButton lb	=	lbs.get(i);
			if(lb == currentLb)
				continue;
			if(tLb == null && lb.x < currentLb.x)
			{
				tLb	=	lb;
			}
			else if(lb.x < currentLb.x && lb.x > tLb.x)
			{
				tLb	=	lb;
			}
			
		}
		return tLb;
	}
	
	private LocateButton check_key_right()
	{
		LocateButton tLb	=	null;
		for(int i=0;i<lbs.size();i++)
		{
			LocateButton lb	=	lbs.get(i);
			if(lb == currentLb)
				continue;
			if(tLb == null && lb.x > currentLb.x)
			{
				tLb	=	lb;
			}
			else if(lb.x > currentLb.x && lb.x < tLb.x)
			{
				tLb	=	lb;
			}
			
		}
		return tLb;
	}
	
	class LocateButton
	{
		public String name;
		
		public int x;
		
		public int y;
		
		public LocateButton(String name,int x,int y)
		{
			this.name	=	name;
			this.x		=	x;
			this.y		=	y;
		}
	}
	
}
