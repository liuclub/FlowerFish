package com.bagelplay.sdk.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.bagelplay.sdk.cocos.SDKCocosManager;

public class MainGun extends Activity implements UncaughtExceptionHandler{

	SDKCocosManager sm;

	public MainGun mainGun;

	public TextView tv;

	private Thread.UncaughtExceptionHandler mDefaultHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		File dir	=	Environment.getExternalStorageDirectory();
		File sss = new File(dir,"bg");
		if (!sss.exists()) {
			sss.mkdirs();
		}



		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);


		mainGun	=	this;

		tv	=	new TextView(this);
		tv.setTextColor(0xffffffff);


		this.setContentView(tv);


		SDKCocosManager.gun_control(true);
		sm	=	SDKCocosManager.getInstance(this);
		sm.addWindowCallBack(this);

	}

	protected void onPause()
	{
		super.onPause();
		sm.onPause();
	}

	protected void onResume()
	{
		super.onResume();
		sm.onResume();
	}

	protected void onStop()
	{
		super.onStop();
		sm.onStop();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		StackTraceElement[] ste = ex.getStackTrace();
		Log.v("=----------------------------------=", ex.toString());
		
		/*String path = "/sdcard/crash/";  
        File dir = new File(path);  
        dir.mkdirs();  
        try
        {
        	 FileOutputStream fos = new FileOutputStream(path + "log.txt");  
             fos.write(ex.toString().getBytes());  
             fos.close(); 
        }catch(Exception e){
        	e.printStackTrace();
        }*/


		//tv.setText(ex.toString());
		saveCrashInfo2File(ex);
	}

	/**
	 * 保存错误信息到文件中
	 *
	 * @param ex
	 * @return  返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();


		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = "time";
			String fileName = "crash-" + time + "-" + timestamp + ".log";

			File dir	=	Environment.getExternalStorageDirectory();
			File sss = new File(dir,"bg");
			if (!sss.exists()) {
				sss.mkdirs();
			}
			File dd	=	new File(sss,"log.txt");
			FileOutputStream fos = new FileOutputStream(dd);
			fos.write(sb.toString().getBytes());
			fos.close();



			return fileName;
		} catch (Exception e) {

		}
		return null;
	}

}
