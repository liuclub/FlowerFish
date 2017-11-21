package com.bagelplay.controller.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bagelplay.controller.BagelPlayActivity;

import com.bagelplay.controller.com.R;
import com.bagelplay.controller.domotion.DoMotionViewManager;
import com.bagelplay.controller.utils.Config;


public class BagelPlayTopMenu extends BagelPlayTouch{
		
	private View moreIV;
	
	private View guideIV;
	
	private ImageView mouseIV;
	
	private int firstBtn;
	 	
	public BagelPlayTopMenu(final Context context, AttributeSet attrs) {
		super(context, attrs);
		
		View view	=	View.inflate(context, R.layout.topmenu, null);
		addView(view);
		
		moreIV	=	findViewById(R.id.moreiv);
		
		guideIV	=	findViewById(R.id.guideiv);
		
		mouseIV	=	(ImageView) findViewById(R.id.mouseiv);
  		
 		// menu on top of the screen
 		final ImageView mouse_mode_iv	=	(ImageView) findViewById(R.id.mouseiv);
 		 
 		final View screenshot_iv	=	findViewById(R.id.screenshotiv);
 		final View screenshot_v	=	findViewById(R.id.screenshotv);
 		
  		setScreenWidth(Config.widthPixels);
  		 
 		mouse_mode_iv.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				
				BagelPlayVibrator.getInstance().vibrate(30);
				if(firstBtn == 0)
				{
					firstBtn	=	1;
					BagelPlayActivity.getInstance().changeDoMotionView("/sdcard/shoubing.zip");
 				}
				else
				{
					firstBtn	=	0;
					BagelPlayActivity.getInstance().changeDoMotionView("/sdcard/mouse.zip");
				}
				
			}
		});
 		
 		guideIV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				BagelPlayVibrator.getInstance().vibrate(30);
//				Intent intent	=	new Intent(context,GuideActivity.class);
//				context.startActivity(intent);
 			}
		});
 		
 		moreIV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BagelPlayVibrator.getInstance().vibrate(30);
				BagelPlayBottomMenu bfm	=	BagelPlayBottomMenu.getInstance();
				if(bfm.getVisibility() == View.VISIBLE)
					bfm.setVisibility(View.GONE);
				else
					bfm.setVisibility(View.VISIBLE);
 			}
		});
 		
 		/*
 		qa_iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vibrator.vibrate(30);
				Locale locale = BiggiFiVmgPlusActivity.this.getResources().getConfiguration().locale;
				String language = locale.getLanguage();
				StatService.onEvent(BiggiFiVmgPlusActivity.this, "03", "pass", 1);
				if (language.equals("zh")) {
					showHelpInfoZh();
				}
				else {
					showHelpInfoEn();
				}
				hideMoreOptions();
			}
		});
 		
 		screenshot_iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vibrator.vibrate(30);
				getScreenshot();
			}
		});
 		*/
 
 		 
	}
	
	/*
	public void mouseMode()
 	{
		BiggiFiTopMenu bt	=	(BiggiFiTopMenu) findViewById(R.id.vmg_menu_gridview);
	 	ImageView mouse_mode_iv	=	(ImageView) findViewById(R.id.mouseiv);
	 	View screenshot_iv	=	findViewById(R.id.screenshotiv);
	 	View screenshot_v	=	findViewById(R.id.screenshotv);
	 		
	 	if (BiggiFiVmgManager.mCurUiMode == BiggiFiVmgManager.UI_TOUCH_MODE) 
	 	{
			mUiHandler.sendEmptyMessage(MSG_SMART_GESTURE_UI);
			mouse_mode_iv.setImageResource(R.drawable.mouse_mode);
			screenshot_iv.setVisibility(View.GONE);
			screenshot_v.setVisibility(View.GONE);
			StatService.onEvent(BiggiFiVmgPlusActivity.this, "02", "pass", 1);
			bt.setCenter();
			hideMoreOptions();
		}
 		
 	}
 	
 	public void touchMode()
 	{
 		BiggiFiTopMenu bt	=	(BiggiFiTopMenu) findViewById(R.id.vmg_menu_gridview);
	 	ImageView mouse_mode_iv	=	(ImageView) findViewById(R.id.mouseiv);
	 	View screenshot_iv	=	findViewById(R.id.screenshotiv);
	 	View screenshot_v	=	findViewById(R.id.screenshotv);
	 		
	 	if (BiggiFiVmgManager.mCurUiMode == BiggiFiVmgManager.UI_SMART_GESTURE_MODE) 
	 	{
	 		mUiHandler.sendEmptyMessage(MSG_TOUCH_UI);
			mouse_mode_iv.setImageResource(R.drawable.touch_mode);
			if(level == 1)
			{
				screenshot_iv.setVisibility(View.VISIBLE);
				screenshot_v.setVisibility(View.VISIBLE);
			}
			StatService.onEvent(BiggiFiVmgPlusActivity.this, "01", "pass", 1);
			bt.setCenter();
			hideMoreOptions();
		}
 		
 	}
 	*/
 	

}
