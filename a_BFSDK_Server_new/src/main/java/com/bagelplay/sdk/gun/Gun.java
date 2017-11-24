package com.bagelplay.sdk.gun;

import android.content.Context;
import android.view.MotionEvent;

import cn.tvplay.cursor.PScursor;

import com.bagelplay.sdk.common.SDKManager;

public class Gun {
    static {
        System.loadLibrary("com_bagelplay_sdk_gun");//载入本地库
    }

    protected SDKManager sdkManager;

    private int lastTrigger;

    public Gun(Context context, SDKManager sdkManager) {
        this.sdkManager = sdkManager;
        new PScursor(context, "cursorim");
    }

	
	/*TextView tv;
	Handler handler	=	new Handler();
	public Gun(Context context,TextView tv)
	{
		this.tv	=	tv;
		new PScursor(context,"cursorim");
	}*/

    public native void start(GunEvent event, Gun gun);

    public native void pause();

    public native void resume();

    public native void stop();

    public void OnGunEvent(final GunEvent event) {
        int trigger = event.key & GunEvent.mKEY_TRIGGER;
        if (trigger > 0 && lastTrigger == 0) {
            sdkManager.getTouch().touch(MotionEvent.ACTION_DOWN, (float) event.pointX, (float) event.pointY);
            sdkManager.getTouch().touch(MotionEvent.ACTION_UP, (float) event.pointX, (float) event.pointY);
        }
        sdkManager.getSocketThirdStub().sendGunEventData(event);
        lastTrigger = trigger;
		
		
		/*handler.post(new Runnable(){

			@Override
			public void run() {
				tv.setText(event.)
			}
			
		});*/
    }
}
