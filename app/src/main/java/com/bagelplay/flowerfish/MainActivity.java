package com.bagelplay.flowerfish;

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





    VideoView mVvVideo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






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
            }

            @Override
            public void WrongClick() {
                Log.d(Tag, "Wrongclick");

                mSoundUtil.startPlaySound(R.raw.disagree);

               // mRwView.setVisibility(View.GONE);
            }
        });


        mNgvView = (NumberGameView) findViewById(R.id.ngv_view);

        mNgvView.setOnNumGameFinishListener(new NumberGameView.NumGameFinishListener() {
            @Override
            public void numGameFinish() {
                Log.d(Tag, "gamewangchen");

                mNgvView.setVisibility(View.GONE);

                mNgcView.startAnimation();


                mSoundUtil.startPlaySound(R.raw.congration);

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
                mNgcView.setVisibility(View.GONE);

                mFgvView.startAnimation();

                mSoundUtil.startPlaySound(R.raw.win_petal);


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


        mVvVideo= (VideoView) findViewById(R.id.vv_video);

        mVvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVvVideo.stopPlayback();

                mVvVideo.setVisibility(View.GONE);

            }
        });

        mVvVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });




        mVvVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome));


        mVvVideo.start();

    }


}
