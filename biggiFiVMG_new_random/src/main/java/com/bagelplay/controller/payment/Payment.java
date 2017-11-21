package com.bagelplay.controller.payment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.BagelPlayUtil;
import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.HttpP;
import com.bagelplay.controller.utils.Log;
import com.bagelplay.controller.utils.WebHost;
import com.bagelplay.pay.OnPaymentListener;
import com.bagelplay.pay.PayHandler;


public class Payment {
	
	public static final String VERSION		=	"1.0";
		
	private BagelPlayVmaStub bvs;
	
	private boolean finish	=	true;
	
	private String token;
	
	private String pName;
		
	private Context context;
		
	private String macaddr;
	
	private String deviceModel;
	
	private String googleAccount;
		
	private String argsJson;
	
	private String platform_from_paymentServer;
	
	private PayHandler payH;
	
	public Payment(Context context)
	{
		this.context	=	context;
		bvs	=	BagelPlayVmaStub.getInstance();
	}
	
	public boolean finish()
	{
		return finish;
	}
	
	public void pay(String version,String platform,String token, String pName, String macaddr,String googleAccount,String deviceModel,String argsJson)
	{
		if(!finish)
			return;
		
		if(!Payment.VERSION.equals(version))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(R.string.payversionwrong);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int id) {
							finish	=	true;
							finish(getErrorJson(4));
						}
					});
			builder.show();
			return;
		}

		platform_from_paymentServer	=	platform;
		this.token			=	token;
		this.pName			=	pName;
		this.macaddr		=	macaddr;
		this.deviceModel	=	deviceModel;
		this.googleAccount	=	googleAccount;
		this.argsJson		=	argsJson;
		
		Log.v("-------------------------------", platform_from_paymentServer + " " + token + " " + pName + " " + macaddr + " " + googleAccount);
		Log.v("-------------------------------", argsJson + " ");
		
		
		startHeart();
		
		start();
 	}
	
	private void start()
	{
		doPayment();
 	}
	
	private void startHeart()
	{
		finish		=	false;
		Heart h		=	new Heart();
		h.start();
	}
	
	private void finish(String resultJson)
	{
		bvs.sendPaymentResultData(token, pName,resultJson);
	}
	
	
	private void doPayment()
	{
		Handler handler	=	new Handler(context.getMainLooper());
 		handler.post(new Runnable(){
			@Override
			public void run()
			{
				Intent intent	=	new Intent(platform_from_paymentServer);
				if(BagelPlayUtil.isServiceExist(context, intent))
				{
					context.bindService(intent, sc, Context.BIND_AUTO_CREATE);
				}
				else
				{
					Toast.makeText(context, "please install apk", Toast.LENGTH_LONG).show();
					finish	=	true;
					finish(getErrorJson(3));
					Pay_adk_download pad	=	new Pay_adk_download(context,platform_from_paymentServer.substring(0, platform_from_paymentServer.lastIndexOf(".")));
					pad.download();
				}
				
			}
		});
	}
	
	private void uploadPayLog(final int result,final String reArgsJson)
	{
		Log.v("==---------------------", "uploadPayLog0");
		
		new Thread()
		{
			public void run()
			{
				try
				{		
					Log.v("==---------------------", "uploadPayLog1");
					
					JSONObject jo	=	new JSONObject();
					
					jo.put("pay_result", result);
					
					jo.put("platform", platform_from_paymentServer);
				
					jo.put("token", token);

					jo.put("packangeName", pName);
					
					jo.put("tvMacAddr", macaddr);
			 			
					jo.put("tvDeviceModel", deviceModel);
					
					jo.put("mobileMacAddr", BagelPlayUtil.getMacAddressStr(context)); 
					
					jo.put("mobileDeviceModel", Build.MODEL);
					
					TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
					
					String te1  = tm.getLine1Number();//获取本机号码
					
					jo.put("phoneNumber", te1 == null ? "" : te1);
							
					jo.put("product_info", argsJson);
					
					jo.put("pay_result_info", reArgsJson);
					
					HttpP hp	=	new HttpP(WebHost.PAYMENT_URL);
					hp.appendValue("json", jo.toString());
					hp.post();
					Log.v("==---------------------", "uploadPayLog2");
					
					
					
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	
	private OnPaymentListener.Stub opls	=	new OnPaymentListener.Stub()
	{
		@Override
		public void OnPayment(final int result, final String reArgsJson)
				throws RemoteException {
			if(finish)
				return;
			
			Log.v("----------OnPaymentListener------", result + " " + reArgsJson);
			
			
			
			finish	=	true;
			finish(getSuccessJson(result,reArgsJson));
			uploadPayLog(result,reArgsJson);
		}

		@Override
		public void OnOnlyUploadLog(final int result,final String reArgsJson)
				throws RemoteException {
			if(finish)
				return;
			finish	=	true;
			finish(getSuccessJson(result,reArgsJson));
			uploadPayLog(result,reArgsJson);
		}
		
	};
	
	private ServiceConnection sc	=	new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			payH	=	PayHandler.Stub.asInterface(service);
			try {
				payH.pay(token, argsJson, opls);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			/*Toast.makeText(context, "please install apk", Toast.LENGTH_LONG).show();
			finish	=	true;
			finish(getErrorJson(3));*/
		}
		
	};
	
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
	
	private String getSuccessJson(int platform_status,String reArgsJson)
	{
 		JSONObject j	=	new JSONObject();
		try {
			j.put("status", 0);
			j.put("platform_status", platform_status);
			JSONObject jo	=	new JSONObject(reArgsJson);
			j.put("platform_info", jo);
		} catch (JSONException e) {
 			e.printStackTrace();
		}
		return j.toString();
	}
	
	
	class Heart extends Thread
	{
		@Override
		public void run()
		{
			while(!finish)
			{
				//hart data
				bvs.sendPaymentHeartData(token,pName);
				Log.v("----------Payment------", "Payment heart");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
