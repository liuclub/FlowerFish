package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.evagame.utils.EvaUtils;
import com.bagelplay.gameset.utils.MyAnim;
import com.bagelplay.gameset.utils.RandNum;
import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.gameset.view.XLHRatingBar;
import com.jimmy.wavelibrary.WaveLineView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.animation;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.bagelplay.gameset.utils.RandNum.getRandNumNumArray;
import static com.lidroid.xutils.util.core.CompatibleAsyncTask.init;

/**
 * Created by zhangtianjie on 2017/8/26.
 */

public class EvaluationGameView extends RelativeLayout {

    private static String Tag = EvaluationGameView.class.getSimpleName();

    Context mContext;

    EvaObjectView mEvaObjectView;
    EvaHamburger mEvaHamburger;

    EvaUtils mEvaUtils;

    WaveLineView mWaveLineView;

    XLHRatingBar mXLHRatingBar;

    LinearLayout mRatingbarAboveLl;
    FrameLayout mRatingbarParentFl;


    static int soundComplateToEva = 1000;

    private Handler timeHandler;

    private ImageView mIvNext;

    //第一关
    int statge = 1;

    //评测文字，语音，用图片
    List<String> evaTexts;
    List<Integer> evaSounds, evaImages;


    //是否提示操作
    boolean isFirst = true;

    //是否开场
    boolean isWelcome = true;

    int currentGameIndex = 0;

    int maxGameIndex = 8;

    public void setButtonClickable(boolean buttonClickable) {
        this.buttonClickable = buttonClickable;


    }


    public boolean isButtonClickable() {
        return buttonClickable;
    }

    private boolean buttonClickable = true;

    public EvaluationGameView(Context context) {
        super(context);
    }

    public EvaluationGameView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.evaluation_game_view_layout, this, true);
        initUI();

        initDATA();


        mEvaUtils = EvaUtils.getInstance(mContext);

        timeHandler = new Handler();

        mEvaObjectView.setEvaObjectViewListener(new EvaObjectView.EvaObjectViewListener() {
            @Override
            public void objectClick() {


                startPlaySound();


            }


            @Override
            public void objectStartPlaySounds() {
                startPlaySound();
            }
        });


        //星星点击
        mRatingbarAboveLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isButtonClickable())
                    return;
                Animation animation = MyAnim.getInstance(mContext).getAnimNormalToLittleLarge();


                mXLHRatingBar.startAnimation(animation);


                mEvaUtils.evaPlay();


            }
        });

        //next点击
        mIvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isButtonClickable())
                    return;


                mRatingbarParentFl.setVisibility(GONE);

                mWaveLineView.setVisibility(INVISIBLE);

                mIvNext.setVisibility(GONE);

                //食物的英文第二关
                if (currentGameIndex < maxGameIndex && currentGameIndex % 2 == 1) {
                    Animation objectAnimationMove = MyAnim.getInstance(mContext).getAnimMoveToFood();


                    objectAnimationMove.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {


                            mEvaHamburger.setFruitVisibleByIndex(currentGameIndex / 2, evaImages.get(currentGameIndex));

                            currentGameIndex++;
                            mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    mEvaObjectView.getFlashObject().startAnimation(objectAnimationMove);


                } else if (currentGameIndex < maxGameIndex) {  //中文第一关
                    currentGameIndex++;
                    mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));

                } else {
                    Toast.makeText(mContext, "结束啦", Toast.LENGTH_SHORT).show();
                }


            }
        });


        init();


    }


    private void init() {
        //显示开场动画
        if (isWelcome) {




            SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_xiaohaoqi_like_ham, new SoundUtil.MediaPlayListener() {
                @Override
                public void onPlayerCompletion() {




                    Animation objectAnimationMove = MyAnim.getInstance(mContext).getAnimMoveToFood();

                    objectAnimationMove.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            mEvaObjectView.getObjectHamNull().setVisibility(GONE);

                            mEvaHamburger.setVisibility(VISIBLE);


                            SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_only_meat_no_vegetable, new SoundUtil.MediaPlayListener() {
                                @Override
                                public void onPlayerCompletion() {

                                    mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));


                                }

                            });


                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    mEvaObjectView.getObjectHamNull().startAnimation(objectAnimationMove);


                }
            });


            Animation objectAnimationFlash = MyAnim.getInstance(mContext).getFlashThreeTimes();

            mEvaObjectView.getObjectHamNull().startAnimation(objectAnimationFlash);


        } else {
            mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));

        }


    }


    //随机评测数据
    private void initDATA() {
        List<String> texts = new ArrayList<>();
        texts.add("西红柿");
        texts.add("tomato");
        texts.add("黄瓜");
        texts.add("cucumber");
        texts.add("圆白菜");
        texts.add("cabbage");
        texts.add("土豆");
        texts.add("potato");
        texts.add("胡萝卜");
        texts.add("carrot");
        texts.add("洋葱");
        texts.add("onion");


        List<Integer> sounds = new ArrayList<>();


        sounds.add(R.raw.eva_tomato_zh);

        sounds.add(R.raw.eva_tomato_en);

        sounds.add(R.raw.eva_cucumber_zh);
        sounds.add(R.raw.eva_cucumber_en);


        sounds.add(R.raw.eva_cabbage_zh);
        sounds.add(R.raw.eva_cabbage_en);


        sounds.add(R.raw.eva_potato_zh);
        sounds.add(R.raw.eva_potato_en);


        sounds.add(R.raw.eva_carrot_zh);
        sounds.add(R.raw.eva_carrot_en);
        //替代语音
        sounds.add(R.raw.eva_onion_zh);
        sounds.add(R.raw.eva_onion_en);


        List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.eva_tomato);
        images.add(R.mipmap.eva_tomato_small);

        images.add(R.mipmap.eva_cucumber);
        images.add(R.mipmap.eva_cucumber_small);

        images.add(R.mipmap.eva_cabbage);
        images.add(R.mipmap.eva_cabbage_small);

        images.add(R.mipmap.eva_potato);
        images.add(R.mipmap.eva_potato_small);

        images.add(R.mipmap.eva_carrot);
        images.add(R.mipmap.eva_carrot_small);

        images.add(R.mipmap.eva_onion);
        images.add(R.mipmap.eva_onion_small);


        evaTexts = new ArrayList<>();
        evaSounds = new ArrayList<>();
        evaImages = new ArrayList<>();


        int[] tempArray = RandNum.getRandNumNumArray(6);


        for (int i = 0; i < tempArray.length; i++) {
            evaTexts.add(texts.get(tempArray[i] * 2));
            evaTexts.add(texts.get(tempArray[i] * 2 + 1));

            evaSounds.add(sounds.get(tempArray[i] * 2));
            evaSounds.add(sounds.get(tempArray[i] * 2 + 1));

            evaImages.add(images.get(tempArray[i] * 2));
            evaImages.add(images.get(tempArray[i] * 2 + 1));
        }


    }


    private void startPlaySound() {
        mRatingbarParentFl.setVisibility(GONE);

        mWaveLineView.setVisibility(INVISIBLE);

        mIvNext.setVisibility(GONE);

        mEvaUtils.cancelEva();

        SoundUtil.getInstance(mContext).startPlaySoundWithListener(evaSounds.get(currentGameIndex), new SoundUtil.MediaPlayListener() {
            @Override
            public void onPlayerCompletion() {

                if (isFirst) {
                    //小朋友跟我一起读吧
                    SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_follow_me, new SoundUtil.MediaPlayListener() {
                        @Override
                        public void onPlayerCompletion() {


                            timeHandler.postDelayed(evaRunnable, soundComplateToEva);

                        }
                    });

                } else {

                    timeHandler.postDelayed(evaRunnable, soundComplateToEva);
                }


            }
        });
    }


    Runnable evaRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                startEva();

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    };


    private void startEva() {

        boolean isChinese = false;
        if (currentGameIndex % 2 == 0) {
            isChinese = true;
        }

        mEvaUtils.startEvaWithListener(new EvaUtils.EvaListener() {
            @Override
            public void onVolumeChanged(int volume) {
                mWaveLineView.setVolume(volume * 5);
            }

            @Override
            public void onResult(int grade) {


                mWaveLineView.setVisibility(GONE);
                mRatingbarParentFl.setVisibility(VISIBLE);
                mXLHRatingBar.setCountSelected(grade);

                if (grade >= 3) {
                    SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_you_great);
                } else {
                    SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_continue_refueling);
                }

                //next 按钮出现
                mIvNext.setVisibility(VISIBLE);

                Animation animation = MyAnim.getInstance(mContext).getAnimHideToShow();
                mIvNext.startAnimation(animation);


                //首次进入提示操作
                firstComeReminder();


            }

            @Override
            public void onError() {
                mWaveLineView.setVisibility(GONE);
                mRatingbarParentFl.setVisibility(GONE);
            }

            @Override
            public void onBeginOfSpeech() {
                mRatingbarParentFl.setVisibility(GONE);

                mWaveLineView.setVisibility(VISIBLE);

                mWaveLineView.startAnim();
            }


        }, isChinese, evaTexts.get(currentGameIndex));
    }

    private void firstComeReminder() {
        //第一次显示提示操作
        if (isFirst) {
            isFirst = !isFirst;

            mEvaObjectView.setButtonClickable(false);
            setButtonClickable(false);


            final Animation nextAnimationFlash = MyAnim.getInstance(mContext).getFlashThreeTimes();

            nextAnimationFlash.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {


                    mEvaObjectView.setButtonClickable(true);
                    setButtonClickable(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            final Animation startAnimationFlash = MyAnim.getInstance(mContext).getFlashThreeTimes();


            startAnimationFlash.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {


                    //你是想要next
                    SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_reminder_next_stage, new SoundUtil.MediaPlayListener() {
                        @Override
                        public void onPlayerCompletion() {


                            mIvNext.startAnimation(nextAnimationFlash);


                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            final Animation objectAnimationFlash = MyAnim.getInstance(mContext).getFlashThreeTimes();

            objectAnimationFlash.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    //你是想要听听自己声音
                    SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_reminder_listen_yoursound, new SoundUtil.MediaPlayListener() {
                        @Override
                        public void onPlayerCompletion() {


                            mXLHRatingBar.startAnimation(startAnimationFlash);

                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            //你是想要再读一次
            SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_reminder_try_again, new SoundUtil.MediaPlayListener() {
                @Override
                public void onPlayerCompletion() {


                    mEvaObjectView.getFlashObject().startAnimation(objectAnimationFlash);


                }
            });


        }
    }

    private void initUI() {

        mEvaHamburger = (EvaHamburger) findViewById(R.id.eva_hamburger);
        mEvaObjectView = (EvaObjectView) findViewById(R.id.eva_object);

        mWaveLineView = (WaveLineView) findViewById(R.id.wave_line_view);

        mXLHRatingBar = (XLHRatingBar) findViewById(R.id.rating_bar);

        mRatingbarAboveLl = (LinearLayout) findViewById(R.id.rating_bar_above_ll);

        mRatingbarParentFl = (FrameLayout) findViewById(R.id.rating_bar_parent_fl);

        mIvNext = (ImageView) findViewById(R.id.iv_next);
    }

    public EvaluationGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
