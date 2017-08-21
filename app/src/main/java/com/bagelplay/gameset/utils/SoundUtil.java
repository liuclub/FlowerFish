package com.bagelplay.gameset.utils;

import android.content.Context;
import android.media.MediaPlayer;

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
            // TODO Auto-generated catch block
            e.printStackTrace();
            player.release();
            player=null;
        }
    }

    public void stopPlaySound() {


        if (player != null) {
            player.release();

            isPlayComplete = true;
        }

    }
}
