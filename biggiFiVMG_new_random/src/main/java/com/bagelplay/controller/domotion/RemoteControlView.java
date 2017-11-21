package com.bagelplay.controller.domotion;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.widget.BagelPlayVibrator;

public class RemoteControlView extends DoMotionView {

    private static final int MESSAGE_WHAT_DOWN = 1;

    private static final int MESSAGE_WHAT_UP = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MESSAGE_WHAT_DOWN: {
                    int keyCode = msg.arg1;
                    bfVmaStub.sendKeyData(keyCode, KeyEvent.ACTION_DOWN);
                    sendDownRepeat(keyCode, 20);
                }
                break;

                case MESSAGE_WHAT_UP: {
                    int keyCode = msg.arg1;
                    bfVmaStub.sendKeyData(keyCode, KeyEvent.ACTION_UP);
                }
                break;

            }
        }
    };

    ImageButton powerBtn;
    ImageButton volumeAddBtn, volumeReduceBtn;
    ImageButton upBtn, downBtn, leftBtn, rightBtn;
    ImageButton okBtn;
    ImageButton backBtn;
    ImageButton homeBtn;
    ImageButton menuBtn;

    private BagelPlayVibrator vibrator;
    private BagelPlayVmaStub bfVmaStub;

    public RemoteControlView(Context context, JsonParser jp) {
        super(context, jp);

        bfVmaStub = BagelPlayVmaStub.getInstance();
        vibrator = BagelPlayVibrator.getInstance();

        View view = View.inflate(context, R.layout.keypad_ui_v, null);
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, lllp);

        clickKeypad();
    }

    private void clickKeypad() {
        backBtn = (ImageButton) findViewById(R.id.keypad_back);
        backBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_BACK, backBtn, R.drawable.back_keypad_focus, R.drawable.back_keypad);

                return true;
            }
        });

        homeBtn = (ImageButton) findViewById(R.id.keypad_home);
        homeBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_HOME, homeBtn, R.drawable.home_keypad_focus, R.drawable.home_keypad);

                return true;
            }
        });

        menuBtn = (ImageButton) findViewById(R.id.keypad_menu);
        menuBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_MENU, menuBtn, R.drawable.menu_keypad_focus, R.drawable.menu_keypad);

                return true;
            }
        });

        powerBtn = (ImageButton) findViewById(R.id.keypad_power);
        powerBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_POWER, powerBtn, R.drawable.poweroff_focus, R.drawable.poweroff);

                return true;
            }
        });

        volumeAddBtn = (ImageButton) findViewById(R.id.keypad_volume_add);
        volumeAddBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_VOLUME_UP, volumeAddBtn, R.drawable.volume_add_focus, R.drawable.volume_add);

                return true;
            }
        });

        volumeReduceBtn = (ImageButton) findViewById(R.id.keypad_volume_reduce);
        volumeReduceBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_VOLUME_DOWN, volumeReduceBtn, R.drawable.volume_reduce_focus, R.drawable.volume_reduce);

                return true;
            }
        });

        okBtn = (ImageButton) findViewById(R.id.keypad_ok);
        okBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_DPAD_CENTER, okBtn, R.drawable.ok_focus, R.drawable.ok);

                return true;
            }
        });

        upBtn = (ImageButton) findViewById(R.id.keypad_up);
        upBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_DPAD_UP, upBtn, R.drawable.up_focus, R.drawable.up);

                return true;
            }
        });

        downBtn = (ImageButton) findViewById(R.id.keypad_down);
        downBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_DPAD_DOWN, downBtn, R.drawable.down_focus, R.drawable.down);

                return true;
            }
        });

        leftBtn = (ImageButton) findViewById(R.id.keypad_left);
        leftBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_DPAD_LEFT, leftBtn, R.drawable.left_focus, R.drawable.left);

                return true;
            }
        });

        rightBtn = (ImageButton) findViewById(R.id.keypad_right);
        rightBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch(event, KeyEvent.KEYCODE_DPAD_RIGHT, rightBtn, R.drawable.right_focus, R.drawable.right);

                return true;
            }
        });

    }

    private void touch(MotionEvent event, int key, View view, int downResource, int upResource) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            view.setBackgroundResource(downResource);
            vibrator.vibrate(30);
            sendDownRepeat(key, 0);
        }
        if (action == MotionEvent.ACTION_UP) {
            handler.removeMessages(MESSAGE_WHAT_DOWN);
            view.setBackgroundResource(upResource);
            sendUp(key);
        }
    }

    private void sendDownRepeat(int keyCode, int delay) {
        Message m = new Message();
        m.what = MESSAGE_WHAT_DOWN;
        m.arg1 = keyCode;
        handler.sendMessageDelayed(m, delay);
    }

    private void sendUp(int keyCode) {
        Message m = new Message();
        m.what = MESSAGE_WHAT_UP;
        m.arg1 = keyCode;
        handler.sendMessage(m);
    }

    @Override
    public Bitmap getDefaultBackground() {
        return null;
    }
}	
