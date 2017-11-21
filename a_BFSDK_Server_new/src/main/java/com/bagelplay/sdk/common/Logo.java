package com.bagelplay.sdk.common;


import com.bagelplay.sdk.common.util.FileDir;
import com.bagelplay.sdk.common.util.Log;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public abstract class Logo extends Activity{

	private SDKManager bfusm;

	private ImageView logoIV;

	private int step;

	private boolean isExit = true;

	private boolean isStopWantPlayerIDs;

	private OnShowPlayerLinstener ospl	=	new OnShowPlayerLinstener()
	{

		@Override
		public void OnGetPlayerIDs(int[] playerIDs, int num) {
			if(step == 1 && num > 0)
			{
				isStopWantPlayerIDs	=	true;
				bfusm.removeOnShowPlayerLinstener(ospl);
				doStep(2);


			}
		}

	};

	private SDKManager.OnSDKKeyListener osdkkl	=	new SDKManager.OnSDKKeyListener()
	{

		@Override
		public void OnSDKKeyDown(int playerOrder, int keyCode) {
			if(playerOrder < 100)		//遥控器
			{
				if(step == 1)
				{
					doStep(4);
				}
			}
			else						//游戏手柄
			{
				if(step == 1)
				{
					doStep(3);
				}
			}
		}

		@Override
		public void OnSDKKeyUp(int playerOrder, int keyCode) {

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		bfusm	=	getSDKManager();
		if(bfusm.isNormalLaunched())
		{
			bfusm.addWindowCallBack(this);
			bfusm.setOnShowPlayerLinstener(ospl);

			doStep(1);

			wantPlayerIDs();

			bfusm.setOnSDKKeyListener(osdkkl);
		}


	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(event.getAction() == KeyEvent.ACTION_DOWN)
		{
			doFinish();
		}
		return super.onKeyDown(keyCode, event);
	}*/

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			if(step == 1)
			{
				doStep(2);
			}
			else if(step == 2 || step == 3 || step == 4)
			{
				doFinish();
			}
		}
		return super.onTouchEvent(event);
	}

	public boolean dispatchKeyEvent(KeyEvent event) {

		if(event.getAction() == KeyEvent.ACTION_DOWN && (step == 3 || step == 4 || step == 2))
			doFinish();
		if(bfusm.dispatchKeyEvent(event))
			return true;
		return super.dispatchKeyEvent(event);
	}

	public boolean dispatchGenericMotionEvent(MotionEvent event)
	{
		if(event.getAction() == KeyEvent.ACTION_DOWN && (step == 3 || step == 4 || step == 2))
			doFinish();
		if(bfusm.dispatchGenericMotionEvent(event))
			return true;
		return super.dispatchGenericMotionEvent(event);
	}




	@Override
	protected void onStart() {
		super.onStart();

		bfusm.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();

		bfusm.onStop();
	}

	protected void onResume()
	{
		super.onResume();

		bfusm.onResume();


	}

	protected void onPause() {
		super.onPause();

		bfusm.onPause();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();

		bfusm.removeWindowCallBack(this);

		if(isExit)
		{
			bfusm.destroy();
			new Thread()
			{
				@Override
				public void run()
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}.start();

		}

	}


	private void wantPlayerIDs()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				while(!isStopWantPlayerIDs)
				{
					bfusm.wantPlayerIDs();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}.start();

	}

	private void doStep(int step)
	{
		if(step == 1)
		{
			logoIV	=	new ImageView(this);
			Bitmap pic	=	null;
			try {
				pic	=	BitmapFactory.decodeStream(getAssets().open(FileDir.getInstance().LOGO));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			logoIV.setImageBitmap(pic);
			logoIV.setScaleType(ScaleType.FIT_XY);

			setContentView(logoIV);
		}
		else if(step == 2)						//手机说明
		{
			logoIV	=	new ImageView(this);
			Bitmap pic	=	null;
			try {
				pic	=	BitmapFactory.decodeStream(getAssets().open(FileDir.getInstance().HOWTOPLAY_PHONE));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			logoIV.setImageBitmap(pic);
			logoIV.setScaleType(ScaleType.FIT_XY);

			setContentView(logoIV);
		}
		else if(step == 3)					//手柄说明
		{
			logoIV	=	new ImageView(this);
			Bitmap pic	=	null;
			try {
				pic	=	BitmapFactory.decodeStream(getAssets().open(FileDir.getInstance().HOWTOPLAY_GHANDLE));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			logoIV.setImageBitmap(pic);
			logoIV.setScaleType(ScaleType.FIT_XY);

			setContentView(logoIV);
		}
		else if(step == 4)					//遥控器说明
		{
			logoIV	=	new ImageView(this);
			Bitmap pic	=	null;
			try {
				pic	=	BitmapFactory.decodeStream(getAssets().open(FileDir.getInstance().HOWTOPLAY_RCONTROLLER));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			logoIV.setImageBitmap(pic);
			logoIV.setScaleType(ScaleType.FIT_XY);

			setContentView(logoIV);
		}

		this.step	=	step;

	}

	private void doFinish()
	{
		isExit	=	false;
		Intent intent	=	new Intent(Logo.this,getNextClass());
		startActivity(intent);

		isStopWantPlayerIDs	=	true;
		bfusm.removeOnShowPlayerLinstener(ospl);
		bfusm.removeOnSDKKeyListener(osdkkl);
		finish();
	}

	public abstract Class getNextClass();

	public abstract SDKManager getSDKManager();


}
