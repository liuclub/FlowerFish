package com.bagelplay.controller.wifiset.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.controller.BagelPlayActivity;
import com.bagelplay.controller.BagelPlaySettingActivity;
import com.bagelplay.controller.HelpActivity;
import com.bagelplay.controller.com.R;
import com.bagelplay.controller.domotion.DoMotionViewManager;
import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.Config;
import com.bagelplay.controller.utils.Log;

public class Menu extends LinearLayout {

	private static final int MSG_WHAT_SHOW = 1;

	private View backV;

	private View homeV;

	private View settingsV;

	private View helpV;

	private View exitV;

	private View aboutV;

	private View[] views = new View[6];

	private View frameV;

	private View moreV;

	private long begin, end;

	private boolean frameShow=false;

	private int[] moreVwh = new int[2];
	private int[] moreVxy = new int[2];
	private boolean isTouched;
	private int downID = -1;

	private boolean isOnMouse = false;
	
	private boolean isOnTouch = false;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_WHAT_SHOW) {
				doShow();
			}
		}
	};

	private int showIndex;

	private Context context;

	public void arrangeMoreView(int x, int y) {


		if (x + moreVwh[0] < Config.widthPixels && x - moreVwh[0] > 0
				&& y + moreVwh[1] < Config.heightPixels
				&& y - (moreVwh[1] * 0.5) > 0) {

			int cx = x - moreVwh[0] / 2;
			int cy = y - moreVwh[1] / 2;

			moreVxy[0] = cx;
			moreVxy[1] = cy;

			RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rllp.leftMargin = moreVxy[0];
			rllp.topMargin = moreVxy[1];

			moreV.setLayoutParams(rllp);
		}

		else if ((x + moreVwh[0] > Config.widthPixels || x - moreVwh[0] < 0)
				&& y + moreVwh[1] < Config.heightPixels
				&& y - (moreVwh[1] * 0.5) > 0) {
			// int cx = x - moreVwh[0] / 2;
			int cy = y - moreVwh[1] / 2;

			// moreVxy[0] = cx;
			moreVxy[1] = cy;

			RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rllp.leftMargin = moreVxy[0];
			rllp.topMargin = moreVxy[1];

			moreV.setLayoutParams(rllp);

		}

		else if (x + moreVwh[0] < Config.widthPixels
				&& x - moreVwh[0] > 0
				&& (y + moreVwh[1] > Config.heightPixels || y
						- (moreVwh[1] * 0.5) < 0)) {

			int cx = x - moreVwh[0] / 2;
			// int cy = y - moreVwh[1] / 2;

			moreVxy[0] = cx;
			// moreVxy[1] = cy;

			RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rllp.leftMargin = moreVxy[0];
			rllp.topMargin = moreVxy[1];

			moreV.setLayoutParams(rllp);

		}

	}

	public void initMoreView() {

		// RelativeLayout.LayoutParams rllp1 = new RelativeLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//
		// rllp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
		// rllp1.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);

		Resources rec = getResources();
		// BitmapDrawable
		BitmapDrawable bitmapDrawable = (BitmapDrawable) rec
				.getDrawable(R.drawable.more1);
		// 得到Bitmap
		Bitmap bitmap = bitmapDrawable.getBitmap();

		moreVwh[0] = bitmap.getWidth();
		moreVwh[1] = bitmap.getHeight();


		moreVxy[0] = Config.widthPixels - moreVwh[0] * 2;
		moreVxy[1] = (int) (moreVwh[1] * 0.3);

		RelativeLayout.LayoutParams rllp1 = new RelativeLayout.LayoutParams(
				moreVwh[0], moreVwh[1]);

		rllp1.leftMargin = moreVxy[0];
		rllp1.topMargin = moreVxy[1];

		moreV.setLayoutParams(rllp1);

	}

	private void arrangeFrameView() {

		// moreVxy[0] = x;
		// moreVxy[1] = y;
		//
		// int cx = moreVxy[0] - moreVwh[0] / 2;
		// int cy = moreVxy[1] - moreVwh[1] / 2;
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (moreVxy[0] > 300) {
			rllp.leftMargin = moreVxy[0] - moreVwh[0] * 2;
		} else {
			rllp.leftMargin = moreVxy[0] + moreVwh[1] * 1;
		}

		// rllp.topMargin = cy;

		frameV.setLayoutParams(rllp);
	}

	private boolean isInBorder(int ex, int ey) {

		return ex > moreVxy[0] && ex < moreVxy[0] + moreVwh[0]
				&& ey > moreVxy[1] && ey < moreVxy[1] + moreVwh[1];
	}

	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		int action = event.getAction();
		int pointAction = action & MotionEvent.ACTION_MASK;
		if (!isTouched
				&& (pointAction == MotionEvent.ACTION_DOWN || pointAction == MotionEvent.ACTION_POINTER_DOWN)) {
			int index = event.getActionIndex();
			downID = event.getPointerId(index);

			if (isInBorder((int) event.getX(index), (int) event.getY(index))) {
			
				isTouched = true;

				begin = System.currentTimeMillis();

			}else{
				autoHide();
			}
		} else if (isTouched && event.getAction() == MotionEvent.ACTION_MOVE) {

			for (int i = 0; i < event.getPointerCount(); i++) {
				int id = event.getPointerId(i);

				if (id == downID) {

					arrangeMoreView((int) event.getX(), (int) event.getY());

					if (frameShow) {

						arrangeFrameView();
					}
					break;
				}
			}

		} else if (isTouched
				&& (pointAction == MotionEvent.ACTION_UP || pointAction == MotionEvent.ACTION_POINTER_UP)) {
			int id = event.getPointerId(event.getActionIndex());
			if (id == downID) {

				downID = -1;
				isTouched = false;

				end = System.currentTimeMillis();
				arrangeMoreView((int) event.getX(), (int) event.getY());

				if (end - begin > 200) {
					return true;
				}

			}
		}
		
		

		return super.onInterceptTouchEvent(event);
	}

	
	public Menu(final Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		View view = View.inflate(context, R.layout.menu, null);
		addView(view);
		
		
		
		backV = findViewById(R.id.back);
		homeV = findViewById(R.id.home);
		settingsV = findViewById(R.id.settings);
		helpV = findViewById(R.id.help);
		exitV = findViewById(R.id.exit);
		aboutV = findViewById(R.id.about);

		frameV = findViewById(R.id.frame);
		moreV = findViewById(R.id.more);

		views[0] = backV;
		views[1] = homeV;
		views[2] = settingsV;
		views[3] = helpV;
		views[4] = exitV;
		views[5] = aboutV;


		moreV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			
//			if(frameShow==false){
//				show();
//			}
//			else
//			{
//				hide();
//			}
				if (frameV.getVisibility() == View.GONE) {
				
					show();
				} else if (frameV.getVisibility() == View.VISIBLE) {
					
					hide();
				}
			}
		});

		backV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("=-------------------------------------------=",
						"backV click");
			

				BagelPlayVmaStub.getInstance().sendKeyData(
						KeyEvent.KEYCODE_BACK, KeyEvent.ACTION_DOWN);
				BagelPlayVmaStub.getInstance().sendKeyData(
						KeyEvent.KEYCODE_BACK, KeyEvent.ACTION_UP);

			}
		});

		homeV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("=-------------------------------------------=",
						"homeV click");
				

//				BagelPlayVmaStub.getInstance().sendKeyData(
//						KeyEvent.KEYCODE_HOME, KeyEvent.ACTION_DOWN);
//				BagelPlayVmaStub.getInstance().sendKeyData(
//						KeyEvent.KEYCODE_HOME, KeyEvent.ACTION_UP);
				
				
				
				
//				if (Log.isShow) {
//					
//					
//					if (!isOnTouch) {
//
//						Log.isDiyChooseModel=true;
//						((BagelPlayActivity) context).chooseTouchModel();
//						// Intent intent = new
//						// Intent(context,BagelPlayActivity.class);
//						//
//						// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						// context.startActivity(intent);
//						isOnTouch = !isOnTouch;
//					} else {
//						Log.isDiyChooseModel=false;
//						((BagelPlayActivity) context).recoverModel();
//						
//						isOnTouch = !isOnTouch;
//					}
//					hide();
//
//				}
			}
		});

		settingsV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(context,
						BagelPlaySettingActivity.class);
				context.startActivity(intent);
				
				hide();
				
			}
		});

		aboutV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Log.isShow) {
					if (getCurrentModel(context)==DoMotionViewManager.DOMOTION_SCREEN_TOUCH) {

						((BagelPlayActivity) context).chooseMouseModel();
						// Intent intent = new
						// Intent(context,BagelPlayActivity.class);
						//
						// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// context.startActivity(intent);
						
					} else if(getCurrentModel(context)==DoMotionViewManager.DOMOTION_SCREEN_MOUSE){

						((BagelPlayActivity) context).chooseTouchModel();
						
						
					}
					hide();

				} else {
					final MessageDialog dialog = new MessageDialog(context);

					Window dialogWindow = dialog.getWindow();
					// WindowManager.LayoutParams lp =
					// dialogWindow.getAttributes();
					dialogWindow.setGravity(Gravity.TOP);

					dialog.setTit(getResources().getString(
							R.string.setting_about));
					// dialog.setMess("版本: 2.7.20\n日期: 6-2-2015\n\n意见反馈\nQQ：234631578\n邮箱：philip@biggifi.com\n\n版权 ⊙2014 BiggiFi Mobile, Inc.");
					dialog.setMess(getResources().getString(R.string.info2));
					dialog.show();

					hide();
				}

			}
		});

		helpV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, HelpActivity.class);
				context.startActivity(intent);
				hide();
			}
		});

		exitV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				BagelPlayVmaStub.getInstance().sendServerClose();
				BagelPlayActivity.getInstance().exit();

			}
		});

	}
	
	
	
	private static int getCurrentModel(Context context){
		
		
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		int model = mPrefs.getInt("current_model", -1);
		return model;
	}

	public void show() {
		frameShow = true;
		arrangeFrameView();

		frameV.setVisibility(View.VISIBLE);
		for (int i = 0; i < views.length; i++) {
			views[i].setVisibility(View.GONE);
		}
		showIndex = 0;
		handler.sendEmptyMessage(MSG_WHAT_SHOW);
	}

	public void hide() {
		
		frameShow = false;
		handler.removeMessages(MSG_WHAT_SHOW);

		// 初始化
		Animation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 设置动画时间
		scaleAnimation.setDuration(100);

		// 初始化
		// Animation scaleAnimation2 = new ScaleAnimation(1.0f, 0.1f, 1.0f,
		// 0.1f,
		// Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		// 0.5f);
		// scaleAnimation2.setDuration(200);

		scaleAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				doHide();
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				
				//doHide();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

		});

		for (int i = 1; i < views.length; i++) {
			views[i].startAnimation(scaleAnimation);
		}

		views[0].startAnimation(scaleAnimation);
	}

	private void doHide() {
		frameShow = false;
		frameV.setVisibility(View.GONE);
		for (int i = 0; i < views.length; i++) {
			views[i].setVisibility(View.GONE);
		}
	}
//
	private void doShow() {
		frameShow = true;
		views[showIndex].setVisibility(View.VISIBLE);
		if (++showIndex == views.length)
			return;
		
		handler.sendEmptyMessageDelayed(MSG_WHAT_SHOW, 50);
	}
	
	public void autoHide(){
		 if (frameShow == true) {
			 hide();
		 }
	}
	


}
