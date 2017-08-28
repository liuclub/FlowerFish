package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.activity.NumGameActivity;
import com.bagelplay.gameset.evagame.utils.EvaUtils;
import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.gameset.utils.UnitUtil;
import com.bagelplay.gameset.view.XLHRatingBar;
import com.jimmy.wavelibrary.WaveLineView;

/**
 * Created by zhangtianjie on 2017/8/26.
 */

public class EvaluationGameView extends RelativeLayout {

    private static String Tag=EvaluationGameView.class.getSimpleName();
    Context mContext;

    EvaObjectView mEvaObjectView;

    EvaUtils mEvaUtils;

    WaveLineView mWaveLineView;

    XLHRatingBar mXLHRatingBar;

    LinearLayout mRatingbarAboveLl;
    FrameLayout mRatingbarParentFl;

    public EvaluationGameView(Context context) {
        super(context);
    }

    public EvaluationGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.evaluation_game_view_layout, this, true);
        initUI();

        mEvaUtils=  EvaUtils.getInstance(mContext);

        mEvaObjectView.setEvaObjectViewListener(new EvaObjectView.EvaObjectViewListener() {
            @Override
            public void objectClick() {




                SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.c_elephant, new SoundUtil.MediaPlayListener() {
                    @Override
                    public void onPlayerCompletion() {


                        mEvaUtils.startEvaWithListener(new EvaUtils.EvaListener() {
                            @Override
                            public void onVolumeChanged(int volume) {
                                mWaveLineView.setVolume(volume*5);
                            }

                            @Override
                            public void onResult(int grade) {


                                mWaveLineView.setVisibility(GONE);
                                mRatingbarParentFl.setVisibility(VISIBLE);
                                mXLHRatingBar.setCountSelected(grade);
                            }

                            @Override
                            public void onError(){
                                mWaveLineView.setVisibility(GONE);
                                mRatingbarParentFl.setVisibility(GONE);
                            }
                            @Override
                            public void onBeginOfSpeech(){
                                mRatingbarParentFl.setVisibility(GONE);

                                mWaveLineView.setVisibility(VISIBLE);

                                mWaveLineView.startAnim();
                            }


                        });

                    }
                });



            }
        });



        mRatingbarAboveLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.btn_normal_to_little_large);
                mXLHRatingBar.startAnimation(animation);


                mEvaUtils.evaPlay();

                Log.d(Tag,"mXLHRatingBar_click");
            }
        });

    }

    private void initUI() {
        mEvaObjectView= (EvaObjectView) findViewById(R.id.eva_object);

        mWaveLineView= (WaveLineView) findViewById(R.id.wave_line_view);

        mXLHRatingBar= (XLHRatingBar) findViewById(R.id.rating_bar);

        mRatingbarAboveLl= (LinearLayout) findViewById(R.id.rating_bar_above_ll);

        mRatingbarParentFl= (FrameLayout) findViewById(R.id.rating_bar_parent_fl);
    }

    public EvaluationGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
