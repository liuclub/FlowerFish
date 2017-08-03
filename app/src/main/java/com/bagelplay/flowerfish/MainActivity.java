package com.bagelplay.flowerfish;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import com.bagelplay.flowerfish.utils.SoundUtil;
import com.bagelplay.flowerfish.view.FinishGameView;
import com.bagelplay.flowerfish.view.FllScreenVideoView;
import com.bagelplay.flowerfish.view.FlowerView;
import com.bagelplay.flowerfish.view.NumGameCongrationView;
import com.bagelplay.flowerfish.view.NumberGameView;
import com.bagelplay.flowerfish.view.RightWrongView;

public class MainActivity extends AppCompatActivity {
    String Tag = "MainActivity";

    FlowerView mFv_flower;

    RightWrongView mRwView;

    NumberGameView mNgvView;

    NumGameCongrationView mNgcView;

    FinishGameView mFgvView;

    SoundUtil mSoundUtil;


    FllScreenVideoView mVvVideo;

    int numGameCurrentStage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //去除黑边
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mFv_flower = (FlowerView) findViewById(R.id.fv_flower);


        mFv_flower.setonFlowerChooseListener(new FlowerView.FlowerChoose() {
            @Override
            public void flowerChoose(int chooseid) {
                //Toast.makeText(MainActivity.this,"第"+chooseid+"关",Toast.LENGTH_SHORT).show();

                Log.d(Tag, "第" + chooseid + "关");
            }
        });

        mRwView = (RightWrongView) findViewById(R.id.rw_view);

        mRwView.setOnRightWrongClickLinstener(new RightWrongView.RightWrongClickLinstener() {
            @Override
            public void RightClick() {
                Log.d(Tag, "Rightclick");

                mSoundUtil.startPlaySound(R.raw.agree);


                if (numGameCurrentStage == 0){

                    mNgvView.setVisibility(View.VISIBLE);
                    mSoundUtil.startPlaySound(R.raw.num_game_introduce);
                }else if(numGameCurrentStage == 1){
                    mNgvView.setVisibility(View.VISIBLE);

                }else if(numGameCurrentStage == 2){
                    mNgvView.setVisibility(View.VISIBLE);
                }





            }

            @Override
            public void WrongClick() {
                Log.d(Tag, "Wrongclick");

                mSoundUtil.startPlaySound(R.raw.disagree);

                // mRwView.setVisibility(View.GONE);



//                    mNgvView.setVisibility(View.VISIBLE);
//                    mSoundUtil.startPlaySound(R.raw.num_game_introduce);

                    mRwView.setVisibility(View.GONE);






            }
        });


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
                    mSoundUtil.startPlaySound(R.raw.next_stage);
                    mNgvView.setVisibility(View.GONE);
                    mNgcView.setVisibility(View.VISIBLE);
                    mNgcView.startAnimation();


                } else if (currentstage == 3) {
                    mSoundUtil.startPlaySound(R.raw.congration);
                    mNgvView.setVisibility(View.GONE);
                    mNgcView.setVisibility(View.VISIBLE);
                    mNgcView.startAnimation();

                }


            }

            @Override
            public void numGameChooseRight() {

            }

            @Override
            public void numGameChooseWrong() {

            }
        });


        mNgcView = (NumGameCongrationView) findViewById(R.id.ngc_view);

        mNgcView.setOnNumGameCongrationFinishListener(new NumGameCongrationView.NumGameCongrationFinishListener() {
            @Override
            public void numGameConfigurationFinish() {

                if (numGameCurrentStage == 3) {

                    mNgcView.setVisibility(View.GONE);

                    mRwView.setVisibility(View.GONE);
                    mFgvView.setVisibility(View.VISIBLE);

                    mFgvView.startAnimation();

                    mSoundUtil.startPlaySound(R.raw.win_petal);


                }
                else{
                    mNgcView.setVisibility(View.GONE);

                    mRwView.setVisibility(View.VISIBLE);

                }


            }
        });


        mFgvView = (FinishGameView) findViewById(R.id.fgv_view);

        mFgvView.setOnRightWrongClickLinstener(new FinishGameView.RightWrongClickLinstener() {
            @Override
            public void RightClick() {
                mSoundUtil.startPlaySound(R.raw.agree);

            }

            @Override
            public void WrongClick() {

                mFgvView.setVisibility(View.GONE);

                mSoundUtil.startPlaySound(R.raw.disagree);

            }
        });


        mSoundUtil = new SoundUtil(MainActivity.this);


        mVvVideo = (FllScreenVideoView) findViewById(R.id.vv_video);

//        mVvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mVvVideo.stopPlayback();
//
//                mVvVideo.setVisibility(View.GONE);
//
//            }
//        });
//
//
//
//
//
//
//
//        mVvVideo.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
//
//       // mVvVideo.setVideoPath("http://112.126.81.84/video/video_book.mp4");
//        mVvVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome));
//
//
//
//        mVvVideo.start();

    }


}
