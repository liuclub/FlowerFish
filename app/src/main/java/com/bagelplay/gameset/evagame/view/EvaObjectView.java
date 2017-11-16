package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.LogUtils;
import com.bagelplay.gameset.utils.SPUtil;

/**
 * Created by zhangtianjie on 2017/8/28.
 */

public class EvaObjectView extends FrameLayout {
    TextView mTvEvaText;
    ImageView mIvEvaObject;
    ImageView mIvEvaHamNull;
    Context mContext;

    private Handler timeHandler;

    Animation textAnimation;
    private int TIME = 1000;

    EvaFruitRotate mEvaFruitRotate;


    private boolean move;
    private long lastClickTime;
    private boolean disableTouch;

    private TouchInterface touchInterface;

    public void setDisableTouch(boolean disableTouch) {
        this.disableTouch = disableTouch;
    }


    public interface TouchInterface {
        /**
         * 移动结束后，显示控件
         */
        void showUI();

        /**
         * 移动水果/蔬菜过程中，隐藏控件
         */
        void hideUI();
    }

    public void setTouchInterface(TouchInterface touchInterface) {
        this.touchInterface = touchInterface;
    }


    public void setButtonClickable(boolean buttonClickable) {
        this.buttonClickable = buttonClickable;
    }


    public boolean isButtonClickable() {
        return buttonClickable;
    }

    private boolean buttonClickable = true;

    public EvaObjectView(@NonNull Context context) {
        super(context);
    }

    int lastX, lastY;
    int left, top, right, bottom;

    public EvaObjectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.eva_object_view_layout, this, true);

        initUI();

        mIvEvaObject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                LogUtils.lb("onClick");

                if (!isButtonClickable()) {
                    return;
                }

                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.btn_normal_to_middle_large);
                v.startAnimation(animation);

                if (mEvaObjectViewListener != null) {
                    mEvaObjectViewListener.objectClick();
                }
            }
        });
        textAnimation = AnimationUtils.loadAnimation(mContext, R.anim.hide_to_show);
        timeHandler = new Handler();
        mIvEvaObject.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (disableTouch) {
                    return false;
                } else {
//                LogUtils.lb("onTOuch");
                    int action = event.getAction();
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
//                        LogUtils.lb("ACTION_DOWN");
                            lastX = x;
                            lastY = y;

                            left = getLeft();
                            top = getTop();
                            right = getRight();
                            bottom = getBottom();
                            lastClickTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_MOVE:
//                        LogUtils.lb("ACTION_MOVE");
                            break;
                        case MotionEvent.ACTION_UP:
//                        LogUtils.lb("ACTION_UP");
                            break;
                    }
                    long current = System.currentTimeMillis();
                    boolean boo = current - lastClickTime < 1000 && ((x - lastX < 100) && (y - lastY < 100)) && !move;
                LogUtils.lb("current - lastClickTime < 1000            " + (current - lastClickTime < 1000));
                LogUtils.lb("(x == lastX && y == lastY)            " + (x == lastX && y == lastY));
                LogUtils.lb("!move            " + (!move));
                LogUtils.lb("boo            " + (boo));
                    if (boo) {//没有移动
                        move = false;
                        return false;
                    } else {//移动了
                        if (event.getAction() == MotionEvent.ACTION_MOVE) {
                            int offsetX = x - lastX;
                            int offsetY = y - lastY;
                            layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                            move = true;
                            touchInterface.hideUI();
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            touchInterface.showUI();
                            move = false;
                        }
                        return true;
                    }
                }
            }
        });
    }

    public void initView() {
        mTvEvaText.setVisibility(INVISIBLE);
        mIvEvaObject.setVisibility(VISIBLE);
        timeHandler.postDelayed(runnable, TIME);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                mTvEvaText.setVisibility(VISIBLE);

                mTvEvaText.startAnimation(textAnimation);
                if (mEvaObjectViewListener != null) {
                    mEvaObjectViewListener.objectStartPlaySounds();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void startAnim() {
        mTvEvaText.setVisibility(VISIBLE);
        mTvEvaText.startAnimation(textAnimation);
    }

    public void setObjectGone() {
        mTvEvaText.setVisibility(GONE);
        mIvEvaObject.setVisibility(GONE);

    }

    //获得要闪三下的食物物体
    public ImageView getFlashObject() {
        return mIvEvaObject;
    }


    public ImageView getObjectHamNull() {

        return mIvEvaHamNull;
    }


    public EvaFruitRotate getObjectFruteRotate() {
        return mEvaFruitRotate;
    }

    private void initUI() {
        mTvEvaText = (TextView) findViewById(R.id.tv_eva_text);
        mIvEvaObject = (ImageView) findViewById(R.id.iv_eva_object);
        mIvEvaHamNull = (ImageView) findViewById(R.id.iv_eva_ham_null);
        mEvaFruitRotate = (EvaFruitRotate) findViewById(R.id.eva_fruit_rotate);
    }


    private EvaObjectViewListener mEvaObjectViewListener;

    public void setEvaObjectViewListener(EvaObjectViewListener mEvaObjectViewListener) {
        this.mEvaObjectViewListener = mEvaObjectViewListener;
    }

    public void changeObject(String text, int imgId) {
        mTvEvaText.setText(text);
        mIvEvaObject.setImageResource(imgId);
        initView();
    }

    public interface EvaObjectViewListener {
        void objectClick();

        void objectStartPlaySounds();
    }


    public EvaObjectView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.lb("keyCode = " + keyCode);
        LogUtils.lb("event.getKeyCode() = " + event.getKeyCode());
        return true;
    }

}
