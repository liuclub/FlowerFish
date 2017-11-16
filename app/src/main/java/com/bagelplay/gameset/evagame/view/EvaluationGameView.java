package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.evagame.utils.EvaUtils;
import com.bagelplay.gameset.utils.LocalAnimationUtils;
import com.bagelplay.gameset.utils.LogUtils;
import com.bagelplay.gameset.utils.RandNum;
import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.gameset.view.FinishGameView;
import com.bagelplay.gameset.view.FllScreenVideoView;
import com.bagelplay.gameset.view.GameCongrationView;
import com.bagelplay.gameset.view.XLHRatingBar;
import com.jimmy.wavelibrary.WaveLineView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtianjie on 2017/8/26.
 */

public class EvaluationGameView extends RelativeLayout {
    Context mContext;
    EvaObjectView mEvaObjectView;
    EvaHamburger2 mEvaHamburger;

    EvaPlate mEvaPlate;

    EvaUtils mEvaUtils;

    WaveLineView mWaveLineView;

    XLHRatingBar mXLHRatingBar;

    LinearLayout mRatingbarAboveLl;
    FrameLayout mRatingbarParentFl;

    FrameLayout mFlVideo;
    ImageView mIvVideo;


    GameCongrationView mGameCongrationView;

    FinishGameView mFinishGameView;

    LinearLayout mLlChooseEnZh;

    ImageView mIvEvaZh, mIvEvaEn;

    static int soundComplateToEva = 0;
    private Handler timeHandler;
    //    private ImageView mIvNext;
    private ImageView mIvLanguageNext;//语言选择关的next
    //第一关
    public int section = 1;
    //评测文字，语音，图片
    List<String> evaTexts;
    List<Integer> evaSounds, evaImages;
    //是否提示操作
    boolean isFirst = true;
    //是否开场
    boolean isWelcome = true;
    int currentGameIndex = 0;
    int maxGameIndex = 8;
    //到第几关
    int currentStage = 0;

    public void setSection(int section) {
        this.section = section;
    }

    public void setChooseZh(boolean chooseZh) {
        this.chooseZh = chooseZh;
    }

    //选择中文
    public boolean chooseZh;
    FllScreenVideoView mVvVideo;
    private TextView eva_grade;//积分
    //是否二次进入游戏
    private boolean evaFinish = false;

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

        //设置积分字体颜色
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0
                , eva_grade.getPaint().getTextSize()
                , getResources().getColor(R.color.orange_start), getResources().getColor(R.color.orange_end)
                , Shader.TileMode.CLAMP);
        eva_grade.getPaint().setShader(mLinearGradient);


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
                Animation animation = LocalAnimationUtils.getInstance(mContext).getAnimNormalToLittleLarge();
                mXLHRatingBar.startAnimation(animation);
                mEvaUtils.evaPlay();
            }
        });

        //next点击
//        mIvNext.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {}
//        });

//        nextClick();


        //鼓励界面结束
        mGameCongrationView.setOnGameCongrationFinishListener(new GameCongrationView.GameCongrationFinishListener() {
            @Override
            public void gameConfigurationFinish() {
                if (!evaFinish) {
                    section++;

                    if (section <= 2) {//第一关结束
                        mGameCongrationView.setVisibility(GONE);
                        //第一关结束
                        mEvaHamburger.setVisibility(GONE);

                        //isWelcome = true;

                        currentGameIndex = 0;

                        init(evaFinish);
                    } else {//第二关结束
                        if (!evaFinish) {
                            mFinishGameView.setPetalandBg(R.mipmap.flower1, R.mipmap.eva_game_bg);

                            mFinishGameView.setVisibility(VISIBLE);


                            //TODO 花瓣
                            SoundUtil.getInstance(mContext).startPlaySound(R.raw.win_petal);
                            //  mSoundUtil.startPlaySound(R.raw.win_petal);
                            mFinishGameView.startAnimation();
                        } else {


                            if (mOnEvaStageStateListener != null) {
                                mOnEvaStageStateListener.gameFinish();
                            }
                        }


                    }
                } else {  //如果是二次游戏就进入语言选择页面

                    mGameCongrationView.setVisibility(GONE);
                    if (section == 1) {
                        Log.d("test", "here");

                        //TODO 这里是否需要替换
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_want_again);
                        mEvaObjectView.getObjectHamNull().setVisibility(VISIBLE);

//                        mEvaHamburger.initFruit();

                        mEvaHamburger.setVisibility(GONE);

                        mLlChooseEnZh.setVisibility(VISIBLE);
//                        mIvNext.setVisibility(GONE);

                        mIvLanguageNext.setVisibility(VISIBLE);
                    }

                    if (section == 2) {
                        Log.d("test", "here2");
                        //TODO 这里是否需要替换
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_want_again);
                        mEvaObjectView.getObjectFruteRotate().setVisibility(VISIBLE);

                        mEvaPlate.initFruit();

                        mEvaPlate.setVisibility(GONE);

                        mLlChooseEnZh.setVisibility(VISIBLE);
//                        mIvNext.setVisibility(GONE);
                        mIvLanguageNext.setVisibility(VISIBLE);
                    }
                }
            }
        });

        mFinishGameView.setOnTimeFinishListener(new FinishGameView.FinishTimeLinstener() {
            @Override
            public void timeFinish() {
                //花瓣结束，此处应该有自定义接口

                if (mOnEvaStageStateListener != null) {
                    mOnEvaStageStateListener.gameFinish();
                }
            }
        });
        //init();

        //选择中文
        mIvEvaZh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = LocalAnimationUtils.getInstance(mContext).getAnimNormalToLittleLarge();
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        SoundUtil.getInstance(mContext).stopPlaySound();
                        chooseZh = true;
                        mLlChooseEnZh.setVisibility(GONE);
                        if (section == 1) {
                            mEvaObjectView.getObjectHamNull().setVisibility(GONE);
                            mIvLanguageNext.setVisibility(GONE);
                            mEvaHamburger.setVisibility(VISIBLE);

                            initData();

                            currentStage = 0;
                            mOnEvaStageStateListener.startStage(8);
                            maxGameIndex = 4;


                            currentGameIndex = 0;
                            mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));
                        } else if (section >= 2) {
                            mEvaObjectView.getObjectFruteRotate().setVisibility(GONE);
                            mIvLanguageNext.setVisibility(GONE);
                            mEvaPlate.setVisibility(VISIBLE);
                            initNextStageDATA();

                            currentStage = 4;
                            mOnEvaStageStateListener.startStage(8);
                            mOnEvaStageStateListener.finishStageNum(currentStage - 1);
                            maxGameIndex = 4;

                            currentGameIndex = 0;

                            mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                mIvEvaZh.startAnimation(animation);
            }
        });
        //选择英文
        mIvEvaEn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = LocalAnimationUtils.getInstance(mContext).getAnimNormalToLittleLarge();
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        SoundUtil.getInstance(mContext).stopPlaySound();
                        chooseZh = false;
                        mLlChooseEnZh.setVisibility(GONE);
                        if (section == 1) {
                            mEvaObjectView.getObjectHamNull().setVisibility(GONE);
                            mIvLanguageNext.setVisibility(GONE);
                            mEvaHamburger.setVisibility(VISIBLE);
                            initData();

                            currentStage = 0;
                            mOnEvaStageStateListener.startStage(8);
                            maxGameIndex = 4;


                            currentGameIndex = 0;
                            mEvaObjectView.changeObject(evaTexts.get(currentGameIndex + 1), evaImages.get(currentGameIndex));
                        } else {
                            mEvaObjectView.getObjectFruteRotate().setVisibility(GONE);
                            mIvLanguageNext.setVisibility(GONE);
                            mEvaPlate.setVisibility(VISIBLE);
                            initNextStageDATA();

                            currentStage = 4;
                            mOnEvaStageStateListener.startStage(8);
                            mOnEvaStageStateListener.finishStageNum(currentStage - 1);
                            maxGameIndex = 4;

                            currentGameIndex = 0;

                            mEvaObjectView.changeObject(evaTexts.get(currentGameIndex + 1), evaImages.get(currentGameIndex));

                        }


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mIvEvaEn.startAnimation(animation);
            }
        });

        //语言选择关的next
        mIvLanguageNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundUtil.getInstance(mContext).stopPlaySound();
                mIvLanguageNext.setVisibility(GONE);
                mLlChooseEnZh.setVisibility(GONE);

                section++;

                if (section <= 2) {//第一关结束
                    mGameCongrationView.setVisibility(GONE);
                    //第一关结束

                    mEvaObjectView.getObjectHamNull().setVisibility(GONE);
                    mEvaHamburger.setVisibility(GONE);

                    //isWelcome = true;

                    currentGameIndex = 0;

                    init(evaFinish);
                } else {//第二关结束

                    if (!evaFinish) {
                        mFinishGameView.setPetalandBg(R.mipmap.flower1, R.mipmap.eva_game_bg);

                        mFinishGameView.setVisibility(VISIBLE);

                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.win_petal);
                        //  mSoundUtil.startPlaySound(R.raw.win_petal);
                        mFinishGameView.startAnimation();
                    } else {
                        if (mOnEvaStageStateListener != null) {
                            mOnEvaStageStateListener.gameFinish();
                        }
                    }
                }
            }
        });

    }

    public void setLanguageAndSection(boolean chooseZh, int section) {
        if (chooseZh) {//选择中文
            SoundUtil.getInstance(mContext).stopPlaySound();
            this.chooseZh = chooseZh;
            mLlChooseEnZh.setVisibility(GONE);
            if (section == 1) {
                mEvaObjectView.getObjectHamNull().setVisibility(GONE);
                mIvLanguageNext.setVisibility(GONE);
                mEvaHamburger.setVisibility(VISIBLE);

                initData();

                currentStage = 0;
                mOnEvaStageStateListener.startStage(8);
                maxGameIndex = 4;


                currentGameIndex = 0;
                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));
            } else if (section >= 2) {
                mEvaObjectView.getObjectFruteRotate().setVisibility(GONE);
                mIvLanguageNext.setVisibility(GONE);
                mEvaPlate.setVisibility(VISIBLE);
                initNextStageDATA();

                currentStage = 4;
                mOnEvaStageStateListener.startStage(8);
                mOnEvaStageStateListener.finishStageNum(currentStage - 1);
                maxGameIndex = 4;

                currentGameIndex = 0;

                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));
            }
        }else{//选择英文
            SoundUtil.getInstance(mContext).stopPlaySound();
            this.chooseZh = chooseZh;
            mLlChooseEnZh.setVisibility(GONE);
            if (section == 1) {
                mEvaObjectView.getObjectHamNull().setVisibility(GONE);
                mIvLanguageNext.setVisibility(GONE);
                mEvaHamburger.setVisibility(VISIBLE);
                initData();

                currentStage = 0;
                mOnEvaStageStateListener.startStage(8);
                maxGameIndex = 4;

                currentGameIndex = 0;
                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex + 1), evaImages.get(currentGameIndex));
            } else {
                mEvaObjectView.getObjectFruteRotate().setVisibility(GONE);
                mIvLanguageNext.setVisibility(GONE);
                mEvaPlate.setVisibility(VISIBLE);
                initNextStageDATA();
                currentStage = 4;
                mOnEvaStageStateListener.startStage(8);
                mOnEvaStageStateListener.finishStageNum(currentStage - 1);
                maxGameIndex = 4;
                currentGameIndex = 0;
                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex + 1), evaImages.get(currentGameIndex));
            }

        }
    }


    private void nextClick() {
        if (!isButtonClickable()) {
            return;
        }
        mRatingbarParentFl.setVisibility(GONE);

        mWaveLineView.setVisibility(INVISIBLE);
        //是否是第二次进入游戏
        if (evaFinish) {

            //选择中文
            if (chooseZh) {

                if (section == 1) {

                    Animation objectAnimationMove = LocalAnimationUtils.getInstance(mContext).getAnimMoveToFood();
                    objectAnimationMove.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (mOnEvaStageStateListener != null) {
                                mOnEvaStageStateListener.finishStageNum(currentStage);
                            }
                            currentStage++;
//                                    mEvaHamburger.setFruitVisibleByIndex(currentGameIndex, evaImages.get(currentGameIndex * 2 + 1));
                            mEvaHamburger.addVegetable(evaImages.get(currentGameIndex * 2 + 1));
                            currentGameIndex++;

                            if (currentGameIndex < maxGameIndex) {
                                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex * 2), evaImages.get(currentGameIndex * 2));
                            } else {


                                mEvaObjectView.setObjectGone();
                                //  Toast.makeText(mContext, "结束啦", Toast.LENGTH_SHORT).show();
                                timeHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                                playFinishVideo(R.raw.eva_ham_finish);
                                        playFinishVideo(R.raw.hamburger_finished);
                                    }
                                }, 1000);
                            }

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    mEvaObjectView.getFlashObject().startAnimation(objectAnimationMove);

                } else {

                    Animation objectAnimationMove = LocalAnimationUtils.getInstance(mContext).getAnimMoveToPlate();

                    objectAnimationMove.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mEvaPlate.setFruitVisibleByIndex(currentGameIndex, evaImages.get(currentGameIndex * 2));

                            currentGameIndex++;

                            if (mOnEvaStageStateListener != null) {
                                mOnEvaStageStateListener.finishStageNum(currentStage);
                            }
                            currentStage++;
                            if (currentGameIndex < maxGameIndex) {
                                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex * 2), evaImages.get(currentGameIndex * 2 + 1));
                            } else {


                                mEvaObjectView.setObjectGone();
                                //  Toast.makeText(mContext, "结束啦", Toast.LENGTH_SHORT).show();
                                timeHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //TODO R.raw.eva_salad_finish
//                                                playFinishVideo(R.raw.eva_salad_finish);
                                        playFinishVideo(R.raw.salad_finished);
                                    }
                                }, 2000);

                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    mEvaObjectView.getFlashObject().startAnimation(objectAnimationMove);
                }
            } else {//选择英文

                if (section == 1) {

                    Animation objectAnimationMove = LocalAnimationUtils.getInstance(mContext).getAnimMoveToFood();
                    objectAnimationMove.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (mOnEvaStageStateListener != null) {
                                mOnEvaStageStateListener.finishStageNum(currentStage);
                            }

                            currentStage++;

//                                    mEvaHamburger.setFruitVisibleByIndex(currentGameIndex, evaImages.get(currentGameIndex * 2 + 1));
                            mEvaHamburger.addVegetable(evaImages.get(currentGameIndex * 2 + 1));

                            currentGameIndex++;

                            if (currentGameIndex < maxGameIndex) {
                                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex * 2 + 1), evaImages.get(currentGameIndex * 2));
                            } else {
                                mEvaObjectView.setObjectGone();
                                // Toast.makeText(mContext, "结束啦", Toast.LENGTH_SHORT).show();
                                timeHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //TODO
//                                                playFinishVideo(R.raw.eva_ham_finish);
                                        playFinishVideo(R.raw.hamburger_finished);
                                    }
                                }, 1000);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mEvaObjectView.getFlashObject().startAnimation(objectAnimationMove);
                } else {

                    Animation objectAnimationMove = LocalAnimationUtils.getInstance(mContext).getAnimMoveToPlate();

                    objectAnimationMove.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            mEvaPlate.setFruitVisibleByIndex(currentGameIndex, evaImages.get(currentGameIndex * 2 + 1));

                            currentGameIndex++;

                            if (mOnEvaStageStateListener != null) {
                                mOnEvaStageStateListener.finishStageNum(currentStage);
                            }

                            currentStage++;
                            if (currentGameIndex < maxGameIndex) {
                                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex * 2 + 1), evaImages.get(currentGameIndex * 2));
                            } else {
                                mEvaObjectView.setObjectGone();
                                // Toast.makeText(mContext, "结束啦", Toast.LENGTH_SHORT).show();

                                timeHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //TODO
//                                                playFinishVideo(R.raw.eva_salad_finish);
                                        playFinishVideo(R.raw.salad_finished);
                                    }
                                }, 2000);

                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    mEvaObjectView.getFlashObject().startAnimation(objectAnimationMove);
                }
            }
        } else {
            //食物的英文第二关
            if (currentGameIndex < maxGameIndex && currentGameIndex % 2 == 1) {

                if (section == 1) {
                    Animation objectAnimationMove = LocalAnimationUtils.getInstance(mContext).getAnimMoveToFood();


                    objectAnimationMove.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (mOnEvaStageStateListener != null) {
                                mOnEvaStageStateListener.finishStageNum(currentStage);
                            }

                            currentStage++;

//                                    mEvaHamburger.setFruitVisibleByIndex(currentGameIndex / 2, evaImages.get(currentGameIndex));
                            mEvaHamburger.addVegetable(evaImages.get(currentGameIndex));

                            currentGameIndex++;
                            if (currentGameIndex < maxGameIndex) {
                                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));
                            } else {
                                mEvaObjectView.setObjectGone();
                                // Toast.makeText(mContext, "结束啦", Toast.LENGTH_SHORT).show();
                                timeHandler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        //TODO
                                        //playFinishVideo(R.raw.eva_ham_finish);
                                        playFinishVideo(R.raw.hamburger_finished);
                                    }
                                }, 2000);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    mEvaObjectView.getFlashObject().startAnimation(objectAnimationMove);

                } else {//第二大关

                    Animation objectAnimationMove = LocalAnimationUtils.getInstance(mContext).getAnimMoveToPlate();

                    objectAnimationMove.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {


                            mEvaPlate.setFruitVisibleByIndex(currentGameIndex / 2, evaImages.get(currentGameIndex));

                            currentGameIndex++;

                            if (mOnEvaStageStateListener != null) {
                                mOnEvaStageStateListener.finishStageNum(currentStage);
                            }

                            currentStage++;


                            if (currentGameIndex < maxGameIndex) {
                                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));
                            } else {


                                mEvaObjectView.setObjectGone();
                                //Toast.makeText(mContext, "结束啦", Toast.LENGTH_SHORT).show();


                                timeHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //TODO R.raw.eva_salad_finish
//                                                playFinishVideo(R.raw.eva_salad_finish);
                                        playFinishVideo(R.raw.salad_finished);
                                    }
                                }, 1000);

                            }


                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    mEvaObjectView.getFlashObject().startAnimation(objectAnimationMove);
                }


            } else if (currentGameIndex < maxGameIndex) {  //中文第一关
                currentGameIndex++;
                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex - 1));
                if (mOnEvaStageStateListener != null) {
                    mOnEvaStageStateListener.finishStageNum(currentStage);
                }
                currentStage++;
            }
        }
    }

    /**
     * 初始化第二关(做沙拉)的数据
     */
    private void initNextStageDATA() {
        List<String> texts = new ArrayList<>();
        //TODO 在这里会替代图片，修改源数据
        //添加水果
        String[] fruitArrayCN = getResources().getStringArray(R.array.fruits_list_cn);
        String[] fruitArrayEN = getResources().getStringArray(R.array.fruits_list_en);
        for (int i = 0; i < fruitArrayCN.length; i++) {
            texts.add(fruitArrayCN[i]);
            texts.add(fruitArrayEN[i]);
        }

        List<Integer> sounds = new ArrayList<>();

        sounds.add(R.raw.cf_apple);
        sounds.add(R.raw.ef_apple);

        sounds.add(R.raw.cf_banana);
        sounds.add(R.raw.ef_banana);

        sounds.add(R.raw.cf_cantaloupe);
        sounds.add(R.raw.ef_cantaloupe);

        sounds.add(R.raw.cf_cherry);
        sounds.add(R.raw.ef_cherry);

        sounds.add(R.raw.cf_coconut);
        sounds.add(R.raw.ef_coconut);

        sounds.add(R.raw.cf_dates);
        sounds.add(R.raw.ef_dates);

        sounds.add(R.raw.cf_dragonfruit);
        sounds.add(R.raw.ef_dragonfruit);

        sounds.add(R.raw.cf_grapes);
        sounds.add(R.raw.ef_grapes);

        sounds.add(R.raw.cf_honeydew);
        sounds.add(R.raw.ef_honeydew);

        sounds.add(R.raw.cf_kiwi);
        sounds.add(R.raw.ef_kiwi);

        sounds.add(R.raw.cf_lemon);
        sounds.add(R.raw.ef_lemon);

        sounds.add(R.raw.cf_mango);
        sounds.add(R.raw.ef_mango);

        sounds.add(R.raw.cf_orange);
        sounds.add(R.raw.ef_orange);

        sounds.add(R.raw.cf_peach);
        sounds.add(R.raw.ef_peach);

        sounds.add(R.raw.cf_pear);
        sounds.add(R.raw.ef_pear);

        sounds.add(R.raw.cf_persimmons);
        sounds.add(R.raw.ef_persimmons);

        sounds.add(R.raw.cf_pineapple);
        sounds.add(R.raw.ef_pineapple);

        sounds.add(R.raw.cf_strawberry);
        sounds.add(R.raw.ef_strawberry);

        sounds.add(R.raw.cf_watermelon);
        sounds.add(R.raw.ef_watermelon);

        List<Integer> images = new ArrayList<>();

        images.add(R.drawable.f_apple);
        images.add(R.drawable.f_apple);

        images.add(R.drawable.f_banana);
        images.add(R.drawable.f_banana);

        images.add(R.drawable.f_cantaloupe);
        images.add(R.drawable.f_cantaloupe);

        images.add(R.drawable.f_cherry);
        images.add(R.drawable.f_cherry);

        images.add(R.drawable.f_coconuts);
        images.add(R.drawable.f_coconuts);

        images.add(R.drawable.f_dates);
        images.add(R.drawable.f_dates);

        images.add(R.drawable.f_dragonfruit);
        images.add(R.drawable.f_dragonfruit);

        images.add(R.drawable.f_grapes);
        images.add(R.drawable.f_grapes);

        images.add(R.drawable.f_honeydew);
        images.add(R.drawable.f_honeydew);

        images.add(R.drawable.f_kiwi);
        images.add(R.drawable.f_kiwi);

        images.add(R.drawable.f_lemon);
        images.add(R.drawable.f_lemon);

        images.add(R.drawable.f_mango);
        images.add(R.drawable.f_mango);

        images.add(R.drawable.f_orange);
        images.add(R.drawable.f_orange);

        images.add(R.drawable.f_peach);
        images.add(R.drawable.f_peach);

        images.add(R.drawable.f_pear);
        images.add(R.drawable.f_pear);

        images.add(R.drawable.f_persimmons);
        images.add(R.drawable.f_persimmons);

        images.add(R.drawable.f_pineapple);
        images.add(R.drawable.f_pineapple);

        images.add(R.drawable.f_strawberry);
        images.add(R.drawable.f_strawberry);

        images.add(R.drawable.f_watermelon);
        images.add(R.drawable.f_watermelon);


        evaTexts = new ArrayList<>();
        evaSounds = new ArrayList<>();
        evaImages = new ArrayList<>();
        int numSize = fruitArrayCN.length;
        int[] tempArray = RandNum.getRandNumNumArray(numSize);
        for (int i = 0; i < tempArray.length; i++) {
            int index = tempArray[i] * 2;
            evaTexts.add(texts.get(index));
            evaTexts.add(texts.get(index + 1));
            evaSounds.add(sounds.get(index));
            evaSounds.add(sounds.get(index + 1));

            evaImages.add(images.get(index));
            evaImages.add(images.get(index + 1));
        }

    }

    private void playFinishVideo(int url) {
        if (mOnEvaStageStateListener != null) {
            mOnEvaStageStateListener.endStage();
        }

        mVvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVvVideo.stopPlayback();

                mFlVideo.setVisibility(View.GONE);

                //游戏鼓励
                mGameCongrationView.setCongrationBg(R.mipmap.eva_congration_object);

                mGameCongrationView.setVisibility(VISIBLE);

                if (section == 1) {
                    SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_congration_into2stage);
                } else {
                    SoundUtil.getInstance(mContext).startPlaySound(R.raw.congration);
                }


                mGameCongrationView.startAnimation(5);


            }
        });

        mVvVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // mVvVideo.setVideoPath("http://112.126.81.84/video/video_book.mp4");

        mVvVideo.setVideoURI(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + url));


        mVvVideo.start();


        mFlVideo.setVisibility(View.VISIBLE);
        mIvVideo.setVisibility(View.VISIBLE);

        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mIvVideo.setVisibility(View.GONE);
            }
        }, 1000);


    }


    public void init(boolean isEvaFinish) {

        this.evaFinish = isEvaFinish;

        //第一部分游戏
        if (section == 1) {
            //显示开场动画
            initData();
            if (isWelcome) {

                mEvaObjectView.getObjectHamNull().setVisibility(VISIBLE);

                SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_xiaohaoqi_like_ham, new SoundUtil.MediaPlayListener() {
                    @Override
                    public void onPlayerCompletion() {

                        Animation objectAnimationMove = LocalAnimationUtils.getInstance(mContext).getAnimhamMoveToFood();

                        objectAnimationMove.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                                mEvaObjectView.getObjectHamNull().setVisibility(GONE);

                                mEvaHamburger.setVisibility(VISIBLE);
                                if (mOnEvaStageStateListener != null) {

                                    if (!evaFinish) {

                                        mOnEvaStageStateListener.startStage(16);
                                        maxGameIndex = 8;
                                    } else {
                                        mOnEvaStageStateListener.startStage(8);

                                        maxGameIndex = 4;
                                    }
                                }

                                SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_only_meat_no_vegetable, new SoundUtil.MediaPlayListener() {
                                    @Override
                                    public void onPlayerCompletion() {
                                        //判断是否二次进入
                                        if (!evaFinish) {
                                            mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));
                                        } else {
                                            //TODO 选择语言
                                            SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_choose_language);
                                            mLlChooseEnZh.setVisibility(VISIBLE);
                                        }
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
                Animation objectAnimationFlash = LocalAnimationUtils.getInstance(mContext).getFlashThreeTimes();
                mEvaObjectView.getObjectHamNull().startAnimation(objectAnimationFlash);
            } else {
                mEvaHamburger.setVisibility(VISIBLE);
                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));
            }
        } else {  //第二部分游戏
            initNextStageDATA();
            //是不是开场
            if (isWelcome) {
                mEvaObjectView.getObjectFruteRotate().setVisibility(VISIBLE);
                //TODO 小好奇想要沙拉
                SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_xiaohaoqi_want_salad, new SoundUtil.MediaPlayListener() {
                            @Override
                            public void onPlayerCompletion() {
                                mEvaObjectView.getObjectFruteRotate().startRotate();
                                timeHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mEvaObjectView.getObjectFruteRotate().setVisibility(GONE);

                                        mEvaPlate.setVisibility(VISIBLE);
                                        if (!evaFinish) {

                                            mOnEvaStageStateListener.startStage(16);
                                            maxGameIndex = 8;
                                            mOnEvaStageStateListener.finishStageNum(currentStage - 1);
                                            mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));

                                        } else {
                                            mOnEvaStageStateListener.startStage(8);
                                            maxGameIndex = 4;

                                            mOnEvaStageStateListener.finishStageNum(currentStage - 1);
                                            //TODO 选择游戏语言
                                            SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_choose_language);
                                            mLlChooseEnZh.setVisibility(VISIBLE);
                                        }
                                    }
                                }, 7000);
                            }
                        }
                );


            } else {
                mEvaPlate.setVisibility(VISIBLE);

                mEvaObjectView.changeObject(evaTexts.get(currentGameIndex), evaImages.get(currentGameIndex));
            }
        }


    }
    //随机评测数据

    /**
     * 初始化第一关（做汉堡）数据
     */
    private void initData() {
        List<String> texts = new ArrayList<>();
        //添加蔬菜
        String[] vegetableArrayCN = getResources().getStringArray(R.array.vegetables_list_cn);
        String[] vegetableArrayEN = getResources().getStringArray(R.array.vegetables_list_en);
        for (int i = 0; i < vegetableArrayEN.length; i++) {
            texts.add(vegetableArrayCN[i]);
            texts.add(vegetableArrayEN[i]);
        }
        LogUtils.lb("texts.size() = " + texts.size());
        List<Integer> sounds = new ArrayList<>();

        sounds.add(R.raw.cv_broccoli);
        sounds.add(R.raw.ev_broccoli);

        sounds.add(R.raw.cv_carrot);
        sounds.add(R.raw.ev_carrot);

        sounds.add(R.raw.cv_celery);
        sounds.add(R.raw.ev_celery);

        sounds.add(R.raw.cv_corn);
        sounds.add(R.raw.ev_corn);

        sounds.add(R.raw.cv_cucumber);
        sounds.add(R.raw.ev_cucumber);

        sounds.add(R.raw.cv_eggplant);
        sounds.add(R.raw.ev_eggplant);

        sounds.add(R.raw.cv_garlic);
        sounds.add(R.raw.ev_garlic);

        sounds.add(R.raw.cv_greenpeas);
        sounds.add(R.raw.ev_greenpeas);

        sounds.add(R.raw.cv_lettuce);
        sounds.add(R.raw.ev_lettuce);

        sounds.add(R.raw.cv_onion);
        sounds.add(R.raw.ev_onion);

        sounds.add(R.raw.cv_pepper);
        sounds.add(R.raw.ev_pepper);

        sounds.add(R.raw.cv_potato);
        sounds.add(R.raw.ev_potato);

        sounds.add(R.raw.cv_tomato);
        sounds.add(R.raw.ev_tomato);

        LogUtils.lb("sounds.size() = " + sounds.size());

        List<Integer> images = new ArrayList<>();

        images.add(R.drawable.v_broccoli);
        images.add(R.drawable.v_broccoli);

        images.add(R.drawable.v_carrot);
        images.add(R.drawable.v_carrot);

        images.add(R.drawable.v_celery);
        images.add(R.drawable.v_celery);

        images.add(R.drawable.v_corn);
        images.add(R.drawable.v_corn);

        images.add(R.drawable.v_cucumber);
        images.add(R.drawable.v_cucumber);

        images.add(R.drawable.v_eggplant);
        images.add(R.drawable.v_eggplant);

        images.add(R.drawable.v_garlic);
        images.add(R.drawable.v_garlic);

        images.add(R.drawable.v_greenpeas);
        images.add(R.drawable.v_greenpeas);

        images.add(R.drawable.v_lettuce);
        images.add(R.drawable.v_lettuce);

        images.add(R.drawable.v_onion);
        images.add(R.drawable.v_onion);

        images.add(R.drawable.v_pepper);
        images.add(R.drawable.v_pepper);

        images.add(R.drawable.v_potato);
        images.add(R.drawable.v_potato);

        images.add(R.drawable.v_tomato);
        images.add(R.drawable.v_tomato);

        evaTexts = new ArrayList<>();
        evaSounds = new ArrayList<>();
        evaImages = new ArrayList<>();
        int numSize = vegetableArrayCN.length;
        int[] tempArray = RandNum.getRandNumNumArray(numSize);
        for (int i = 0; i < tempArray.length; i++) {
            int index = tempArray[i] * 2;
            evaTexts.add(texts.get(index));
            evaTexts.add(texts.get(index + 1));
            evaSounds.add(sounds.get(index));
            evaSounds.add(sounds.get(index + 1));

            evaImages.add(images.get(index));
            evaImages.add(images.get(index + 1));
        }
    }


    private void startPlaySound() {
        mRatingbarParentFl.setVisibility(GONE);

        mWaveLineView.setVisibility(INVISIBLE);

//        mIvNext.setVisibility(GONE);

        mEvaUtils.cancelEva();

        if (!evaFinish) {

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

        } else {//二次进入

            if (chooseZh) {
                SoundUtil.getInstance(mContext).startPlaySoundWithListener(evaSounds.get(currentGameIndex * 2), new SoundUtil.MediaPlayListener() {
                    @Override
                    public void onPlayerCompletion() {
                        timeHandler.postDelayed(evaRunnable, soundComplateToEva);
                    }
                });
            } else {
                SoundUtil.getInstance(mContext).startPlaySoundWithListener(evaSounds.get(currentGameIndex * 2 + 1), new SoundUtil.MediaPlayListener() {
                    @Override
                    public void onPlayerCompletion() {
                        timeHandler.postDelayed(evaRunnable, soundComplateToEva);
                    }
                });
            }

        }
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

    /**
     * 开始评测语音
     */
    private void startEva() {
        if (!evaFinish) {
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

//                    if (grade >= 3) {
//                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_you_great);
//                    } else {
//                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_continue_refueling);
//                    }
                    playTips(grade);

                    //next 按钮出现
//                    mIvNext.setVisibility(VISIBLE);

//                    Animation animation = LocalAnimationUtils.getInstance(mContext).getAnimHideToShow();
//                    mIvNext.startAnimation(animation);

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


        } else {//第二次

            if (chooseZh) {//选择中文
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

                        //TODO 在这里播放评测后的提示音
                        //TODO grade取值范围需要确定一下

//                        if (grade >= 3) {
//                            SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_you_great);
//                        } else {
//                            SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_continue_refueling);
//                        }
                        playTips(grade);

                        //next 按钮出现
//                        mIvNext.setVisibility(VISIBLE);
//
//                        Animation animation = LocalAnimationUtils.getInstance(mContext).getAnimHideToShow();
//                        mIvNext.startAnimation(animation);


                        //首次进入提示操作
                        //firstComeReminder();


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


                }, chooseZh, evaTexts.get(currentGameIndex * 2));

            } else {//选择英文
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
                        //TODO 拿到星星，然后播放提示语
//                        if (grade >= 3) {
//                            SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_you_great);
//                        } else {
//                            SoundUtil.getInstance(mContext).startPlaySound(R.raw.eva_continue_refueling);
//                        }
                        playTips(grade);

                        //next 按钮出现
//                        mIvNext.setVisibility(VISIBLE);
//
//                        Animation animation = LocalAnimationUtils.getInstance(mContext).getAnimHideToShow();
//                        mIvNext.startAnimation(animation);


                        //首次进入提示操作
                        // firstComeReminder();


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


                }, chooseZh, evaTexts.get(currentGameIndex * 2 + 1));
            }

        }
    }

    /**
     * 根据评定的不同星星分数，随机播放相应的提示音（每一个grade对应三种提示音）
     *
     * @param grade 星星分数
     */
    private void playTips(int grade) {
        int randNum = RandNum.randNum(3);
        switch (grade) {
            case 0:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_1_1);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_1_2);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_1_3);
                        break;
                }
                break;
            case 1:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_2_1);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_2_2);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_2_3);
                        break;
                }

                break;
            case 2:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_3_1);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_3_2);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_3_3);
                        break;
                }

                break;
            case 3:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_4_1);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_4_2);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_4_3);
                        break;
                }

                break;
            case 4:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_5_1);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_5_2);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_5_3);
                        break;
                }

                break;
            default:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_1_1);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_1_2);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySound(R.raw.star_level_1_3);
                        break;
                }
                break;
        }
    }

    private void firstComeReminder() {
        //第一次显示提示操作
        if (isFirst) {
            isFirst = !isFirst;

            mEvaObjectView.setButtonClickable(false);
            setButtonClickable(false);


            final Animation nextAnimationFlash = LocalAnimationUtils.getInstance(mContext).getFlashThreeTimes();

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

            final Animation startAnimationFlash = LocalAnimationUtils.getInstance(mContext).getFlashThreeTimes();


            startAnimationFlash.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {


                    //你是想要next
                    //.raw.eva_reminder_next_stage
                    SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.continue_the_game, new SoundUtil.MediaPlayListener() {
                        @Override
                        public void onPlayerCompletion() {


//                            mIvNext.startAnimation(nextAnimationFlash);


                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            final Animation objectAnimationFlash = LocalAnimationUtils.getInstance(mContext).getFlashThreeTimes();

            objectAnimationFlash.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    //你是想要听听自己声音
                    //R.raw.eva_reminder_listen_yoursound
                    SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.listen_self_audio, new SoundUtil.MediaPlayListener() {
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
            //R.raw.eva_reminder_try_again
            SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.speak_again, new SoundUtil.MediaPlayListener() {
                @Override
                public void onPlayerCompletion() {


                    mEvaObjectView.getFlashObject().startAnimation(objectAnimationFlash);


                }
            });
        }
    }

    private void initUI() {

        mEvaHamburger = (EvaHamburger2) findViewById(R.id.eva_hamburger);
        mEvaObjectView = (EvaObjectView) findViewById(R.id.eva_object);

        mWaveLineView = (WaveLineView) findViewById(R.id.wave_line_view);

        mXLHRatingBar = (XLHRatingBar) findViewById(R.id.rating_bar);

        mRatingbarAboveLl = (LinearLayout) findViewById(R.id.rating_bar_above_ll);

        mRatingbarParentFl = (FrameLayout) findViewById(R.id.rating_bar_parent_fl);

//        mIvNext = (ImageView) findViewById(R.id.iv_next);
        mVvVideo = (FllScreenVideoView) findViewById(R.id.vv_video);
        mIvVideo = (ImageView) findViewById(R.id.iv_video);
        mFlVideo = (FrameLayout) findViewById(R.id.fl_video);

        mGameCongrationView = (GameCongrationView) findViewById(R.id.congration_view);

        mEvaPlate = (EvaPlate) findViewById(R.id.eva_plate);

        mFinishGameView = (FinishGameView) findViewById(R.id.fgv_view);

        mLlChooseEnZh = (LinearLayout) findViewById(R.id.ll_choose_en_zh);

        mIvEvaZh = (ImageView) findViewById(R.id.iv_eva_zh);
        mIvEvaEn = (ImageView) findViewById(R.id.iv_eva_en);

        mIvLanguageNext = (ImageView) findViewById(R.id.iv_language_next);

        eva_grade = findViewById(R.id.eva_grade);

    }

    public EvaluationGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public interface OnEvaStageStateListener {
        void finishStageNum(int num);

        //传递显示进度条的数量
        void startStage(int stageNum);

        void endStage();

        void gameFinish();
    }

    public OnEvaStageStateListener mOnEvaStageStateListener;

    public void setOnEvaStageStateListener(OnEvaStageStateListener mOnEvaStageStateListener) {
        this.mOnEvaStageStateListener = mOnEvaStageStateListener;
    }

}
