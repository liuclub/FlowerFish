package com.bagelplay.sdk.common;


import com.bagelplay.sdk.common.util.FileDir;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public abstract class Logo extends Activity {

    private SDKManager sdkManager;

    private ImageView logoIV;

    private int step;

    private boolean isExit = true;

    private boolean isStopWantPlayerIDs;

    private OnShowPlayerLinstener ospl = new OnShowPlayerLinstener() {

        @Override
        public void OnGetPlayerIDs(int[] playerIDs, int num) {
            if (step == 1 && num > 0) {
                isStopWantPlayerIDs = true;
                sdkManager.removeOnShowPlayerLinstener(ospl);
                doStep(2);
            }
        }

    };

    private SDKManager.OnSDKKeyListener sdkKeyListener = new SDKManager.OnSDKKeyListener() {

        @Override
        public void OnSDKKeyDown(int playerOrder, int keyCode) {
            if (playerOrder < 100) {      //遥控器
                if (step == 1) {
                    doStep(4);
                }
            } else {       //游戏手柄
                if (step == 1) {
                    doStep(3);
                }
            }
        }

        @Override
        public void OnSDKKeyUp(int playerOrder, int keyCode) {

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        sdkManager = getSDKManager();
        if (sdkManager.isNormalLaunched()) {
            sdkManager.addWindowCallBack(this);
            sdkManager.setOnShowPlayerLinstener(ospl);

            doStep(1);

            wantPlayerIDs();

            sdkManager.setOnSDKKeyListener(sdkKeyListener);
        }


    }

	/*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(event.getAction() == KeyEvent.ACTION_DOWN)
		{
			doFinish();
		}
		return super.onKeyDown(keyCode, event);
	}*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (step == 1) {
                doStep(2);
            } else if (step == 2 || step == 3 || step == 4) {
                doFinish();
            }
        }
        return super.onTouchEvent(event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && (step == 3 || step == 4 || step == 2))
            doFinish();
        if (sdkManager.dispatchKeyEvent(event))
            return true;
        return super.dispatchKeyEvent(event);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && (step == 3 || step == 4 || step == 2))
            doFinish();
        if (sdkManager.dispatchGenericMotionEvent(event))
            return true;
        return super.dispatchGenericMotionEvent(event);
    }


    @Override
    protected void onStart() {
        super.onStart();

        sdkManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        sdkManager.onStop();
    }

    protected void onResume() {
        super.onResume();

        sdkManager.onResume();


    }

    protected void onPause() {
        super.onPause();

        sdkManager.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        sdkManager.removeWindowCallBack(this);

        if (isExit) {
            sdkManager.destroy();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }.start();

        }

    }


    private void wantPlayerIDs() {
        new Thread() {
            @Override
            public void run() {
                while (!isStopWantPlayerIDs) {
                    sdkManager.wantPlayerIDs();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

    }

    private void doStep(int step) {
        if (step == 1) {
            logoIV = new ImageView(this);
            Bitmap pic = null;
            try {
                pic = BitmapFactory.decodeStream(getAssets().open(FileDir.getInstance().LOGO));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            logoIV.setImageBitmap(pic);
            logoIV.setScaleType(ScaleType.FIT_XY);

            setContentView(logoIV);
        } else if (step == 2) {                        //手机说明
            logoIV = new ImageView(this);
            Bitmap pic = null;
            try {
                pic = BitmapFactory.decodeStream(getAssets().open(FileDir.getInstance().HOWTOPLAY_PHONE));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            logoIV.setImageBitmap(pic);
            logoIV.setScaleType(ScaleType.FIT_XY);

            setContentView(logoIV);
        } else if (step == 3) {                   //手柄说明

            logoIV = new ImageView(this);
            Bitmap pic = null;
            try {
                pic = BitmapFactory.decodeStream(getAssets().open(FileDir.getInstance().HOWTOPLAY_GHANDLE));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            logoIV.setImageBitmap(pic);
            logoIV.setScaleType(ScaleType.FIT_XY);

            setContentView(logoIV);
        } else if (step == 4)                    //遥控器说明
        {
            logoIV = new ImageView(this);
            Bitmap pic = null;
            try {
                pic = BitmapFactory.decodeStream(getAssets().open(FileDir.getInstance().HOWTOPLAY_RCONTROLLER));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            logoIV.setImageBitmap(pic);
            logoIV.setScaleType(ScaleType.FIT_XY);

            setContentView(logoIV);
        }
        this.step = step;
    }

    private void doFinish() {
        isExit = false;
        Intent intent = new Intent(Logo.this, getNextClass());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
        isStopWantPlayerIDs = true;
        sdkManager.removeOnShowPlayerLinstener(ospl);
        sdkManager.removeOnSDKKeyListener(sdkKeyListener);
        finish();
    }

    public abstract Class getNextClass();

    public abstract SDKManager getSDKManager();

}
