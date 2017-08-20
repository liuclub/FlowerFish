package com.bagelplay.gameset.numgame.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagelplay.gameset.R;

/**
 * Created by zhangtianjie on 2017/8/1.
 */

public class TimerView extends LinearLayout {

    TextView mTvTimeShow;
    private int i = 0;
    private int TIME = 1000;

    private Handler timeHandler;


    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.timer_view_layout, this, true);

        mTvTimeShow = (TextView) findViewById(R.id.tv_time_show);



    }


    public void startTimer(){
        timeHandler = new Handler();
        i = 0;
        timeHandler.postDelayed(runnable, TIME); //每隔1s执行
    }

    public void reStartTimer(){

        if(timeHandler!=null) {

            //timeHandler.removeCallbacks(runnable);
            i = 0;
            mTvTimeShow.setText(Integer.toString(i));

            //timeHandler.postDelayed(runnable, TIME);
        }

    }

    public void stopTimer(){
        if(timeHandler!=null){
            timeHandler.removeCallbacks(runnable);
            timeHandler = null;
            i = 0;
            mTvTimeShow.setText(Integer.toString(i));
        }


    }



    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                timeHandler.postDelayed(this, TIME);
                mTvTimeShow.setText(Integer.toString(i++));

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    };

    public TimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
