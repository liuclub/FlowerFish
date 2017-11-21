package com.bagelplay.gameset.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bagelplay.gameset.MyApplication;
import com.bagelplay.gameset.R;
import com.bagelplay.gameset.evagame.doman.Food;
import com.bagelplay.gameset.evagame.utils.DataUtil;
import com.bagelplay.gameset.evagame.utils.EvaUtils;
import com.bagelplay.gameset.evagame.utils.SPUtil;
import com.bagelplay.gameset.evagame.view.EvaObjectView;
import com.bagelplay.gameset.utils.AppManager;
import com.bagelplay.gameset.utils.LocalAnimationUtils;
import com.bagelplay.gameset.utils.LogUtils;
import com.bagelplay.gameset.utils.RandNum;
import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.gameset.view.FllScreenVideoView;
import com.bagelplay.gameset.view.GameCongrationView2;
import com.bagelplay.gameset.view.XLHRatingBar;
import com.bagelplay.sdk.cocos.SDKCocosManager;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jimmy.wavelibrary.WaveLineView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EvaluationGame2Activity extends AppCompatActivity implements EvaObjectView.TouchInterface, XLHRatingBar.OnRatingChangeListener, View.OnClickListener {
    private Context mContext;
    private int section;//1:做汉堡；2：做沙拉

    private List<Food> mFoodList;
    private int currentGameIndex;

    private Handler handler;
    public static final int soundComplateToEva = 0;
    private boolean chooseZh;
    private boolean isFirst;

    private RelativeLayout aeFoodContainer;
    private EvaObjectView aeObject;
    private RelativeLayout aeProgressContainer;
    private TextView aeGrade;
    private ImageView aePause;
    private ImageView ae_restart;
    private WaveLineView ae_waveview;
    private XLHRatingBar mXLHRatingBar;
    private LinearLayout ae_progress_container2;
    private ImageView ae_full_screen;
    private ImageView ae_mouse_tip;

    private GameCongrationView2 ae_celebrateview;

    private EvaUtils mEvaUtils;

    private boolean once = true;
    private int fruitHeight;
    private int vegetableHeight;
    private final int DISTANCE = 50;
    //每一小关卡，用户尝试次数
    private int tryCount;

    private int score;//积分/得分
    public static final String SCORE_SUFFIX = " 积分";

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private Runnable evaRunnable = () -> {
        try {
            startEva();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    private EvaUtils.EvaListener mEvaListener = new EvaUtils.EvaListener() {
        @Override
        public void onVolumeChanged(int volume) {
            ae_waveview.setVolume(volume * 5);
        }

        @Override
        public void onResult(final int grade) {
            //grade取值范围是0/1/2/3/4/5
            handler.post(() -> {

                if (grade > 0) {
                    ae_waveview.setVisibility(View.GONE);
                    aeProgressContainer.setVisibility(View.VISIBLE);
                    mXLHRatingBar.setVisibility(View.VISIBLE);
                    mXLHRatingBar.setCountSelected(grade);
                } else if (grade == 0) {
                    //此处的处理方式同onError
                    //应该自动播放语音
                    tryAgain();
                }
            });

        }

        @Override
        public void onError() {
            //用户没有发音的话，error=10118，回调该方法
//            LogUtils.lb("onError");
            tryAgain();
        }

        @Override
        public void onBeginOfSpeech() {

            aeObject.setButtonClickable(false);
            aeObject.setDisableTouch(true);
            handler.post(() -> {
                mXLHRatingBar.setVisibility(View.GONE);
                aeProgressContainer.setVisibility(View.GONE);
                ae_waveview.setVisibility(View.VISIBLE);
                ae_waveview.startAnim();
            });
        }
    };

    /**
     * 提示用户再读一次/继续下一个食物
     */
    private void tryAgain() {
        handler.post(() -> {
            mXLHRatingBar.setVisibility(View.GONE);
            ae_waveview.setVisibility(View.GONE);
            aeProgressContainer.setVisibility(View.GONE);

            //TODO 评分出现错误，怎么操作？
            aeObject.setDisableTouch(true);
            aeObject.setButtonClickable(false);

            tryCount++;
            //TODO 在这里做如下动作
            //提示用户发音:最多三次
            if (tryCount < 4) {
                SoundUtil.getInstance(EvaluationGame2Activity.this).startPlaySoundWithListener(R.raw.try_again, new SoundUtil.MediaPlayListener() {
                    @Override
                    public void onPlayerCompletion() {
                        startPlaySound();
                    }
                });
            } else {
                //超过三次后，跳过
                SoundUtil.getInstance(EvaluationGame2Activity.this).startPlaySoundWithListener(R.raw.try_another, new SoundUtil.MediaPlayListener() {
                    @Override
                    public void onPlayerCompletion() {
                        tryCount = 0;
                        putFoodIntoContainer();
                        next();
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        LogUtils.lb("onCreate");
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.activity_evaluation_game);
        SDKCocosManager.getInstance(this).addWindowCallBack(this);
        AppManager.getInstance().addActivity(this);
        mContext = EvaluationGame2Activity.this;

        initView();

        handler = new Handler();
        currentGameIndex = 0;

        mEvaUtils = EvaUtils.getInstance(this);

        //新版本：四种模式
        chooseZh = !"zh".equals(getIntent().getStringExtra("language"));
        section = getIntent().getIntExtra("section", 1);
        mFoodList = new ArrayList<>();

        switch (section) {
            case 1://做汉堡
                aeFoodContainer.setBackgroundResource(R.mipmap.eva_ham_bread);
                mFoodList.addAll(DataUtil.getVegetableData(this));

                Food vegetabe = mFoodList.get(currentGameIndex);
                aeObject.changeObject(chooseZh ? vegetabe.getChineseName() : vegetabe.getEnglishName(), vegetabe.getImageResId());
                break;
            case 2://做沙拉
                aeFoodContainer.setBackgroundResource(R.mipmap.eva_plate);
                mFoodList.addAll(DataUtil.getFruitData(this));

                aeObject.getObjectFruteRotate().setVisibility(View.GONE);

                Food fruit = mFoodList.get(currentGameIndex);
                aeObject.changeObject(chooseZh ? fruit.getChineseName() : fruit.getEnglishName(), fruit.getImageResId());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKCocosManager.getInstance().onResume();


        aeObject.setDisableTouch(false);
        aeObject.setButtonClickable(true);

        //设置积分字体颜色
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, aeGrade.getPaint().getTextSize(), getResources().getColor(R.color.orange_start), getResources().getColor(R.color.orange_end), Shader.TileMode.CLAMP);
        aeGrade.getPaint().setShader(mLinearGradient);
        score = SPUtil.get("score", 0);
        aeGrade.setText(score + SCORE_SUFFIX);

        //更新进度条
        for (int i = 0; i <= currentGameIndex; i++) {
            if (ae_progress_container2.getChildCount() > i) {
                ImageView child = (ImageView) ae_progress_container2.getChildAt(i);
                child.setImageResource(R.drawable.progress_done);
            }
        }
    }

    @Override
    protected void onPause() {
//        LogUtils.lb("onPause");
        super.onPause();
        mEvaUtils.cancelEva();

        aeObject.setDisableTouch(true);
        aeObject.setButtonClickable(false);

        SDKCocosManager.getInstance().onPause();
        SoundUtil.getInstance(EvaluationGame2Activity.this).stopPlaySound();
    }

    protected void onStop() {
//        LogUtils.lb("onStop");
        super.onStop();
        SDKCocosManager.getInstance().onStop();
    }

    @Override
    protected void onDestroy() {
//        LogUtils.lb("onDestroy");
        super.onDestroy();
        SoundUtil.getInstance(mContext).stopPlaySound();
        SDKCocosManager.getInstance().removeWindowCallBack(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
//        LogUtils.lb("onWindowFocusChanged");
        super.onWindowFocusChanged(hasFocus);
        //添加进度条
        if (once) {

            initProgress();
            once = false;
        }
        ImageView imageView = aeObject.getFlashObject();
        int height = (int) (imageView.getHeight() * 1.0 * 12 / 28);
        fruitHeight = height;
        vegetableHeight = height;
    }

    /**
     * 初始化进度条
     */
    private void initProgress() {
        Integer progress = MyApplication.progress;

        //动态设置进度条的背景的宽高
        LinearLayout parent2 = (LinearLayout) ae_progress_container2.getParent();
        int height2 = (int) (parent2.getWidth() * 1.0 * 84 / 1002);
        RelativeLayout.LayoutParams parentParams = new RelativeLayout.LayoutParams(parent2.getWidth(), height2);
        parent2.setLayoutParams(parentParams);

        LogUtils.lb("背景 \r\n width = " + parent2.getWidth() + "\r\nheight = " + height2);

        int containerHeight = (int) (height2 * 0.4);
        int containerWidth = (int) (parent2.getWidth() * 15.37d / 23.07);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(containerWidth, containerHeight);
        int marginTop = (int) ((height2 - containerHeight) * 1.0 / 2 * 4 / 5);
        containerParams.topMargin = marginTop;
        ae_progress_container2.setLayoutParams(containerParams);
        LogUtils.lb("进度条容器 \r\n width = " + containerWidth + "\r\nheight = " + containerHeight);


        //设置暂停按钮的宽高
        int pauseHeight = height2 / 2;
        RelativeLayout.LayoutParams pauseParams = new RelativeLayout.LayoutParams(pauseHeight, pauseHeight);
        pauseParams.setMargins((int) (pauseHeight * 0.8), marginTop, 0, 0);
        aePause.setLayoutParams(pauseParams);

        //设置积分的宽高
        LinearLayout gradeContainer = findViewById(R.id.linearLayout);
        RelativeLayout.LayoutParams gradeContainerParams = new RelativeLayout.LayoutParams(height2, RelativeLayout.LayoutParams.WRAP_CONTENT);
        gradeContainerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.ALIGN_PARENT_TOP);
        gradeContainerParams.setMargins(0, marginTop, (int) (pauseHeight * 0.8), 0);
        gradeContainer.setLayoutParams(gradeContainerParams);

        int marginLeft = (int) ((containerWidth - containerHeight * 16) * 1.0 / 17);
        ae_progress_container2.removeAllViews();
        for (int i = 0; i < 16; i++) {
            ImageView imageView = new ImageView(EvaluationGame2Activity.this);
            imageView.setImageResource(i < progress ? R.drawable.progress_done : R.drawable.progress_undo);
            LinearLayout parent = (LinearLayout) ae_progress_container2.getParent();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(containerHeight, containerHeight);
            if (i == 15) {
                params.setMargins(marginLeft, 0, 0, marginLeft);
            } else {
                params.setMargins(marginLeft, 0, 0, 0);
            }
            params.gravity = Gravity.CENTER_HORIZONTAL;
            imageView.setLayoutParams(params);
            ae_progress_container2.addView(imageView);
        }
    }

    @Override
    public void showUI() {
        LogUtils.lb("showUI");
        Integer progress = MyApplication.progress;
        ImageView child = (ImageView) ae_progress_container2.getChildAt(progress > 15 ? 15 : progress);
        child.setImageResource(R.drawable.progress_done);
        MyApplication.progress++;

        //增加积分
        score += 100;
        aeGrade.setText(score + SCORE_SUFFIX);
        aeGrade.startAnimation(LocalAnimationUtils.getInstance(mContext).getFlashThreeTimes());
        SPUtil.set("score", score);

        putFoodIntoContainer();
        next();
    }

    @Override
    public void hideUI() {
//        LogUtils.lb("hideUI");
        ae_waveview.setVisibility(View.GONE);
        LinearLayout parent2 = (LinearLayout) ae_waveview.getParent();
        parent2.setVisibility(View.GONE);

        mXLHRatingBar.setVisibility(View.GONE);

        ((RelativeLayout) (aeProgressContainer.getParent())).setVisibility(View.GONE);
        findViewById(R.id.ae_center).setVisibility(View.VISIBLE);
        aeObject.findViewById(R.id.bg).setVisibility(View.INVISIBLE);
        aeObject.findViewById(R.id.tv_eva_text).setVisibility(View.INVISIBLE);
        SoundUtil.getInstance(mContext).stopPlaySound();
    }

    /**
     * 初始化控件
     */
    private void initView() {
//        LogUtils.lb("initView");
        tryCount = 0;
//        isFirst = SPUtil.get("isFirst", true);
        isFirst = MyApplication.isFirst;
        RelativeLayout root_view = findViewById(R.id.ae_root_view);
        aeFoodContainer = (RelativeLayout) root_view.findViewById(R.id.ae_food_container);
        aeObject = (EvaObjectView) root_view.findViewById(R.id.ae_object);
//        aeXiaohaoqi = (ImageView) root_view.findViewById(R.id.ae_xiaohaoqi);
        aeProgressContainer = (RelativeLayout) root_view.findViewById(R.id.ae_progress_container);
        aeGrade = (TextView) root_view.findViewById(R.id.ae_grade);
        aePause = (ImageView) root_view.findViewById(R.id.ae_pause);
        ae_restart = (ImageView) root_view.findViewById(R.id.ae_restart);
        ae_waveview = (WaveLineView) root_view.findViewById(R.id.ae_waveview);
        mXLHRatingBar = (XLHRatingBar) root_view.findViewById(R.id.ae_ratingbar);
        ae_progress_container2 = (LinearLayout) root_view.findViewById(R.id.ae_progress_container2_inner);
        surfaceView = root_view.findViewById(R.id.eva_surfaceview);
        ae_celebrateview = root_view.findViewById(R.id.ae_celebrateview);
        ae_full_screen = root_view.findViewById(R.id.ae_full_screen);
        ae_mouse_tip = root_view.findViewById(R.id.ae_mouse_tip);

        aePause.setOnClickListener(this);
        ae_restart.setOnClickListener(this);

        aeObject.setTouchInterface(this);
        aeObject.setVisibility(View.VISIBLE);
        aeObject.setEvaObjectViewListener(new EvaObjectView.EvaObjectViewListener() {
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
        mXLHRatingBar.setOnClickListener(v -> {
            Animation animation = LocalAnimationUtils.getInstance(mContext).getAnimNormalToLittleLarge();
            mXLHRatingBar.startAnimation(animation);
            mEvaUtils.evaPlay();
        });

        mXLHRatingBar.setOnRatingChangeListener(this);
    }

    private void startPlaySound() {
        aeObject.setButtonClickable(false);

        //获取用户录音输入数据之前，Food不可移动
        aeObject.setDisableTouch(true);

        mXLHRatingBar.setVisibility(View.GONE);
        aeProgressContainer.setVisibility(View.GONE);
        ae_waveview.setVisibility(View.INVISIBLE);
        mEvaUtils.cancelEva();
        SoundUtil.getInstance(mContext).stopPlaySound();

        Food food = mFoodList.get(currentGameIndex);
        SoundUtil.getInstance(mContext).startPlaySoundWithListener(
                chooseZh ? food.getChineseSoundResId() : food.getEnglishSoundResId()
                , () -> {
                    ae_waveview.setVisibility(View.VISIBLE);
                    if (isFirst) {
                        MyApplication.isFirst = false;
                        isFirst = false;
                        //小朋友跟我一起读吧
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.eva_follow_me, new SoundUtil.MediaPlayListener() {
                            @Override
                            public void onPlayerCompletion() {
                                handler.postDelayed(evaRunnable, soundComplateToEva);
                            }
                        });
                    } else {
                        handler.postDelayed(evaRunnable, soundComplateToEva);
                    }
                });
    }

    /**
     * 开始评测语音
     */
    private void startEva() {
        Food food = mFoodList.get(currentGameIndex);
        mEvaUtils.startEvaWithListener(mEvaListener, chooseZh, chooseZh ? food.getChineseName() : food.getEnglishName());
    }

    private SoundUtil.MediaPlayListener mediaPlayListener = new SoundUtil.MediaPlayListener() {
        @Override
        public void onPlayerCompletion() {
            //TODO 系统读了一遍后，用户跟着读一遍后，方能播放示例读音
            tryCount = 0;
            if (MyApplication.firstMind) {
                firstComeReminder();
            } else {
                aeObject.setButtonClickable(true);
                //评分结束，将Food设置为可以触摸移动状态
                aeObject.setDisableTouch(false);
            }
        }
    };

    /**
     * 根据评定的不同星星分数，随机播放相应的提示音（每一个grade对应三种提示音）
     *
     * @param grade 星星分数
     */
    private void playTips(int grade) {
//        LogUtils.lb("playTips");
        int randNum = RandNum.randNum(3);
        switch (grade) {
            default:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_1_1, mediaPlayListener);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_1_2, mediaPlayListener);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_1_3, mediaPlayListener);
                        break;
                }
                break;
            case 2:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_2_1, mediaPlayListener);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_2_2, mediaPlayListener);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_2_3, mediaPlayListener);
                        break;
                }

                break;
            case 3:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_3_1, mediaPlayListener);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_3_2, mediaPlayListener);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_3_3, mediaPlayListener);
                        break;
                }

                break;
            case 4:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_4_1, mediaPlayListener);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_4_2, mediaPlayListener);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_4_3, mediaPlayListener);
                        break;
                }

                break;
            case 5:
                switch (randNum) {
                    case 0:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_5_1, mediaPlayListener);
                        break;
                    case 1:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_5_2, mediaPlayListener);
                        break;
                    default:
                        SoundUtil.getInstance(mContext).startPlaySoundWithListener(R.raw.star_level_5_3, mediaPlayListener);
                        break;
                }

                break;
        }
    }

    private void firstComeReminder() {
//        LogUtils.lb("firstComeReminder");

        //3-3 还是把水果拖进碗里，继续闯关(小手拖划示意)呢
//        final Animation thirdAnimation = LocalAnimationUtils.getInstance(mContext).getTranslateAnimation();
        final Animation thirdAnimation = LocalAnimationUtils.getInstance(mContext).getMouseMoveAnimation();
        thirdAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                SoundUtil.getInstance(EvaluationGame2Activity.this).startPlaySound(section == 1 ? R.raw.drag_vegetable_into_plate : R.raw.drag_fruit_into_bowl);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                LogUtils.lb("onAnimationEnd");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ae_mouse_tip.setVisibility(View.GONE);
                        ae_full_screen.setVisibility(View.GONE);
                        MyApplication.firstMind = false;

                        aeObject.setDisableTouch(false);
                        aeObject.setButtonClickable(true);
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
//                LogUtils.lb("onAnimationRepeat");
            }
        });

        //3-2 星星闪烁三次+听听自己的读音
        final Animation secondAnimation = LocalAnimationUtils.getInstance(mContext).getFlashThreeTimes();
        secondAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                SoundUtil.getInstance(EvaluationGame2Activity.this).startPlaySound(R.raw.listener_self);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ae_mouse_tip.setVisibility(View.VISIBLE);
                        ae_mouse_tip.startAnimation(thirdAnimation);
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //3-1 食物闪烁三次+小朋友你想再读一次？
        final Animation firstAnimation = LocalAnimationUtils.getInstance(mContext).getFlashThreeTimes();
        firstAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                SoundUtil.getInstance(EvaluationGame2Activity.this).startPlaySound(R.raw.read_once_again);
                ae_full_screen.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXLHRatingBar.startAnimation(secondAnimation);
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        aeObject.getFlashObject().startAnimation(firstAnimation);
    }

    /**
     * 继续游戏
     */
    private void next() {
        //每次游戏最多添加四种水果/蔬菜
        if (currentGameIndex < 3) {
            ((RelativeLayout) (aeProgressContainer.getParent())).setVisibility(View.VISIBLE);
            findViewById(R.id.ae_center).setVisibility(View.INVISIBLE);
            aeObject.findViewById(R.id.bg).setVisibility(View.VISIBLE);
            aeObject.findViewById(R.id.tv_eva_text).setVisibility(View.VISIBLE);


            currentGameIndex++;
            if (currentGameIndex < mFoodList.size()) {
                Food food = mFoodList.get(currentGameIndex);
                aeObject.changeObject(chooseZh ? food.getChineseName() : food.getEnglishName(), food.getImageResId());
            }

            ((RelativeLayout) (aeProgressContainer.getParent())).setVisibility(View.VISIBLE);
            findViewById(R.id.ae_center).setVisibility(View.INVISIBLE);
            aeObject.findViewById(R.id.bg).setVisibility(View.VISIBLE);
            aeObject.findViewById(R.id.tv_eva_text).setVisibility(View.VISIBLE);
        } else {

            aeObject.setVisibility(View.GONE);
            LinearLayout parent2 = (LinearLayout) ae_waveview.getParent();
            parent2.setVisibility(View.GONE);
            findViewById(R.id.ae_center).setVisibility(View.GONE);

            ae_waveview.clearAnimation();
            ae_waveview.setVisibility(View.GONE);

            YoYo.with(Techniques.values()[6])
                    .duration(1200)
//                    .repeat(YoYo.INFINITE)
                    .repeat(1)
                    .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            currentGameIndex = 0;
                            String uriString = "android.resource://" + getPackageName() + "/" + (section == 1 ? R.raw.hamburger_finished : R.raw.salad_finished);

                            mediaPlayer = new MediaPlayer();
                            surfaceHolder = surfaceView.getHolder();
                            try {
                                mediaPlayer.setDataSource(EvaluationGame2Activity.this, Uri.parse(uriString));

                                surfaceHolder.addCallback(new MyCallBack());
                                mediaPlayer.prepare();
                                mediaPlayer.setOnPreparedListener(mp -> {
                                    mediaPlayer.start();
                                    surfaceView.setVisibility(View.VISIBLE);
                                });
                                mediaPlayer.setOnCompletionListener(mp -> {
                                    surfaceView.setVisibility(View.GONE);
                                    mp.stop();
                                    mp.release();
                                    mp = null;
                                    startActivity(
                                            new Intent(EvaluationGame2Activity.this, Main2Activity.class)
                                                    .putExtra("enteragain", true));
                                    handler.postDelayed(() -> EvaluationGame2Activity.this.finish(), 1000);
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .playOn(aeFoodContainer);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ae_pause:
                aePause.setVisibility(View.GONE);
                ((LinearLayout) ae_restart.getParent()).setVisibility(View.VISIBLE);
                onPause();
                break;
            case R.id.ae_restart:
                aePause.setVisibility(View.VISIBLE);
                ((LinearLayout) ae_restart.getParent()).setVisibility(View.GONE);
                onResume();
                break;
        }
    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceView.setBackgroundColor(Color.TRANSPARENT);
            holder.setFormat(PixelFormat.TRANSPARENT);
            mediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    private int left;

    /**
     * 把食物放进盘子/碗里
     */
    private void putFoodIntoContainer() {
        //放置水果/蔬菜
        ImageView imageView = new ImageView(EvaluationGame2Activity.this);
        imageView.setImageResource(mFoodList.get(currentGameIndex).getImageResId());
        RelativeLayout.LayoutParams params;
        if (section == 1) {
            params = new RelativeLayout.LayoutParams(fruitHeight, fruitHeight);
            if (currentGameIndex == 0) {
                params.addRule(RelativeLayout.CENTER_VERTICAL);
            }
            switch (currentGameIndex) {
                case 0:
                    params.setMargins(0, 0, 0, 0);
                    break;

                case 1:
                    params.setMargins(DISTANCE, 0, 0, 0);
                    break;

                case 2:
                    params.setMargins(0, DISTANCE, 0, 0);
                    break;

                case 3:
                    params.setMargins(DISTANCE * 2, DISTANCE * 2, 0, 0);
                    break;
                default:
                    break;
            }
        } else {
            params = new RelativeLayout.LayoutParams(vegetableHeight, vegetableHeight);
//            if (currentGameIndex == 0) {
//                params.addRule(RelativeLayout.CENTER_IN_PARENT);
//            }
            switch (currentGameIndex) {
                case 0:
                    int width = aeFoodContainer.getWidth();
                    left = (width - 2 * vegetableHeight) / 2;
                    params.setMargins(left, 0, 0, 0);
                    break;
                case 1:
                    params.setMargins(vegetableHeight + left, 0, 0, 0);
                    break;
                case 2:
                    params.setMargins(vegetableHeight + left, vegetableHeight, 0, 0);
                    break;
                case 3:
                    params.setMargins(left, vegetableHeight, 0, 0);
                    break;
                default:
                    break;
            }
        }
        imageView.setLayoutParams(params);
        aeFoodContainer.addView(imageView);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
//        LogUtils.lb("dispatchKeyEvent");
        if (SDKCocosManager.getInstance().dispatchKeyEvent(event))
            return true;
        return super.dispatchKeyEvent(event);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
//        LogUtils.lb("dispatchGenericMotionEvent");
        if (SDKCocosManager.getInstance().dispatchGenericMotionEvent(ev))
            return true;
        return super.dispatchGenericMotionEvent(ev);

    }

    @Override
    public void onChange(int countSelected) {
//        LogUtils.lb("onChange");
        playTips(countSelected);
    }

    private long mBackTime;

    @Override
    public void onBackPressed() {
        // 连续点击返回退出
        long nowTime = SystemClock.elapsedRealtime();
        long diff = nowTime - mBackTime;
        if (diff >= 500) {
            mBackTime = nowTime;
        } else {
            AppManager.getInstance().AppExit(EvaluationGame2Activity.this);
        }
    }

}
