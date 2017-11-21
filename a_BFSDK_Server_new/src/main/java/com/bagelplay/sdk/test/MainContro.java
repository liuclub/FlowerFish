package com.bagelplay.sdk.test;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bagelplay.sdk.cocos.SDKCocosManager;
import com.bagelplay.sdk.common.OnVoiceListener;
import com.bagelplay.sdk.common.SDKManager;
import com.bagelplay.sdk.common.player.OnPlayerState;
import com.bagelplay.sdk.common.player.PlayerManager;

public class MainContro extends Activity {
    private SDKCocosManager bfusm;

    public Button btn;

    boolean isVoice;

    private Handler handler = new Handler();

    private int lastPlayerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bfusm = SDKCocosManager.getInstance(this);
        bfusm.addWindowCallBack(this);
        bfusm.setControlZip("mouse.zip");
         /*bfusm.setLocateRemoteControlJson("r1.json");
     	
     	
     	handler.postDelayed(new Runnable(){

			@Override
			public void run() {
				 
				bfusm.onControllerKeyDown(1003, 19);
			}
     		
     	}, 5000);
     	
     	handler.postDelayed(new Runnable(){

			@Override
			public void run() {
				 
				bfusm.onControllerKeyDown(1003, 20);
			}
     		
     	}, 6000);
     	
     	handler.postDelayed(new Runnable(){

			@Override
			public void run() {
				 
				bfusm.onControllerKeyDown(1003, 21);
			}
     		
     	}, 7000);
     	
     	handler.postDelayed(new Runnable(){

			@Override
			public void run() {
				 
				bfusm.onControllerKeyDown(1003, 22);
			}
     		
     	}, 8000);*/


        btn = new Button(this);
        btn.setText("点一下说话");
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
				/*if(!isVoice)
				{
					lastPlayerID	=	SDKManager.MOUSE_CLICK_PLAYER_ID;
					initVoice();
					isVoice	=	true;
					btn.setText("点我关闭");
					Log.v("=--------------------------------=", "11111111111111   "   + lastPlayerID);
				}
				else
				{
					bfusm.stopVoice(lastPlayerID);
					isVoice	=	false;
					btn.setText("点一下说话");
					audioTrack.stop();
					Log.v("=--------------------------------=", "2222222222   "  + lastPlayerID);
				}*/

				/*bfusm.checkVoice("ok", new OnVoiceResultLinstener(){

					@Override
					public void OnVoiceResult(final String right, final String result) {
						Log.v("=----------------------------------------=", right + "  " + result);

						handler.post(new Runnable(){
							public void run(){
								btn.setText( right + "  " + result);
							}


						});

					}

				});*/

                PlayerManager.getInstance().getPlayerIDs();// 可得到当前多少手机助手连入
            }
        });
        //setContentView(btn);

        final EditText et = new EditText(this);
        //setContentView(et);
        et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bfusm.showKeyboardForEditText(et);
            }
        });
        et.setInputType(InputType.TYPE_NULL);


     	/*btn = new Button(this);
     	btn.setText("得到用户数");
     	btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int[] ids	=	PlayerManager.getInstance().getPlayerIDs();
				for(int i=0;i<ids.length;i++)
				{
					Log.v("=----------------------------------------=", ids[i] + " ");
				}
			}
		});
     	PlayerManager.getInstance().setOnPlayerState(new OnPlayerState(){

			@Override
			public void onPlayerConnect(int playerID) {
				Log.v("=----------------------------------------=", playerID + " onPlayerConnect");
			}

			@Override
			public void onPlayerDisconnect(int playerID) {
				Log.v("=----------------------------------------=", playerID + " onPlayerDisconnect");
			}

     	});
     	setContentView(btn);*/


        ListView lv = new ListView(this);
        lv.setAdapter(new Item());
        setContentView(lv);


    }

    public boolean dispatchKeyEvent(KeyEvent event) {


        if (bfusm.dispatchKeyEvent(event))
            return true;
        return super.dispatchKeyEvent(event);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {

        if (bfusm.dispatchGenericMotionEvent(event))
            return true;
        return super.dispatchGenericMotionEvent(event);
    }


    protected void onStop() {
        super.onStop();
        bfusm.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        com.bagelplay.sdk.cocos.SDKCocosManager.getInstance(this).onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        com.bagelplay.sdk.cocos.SDKCocosManager.getInstance(this).onResume();
    }


    class Item extends BaseAdapter {

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(MainContro.this);
            tv.setTextColor(0xffffffff);
            tv.setText(position + " hello");
            tv.setTextColor(0xff000000);
            return tv;
        }

    }

    ;

    AudioTrack audioTrack;

    private void initVoice() {
        int frequency = 16000;
        int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

        int bufferSize = AudioTrack.getMinBufferSize(frequency, AudioFormat.CHANNEL_OUT_MONO, audioEncoding);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, AudioFormat.CHANNEL_OUT_MONO, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
        audioTrack.play();//开始

        bfusm.startVoice(lastPlayerID, frequency, channelConfiguration, audioEncoding, new OnVoiceListener() {

            @Override
            public void OnVoice(byte[] data, int playerID) {
                Log.v("=----OnVoice-------=", "OnVoice   " + playerID + "  " + data.length);
                audioTrack.write(data, 0, data.length);
            }

        });


    }


}
