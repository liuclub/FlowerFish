package com.bagelplay.controller;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.bagelplay.controller.domotion.DoMotionViewManager;
import com.bagelplay.controller.domotion.SensorListener;
import com.bagelplay.controller.utils.BagelPlayManager;
import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.Count;
import com.bagelplay.controller.utils.FileDir;
import com.bagelplay.controller.widget.BagelPlaySharedPreferences;
import com.bagelplay.controller.widget.BagelPlayVibrator;
import com.bagelplay.controller.wifiset.StickInfo;
import com.bagelplay.controller.wifiset.WifManager;

public class BagelPlayApplication extends Application {
	private long mVmaIP = 0;

	private WifManager wm;

	public StickInfo stick;

	private Count count;

	@Override
	public void onCreate() {
		super.onCreate();

		Context context = this.getApplicationContext();

		FileDir.init(context);

		BagelPlayVmaStub.init(context);

		BagelPlayManager.init(context);

		BagelPlayVibrator.init(context);

		SensorListener.init(context);

		BagelPlaySharedPreferences.Sensor.setOverturn(context, true);

		BagelPlaySharedPreferences.Sensor.setRuntime(context);

		BagelPlaySharedPreferences.Mouse.setRuntime(context);

		wm = WifManager.initInstance(context);

		DoMotionViewManager.clear();

		count = new Count(context);

		count.start();

	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		wm.release();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public long getmVmaIP() {
		return mVmaIP;
	}

	public void setmVmaIP(long vmaIP) {
		this.mVmaIP = vmaIP;
	}

}
