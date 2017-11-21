package com.bagelplay.controller.update;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;


import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.Log;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager {
	private static final int MSG_DOWNLOAD = 0;
	private static final int MSG_DOWNLOAD_FINISH = 1;
	private static final int MSG_UPDATE_APP = 2;
	private static final int MSG_DOWNLOAD_FAILURED = 3;

	protected static final String TAG = "UpdateManager";
	private int progress;
	private boolean cancelUpdate = false;

	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	private static WeakReference<Context> mContextRef;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_DOWNLOAD:
				mProgress.setProgress(progress);
				break;
			case MSG_DOWNLOAD_FINISH:
				installApk();
				break;
			case MSG_UPDATE_APP:
				showNoticeDialog();
				break;
			case MSG_DOWNLOAD_FAILURED:
				Context context = mContextRef.get();
				Toast.makeText(context, R.string.download_update_apk_failure,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		mContextRef = new WeakReference<Context>(context);
	}

	public void checkUpdate() {
		initGlobal();
		getUpdateVersion();
	}

	private void showNoticeDialog() {
		Context context = mContextRef.get();
		String msg = context.getString(R.string.new_version) + Global.mSerVerNameStr + "\n\n"
				+ Global.mDescription;
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(R.string.update);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.alert_dialog_update,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showDownloadDialog();
					}
				});
		builder.setNegativeButton(R.string.alert_dialog_cancel,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	private void showDownloadDialog() {
		Context context = mContextRef.get();
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(R.string.download);
		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		builder.setNegativeButton(R.string.alert_dialog_cancel,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						try {
							mIs.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						cancelUpdate = true;
					}
				});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		mDownloadDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Log.d(TAG, "Get back key event");
					dialog.dismiss();
					try {
						mIs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return true;
				}
				return false;
			}
		});
		downloadApk();
	}

	private void downloadApk() {
		new DownloadApkThread().start();
	}

	InputStream mIs;

	private class DownloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				if (Global.mHaveExternalStorage) {
					URL url = new URL(Global.UPDATE_SERVER_URI
							+ Global.UPDATE_APKNAME);
					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					con.connect();
					int length = con.getContentLength();
					mIs = con.getInputStream();

					File file = new File(Global.mFullDir);
					
					if (!file.exists()) {
						boolean ret = file.mkdirs();
						if (!ret) 
							Log.w(TAG, "create directory:" + Global.mFullDir + ", failure");
					}					
					File apkFile = new File(Global.mFullDir,
							Global.UPDATE_APKNAME);
					
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					byte buf[] = new byte[4096];
					do {
						int numread = mIs.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						mHandler.sendEmptyMessage(MSG_DOWNLOAD);
						if (numread <= 0) {
							// count always less then length, i don't know why,
							// so add this rang
							if (count > length - 5 && count < length + 5)
								mHandler.sendEmptyMessage(MSG_DOWNLOAD_FINISH);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);
					fos.close();
					mIs.close();
					con.disconnect();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(MSG_DOWNLOAD_FAILURED);
			} catch (IOException e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(MSG_DOWNLOAD_FAILURED);
			}
			mDownloadDialog.dismiss();
		}
	};

	private void installApk() {
		Context context = mContextRef.get();
		File apkfile = new File(Global.mFullDir, Global.UPDATE_APKNAME);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
	}

	public void checkVersion() {
		if (Global.mLocalVersion < Global.mSerVerCode) {
			mHandler.sendEmptyMessage(MSG_UPDATE_APP);
		} else {
			cheanUpdateFile();
		}
	}

	private void cheanUpdateFile() {
		if (Global.mHaveExternalStorage) {
			File apkFile = new File(Global.mFullDir,
					Global.UPDATE_APKNAME);
			if (apkFile.exists()) {
				apkFile.delete();
			}
		}
	}

	public void initGlobal() {
		Context context = mContextRef.get();
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			Global.mHaveExternalStorage = true;
			Global.mFullDir = Environment.getExternalStorageDirectory()
					+ Global.mDownloadDir;
		} else {
			Global.mHaveExternalStorage = false;
		}

		try {
			Global.mLocalVersion = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0).versionCode; // get
																				// local
																				// version
			Global.mLocalVersionName = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0).versionName;
			Global.mSerVerCode = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void getUpdateVersion() {
		new GetUpdateVersionThread().start();
	}

	private class GetUpdateVersionThread extends Thread {
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		int totalSize = 0;

		@Override
		public void run() {
			URL url;
			try {
				Context context = mContextRef.get();
				Locale locale = context.getResources().getConfiguration().locale;
				String language = locale.getLanguage();
				if (language.equals("zh")) {
					url = new URL(Global.UPDATE_SERVER_URI
							+ Global.UPDATE_VERXML_ZH);
				}
				else {
					url = new URL(Global.UPDATE_SERVER_URI
							+ Global.UPDATE_VERXML_EN);
				}
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestProperty("User-Agent",
						"PacificHttpClient");
				httpConnection.setConnectTimeout(10000);
				httpConnection.setReadTimeout(20000);
				if (httpConnection.getResponseCode() == 404) {
					return;
				}
				is = httpConnection.getInputStream();
				int readsize = 0;
				int len = httpConnection.getContentLength();
				len += 5;
				byte[] buffer = new byte[len];
				while ((readsize = is.read(buffer, totalSize, len - totalSize)) > 0) {
					totalSize += readsize;
				}
				ByteArrayInputStream bIns = new ByteArrayInputStream(buffer, 0,
						totalSize);
				XMLParse.parseVersionXML(bIns);

				Log.d(TAG, "version code:" + Global.mSerVerCodeStr
						+ ",version name:" + Global.mSerVerNameStr);
				is.close();
				bIns.close();
				httpConnection.disconnect();
				checkVersion();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
		}
	}
}
