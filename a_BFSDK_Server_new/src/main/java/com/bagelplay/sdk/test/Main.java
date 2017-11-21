package com.bagelplay.sdk.test;

 

//import com.bagelplay.sdk.common.Logo;
//import com.bagelplay.sdk.common.SDKManager;
 


public class Main extends Logo{

	@Override
	public Class getNextClass() {
 		return MainGun.class;
	}

	@Override
	public SDKManager getSDKManager() {
		
 		com.bagelplay.sdk.cocos.SDKCocosManager ss	=	com.bagelplay.sdk.cocos.SDKCocosManager.getInstance(this);
		
		return ss;
	}
	
 
	
	 
}
