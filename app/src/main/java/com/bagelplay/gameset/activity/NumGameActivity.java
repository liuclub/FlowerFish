package com.bagelplay.gameset.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.numgame.view.NumGameCongrationView;
import com.bagelplay.gameset.numgame.view.NumGameReleaseView;
import com.bagelplay.gameset.numgame.view.NumPauseButtonView;
import com.bagelplay.gameset.numgame.view.NumberGameView;
import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.gameset.view.FinishGameView;
import com.bagelplay.gameset.view.GameProgressView;
import com.bagelplay.sdk.cocos.SDKCocosManager;


public class NumGameActivity extends AppCompatActivity {
    String Tag = "NumGameActivity";

    NumGameReleaseView mNgrView;

    NumberGameView mNgvView;

    NumGameCongrationView mNgcView;

    FinishGameView mFgvView;

    NumPauseButtonView mNpbvView;

    int numGameCurrentStage;

    private Handler timeHandler;

    GameProgressView mGpV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //去除黑边
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_num_game);

        SDKCocosManager.getInstance(this).addWindowCallBack(this);

        timeHandler = new Handler();


        //暂停按钮

        mNpbvView = (NumPauseButtonView) findViewById(R.id.npbv_view);

        mNpbvView.setOnPauseButtonViewClickLinstener(new NumPauseButtonView.PauseButtonViewClickLinstener() {
            @Override
            public void pauseHomeClick() {

                mNgrView.setVisibility(View.VISIBLE);


                SoundUtil.getInstance(NumGameActivity.this).stopPlaySound();


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

       //游戏进程
        mGpV= (GameProgressView) findViewById(R.id.gp_view);



        //游戏
        mNgvView = (NumberGameView) findViewById(R.id.ngv_view);

        mNgvView.setOnNumGameFinishListener(new NumberGameView.NumGameFinishListener() {


            @Override
            public void numGameFinish(final int currentstage) {
                timeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGpV.setVisibility(View.GONE);
                        numGameCurrentStage = currentstage;
                        if (currentstage == 1) {
                            SoundUtil.getInstance(NumGameActivity.this).startPlaySound(R.raw.next_stage);
                            //mSoundUtil.startPlaySound(R.raw.next_stage);
                            mNgvView.setVisibility(View.GONE);
                            mNgcView.setVisibility(View.VISIBLE);


                            mNgcView.startAnimation(9);

                        } else if (currentstage == 2) {
                            SoundUtil.getInstance(NumGameActivity.this).startPlaySound(R.raw.three_stage);
                            // mSoundUtil.startPlaySound(R.raw.three_stage);
                            mNgvView.setVisibility(View.GONE);
                            mNgcView.setVisibility(View.VISIBLE);
                            mNgcView.startAnimation(4);


                        } else if (currentstage == 3) {
                            SoundUtil.getInstance(NumGameActivity.this).startPlaySound(R.raw.congration);
                            // mSoundUtil.startPlaySound(R.raw.congration);
                            mNgvView.setVisibility(View.GONE);
                            mNgcView.setVisibility(View.VISIBLE);
                            mNgcView.startAnimation(9);

                            //通关


                        }

                    }
                }, 2000);


            }

            @Override
            public void numGameChooseRight(int num) {
                SoundUtil.getInstance(NumGameActivity.this).startPlaySound(R.raw.right);

                mGpV.setChooseNum(num);
            }

            @Override
            public void numGameChooseWrong() {
                SoundUtil.getInstance(NumGameActivity.this).startPlaySound(R.raw.wrong);

            }
        });


        mNgvView.initGame();


        //鼓励
        mNgcView = (NumGameCongrationView) findViewById(R.id.ngc_view);

        mNgcView.setOnNumGameCongrationFinishListener(new NumGameCongrationView.NumGameCongrationFinishListener() {
            @Override
            public void numGameConfigurationFinish() {
                if (numGameCurrentStage == 3) {

                    mNgcView.setVisibility(View.GONE);
                    mFgvView.setVisibility(View.VISIBLE);
                    SoundUtil.getInstance(NumGameActivity.this).startPlaySound(R.raw.win_petal);
                    //  mSoundUtil.startPlaySound(R.raw.win_petal);
                    mFgvView.startAnimation();


                } else {
                    mNgcView.setVisibility(View.GONE);

                    mNgvView.setVisibility(View.VISIBLE);
                    mGpV.setVisibility(View.VISIBLE);

                    mNgvView.initGame();


                }


            }
        });


        //奖励
        mFgvView = (FinishGameView) findViewById(R.id.fgv_view);


        mFgvView.setOnTimeFinishListener(new FinishGameView.FinishTimeLinstener() {
            @Override
            public void timeFinish() {
                setResult(RESULT_OK);
                finish();

            }
        });



    /*    mFgvView.setOnRightWrongClickLinstener(new FinishGameView.RightWrongClickLinstener() {
            @Override
            public void RightClick() {
                SoundUtil.getInstance(NumGameActivity.this).startPlaySound(R.raw.agree);
                //mSoundUtil.startPlaySound(R.raw.agree);

            }

            @Override
            public void WrongClick() {


                timeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFgvView.setVisibility(View.GONE);


                        numGameCurrentStage = 0;
                        mNgvView.restartNumGame();


                        SoundUtil.getInstance(NumGameActivity.this).startPlaySound(R.raw.disagree);


//                        Intent intent = new Intent();
//                        intent.putExtra("Stage", icons.get(position).first);
//                        intent.putExtra("choosenName", icons.get(position).second);
//
//
//                        setResult(RESULT_OK, intent);

                        setResult(RESULT_OK);
                        finish();


                    }
                }, 1000);


            }
        });*/





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
