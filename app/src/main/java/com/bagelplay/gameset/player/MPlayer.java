/*
 *
 * MPlayer.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.bagelplay.gameset.player;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.activity.EvaluationGame2Activity;
import com.bagelplay.gameset.activity.Main2ActivityBackup;
import com.bagelplay.gameset.utils.LogUtils;

import java.io.IOException;

/**
 * Description:
 */
public class MPlayer implements IMPlayer, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnErrorListener, SurfaceHolder.Callback {

    private MediaPlayer player;

    private String source;
    private IMDisplay display;

    private boolean isVideoSizeMeasured = false;  //视频宽高是否已获取，且不为0
    private boolean isMediaPrepared = false;      //视频资源是否准备完成
    private boolean isSurfaceCreated = false;     //Surface是否被创建
    private boolean isUserWantToPlay = false;     //使用者是否打算播放
    public boolean isResumed = false;            //是否在Resume状态

    private boolean mIsCrop = false;

    private IMPlayListener mPlayListener;

    private int currentVideoWidth;              //当前视频宽度
    private int currentVideoHeight;             //当前视频高度

    private void createPlayerIfNeed() {
        if (null == player) {
            LogUtils.lb("createPlayerIfNeed");
            player = new MediaPlayer();
            player.setScreenOnWhilePlaying(true);
            player.setOnBufferingUpdateListener(this);
            player.setOnVideoSizeChangedListener(this);
            player.setOnCompletionListener(this);
            player.setOnPreparedListener(this);
            player.setOnSeekCompleteListener(this);
            player.setOnErrorListener(this);
        }
    }

    private void playStart() {
        boolean temp = isVideoSizeMeasured && isMediaPrepared && isSurfaceCreated && isUserWantToPlay && isResumed;
        LogUtils.lb("isVideoSizeMeasured = " + isVideoSizeMeasured);
        LogUtils.lb("isMediaPrepared = " + isMediaPrepared);
        LogUtils.lb("isSurfaceCreated = " + isSurfaceCreated);
        LogUtils.lb("isUserWantToPlay = " + isUserWantToPlay);
        LogUtils.lb("isResumed = " + isResumed);
        if (temp) {
            if (activity instanceof EvaluationGame2Activity) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((EvaluationGame2Activity) activity).eva_surfaceview_container.setVisibility(View.VISIBLE);
                    }
                });
            }
            LogUtils.lb("playStart");
            player.setDisplay(display.getHolder());
            player.start();
            log("视频开始播放");
            display.onStart(this);
            if (mPlayListener != null) {
                mPlayListener.onStart(this);
            }
        }
    }

    private void playPause() {
        if (player != null && player.isPlaying()) {
            LogUtils.lb("playPause");
            player.pause();
            display.onPause(this);
            if (mPlayListener != null) {
                mPlayListener.onPause(this);
            }
        }
    }

    private boolean checkPlay() {
        LogUtils.lb("checkPlay");
        if (source == null || source.length() == 0) {
            return false;
        }
        return true;
    }

    public void setPlayListener(IMPlayListener listener) {
        LogUtils.lb("setPlayListener");
        this.mPlayListener = listener;
    }

    /**
     * 设置是否裁剪视频，若裁剪，则视频按照DisplayView的父布局大小显示。
     * 若不裁剪，视频居中于DisplayView的父布局显示
     *
     * @param isCrop 是否裁剪视频
     */
    public void setCrop(boolean isCrop) {
        this.mIsCrop = isCrop;
        if (display != null && currentVideoWidth > 0 && currentVideoHeight > 0) {
            LogUtils.lb("setCrop");
            tryResetSurfaceSize(display.getDisplayView(), currentVideoWidth, currentVideoHeight);
        }
    }

    public boolean isCrop() {
        LogUtils.lb("isCrop");
        return mIsCrop;
    }

    /**
     * 视频状态
     *
     * @return 视频是否正在播放
     */
    public boolean isPlaying() {
        LogUtils.lb("isPlaying");
        return player != null && player.isPlaying();
    }

    //根据设置和视频尺寸，调整视频播放区域的大小
    private void tryResetSurfaceSize(final View view, int videoWidth, int videoHeight) {
        LogUtils.lb("tryResetSurfaceSize");
        ViewGroup parent = (ViewGroup) view.getParent();
        int width = parent.getWidth();
        int height = parent.getHeight();
        if (width > 0 && height > 0) {
//            final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            if (mIsCrop) {
                float scaleVideo = videoWidth / (float) videoHeight;
                float scaleSurface = width / (float) height;
                if (scaleVideo < scaleSurface) {
                    params.width = width;
                    params.height = (int) (width / scaleVideo);
                    params.setMargins(0, (height - params.height) / 2, 0, (height - params.height) / 2);
                } else {
                    params.height = height;
                    params.width = (int) (height * scaleVideo);
                    params.setMargins((width - params.width) / 2, 0, (width - params.width) / 2, 0);
                }
            } else {
                if (videoWidth > width || videoHeight > height) {
                    float scaleVideo = videoWidth / (float) videoHeight;
                    float scaleSurface = width / height;
                    if (scaleVideo > scaleSurface) {
                        params.width = width;
                        params.height = (int) (width / scaleVideo);
                        params.setMargins(0, (height - params.height) / 2, 0, (height - params.height) / 2);
                    } else {
                        params.height = height;
                        params.width = (int) (height * scaleVideo);
                        params.setMargins((width - params.width) / 2, 0, (width - params.width) / 2, 0);
                    }
                }
            }
            view.setLayoutParams(params);
        }
    }

    private Activity activity;

    //    @Override
    public void setSource(Activity activity, String url) throws MPlayerException {
        LogUtils.lb("setSource");
        this.activity = activity;
        this.source = url;
        createPlayerIfNeed();
        isMediaPrepared = false;
        isVideoSizeMeasured = false;
        currentVideoWidth = 0;
        currentVideoHeight = 0;
        player.reset();
        try {
//            player.setDataSource(url);
            player.setDataSource(activity, Uri.parse(url));
//            player.prepareAsync();
            player.prepare();
            log("异步准备视频");
        } catch (IOException e) {
            throw new MPlayerException("set source error", e);
        }
    }

    @Override
    public void setDisplay(IMDisplay display) {
        LogUtils.lb("setDisplay");
        if (this.display != null && this.display.getHolder() != null) {
            this.display.getHolder().removeCallback(this);
        }
        this.display = display;
        this.display.getHolder().addCallback(this);
    }

    @Override
    public void play() throws MPlayerException {
        LogUtils.lb("play");
        if (!checkPlay()) {
            throw new MPlayerException("Please setSource");
        }
        createPlayerIfNeed();
        isUserWantToPlay = true;
        playStart();
    }

    @Override
    public void pause() {
        LogUtils.lb("pause");
        isUserWantToPlay = false;
        playPause();
    }

    @Override
    public void onPause() {
        LogUtils.lb("onPause");
        isResumed = false;
        playPause();
    }

    @Override
    public void onResume() {
        LogUtils.lb("onResume");
        isResumed = true;
        playStart();
    }

    @Override
    public void onDestroy() {
        LogUtils.lb("onDestroy");
        if (player != null) {
            player.release();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtils.lb("onBufferingUpdate");

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtils.lb("onCompletion");
        display.onComplete(this);
        if (mPlayListener != null) {
            mPlayListener.onComplete(this);
        }

        boolean temp = isVideoSizeMeasured && isMediaPrepared && isSurfaceCreated && isUserWantToPlay && isResumed;

        if (temp && activity instanceof Main2ActivityBackup) {
            pause();
            ((Main2ActivityBackup) activity).next();
        }

        if (activity instanceof EvaluationGame2Activity) {
            ((EvaluationGame2Activity) activity).startActivity(
                    new Intent(activity, Main2ActivityBackup.class)
                            .putExtra("enteragain", true));
            activity.finish();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtils.lb("onPrepared");
        log("视频准备完成");
        isMediaPrepared = true;
        playStart();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        log("视频大小被改变->" + width + "/" + height);
        if (width > 0 && height > 0) {
            LogUtils.lb("onVideoSizeChanged");
            this.currentVideoWidth = width;
            this.currentVideoHeight = height;
            tryResetSurfaceSize(display.getDisplayView(), width, height);
            isVideoSizeMeasured = true;
            playStart();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        LogUtils.lb("onSeekComplete");

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtils.lb("onError : " + "\r\n" + "what = " + what + "\r\nextra = " + extra);
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (display != null && holder == display.getHolder()) {
            LogUtils.lb("surfaceCreated");
            isSurfaceCreated = true;
            //此举保证以下操作下，不会黑屏。（或许还是会有手机黑屏）
            //暂停，然后切入后台，再切到前台，保持暂停状态
            if (player != null) {
                player.setDisplay(holder);
                //不加此句360f4不会黑屏、小米note1会黑屏，其他机型未测
                player.seekTo(player.getCurrentPosition());
            }
            log("surface被创建");
            playStart();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtils.lb("surfaceChanged");
        log("surface大小改变");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.lb("surfaceDestroyed");
        if (display != null && holder == display.getHolder()) {
            log("surface被销毁");
            isSurfaceCreated = false;
        }
    }

    private void log(String content) {
        Log.e("MPlayer", content);
    }

}
