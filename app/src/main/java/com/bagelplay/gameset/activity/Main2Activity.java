package com.bagelplay.gameset.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.AppManager;
import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.sdk.cocos.SDKCocosManager;

import java.io.IOException;

public class Main2Activity extends Activity implements View.OnClickListener {
    private ViewFlipper main2_rootview;

    private ImageView main2Hamburger;
    private ImageView main2Salad;
    private RelativeLayout languageContainer;
    private ImageView main2Exit;
    private RelativeLayout main2_videoview_container;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private int currentSectiion;

    private LinearLayout main2_second;

    private boolean click;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除黑边
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main2);
        SDKCocosManager.getInstance(this).addWindowCallBack(this);
        AppManager.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        handler = new Handler();

        main2_rootview = findViewById(R.id.main2_rootview);

        main2_videoview_container = (RelativeLayout) main2_rootview.findViewById(R.id.main2_videoview_container);
        surfaceView = main2_videoview_container.findViewById(R.id.surfaceview);
        surfaceView.setBackgroundColor(Color.TRANSPARENT);
        main2_videoview_container.findViewById(R.id.main2_skip).setOnClickListener(this);

        main2_second = main2_rootview.findViewById(R.id.main2_second);
        main2Hamburger = (ImageView) main2_second.findViewById(R.id.main2_hamburger);
        main2Salad = (ImageView) main2_second.findViewById(R.id.main2_salad);
        languageContainer = (RelativeLayout) main2_second.findViewById(R.id.language_container);
        main2Exit = (ImageView) main2_second.findViewById(R.id.main2_exit);

        //是否再次进入该界面
        boolean enteragain = getIntent().getBooleanExtra("enteragain", false);

        if (!enteragain) {//否，这是第一次进入该界面
            click = false;
            mediaPlayer = new MediaPlayer();
            surfaceHolder = surfaceView.getHolder();
            try {
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.game_introduction));
                surfaceHolder.addCallback(new MyCallBack());
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(mp -> {
                    mediaPlayer.start();
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        next();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (main2_videoview_container.getVisibility() != View.VISIBLE) {
                main2_videoview_container.setVisibility(View.VISIBLE);
            }

        } else {//再次进入该界面
            if (main2_videoview_container.getVisibility() != View.GONE) {
                main2_videoview_container.setVisibility(View.GONE);
            }
            click = true;
            SoundUtil.getInstance(Main2Activity.this).startPlaySound(R.raw.continue_or_exit_game);
        }
        findViewById(R.id.language_en).setOnClickListener(this);
        findViewById(R.id.language_zh).setOnClickListener(this);
        main2Hamburger.setOnClickListener(this);
        main2Salad.setOnClickListener(this);
        main2Exit.setOnClickListener(this);

        currentSectiion = 1;
    }

    private void next() {
        main2_rootview.setInAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
        main2_rootview.setOutAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_out_left));
        main2_rootview.showNext();

        main2_rootview.setLayoutAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                click=false;
                SoundUtil.getInstance(Main2Activity.this).startPlaySoundWithListener(R.raw.hamburger_or_salad, new SoundUtil.MediaPlayListener() {
                    @Override
                    public void onPlayerCompletion() {
                        click = true;
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.language_en:
                if (click) {
                    startActivity(
                            new Intent(Main2Activity.this, EvaluationGame2Activity.class)
                                    .putExtra("language", "en")
                                    .putExtra("section", currentSectiion));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Main2Activity.this.finish();
                        }
                    }, 1000);
                }
                break;
            case R.id.language_zh:
                if (click) {
                    startActivity(
                            new Intent(Main2Activity.this, EvaluationGame2Activity.class)
                                    .putExtra("language", "zh")
                                    .putExtra("section", currentSectiion));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Main2Activity.this.finish();
                        }
                    }, 1000);
                }
                break;
            case R.id.main2_hamburger:
                if (click) {
                    click = false;
                    LinearLayout parent = (LinearLayout) main2Hamburger.getParent();
                    parent.setGravity(Gravity.CENTER_HORIZONTAL);
                    languageContainer.setVisibility(View.VISIBLE);
                    main2Salad.setVisibility(View.GONE);
                    currentSectiion = 1;
                    SoundUtil.getInstance(Main2Activity.this).startPlaySoundWithListener(R.raw.select_language, new SoundUtil.MediaPlayListener() {
                        @Override
                        public void onPlayerCompletion() {
                            click = true;
                        }
                    });
                }
                break;
            case R.id.main2_salad:
                if (click) {
                    click = false;
                    LinearLayout parent = (LinearLayout) main2Hamburger.getParent();
                    parent.setGravity(Gravity.CENTER_HORIZONTAL);
                    main2Hamburger.setVisibility(View.GONE);
                    languageContainer.setVisibility(View.VISIBLE);
                    currentSectiion = 2;
                    SoundUtil.getInstance(Main2Activity.this).startPlaySoundWithListener(R.raw.select_language, new SoundUtil.MediaPlayListener() {
                        @Override
                        public void onPlayerCompletion() {
                            click = true;
                        }
                    });
                }
                break;
            case R.id.main2_exit:
                if (click) {
                    if (languageContainer.getVisibility() != View.GONE) {
                        switch (currentSectiion) {
                            case 1:
                                main2Salad.setVisibility(View.VISIBLE);

                                break;
                            case 2:
                                main2Hamburger.setVisibility(View.VISIBLE);
                                break;
                        }

                        LinearLayout parent = (LinearLayout) main2Hamburger.getParent();
                        parent.setGravity(Gravity.LEFT);
                        languageContainer.setVisibility(View.GONE);
                    } else {//开始界面:提示是否退出应用
                        AppManager.getInstance().AppExit(this);
                    }
                }
                break;
            case R.id.main2_skip:
//                main2_videoview_container.setVisibility(View.GONE);
                mediaPlayer.stop();
                mediaPlayer.release();
//                click = false;
//                SoundUtil.getInstance(Main2Activity.this).startPlaySoundWithListener(R.raw.hamburger_or_salad, new SoundUtil.MediaPlayListener() {
//                    @Override
//                    public void onPlayerCompletion() {
//                        click = true;
//                    }
//                });
                next();
                break;
        }
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
        SoundUtil.getInstance(Main2Activity.this).stopPlaySound();
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

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
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
            AppManager.getInstance().AppExit(Main2Activity.this);
        }
    }
}
