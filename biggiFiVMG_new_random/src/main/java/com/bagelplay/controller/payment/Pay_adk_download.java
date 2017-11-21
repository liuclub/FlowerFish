package com.bagelplay.controller.payment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.BagelPlayUtil;
import com.bagelplay.controller.utils.WebHost;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class Pay_adk_download {
		
	private Context context;
	
	private String packageName;
	
	private int state;
	
	private String apk;
	
	private int versionCode;
	
	private long downloadId;
	
	private DownloadCompleteReceiver receiver;
	
	private File apkSave;
	
	private Handler handler;
	
	public Pay_adk_download(Context context,String packageName)
	{
		this.context		=	context;
		
		this.packageName	=	packageName;
		
		handler	=	new Handler(context.getMainLooper());
		
		receiver			=	new DownloadCompleteReceiver();
		
		createSdFolder();
	}
	
	public void download()
	{
		new GetJsonFromWeb().start();
	}
	
	public PackageInfo isPayApkExist()
	{
		boolean hasInstalled = false; 
        PackageManager pm = context.getPackageManager(); 
        List<PackageInfo> list = pm 
                .getInstalledPackages(PackageManager.PERMISSION_GRANTED); 
        for (PackageInfo p : list) { 
            if (packageName != null && packageName.equals(p.packageName)) { 
                return p;
             } 
        } 
        return null; 
	}
	
	private void parseJson(String str)
	{
		try
		{
			JSONObject jo		=	new JSONObject(str);
			state				=	jo.getInt("status");
			String packageName	=	jo.getString("packageName");
			versionCode			=	jo.getInt("versionCode");
			apk					=	jo.getString("apk");
		}catch(Exception e){
			e.printStackTrace();
		}
 	}
	
	private boolean checkDownload()
	{
		PackageInfo p	=	isPayApkExist();
		if(p == null || p.versionCode < versionCode)
			return true;
		return false;
	}
	
	private void downloadApk()
	{
		if(!checkDownload())
			return;
		DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apk));
		Log.v("=-----------------------------------=", apk);
		request.setDestinationInExternalPublicDir("biggifi_pay_sdk", packageName + ".apk");
		request.setDestinationUri(Uri.fromFile(apkSave));
		downloadId = downloadManager.enqueue(request);
		
		context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));  
	}
	
	private void createSdFolder()
	{
		File dir = Environment.getExternalStorageDirectory();           
        String path=dir.getPath()+"/";
        File dir2= new File(path+ "biggifi_pay_sdk");  
        dir2.mkdirs();
        apkSave	=	new File(dir2.getPath() + "/" + packageName + ".apk");
        apkSave.delete();
 	}
	
	private void installApk()
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.fromFile(apkSave),  
                "application/vnd.android.package-archive");  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);  
	}
	
	private void showToast(final String str)
	{
		handler.post(new Runnable(){
			@Override
			public void run()
			{
				Toast.makeText(context, str, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	class GetJsonFromWeb extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				HttpClient client = new DefaultHttpClient(); 
				HttpPost httpPost = new HttpPost(WebHost.PAYAPK_URL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("packageName", packageName));  
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				httpPost.setEntity(entity);  
				HttpResponse response = client.execute(httpPost);
				if(response.getStatusLine().getStatusCode()==200){//判断状态码  
 	               String result	=	EntityUtils.toString(response.getEntity(),"utf-8");
 	               parseJson(result);
 	               downloadApk();
	            }  
				else
				{
					
					showToast(context.getString(R.string.access_to_APK_information_file_failed));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
         
		}
	}
	
	class DownloadCompleteReceiver extends BroadcastReceiver {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
        	
        	context.unregisterReceiver(receiver);
        	
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            {  
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1); 
                if(downloadId == downId)
                {
                	installApk();
                }
            }  
            else
            {
            	
            	showToast(context.getString(R.string.download_APK_failed));
            }
            
            
        }  
    } 
}
