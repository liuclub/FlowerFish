package com.bagelplay.controller.wifiset;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bagelplay.controller.com.R;
import com.bagelplay.controller.domotion.customize.CSlider;
import com.bagelplay.controller.update.UpdateManager;
import com.bagelplay.controller.utils.BagelPlayManager;
import com.bagelplay.controller.utils.Config;
import com.bagelplay.controller.utils.Log;
import com.bagelplay.controller.widget.BaseActivity;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class WifiSetAct extends BaseActivity {

	private TextView titleTV;

	private LinearLayout contentLL;

	private RelativeLayout fullRL;

	private LinearLayout dialogLL;

	private View loadingV;

	private boolean isShowLoading;

	// private TextView btnTV;

	// private ImageView refreshIV;

	private boolean isLoading;

	private Screen screen;

	private boolean canBack = true;

	protected Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wifisetact);

		
		
		
		
		titleTV = (TextView) findViewById(R.id.title);

		contentLL = (LinearLayout) findViewById(R.id.content);

		fullRL = (RelativeLayout) findViewById(R.id.full);

		// btnTV = (TextView) findViewById(R.id.btn2);

		// refreshIV = (ImageView) findViewById(R.id.refresh);

		dialogLL = (LinearLayout) findViewById(R.id.dialog);
		Intent intent =getIntent();
		if(intent!=null){
			
		StickList sl = new StickList(this, intent.getBooleanExtra("iswelcome", true));
		setSubView(sl);
		}
		else{
			StickList sl = new StickList(this, true);
			setSubView(sl);
		}
	
		
//		ConnectToServer cts	=	new ConnectToServer(this,true);
//		setSubView(cts);

		Log.v("=---------------------", "Product Model: "
				+ android.os.Build.MODEL + "," + android.os.Build.VERSION.SDK
				+ "," + android.os.Build.VERSION.RELEASE);

		/*
		 * WifiListDialog dialog = new WifiListDialog(this);
		 * dialog.setTit("请选择网络连接");
		 * dialog.setMessage("当前WiFi网络:Biggifi\r\n(提示：请将手机连接至电视设备相同的网络)");
		 * dialog.show();
		 */

		UpdateManager um = new UpdateManager(this);
		um.checkUpdate();

		/*
		 * cs = new CSlider(this,null); cs.setPhoneXY(0, 0); cs.setWH(600,
		 * 1000); View view = new View(this); this.setContentView(view, new
		 * ViewGroup.LayoutParams(600,1000));
		 * view.setBackgroundColor(0xff00ff00);
		 */

		// try {
		//
		//
		//
		//
		//
		//
		//
		// ApplicationInfo appInfo = this.getPackageManager()
		// .getApplicationInfo(getPackageName(),
		// PackageManager.GET_META_DATA);
		// String msg=appInfo.metaData.getString("UMENG_CHANNEL");
		//
		//
		//
		//
		// MobclickAgent.openActivityDurationTrack(false);
		// } catch (NameNotFoundException e) {
		// e.printStackTrace();
		// }

		
		
	}
	


	/*
	 * CSlider cs; public boolean dispatchTouchEvent(MotionEvent ev) {
	 * 
	 * cs.dispatchTouchEvent(ev); return super.dispatchTouchEvent(ev); }
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	public void suddenExit() {

		finish();

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}.start();
	}

	public void setTitle(String text) {
		titleTV.setText(text);
	}

	public void setSubView(Screen screen) {
		contentLL.removeAllViews();
		contentLL.addView(screen, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		WifiSetAct.this.screen = screen;
	}

	public void showProcessDialog(String str) {
		if (isShowLoading) {
			((TextView) loadingV.findViewById(R.id.setupmessage)).setText(str);
			return;
		}
		loadingV = View.inflate(this, R.layout.wifisetuping, null);
		setFullView(loadingV);
		Animation ani = AnimationUtils.loadAnimation(this, R.anim.loading);
		loadingV.findViewById(R.id.loading).startAnimation(ani);
		((TextView) loadingV.findViewById(R.id.setupmessage)).setText(str);
		loadingV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		isShowLoading = true;
	}

	public void removeProcessDialog() {
		removeFullView(loadingV);
		isShowLoading = false;
	}

	public void startLoading() {

		// refreshIV.startAnimation(AnimationUtils.loadAnimation(this,
		// R.anim.loading));
		isLoading = true;
		Log.v("=--------------ddd-------------------------", "startLoading");
	}

	public void stopLoading() {
		// if (refreshIV.getAnimation() != null)
		// refreshIV.getAnimation().cancel();
		isLoading = false;
		Log.v("=--------------ddd-------------------------", "stopLoading");
	}

	public void showLoading(View.OnClickListener vocl) {

		// refreshIV.setVisibility(View.VISIBLE);
		// refreshIV.setOnClickListener(vocl);
		Log.v("=--------------ddd-------------------------", "showLoading");
	}

	public void hiddenLoading() {
		stopLoading();
		// refreshIV.clearAnimation();
		// refreshIV.setVisibility(View.GONE);
		// refreshIV.setOnClickListener(null);
		Log.v("=--------------ddd-------------------------", "hiddenLoading");
	}

	public boolean isLoading() {
		return isLoading;
	}

	// public void setBtnClick(String str, View.OnClickListener vocl) {
	// btnTV.setText(str);
	// btnTV.setOnClickListener(vocl);
	// btnTV.setVisibility(View.VISIBLE);
	// }

	// public void removeBtn() {
	// btnTV.setText(null);
	// btnTV.setOnClickListener(null);
	// btnTV.setVisibility(View.GONE);
	// }

	public void setFullView(View view) {
		fullRL.addView(view, new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void removeFullView(View view) {
		fullRL.removeView(view);
	}

	public void showDialog(View view) {
		dialogLL.setVisibility(View.VISIBLE);
		dialogLL.addView(view, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		canBack = false;
	}

	public void removeDialog() {
		dialogLL.setVisibility(View.GONE);
		dialogLL.removeAllViews();
		canBack = true;
	}

	@Override
	public void onBackPressed() {
		if (canBack) {
			suddenExit();
		}

		Log.v("=--------------onBackPressed----------------", canBack + " ");
	}

	@Override
	public void orientationChanged(int orientation) {

	}

	/*
	 * @Override public void onBackPressed() { if(screen != null &&
	 * !isShowLoading && !isLoading) screen.onBackPressed(); }
	 */

}
