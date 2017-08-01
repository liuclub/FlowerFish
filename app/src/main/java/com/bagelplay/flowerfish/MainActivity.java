package com.bagelplay.flowerfish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
            }

            @Override
            public void WrongClick() {
                Log.d(Tag, "Wrongclick");

                mRwView.setVisibility(View.GONE);
            }
        });


        mNgvView= (NumberGameView) findViewById(R.id.ngv_view);

        mNgvView.setOnNumGameFinishListener(new NumberGameView.NumGameFinishListener() {
            @Override
            public void numGameFinish() {
                Log.d(Tag, "gamewangchen");

                mNgvView.setVisibility(View.GONE);

                mNgcView.startAnimation();




            }
        });
        mNgcView = (NumGameCongrationView) findViewById(R.id.ngc_view);

        mNgcView.setOnNumGameCongrationFinishListener(new NumGameCongrationView.NumGameCongrationFinishListener() {
            @Override
            public void numGameConfigurationFinish() {
                mNgcView.setVisibility(View.GONE);

                mFgvView.startAnimation();
            }
        });


        mFgvView= (FinishGameView) findViewById(R.id.fgv_view);

        mFgvView.setOnRightWrongClickLinstener(new FinishGameView.RightWrongClickLinstener() {
            @Override
            public void RightClick() {

                Toast.makeText(MainActivity.this,"该按钮还没写好",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void WrongClick() {
                mFgvView.setVisibility(View.GONE);
            }
        });

    }


}
