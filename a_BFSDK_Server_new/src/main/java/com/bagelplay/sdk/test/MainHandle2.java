package com.bagelplay.sdk.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.bagelplay.sdk.cocos.SDKCocosManager;
import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.SocketStub;
import com.bagelplay.sdk.common.ghandle.GHandle;
import com.bagelplay.sdk.common.util.Log;

import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainHandle2 extends Activity{

	private SDKManager bfusm;

	private int resolutionWidth;

	private int resolutionHeight;

	private GHandle gHandle;

	private ListView lv;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

		bfusm	=	SDKCocosManager.getInstance(this);
		bfusm.addWindowCallBack(this);
		bfusm.setControlZip("mouse.zip");

		gHandle	=	bfusm.getGHandle();

		lv	=	new ListView(this);

		lv.setAdapter(new Item());

		this.setContentView(lv);



	}

	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			Log.v("--==-------------------------------------", event.getX() + " " + event.getY());
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void onStart() {
		super.onStart();


	}

	private void getResolution()
	{
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		resolutionWidth = displayMetrics.widthPixels;
		resolutionHeight = displayMetrics.heightPixels;
	}

	class Item extends BaseAdapter
	{

		@Override
		public int getCount() {
			return 200;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView tv	=	new TextView(MainHandle2.this);
			tv.setText(position + "  aa");

			return tv;
		}

	}




}
