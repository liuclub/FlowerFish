package com.bagelplay.gameset.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.AppManager;
import com.bagelplay.gameset.utils.SoundUtil;

import java.io.IOException;

/**
 * Created by liubo on 2017/11/17.
 */

public class TestFragment extends Fragment implements View.OnClickListener {
    private ViewFlipper main2_rootview;

    private ImageView main2Hamburger;
    private ImageView main2Salad;
    private RelativeLayout languageContainer;
    private ImageView main2Exit;
    private RelativeLayout main2_videoview_container;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private int currentSectiion;

    private LinearLayout main2_second;

    private boolean click;

    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main2, container, false);

        initView(view);


        return view;
    }

    private void initView(View view) {
//        handler = new Handler();
//        main2_rootview = view.findViewById(R.id.main2_rootview);
//
//        main2_videoview_container = (RelativeLayout) main2_rootview.findViewById(R.id.main2_videoview_container);
//        surfaceView = main2_videoview_container.findViewById(R.id.surfaceview);
//        main2_videoview_container.findViewById(R.id.main2_skip).setOnClickListener(this);
//
//        main2_second = main2_rootview.findViewById(R.id.main2_second);
//        main2Hamburger = (ImageView) main2_second.findViewById(R.id.main2_hamburger);
//        main2Salad = (ImageView) main2_second.findViewById(R.id.main2_salad);
//        languageContainer = (RelativeLayout) main2_second.findViewById(R.id.language_container);
//        main2Exit = (ImageView) main2_second.findViewById(R.id.main2_exit);
//
//        //是否再次进入该界面
//        boolean enteragain = ((FragmentActivity)getActivity()).isEnteragain();
//
//        if (!enteragain) {//否，这是第一次进入该界面
//            click = false;
//            mediaPlayer = new MediaPlayer();
//            surfaceHolder = surfaceView.getHolder();
//            surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
//            surfaceView.setBackgroundColor(0);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//            try {
//                mediaPlayer.setDataSource(getActivity(), Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.game_introduction));
//                surfaceHolder.addCallback(new SurfaceHolder.Callback() {
//                    @Override
//                    public void surfaceCreated(SurfaceHolder holder) {
//                        mediaPlayer.setDisplay(holder);
//                        mediaPlayer.start();
//                    }
//
//                    @Override
//                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                    }
//
//                    @Override
//                    public void surfaceDestroyed(SurfaceHolder holder) {
//
//                    }
//                });
//                mediaPlayer.prepare();
//                mediaPlayer.setOnCompletionListener(mp -> {
//                    mp.stop();
//                    mp.release();
//                    next();
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {//再次进入该界面
//            if (main2_videoview_container.getVisibility() != View.GONE) {
//                main2_videoview_container.setVisibility(View.GONE);
//            }
//            click = true;
//            SoundUtil.getInstance(getActivity()).startPlaySound(R.raw.continue_or_exit_game);
//        }
//        view.findViewById(R.id.language_en).setOnClickListener(this);
//        view.findViewById(R.id.language_zh).setOnClickListener(this);
//        main2Hamburger.setOnClickListener(this);
//        main2Salad.setOnClickListener(this);
//        main2Exit.setOnClickListener(this);
//
//        currentSectiion = 1;
    }

    private void next() {
        main2_rootview.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.test_in));
        main2_rootview.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.test_out));

        main2_rootview.showNext();
        handler.postDelayed(() -> {
            click = false;
            SoundUtil.getInstance(getActivity()).startPlaySoundWithListener(R.raw.hamburger_or_salad, new SoundUtil.MediaPlayListener() {
                @Override
                public void onPlayerCompletion() {
                    click = true;
                }
            });
        }, 300);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.language_en:
                if (click) {
                    startActivity(new Intent(getActivity(), EvaluationGame2Activity.class)
                            .putExtra("language", "en")
                            .putExtra("section", currentSectiion));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                        }
                    }, 1000);
                }
                break;
            case R.id.language_zh:
                if (click) {
                    startActivity(new Intent(getActivity(), EvaluationGame2Activity.class)
                            .putExtra("language", "zh")
                            .putExtra("section", currentSectiion));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                        }
                    }, 1000);
                }
                break;
            case R.id.main2_hamburger:
                if (click) {
                    click = false;
                    LinearLayout parent = (LinearLayout) main2Hamburger.getParent();
                    parent.setGravity(Gravity.CENTER_HORIZONTAL);
                    languageContainer.setVisibility(View.VISIBLE);
                    main2Salad.setVisibility(View.GONE);
                    currentSectiion = 1;
                    SoundUtil.getInstance(getActivity()).startPlaySoundWithListener(R.raw.select_language, new SoundUtil.MediaPlayListener() {
                        @Override
                        public void onPlayerCompletion() {
                            click = true;
                        }
                    });
                }
                break;
            case R.id.main2_salad:
                if (click) {
                    click = false;
                    LinearLayout parent = (LinearLayout) main2Hamburger.getParent();
                    parent.setGravity(Gravity.CENTER_HORIZONTAL);
                    main2Hamburger.setVisibility(View.GONE);
                    languageContainer.setVisibility(View.VISIBLE);
                    currentSectiion = 2;
                    SoundUtil.getInstance(getActivity()).startPlaySoundWithListener(R.raw.select_language, new SoundUtil.MediaPlayListener() {
                        @Override
                        public void onPlayerCompletion() {
                            click = true;
                        }
                    });
                }
                break;
            case R.id.main2_exit:
                if (click) {
                    if (languageContainer.getVisibility() != View.GONE) {
                        switch (currentSectiion) {
                            case 1:
                                main2Salad.setVisibility(View.VISIBLE);

                                break;
                            case 2:
                                main2Hamburger.setVisibility(View.VISIBLE);
                                break;
                        }

                        LinearLayout parent = (LinearLayout) main2Hamburger.getParent();
                        parent.setGravity(Gravity.LEFT);
                        languageContainer.setVisibility(View.GONE);
                    } else {//开始界面:提示是否退出应用
                        AppManager.getInstance().AppExit(((FragmentActivity)getActivity()));
                    }
                }
                break;
//            case R.id.main2_skip:
//                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    mediaPlayer=null;
//                }
//                next();
//                break;
        }
    }
}
