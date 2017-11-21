package com.bagelplay.sdk.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.bagelplay.sdk.common.util.FileDir;
import com.bagelplay.sdk.common.util.Log;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

class CServer {

	private boolean isSo = false;

	private Process process;

	private SDKManager bfusm;

	CServer(SDKManager bfusm) {
		this.bfusm = bfusm;
	}

	void startServer() {
		if (isSo) {
			new Thread() {
				@Override
				public void run() {
					com.bagelplay.server.Jni.start(bfusm.getApplicationContext().getPackageName(), bfusm.getScreenWidth(), bfusm.getScreenHeight());
				}
			}.start();
		} else {
			try {
				copy();
				File file = new File(FileDir.getInstance().SERVER_FILE);
				file.setExecutable(true, true);
				process = Runtime.getRuntime().exec(FileDir.getInstance().SERVER_FILE + " " + bfusm.getApplicationContext().getPackageName() + " " + bfusm.getScreenWidth() + " " + bfusm.getScreenHeight() + " " + bfusm.getExtral() + " " + 12355 + " " + "101.200.231.169" + " " + 12345 + " &");
				//process	=	Runtime.getRuntime().exec(FileDir.getInstance().SERVER_FILE + " " + bfusm.getApplicationContext().getPackageName() + " " + bfusm.getScreenWidth() + " " + bfusm.getScreenHeight() + " " + bfusm.getExtral() + " &");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	void stopServer() {
		process.destroy();
	}

	private void copy() throws Exception {
		Log.v("---bagelplay---", Build.CPU_ABI + "");
		InputStream is = null;
		try {
			is = bfusm.getApplicationContext().getAssets().open(FileDir.getInstance().ASSETS_DIR + Build.CPU_ABI + "/" + FileDir.getInstance().SERVER_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (is == null) {
			is = bfusm.getApplicationContext().getAssets().open(FileDir.getInstance().ASSETS_DIR + "armeabi/" + FileDir.getInstance().SERVER_NAME);
		}

		FileOutputStream fos = new FileOutputStream(FileDir.getInstance().SERVER_FILE);
		while (true) {
			int data = is.read();
			if (data == -1) {
				break;
			}
			fos.write(data);
		}
		fos.close();
		is.close();
	}

	static String checkAlreadyRun(Context context) {
		try {
			Process p = Runtime.getRuntime().exec(new String[] { "sh", "-c", "ps | grep " + FileDir.SERVER_NAME });
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.contains(FileDir.SERVER_NAME)) {
					break;
				}
			}
			if (line == null) {
				return null;
			}

			int lastIndex = line.lastIndexOf("/");
			int index = line.lastIndexOf("/", line.length() - FileDir.SERVER_NAME.length() - 1 - 1);
			String packageName = line.substring(index + 1, lastIndex);
			PackageManager pm = context.getPackageManager();
			ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
			String name = (String) pm.getApplicationLabel(info);
			return name;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
