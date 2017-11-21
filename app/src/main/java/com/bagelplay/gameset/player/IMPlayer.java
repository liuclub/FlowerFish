/*
 *
 * IMPlayer.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.bagelplay.gameset.player;

import android.app.Activity;

/**
 * Description:
 */
public interface IMPlayer {

    /**
     * 设置资源
     *
     * @param url 资源路径
     * @throws MPlayerException
     */
    void setSource(Activity activity, String url) throws MPlayerException;

    /**
     * 设置显示视频的载体
     *
     * @param display 视频播放的载体及相关界面
     */
    void setDisplay(IMDisplay display);

    /**
     * 播放视频
     *
     * @throws MPlayerException
     */
    void play() throws MPlayerException;

    /**
     * 暂停视频
     */
    void pause();


    void onPause();

    void onResume();

    void onDestroy();

}
