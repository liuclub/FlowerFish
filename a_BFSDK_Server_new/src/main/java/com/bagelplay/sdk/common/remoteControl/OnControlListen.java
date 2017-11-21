package com.bagelplay.sdk.common.remoteControl;

import com.bagelplay.sdk.common.util.Log;

public interface OnControlListen {
	
	public void onKeyLongTouch(int keyCode);
	
	public void onKeyClick(int keyCode);
	
	public void onKeyDown(int keyCode);
	
	public void onKeyUp(int keyCode);
}
