package com.bagelplay.controller.domotion;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.bagelplay.controller.BagelPlayActivity;
import com.bagelplay.controller.com.R;
import com.bagelplay.controller.domotion.customize.CShaker;

public class DoMotionViewManager {

	public static final int DOMOTION_SCREEN_TOUCH = 1;

	public static final int DOMOTION_SCREEN_MOUSE = 2;

	public static final int DOMOTION_SCREEN_GESTURE = 3;

	public static final int DOMOTION_SCREEN_REMOTECONTROL = 4;

	public static final int DOMOTION_SCREEN_TOUCHDIY = 101;

	public static final int DOMOTION_SCREEN_SHAKE = 102;

	public static final int[] DOMOTION_SCREEN_ORIENTATION = { 0,
			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
			ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
			ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT };

	public static final int[] DOMOTION_SCREEN_DEFAULT_BG = { 0,
			R.drawable.touch_mode_zh, R.drawable.smart_mode_zh,
			R.drawable.smart_mode_zh, R.drawable.bg01 };

	public static final String ZIP_DIR = "/data/data/com.bagelplay.controller.com/motion/";
	static {
		File file = new File(ZIP_DIR);
		if (file.exists() && file.isFile())
			file.delete();
		file.mkdirs();
	}

	private static DoMotionView currentDoMotionView;

	public static void clear() {
		File file = new File(ZIP_DIR);
		File[] files = file.listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	/*
	 * public static synchronized void changeDoMotionView(ViewGroup vg) {
	 * ZipParser zp = new ZipParser("/data/data/" +
	 * BiggiFiActivity.getInstance().getPackageName() + "/" + "domotion.zip");
	 * JsonParser jp = new JsonParser(BiggiFiActivity.getInstance(),zp);
	 * 
	 * changeDoMotionView(vg,jp); }
	 */

	public static synchronized void changeDoMotionView(ViewGroup vg, String zip) {

		vg.removeAllViews();

		// ZipParser zp = new ZipParser("/sdcard/default.zip");

		ZipParser zp = new ZipParser(ZIP_DIR + zip);

		JsonParser jp = new JsonParser(BagelPlayActivity.getInstance(), zp,
				(RelativeLayout) vg);

		changeDoMotionView(vg, jp);
	}

	public static synchronized void changeDoMotionViewTestMouse(
			Context context, ViewGroup vg, String zip) {

		vg.removeAllViews();

		ZipParser zp = new ZipParser(context, zip);

		JsonParser jp = new JsonParser(BagelPlayActivity.getInstance(), zp,
				(RelativeLayout) vg);

		changeDoMotionView(vg, jp);
	}

	public static DoMotionView getCurrentDoMotionView() {
		return currentDoMotionView;
	}
	
	private static void setCurrentModel(Context context,int model){
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = mPrefs.edit(); 
		editor.putInt("current_model", model); 
		editor.commit(); 
	}

	private static void changeDoMotionView(ViewGroup vg, JsonParser jp) {
		
		
		DoMotionView view = null;
		setCurrentModel(vg.getContext(),jp.getType());
		if (jp.getType() == DOMOTION_SCREEN_TOUCH) {
			view = new TouchView(vg.getContext(), jp);

		} else if (jp.getType() == DOMOTION_SCREEN_MOUSE) {
			view = new MouseView(vg.getContext(), jp);
		} else if (jp.getType() == DOMOTION_SCREEN_GESTURE) {
			view = new GestureView(vg.getContext(), jp);
		} else if (jp.getType() == DOMOTION_SCREEN_REMOTECONTROL) {
			view = new RemoteControlView(vg.getContext(), jp);
		} else if (jp.getType() == DOMOTION_SCREEN_TOUCHDIY) {
			// view = new TouchViewDIY(vg.getContext(), jp);
		}

		else {
			view = new CustomizedView(vg.getContext(), jp);
		}
		
		
		if (view != null) {

			ViewGroup.LayoutParams vglp = new ViewGroup.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			vg.addView(view, 0, vglp);
			currentDoMotionView = view;
		}
	}

}
