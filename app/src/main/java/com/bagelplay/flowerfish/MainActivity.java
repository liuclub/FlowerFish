package com.bagelplay.flowerfish;


import android.app.Activity;
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

import com.bagelplay.flowerfish.utils.SoundUtil;
import com.bagelplay.flowerfish.view.FinishGameView;
import com.bagelplay.flowerfish.view.FllScreenVideoView;
import com.bagelplay.flowerfish.view.FlowerView;
import com.bagelplay.flowerfish.view.GamePauseView;
import com.bagelplay.flowerfish.view.NumGameCongrationView;
import com.bagelplay.flowerfish.view.NumGameReleaseView;
import com.bagelplay.flowerfish.view.NumPauseButtonView;
import com.bagelplay.flowerfish.view.NumberGameView;
import com.bagelplay.flowerfish.view.PauseButtonView;
import com.bagelplay.flowerfish.view.RightWrongView;
import com.bagelplay.sdk.cocos.SDKCocosManager;

public class MainActivity extends AppCompatActivity {
    String Tag = "MainActivity";

    NumGameReleaseView mNgrView;

    FlowerView mFvFlower;

   /* RightWrongView mRwView;*/

    NumberGameView mNgvView;

    NumGameCongrationView mNgcView;

    FinishGameView mFgvView;

    SoundUtil mSoundUtil;


    FllScreenVideoView mVvVideo;

    int numGameCurrentStage;

    NumPauseButtonView mNpbvView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除黑边
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        SDKCocosManager.getInstance(this).addWindowCallBack(this);


        mSoundUtil = new SoundUtil(MainActivity.this);

        //暂停按钮

        mNpbvView = (NumPauseButtonView) findViewById(R.id.npbv_view);

        mNpbvView.setOnPauseButtonViewClickLinstener(new NumPauseButtonView.PauseButtonViewClickLinstener() {
            @Override
            public void pauseHomeClick() {

                mNgrView.setVisibility(View.VISIBLE);




                mSoundUtil.stopPlaySound();
            }
        });



        //停止暂停控件
        mNgrView = (NumGameReleaseView) findViewById(R.id.ngr_view);
        mNgrView.setOnReleaseButtonViewClickLinstener(new NumGameReleaseView.ReleaseButtonViewClickLinstener() {
            @Override
            public void releaseButtonClick() {

                mNgrView.setVisibility(View.GONE);
            }
        });





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
            public void flowerChoose(int chooseid,boolean isPassed) {

                Log.d(Tag, "第" + chooseid + "关"+"通过"+isPassed);



                mFvFlower.setVisibility(View.GONE);

                mNgvView.setVisibility(View.VISIBLE);
            }
        });




      /*  //判断
        mRwView = (RightWrongView) findViewById(R.id.rw_view);

        mRwView.setOnRightWrongClickLinstener(new RightWrongView.RightWrongClickLinstener() {
            @Override
            public void RightClick() {
                Log.d(Tag, "Rightclick");

                mSoundUtil.startPlaySound(R.raw.agree);

                mRwView.setVisibility(View.GONE);
                if (numGameCurrentStage == 0) {

                    mNgvView.setVisibility(View.VISIBLE);
                    mSoundUtil.startPlaySound(R.raw.num_game_introduce);
                } else if (numGameCurrentStage == 1) {
                    mNgvView.setVisibility(View.VISIBLE);

                } else if (numGameCurrentStage == 2) {
                    mNgvView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void WrongClick() {
                Log.d(Tag, "Wrongclick");

                mSoundUtil.startPlaySound(R.raw.disagree);



                mRwView.setVisibility(View.GONE);

                mFvFlower.setVisibility(View.VISIBLE);

                numGameCurrentStage = 0;

                mNgvView.restartNumGame();


            }
        });*/


        //游戏
        mNgvView = (NumberGameView) findViewById(R.id.ngv_view);

        mNgvView.setOnNumGameFinishListener(new NumberGameView.NumGameFinishListener() {


            @Override
            public void numGameFinish(int currentstage) {
                Log.d(Tag, "gamewangchen");

                numGameCurrentStage = currentstage;
                if (currentstage == 1) {

                    mSoundUtil.startPlaySound(R.raw.next_stage);
                    mNgvView.setVisibility(View.GONE);
                    mNgcView.setVisibility(View.VISIBLE);
                    mNgcView.startAnimation();

                } else if (currentstage == 2) {
                    mSoundUtil.startPlaySound(R.raw.three_stage);
                    mNgvView.setVisibility(View.GONE);
                    mNgcView.setVisibility(View.VISIBLE);
                    mNgcView.startAnimation();


                } else if (currentstage == 3) {
                    mSoundUtil.startPlaySound(R.raw.congration);
                    mNgvView.setVisibility(View.GONE);
                    mNgcView.setVisibility(View.VISIBLE);
                    mNgcView.startAnimation();

                    //通关


                    mFvFlower.setFlowerStagePass(mFvFlower.CURRENT_SATGE);

                }


            }

            @Override
            public void numGameChooseRight() {
                mSoundUtil.startPlaySound(R.raw.right);
            }

            @Override
            public void numGameChooseWrong() {
                mSoundUtil.startPlaySound(R.raw.wrong);
            }
        });



        //鼓励
        mNgcView = (NumGameCongrationView) findViewById(R.id.ngc_view);

        mNgcView.setOnNumGameCongrationFinishListener(new NumGameCongrationView.NumGameCongrationFinishListener() {
            @Override
            public void numGameConfigurationFinish() {

                if (numGameCurrentStage == 3) {

                    mNgcView.setVisibility(View.GONE);
                    mFgvView.setVisibility(View.VISIBLE);
                    mSoundUtil.startPlaySound(R.raw.win_petal);
                    mFgvView.startAnimation();




                } else {
                    mNgcView.setVisibility(View.GONE);

                    mNgvView.setVisibility(View.VISIBLE);

                }


            }
        });


        //奖励
        mFgvView = (FinishGameView) findViewById(R.id.fgv_view);

        mFgvView.setOnRightWrongClickLinstener(new FinishGameView.RightWrongClickLinstener() {
            @Override
            public void RightClick() {
                mSoundUtil.startPlaySound(R.raw.agree);

            }

            @Override
            public void WrongClick() {

                mFgvView.setVisibility(View.GONE);


                numGameCurrentStage = 0;
                mNgvView.restartNumGame();

                mFvFlower.setVisibility(View.VISIBLE);

                mSoundUtil.startPlaySound(R.raw.disagree);

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
        com.bagelplay.sdk.cocos.SDKCocosManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        com.bagelplay.sdk.cocos.SDKCocosManager.getInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();

        SDKCocosManager.getInstance().removeWindowCallBack(this);
    }


}
