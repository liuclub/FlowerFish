package com.bagelplay.sdk.test;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bagelplay.sdk.R;
import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.util.Log;
import com.bagelplay.sdk.payment.OnPaymentListener;
import com.bagelplay.sdk.payment.Payment;

public class MainPay extends Activity{

	private Payment p;

	private String authCode		=	"authCode";

	private String loginName	=	"loginName";

	private String sdkUserId	=	"sdkUserId";

	private String access_token	=	"access_token";

	private String sdkId		=	"sdkId";

	private String gameId		=	"gameId";

	private String productName	=	"productName";

	private String amount		=	"100";

	private String gameRole		=	"gameRole";

	private String token;

	private SDKManager bfusm;

	private int resolutionWidth;

	private int resolutionHeight;

	private EditText productIDET;

	private EditText priceET;

	private EditText numET;

	private Button buyBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.paymain);

		getResolution();
		bfusm	=	SDKManager.getInstance(this);
		bfusm.addWindowCallBack(this);

		final String json	=	"{\"productID\":\"1001\",\"price\":100,\"num\":\"1\"}";

		productIDET	=	(EditText) this.findViewById(R.id.productID);
		priceET	=	(EditText) this.findViewById(R.id.price);
		numET	=	(EditText) this.findViewById(R.id.num);
		buyBtn	=	(Button) this.findViewById(R.id.btn);

		buyBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String id	=	 productIDET.getText().toString();
				int price	=	Integer.parseInt(priceET.getText().toString());
				int num	=	Integer.parseInt(numET.getText().toString());
				String json	=	"";
				try
				{
					JSONObject obj = new JSONObject();
					obj.put("productID", id);
					obj.put("price", price);
					obj.put("num", num);
					json	=	obj.toString();
				}catch(Exception e){
					e.printStackTrace();
				}


				bfusm.pay(Payment.PAY_PLATFORM_AIBEI, json, new OnPaymentListener() {

					@Override
					public void OnPaymen(String resultJson) {

						Log.v("=-----------------MainPay-------------------", resultJson);
						showAlertDialog(resultJson);
					}
				});
			}
		});

		new Thread()
		{
			public void run()
			{
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Log.v("=-----------------MainPay-------------------", "send");

				bfusm.pay(Payment.PAY_PLATFORM_LETV, json, new OnPaymentListener() {

					@Override
					public void OnPaymen(String resultJson) {

						Log.v("=-----------------MainPay-------------------", resultJson);
					}
				});

			}
		}.start();


	}

	@Override
	protected void onStart() {
		super.onStart();


	}

	private void getResolution()
	{
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		resolutionWidth = displayMetrics.widthPixels;
		resolutionHeight = displayMetrics.heightPixels;
	}

	public void showAlertDialog(String message)
	{
		new AlertDialog.Builder(this).setTitle("结果").setMessage(message).create().show();

	}


}
