package com.bagelplay.controller.wifiset;

import android.widget.RelativeLayout;

public class Screen extends RelativeLayout{

	protected WifiSetAct wifisetact;
		
	public Screen(WifiSetAct wifisetact) {
		super(wifisetact);
		this.wifisetact	=	wifisetact;
		//wifisetact.removeBtn();
		wifisetact.hiddenLoading();
 	}
	
	class SetupThread extends Thread
	{
		protected boolean finish;
		
		public boolean isFinish()
		{
			return finish;
		}
	}
	
	public void onBackPressed()
	{
		
	}

}
