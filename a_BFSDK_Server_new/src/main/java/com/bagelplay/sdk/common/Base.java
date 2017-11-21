package com.bagelplay.sdk.common;

import java.util.List;

import org.json.JSONObject;

import com.bagelplay.sdk.Jni;
import com.bagelplay.sdk.common.util.Log;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

public class Base {

	private static String TAG = "Base_";

	private boolean listening;

	private SockHeartThread sht;

	private static String biggifi_server;

	public static boolean isRunInBackGround;

	private SDKManager bfsdm;

	public static int getPortFromBFServerJson(String key) {
		if (biggifi_server == null) {
			String[] s = new String[0];
			byte[][] bs = Jni.findSticksFromLocal(s);
			if (bs == null || bs.length == 0) {
				return -1;
			}
			biggifi_server = new String(bs[0]);
		}

		String jsonStr = biggifi_server.substring(0, biggifi_server.lastIndexOf("|"));
		Log.v(TAG, "jsonStr:" + jsonStr);
		try {
			JSONObject jo = new JSONObject(jsonStr);
			return jo.getInt(key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -2;
	}

	public Base(SDKManager bfsdm) {
		this.bfsdm = bfsdm;
	}

	/**
	 * <p>
	 * Start data listening process from data on mobile.
	 * </p>
	 *
	 * @param ssl
	 *            The Callback method
	 */
	boolean connectToServer() {
		int port = Base.getPortFromBFServerJson("SDK_CMD_PORT");
		if (bfsdm.getSocketStub().connect(port)) {
			Log.v(TAG, "connectToServer success port:" + port);
			return true;
		} else {
			Log.v(TAG, "start faild port:" + port);
			return false;
		}
	}

	void startHeart() {
		listening = true;
		sht = new SockHeartThread();
		sht.start();
	}

	/**
	 * <p>
	 * Stop data listening process from data on mobile.
	 * </p>
	 */
	void stop() {
		listening = false;
		Log.v(TAG, "stop");
	}

	class SockHeartThread extends Thread {
		private ActivityManager am;

		public SockHeartThread() {
			am = (ActivityManager) bfsdm.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		}

		@Override
		public void run() {
			while (listening) {
				int isRunInForeground = 0;
				List<RunningTaskInfo> tasks = am.getRunningTasks(1);
				if (!tasks.isEmpty()) {
					ComponentName topActivity = tasks.get(0).topActivity;
					if (topActivity.getPackageName().equals(bfsdm.getApplicationContext().getPackageName())) {
						isRunInForeground = 1;
					}
					isRunInBackGround = isRunInForeground == 0;
				}
				bfsdm.getSocketStub().sendHeartData(isRunInForeground);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
