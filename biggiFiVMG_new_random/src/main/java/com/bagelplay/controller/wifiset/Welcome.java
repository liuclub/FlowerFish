package com.bagelplay.controller.wifiset;



import com.bagelplay.controller.com.R;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Welcome extends Screen{

	private View welcomeV;
	
	private WifManager wm;
	
	private OpenWifi ow;
	
	private int waitTime	=	1000;
	
	public Welcome(final WifiSetAct wifisetact) {
		super(wifisetact);
		
		welcomeV	=	View.inflate(wifisetact, R.layout.welcome, null);
		
		this.addView(welcomeV,new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));

		wm			=	WifManager.getInstance();
		
		if(Utils.checkNetworkState(wifisetact))
		{
			wifisetact.handler.postDelayed(new Runnable(){
				@Override
				public void run()
				{
					gotoStickList();
				}
			}, waitTime);
		}
		else
		{
			ow	=	new OpenWifi();
			ow.start();
			Toast.makeText(wifisetact, R.string.wifi_set_toast_wifiopening, Toast.LENGTH_SHORT).show();
		}
		
		Utils.setBold((TextView)welcomeV.findViewById(R.id.name));
 	}
	
	private void gotoStickList()
	{
		wifisetact.removeFullView(Welcome.this);
		//wifisetact.setSubView(new StickList(wifisetact));
	}
	
	class OpenWifi extends Thread
	{
		private boolean finish;
		
		@Override
		public void run()
		{
			long t1	=	System.currentTimeMillis();
			if(wm.wifiOpen())
			{
				long t2	=	System.currentTimeMillis();
				int restTime	=	waitTime - (int)(t2 - t1);
				wifisetact.handler.postDelayed(new Runnable(){
					@Override
					public void run()
					{
						gotoStickList();
					}
				}, restTime < 0 ? 0 : restTime);
			}
		}
		
		public boolean isFinish()
		{
			return finish;
		}
	}
}
