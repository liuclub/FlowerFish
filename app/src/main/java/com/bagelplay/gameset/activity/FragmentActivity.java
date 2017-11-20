package com.bagelplay.gameset.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.AppManager;
import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.sdk.cocos.SDKCocosManager;


public class FragmentActivity extends AppCompatActivity {
private boolean enteragain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);
        SDKCocosManager.getInstance(this).addWindowCallBack(this);
        AppManager.getInstance().addActivity(this);
        TestFragment testFragment=new TestFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, testFragment).commit();

        //是否再次进入该界面
        enteragain = getIntent().getBooleanExtra("enteragain", false);

    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (SDKCocosManager.getInstance().dispatchKeyEvent(event))
            return true;
        return super.dispatchKeyEvent(event);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        if (SDKCocosManager.getInstance().dispatchGenericMotionEvent(ev))
            return true;
        return super.dispatchGenericMotionEvent(ev);

    }

    protected void onStop() {
        super.onStop();
        SDKCocosManager.getInstance().onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SDKCocosManager.getInstance().onPause();
        SoundUtil.getInstance(FragmentActivity.this).stopPlaySound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKCocosManager.getInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SDKCocosManager.getInstance().removeWindowCallBack(this);
    }

    private long mBackTime;

    @Override
    public void onBackPressed() {
        // 连续点击返回退出
        long nowTime = SystemClock.elapsedRealtime();
        long diff = nowTime - mBackTime;
        if (diff >= 500) {
            mBackTime = nowTime;
        } else {
            AppManager.getInstance().AppExit(FragmentActivity.this);
        }
    }

    public boolean isEnteragain() {
        return enteragain;
    }
}
