package com.bagelplay.sdk.common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class CloseBroadcast extends BroadcastReceiver {

	public CloseBroadcast(Activity activity) {
		Intent intent = new Intent("com.bagelplay.sdk.common.close");
		intent.putExtra("package", activity.getPackageName());
		activity.sendBroadcast(intent);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		addFilter(activity);
	}

	public void addFilter(Activity activity) {
		Context applicationContext = activity.getApplicationContext();
		applicationContext.registerReceiver(this, new IntentFilter("com.bagelplay.sdk.common.close"));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String packageName = intent.getStringExtra("package");
		String curPackageName = context.getPackageName();
		if (!curPackageName.equals(packageName)) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

}
