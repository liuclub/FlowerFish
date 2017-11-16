package com.bagelplay.gameset.utils;

import android.content.Context;
import android.media.MediaPlayer;

import static android.R.attr.path;

/**
 * Created by zhangtianjie on 2017/8/2.
 */

public class SoundUtil {

    private static MediaPlayer player;
    private static boolean isPlayComplete = true;

    private static Context mContext;

    private static SoundUtil soundUtil;


    public static SoundUtil getInstance(Context context) {
        if (soundUtil == null) {
            soundUtil = new SoundUtil();
        }

        if (player == null) {
            player = new MediaPlayer();
        }


        mContext = context;

        return soundUtil;

    }


//    public SoundUtil(Context context) {
//        if(player==null) {
//            this.player = new MediaPlayer();
//        }
//        mContext=context;
//    }

    public void startPlaySound(int path) {
        if (isPlayComplete) {
            music_play(path);
        } else {
            stopPlaySound();
            music_play(path);
        }
    }


    public void startPlaySoundWithListener(int path, MediaPlayListener mMediaPlayListener) {
        setOnMediaPlayListener(mMediaPlayListener);

        if (isPlayComplete) {

            music_play_withl_istener(path);

        } else {

            stopPlaySound();

            music_play_withl_istener(path);

        }

    }

    public void startPlaySoundFromSD(String path) {
        if (isPlayComplete) {

            music_play_sd(path);

        } else {

            stopPlaySound();

            music_play_sd(path);

        }

    }

    public void music_play_sd(String path) {

        player = new MediaPlayer();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


                player.release();
                isPlayComplete = true;

            }
        });

        try {
            player.setDataSource(path);
            isPlayComplete = false;
            player.prepare();
            player.start();


        } catch (Exception e) {
            e.printStackTrace();
            player.release();
            player = null;
        }
    }


    public void music_play_withl_istener(int path) {

        player = MediaPlayer.create(mContext, path);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


                player.release();
                isPlayComplete = true;
                if (mMediaPlayListener != null) {
                    mMediaPlayListener.onPlayerCompletion();

                }


            }
        });

        try {
            isPlayComplete = false;
            // player.prepare();
            player.start();


        } catch (Exception e) {
            e.printStackTrace();
            player.release();
            player = null;
        }
    }


    public void music_play(int path) {

        player = MediaPlayer.create(mContext, path);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


                player.release();
                isPlayComplete = true;


            }
        });

        try {
            isPlayComplete = false;
            // player.prepare();
            player.start();


        } catch (Exception e) {
            e.printStackTrace();
            player.release();
            player = null;
        }
    }

    public void stopPlaySound() {


        if (player != null) {
            player.release();

            isPlayComplete = true;
        }

    }


    private MediaPlayListener mMediaPlayListener;

    public void setOnMediaPlayListener(MediaPlayListener mMediaPlayListener) {
        // mMediaPlayListener=null;
        this.mMediaPlayListener = mMediaPlayListener;
    }

    public interface MediaPlayListener {
        void onPlayerCompletion();
    }
}
