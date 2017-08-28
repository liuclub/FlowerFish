package com.bagelplay.gameset.activity;


import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.gameset.view.FllScreenVideoView;
import com.bagelplay.gameset.view.FlowerView;
import com.bagelplay.sdk.cocos.SDKCocosManager;

public class MainActivity extends AppCompatActivity {
    String Tag = "MainActivity";


    FlowerView mFvFlower;

    FllScreenVideoView mVvVideo;

    public static final int NUMGAME_REQUESTCODE = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NUMGAME_REQUESTCODE && resultCode == RESULT_OK) {

            mFvFlower.setFlowerStagePass(mFvFlower.CURRENT_SATGE);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除黑边
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        SDKCocosManager.getInstance(this).addWindowCallBack(this);


        //视频
        mVvVideo = (FllScreenVideoView) findViewById(R.id.vv_video);

        mVvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVvVideo.stopPlayback();

                mVvVideo.setVisibility(View.GONE);

                mFvFlower.setVisibility(View.VISIBLE);

            }
        });

        mVvVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // mVvVideo.setVideoPath("http://112.126.81.84/video/video_book.mp4");

        mVvVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome));


        mVvVideo.start();


        mVvVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        //鲜花控件
        mFvFlower = (FlowerView) findViewById(R.id.fv_flower);


        mFvFlower.setonFlowerChooseListener(new FlowerView.FlowerChoose() {
            @Override
            public void flowerChoose(int chooseid, boolean isPassed) {

                Log.d(Tag, "第" + chooseid + "关" + "通过" + isPassed);


                //蓝色
                if (chooseid == 2) {


                    Intent intent = new Intent(MainActivity.this, NumGameActivity.class);


                    startActivityForResult(intent, NUMGAME_REQUESTCODE);


                    SoundUtil.getInstance(MainActivity.this).startPlaySound(R.raw.num_game_introduce);

                } else if (chooseid == 0) {
                    Intent intent = new Intent(MainActivity.this, EvaluationGameActivity.class);

                    startActivity(intent);
                }


            }
        });


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKCocosManager.getInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();

        SDKCocosManager.getInstance().removeWindowCallBack(this);
    }


}
