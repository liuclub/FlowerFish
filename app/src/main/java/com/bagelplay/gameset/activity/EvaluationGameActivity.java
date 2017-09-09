package com.bagelplay.gameset.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.evagame.view.EvaluationGameView;

import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.gameset.view.GameProgressView;
import com.bagelplay.sdk.cocos.SDKCocosManager;


public class EvaluationGameActivity extends AppCompatActivity {
    private static String TAG = EvaluationGameActivity.class.getSimpleName();


    private EvaluationGameView mEvaGameView;

    private GameProgressView mGameProgressView;

    private boolean evaFinish=true;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_evaluation_game);
        mContext=EvaluationGameActivity.this;
        evaFinish=getIntent().getBooleanExtra("evaFinish",false);



        SDKCocosManager.getInstance(this).addWindowCallBack(this);
        // mEvaView = (EvaluationView) findViewById(R.id.eva_view);

        mGameProgressView= (GameProgressView) findViewById(R.id.gp_view);



        // mGpV.setChooseNum(num);
        mEvaGameView = (EvaluationGameView) findViewById(R.id.eva_game_view);

        mEvaGameView.setOnEvaStageStateListener(new EvaluationGameView.OnEvaStageStateListener() {
            @Override
            public void finishStageNum(int num) {
                mGameProgressView.setChooseNum(num);
            }

            @Override
            public void startStage(int numStage) {

                mGameProgressView.setProgressCount(numStage);

                mGameProgressView.setVisibility(View.VISIBLE);
            }

            @Override
            public void endStage() {
                mGameProgressView.setVisibility(View.GONE);
            }

            @Override
            public void gameFinish() {




                setResult(RESULT_OK);
                finish();
            }
        });

        mEvaGameView.init(evaFinish);



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

        SoundUtil.getInstance(mContext).stopPlaySound();

        SDKCocosManager.getInstance().removeWindowCallBack(this);
    }
}
