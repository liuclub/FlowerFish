/*
 *
 * IMPlayListener.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.bagelplay.gameset.player;

/**
 * Description:
 */
public interface IMPlayListener {

    void onStart(IMPlayer player);
    void onPause(IMPlayer player);
    void onResume(IMPlayer player);
    void onComplete(IMPlayer player);

}
