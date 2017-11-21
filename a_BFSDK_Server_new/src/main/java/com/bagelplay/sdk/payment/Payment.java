package com.bagelplay.sdk.payment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.SocketManager;
import com.bagelplay.sdk.common.SocketStub;
import com.bagelplay.sdk.common.OnShowPlayerLinstener;
import com.bagelplay.sdk.common.util.HttpP;
import com.bagelplay.sdk.common.util.Utils;
import com.bagelplay.sdk.common.util.WebHost;

public class Payment{

	protected static final String PAY_VERSION				=	"1.0";

	public static final String PAY_PLATFORM_AIBEI			=	"com.bagelplay.pay.aibei.service"; //爱贝

	public static final String PAY_PLATFORM_ZSY				=	"com.bagelplay.pay.zsy.service";	//中手游

	public static final String PAY_PLATFORM_LETV			=	"com.bagelplay.pay.letv.service";	//乐视

	private SessionAliveCheck sac;

	private long lastSessionAliveTime;

	private boolean finish	=	true;

	private OnPaymentListener opl;

	private String token;

	private String platform;

	private String paramJson;

	private Handler handler;

	private SDKManager bfsdkm;

	private OnShowPlayerLinstener ospl	=	new OnShowPlayerLinstener()
	{
		@Override
		public void OnGetPlayerIDs(int[] playerIDs, int num) {
			bfsdkm.removeOnShowPlayerLinstener(ospl);
			if(num == 0)
			{
				opl.OnPaymen(getErrorJson(1));
			}
			else
			{
				bfsdkm.getSocketStub().sendPaymentData(PAY_VERSION,platform,token,paramJson);
				sac				=	new SessionAliveCheck();
				sac.start();
			}
		}

	};

	public Payment(SDKManager bfsdkm)
	{
		this.bfsdkm	=	bfsdkm;
		handler		=	new Handler(bfsdkm.getApplicationContext().getMainLooper());
	}

	public void start(String platform,OnPaymentListener opl,String paramJson)
	{
		this.platform	=	platform;
		this.opl		=	opl;
		this.paramJson	=	paramJson;
		token			=	Utils.getUnique(bfsdkm.getApplicationContext());
		bfsdkm.setOnShowPlayerLinstener(ospl);
		bfsdkm.wantPlayerIDs();
	}

	public void resetHeart(String token)
	{
		if(token.equals(this.token))
			lastSessionAliveTime	=	System.currentTimeMillis();
	}

	public void result(String token,final String resultJson)
	{
		if(!token.equals(this.token))
			return;
		finish	=	true;
		handler.post(new Runnable(){
			@Override
			public void run()
			{
				opl.OnPaymen(resultJson);
			}
		});
		sendLog(resultJson);
	}

	String getToken()
	{
		return token;
	}

	private void sendLog(final String resultJson)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					JSONObject jo	=	new JSONObject(resultJson);
					if(jo.getInt("status") == 0)
					{
						jo.put("platform", platform);
						jo.put("token", token);
						jo.put("packangeName", bfsdkm.getApplicationContext().getPackageName());
						jo.put("macAddr", Utils.getMacAddressStr(bfsdkm.getApplicationContext()));
						jo.put("tvDeviceModel", Build.BRAND + " " + Build.MODEL);

						HttpP hp	=	new HttpP(WebHost.PAYMENT_URL);
						hp.appendValue("json", jo.toString());
						hp.post();
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();

	}

	private String getErrorJson(int error)
	{
		JSONObject jo	=	new JSONObject();
		try {
			jo.put("status", error);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
	}

	class SessionAliveCheck extends Thread
	{
		@Override
		public void run()
		{
			finish	=	false;
			lastSessionAliveTime	=	System.currentTimeMillis();
			while(!finish)
			{
				long t	=	System.currentTimeMillis();
				if(t - lastSessionAliveTime > 6000)
				{
					handler.post(new Runnable(){
						@Override
						public void run()
						{
							opl.OnPaymen(getErrorJson(2));
						}
					});

					finish	=	true;
					return;
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}



}
