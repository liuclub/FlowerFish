package com.bagelplay.flowerfish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bagelplay.sdk.cocos.SDKCocosManager;
import com.bagelplay.sdk.common.Logo;
import com.bagelplay.sdk.common.SDKManager;

public class MainLogo extends Logo {



    @Override
    public Class getNextClass() {
        // TODO Auto-generated method stub
        return MainActivity.class;
    }

    @Override
    public SDKManager getSDKManager() {
        // TODO Auto-generated method stub
        return SDKCocosManager.getInstance(this);
    }
}
