package com.bagelplay.controller.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.content.Context;
import android.os.Build;

public class Count {

	private Context context;

	private JSONObject jo = new JSONObject();

	private int loopTime = 30 * 1000;

	private boolean stop;

	public Count(Context context) {
		this.context = context;
	}

	public void close() {
		stop = true;
	}

	public void start() {
		stop = false;
		new Thread() {
			@Override
			public void run() {
				uplodeLastCount();
				doOneTimeCount();
				loop();
			}
		}.start();
	}

	public void putData(String key, String value) {
		try {
			jo.put(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void uplodeLastCount() {
		File file = new File(FileDir.getInstance().COUNT_JSON);
		if (!file.exists()) {
			return;
		}
		try {
			FileInputStream is = new FileInputStream(file);
			String json = BagelPlayUtil.inStreamToStr(is);
			HttpP p = new HttpP(WebHost.COUNT_URL);
			p.appendValue("json", json);
			int res = p.post();
		} catch (Exception e) {
			e.printStackTrace();
		}
		file.delete();
	}

	public void doOneTimeCount() {
		String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String deviceMode = Build.BRAND + " " + Build.MODEL;
		String macAddr = BagelPlayUtil.getMacAddressStr(context);
		String versionCode = BagelPlayUtil.getVersionCode(context);
		putData("startTime", startTime);
		putData("deviceMode", deviceMode);
		putData("macAddr", macAddr == null ? "" : macAddr);
		putData("packageName", context.getPackageName());
		putData("version", versionCode);
	}

	public void doLoopCount() {

	}

	private void loop() {
		while (!stop) {
			String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			putData("endTime", endTime);
			doLoopCount();
			try {
				File file = new File(FileDir.getInstance().COUNT_JSON);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(jo.toString().getBytes());
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(loopTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
