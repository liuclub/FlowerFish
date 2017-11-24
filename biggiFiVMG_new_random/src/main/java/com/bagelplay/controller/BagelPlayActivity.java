package com.bagelplay.controller;

import java.util.HashMap;
import java.util.Map;

import com.bagelplay.controller.com.R;
import com.bagelplay.controller.domotion.DoMotionViewManager;
import com.bagelplay.controller.domotion.SensorListener;
import com.bagelplay.controller.utils.BagelPlayManager;
import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.Config;
import com.bagelplay.controller.utils.Log;
import com.bagelplay.controller.widget.BaseActivity;
import com.bagelplay.controller.wifiset.WifiSetAct;
import com.bagelplay.controller.wifiset.widget.FloatView;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BagelPlayActivity extends BaseActivity {

	private int doMotionView;

	public static byte[] bgPicData;

	private RelativeLayout mVmgMainLayout;

	private View bottomMenu;

	// private BiggiFiTopMenu topMenu;

	private static BagelPlayActivity bagelPlayActivity;

	private View keyboardV;

	private FloatView floatView;

	private ImageView currentControlIV;
	private String zipName;

	private final String TAG = "BagelPlayActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bagelPlayActivity = this;

		setContentView(R.layout.vmg_main_layout);

		mVmgMainLayout = (RelativeLayout) findViewById(R.id.vmg_main_ui);

		// topMenu = (BiggiFiTopMenu)findViewById(R.id.top_menu);
		floatView = (FloatView) findViewById(R.id.more_menu);

		keyboardV = findViewById(R.id.keyboard);

		currentControlIV = (ImageView) findViewById(R.id.currentControl3);

		// resetResolution();

		zipName = getIntent().getStringExtra("zipName");
		Log.isDiyChooseModel = false;
		DoMotionViewManager.changeDoMotionView(mVmgMainLayout, zipName);

		SensorListener.getInstance().open();

		BagelPlayHeartService.start(this);

	}

	private Handler handler = new Handler();

	public void reload() {

		BagelPlayVmaStub bvfs = BagelPlayVmaStub.getInstance();
		boolean result = false;
		if (bvfs.connect(Config.vma_ip, Config.vma_cmd_port)) {
			BagelPlayManager.getInstance().start();
			bvfs.sendLoginData();
			result = true;

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (!BagelPlayManager.getInstance().Loginsuccess) {

						Intent intent = new Intent(BagelPlayActivity.this, WifiSetAct.class);
						intent.putExtra("iswelcome", false);
						startActivity(intent);
					}

				}
			}, 5000);
		} else {

			Intent intent = new Intent(BagelPlayActivity.this, WifiSetAct.class);
			intent.putExtra("iswelcome", false);
			startActivity(intent);
		}
	}

	public static BagelPlayActivity getInstance() {
		return bagelPlayActivity;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		floatView.initMoreView();

	}

	// private void resetResolution() {
	// DisplayMetrics mDm = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(mDm);
	// Config.heightPixelsdiy = mDm.widthPixels;
	// Config.widthPixelsdiy = mDm.heightPixels;
	// }

	/*
	 * public void changeDoMotionView(int doMotionView) { if(doMotionView ==
	 * this.doMotionView || doMotionView <= 0 || doMotionView >
	 * DoMotionViewManager.DOMOTION_SCREEN_ORIENTATION.length - 1) return;
	 * this.doMotionView = doMotionView;
	 * DoMotionViewManager.changeDoMotionView(mVmgMainLayout, doMotionView);
	 * //setRequestedOrientation
	 * (DoMotionViewManager.DOMOTION_SCREEN_ORIENTATION[doMotionView]); }
	 */

	public void showKeyboard() {

		keyboardV.setVisibility(View.VISIBLE);
	}

	public void changeDoMotionView(String zip) {
		DoMotionViewManager.changeDoMotionView(mVmgMainLayout, zip);
	}

	private void setBgPic() {
		if (bgPicData == null || bgPicData.length == 0) {
			return;
		}
		Log.v("=----------------------------------------------=", "setBgPic:" + bgPicData.length);
		Bitmap pic = BitmapFactory.decodeByteArray(bgPicData, 0, bgPicData.length);
		mVmgMainLayout.setBackgroundDrawable(new BitmapDrawable(pic));
		bgPicData = null;
	}

	@Override
	public void orientationChanged(int orientation) {

		// topMenu.setCenter(Config.widthPixels);
		if (floatView != null) {
			floatView.initMoreView();
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.isDiyChooseModel = false;
		zipName = intent.getStringExtra("zipName");
		DoMotionViewManager.changeDoMotionView(mVmgMainLayout, zipName);
	}

	public void chooseMouseModel() {
		Log.isDiyChooseModel = true;
		String zipName = "testmouse.zip";
		DoMotionViewManager.changeDoMotionViewTestMouse(this, mVmgMainLayout, zipName);

	}

	public void chooseTouchModel() {
		Log.isDiyChooseModel = true;
		String zipName = "testtouch.zip";
		DoMotionViewManager.changeDoMotionViewTestMouse(this, mVmgMainLayout, zipName);

	}

	public void recoverModel() {

		DoMotionViewManager.changeDoMotionView(mVmgMainLayout, zipName);

	}

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// exit();
	// }

	public void exit() {
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

	@Override
	public void onResume() {
		super.onResume();
		// MobclickAgent.onPageStart(TAG);

		MobclickAgent.onPageStart(TAG);

		MobclickAgent.onResume(this);

		Map<String, String> m = new HashMap<String, String>();

		m.put("name_channel", Config.tvgamechannel);

		MobclickAgent.onEventValue(this, "game_name_and_channel", m, 1);

	}

	@Override
	public void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);

	}

}
