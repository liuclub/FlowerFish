package com.bagelplay.controller;

import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.BagelPlayVmaStub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class BagelPlayHeartService extends Service {

	public static String ACTION = "com.bagelplay.controller.BiggiFiHeartService";

	private long lastHeartTime;

	private CheckHeart ch;

	private Handler handler = new Handler();

	private static BagelPlayHeartService bfhs;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	public static void start(Context context) {
		Intent intent = new Intent(ACTION);
		context.startService(intent);
	}

	public static void stop(Context context) {
		Intent intent = new Intent(ACTION);
		context.stopService(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		bfhs = this;
	}

	public static BagelPlayHeartService getInstance() {
		return bfhs;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (ch == null || ch.isFinish()) {
			ch = new CheckHeart();
			ch.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public void resetLastHeart() {
		lastHeartTime = System.currentTimeMillis();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	private void checkHeart() {
		long t = System.currentTimeMillis();
		if (t - lastHeartTime > 6000) {
			ch.finish();

			handler.post(new Runnable() {
				@Override
				public void run() {
					try {
						showTimeOutDia();
					} catch (Exception e) {
						e.printStackTrace();
					}

					BagelPlayHeartService.stop(BagelPlayHeartService.this);
				}

			});

		}
	}

	private void showTimeOutDia() {
		AlertDialog.Builder builder = new AlertDialog.Builder(BagelPlayActivity.getInstance());
		builder.setTitle(getResources().getString(R.string.error));
		builder.setMessage(getResources().getString(R.string.disconnected));
		builder.setCancelable(false);
		builder.setPositiveButton(getResources().getString(R.string.reconnection), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				BagelPlayActivity.getInstance().reload();
				BagelPlayActivity.getInstance().finish();

			}

		});

		builder.setNegativeButton(getResources().getString(R.string.disconnect), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				BagelPlayActivity.getInstance().finish();

			}

		});

		Dialog dialog = builder.show();
	}

	class CheckHeart extends Thread {
		private boolean isFinish;

		@Override
		public void run() {
			lastHeartTime = System.currentTimeMillis();
			while (!isFinish) {
				BagelPlayVmaStub.getInstance().sendHeartData();
				checkHeart();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void finish() {
			isFinish = true;
		}

		public boolean isFinish() {
			return isFinish;
		}
	}

}
