package com.bagelplay.sdk.common;

import java.io.IOException;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.bagelplay.sdk.common.util.FileDir;
import com.bagelplay.sdk.common.util.Log;

public class Mouse {

	private static String TAG	=	"Mouse";

	private static final int MESSAGE_WHAT_SET_MOUSE_SHOW	=	1;

	//private static final int MESSAGE_WHAT_CENTER_MOUSE	=	2;

	private static final int MESSAGE_WHAT_MOVE_MOUSE	=	3;

	private static final int MESSAGE_WHAT_MOVE_CLICK	=	4;

	private static final int MESSAGE_WHAT_SET_MOUSE_GONE	=	5;

	private static final int MESSAGE_WHAT_SET_MOUSE_SHOW_WITH_GONE	=	6;

	private static final int MESSAGE_WHAT_SET_MOUSE_XY	=	7;

	private static final int MESSAGE_WHAT_SET_MOUSE_CLICK_PIC	=	8;

	private static final int MESSAGE_WHAT_SET_MOUSE_NORMAL_PIC	=	9;

	private Handler handler	=	new Handler(){
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

				case MESSAGE_WHAT_SET_MOUSE_SHOW:

					showMouse();

					break;
            	
            	/*case MESSAGE_WHAT_CENTER_MOUSE:
            		
            		doCenterMouse();
            		
            	break;*/

				case MESSAGE_WHAT_MOVE_MOUSE:

					int offsetX	=	msg.arg1;
					int offsetY	=	msg.arg2;

					doMouseMove(offsetX,offsetY);

					break;

				case MESSAGE_WHAT_SET_MOUSE_GONE:

					mouseIV.setVisibility(View.GONE);

					break;

				case MESSAGE_WHAT_SET_MOUSE_SHOW_WITH_GONE:
					showMouseWithGone();
					break;

				case MESSAGE_WHAT_SET_MOUSE_XY:
				{
					int[] args	=	(int[]) msg.obj;
					int x		=	args[0];
					int y		=	args[1];
					int playerID	=	args[2];
					doSetMouseXY(playerID,x,y);
				}
				break;

				case MESSAGE_WHAT_SET_MOUSE_CLICK_PIC:
				{
					int playerID	=	msg.arg1;
					doSetMouseClickPic(playerID);
				}
				break;

				case MESSAGE_WHAT_SET_MOUSE_NORMAL_PIC:
				{
					int playerID	=	msg.arg1;
					doSetMouseNormalPic(playerID);
				}
				break;
			}
		}
	};

	private int mouseX;

	private int mouseY;

	private ImageView mouseIV;

	private WindowManager mWindowManager;



	private boolean isMouseActionDowned;

	private int mouseDownX;

	private int mouseDownY;

	private long mouseActionDownTime;


	private SDKManager bfusm;


	private boolean isHide;

	private boolean isDowned;

	private int hold;

	private boolean isAlwaysShow;

	private boolean debug;

	private int playerID;

	private Hashtable<Integer,Bitmap[]> playerID2Icon	=	new Hashtable<Integer,Bitmap[]>();

	private Bitmap defaultIcon;

	Mouse(SDKManager bfusm)
	{
		this.bfusm	=	bfusm;

		try {
			defaultIcon = BitmapFactory.decodeStream(bfusm.getApplicationContext().getAssets().open(FileDir.getInstance().MOUSE));
		} catch (IOException e) {
			e.printStackTrace();
		}

		addMouseView();

	}

	void openDebug()
	{
		debug	=	true;
	}

	void mouseMove(int action,int offsetX,int offsetY,int playerID)
	{
		if(action == MotionEvent.ACTION_MOVE)
		{
			if(!checkDowned())
			{
				markMouseDown(playerID);
				Log.v(TAG,"markMouseDown " + "playerID:" + playerID);
			}
			else
			{
				mouseMove(offsetX, offsetY);
				Log.v(TAG,"mouseMove ");
			}
		}
		else if(action == MotionEvent.ACTION_UP)
		{
			SDKManager.MOUSE_UP_PLAYER_ID	=	playerID;
			checkMouseClick(playerID);
			resetMouseDown();
			handler.sendEmptyMessage(MESSAGE_WHAT_SET_MOUSE_SHOW_WITH_GONE);
		}
	}

	public void putIcon(int playerId,String normalPicFile,String pressPicFile)
	{
		Bitmap[] pics	=	new Bitmap[2];
		try {
			Bitmap normalPic = BitmapFactory.decodeStream(bfusm.getApplicationContext().getAssets().open(FileDir.ASSETS_DIR + normalPicFile));
			pics[0]	=	normalPic;
			Log.v(TAG, "putIcon " + "playerId:" + playerId + " normalPicFile:" + normalPicFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Bitmap pressPic = BitmapFactory.decodeStream(bfusm.getApplicationContext().getAssets().open(FileDir.ASSETS_DIR + pressPicFile));
			pics[1]	=	pressPic;

			Log.v(TAG, "putIcon " + "playerId:" + playerId + " pressPicFile:" + pressPicFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
		playerID2Icon.put(playerId, pics);
	}

	public void mouseMove(int offsetX,int offsetY)
	{
		if(isHide || Base.isRunInBackGround)
			return;

		Message m	=	new Message();
		m.what		=	MESSAGE_WHAT_MOVE_MOUSE;
		m.arg1		=	offsetX;
		m.arg2		=	offsetY;
		handler.sendMessage(m);
	}

	public void setAlwaysShow(boolean alwaysShow)
	{
		isAlwaysShow	=	alwaysShow;
	}

	private boolean checkDowned()
	{
		if(!isDowned)
		{
			isDowned	=	true;
			return false;
		}

		return true;
	}



	public void doMouseMove(int offsetX,int offsetY)
	{
		if(isHide || Base.isRunInBackGround)
			return;

		mouseX	+=	offsetX;
		mouseY	+=	offsetY;

		Log.v(TAG,"doMouseMove " + "mouseX:" + mouseX + " mouseY:" + offsetY + " offsetX:" + offsetX + " offsetY:" + offsetY);

		checkMouseBound();

		android.view.WindowManager.LayoutParams fllp	=	(android.view.WindowManager.LayoutParams) mouseIV.getLayoutParams();
		fllp.x	=	mouseX;
		fllp.y	=	mouseY;
		mWindowManager.updateViewLayout(mouseIV, fllp);

		showMouseWithGone();


		if(hold == 1)
		{

			Touch touch	=	 bfusm.getTouch();
			touch.touch(MotionEvent.ACTION_MOVE,mouseX,mouseY);
		}
	}

	private void checkMouseBound()
	{
		if(mouseX < 0)
		{
			mouseX	=	0;
		}

		if(mouseX > bfusm.getScreenWidth())
		{
			mouseX	=	bfusm.getScreenWidth();
		}

		if(mouseY < 0)
		{
			mouseY	=	0;
		}

		if(mouseY > bfusm.getScreenHeight())
		{
			mouseY	=	bfusm.getScreenHeight();
		}

		Log.v(TAG,"checkMouseBound " + "screenWidth:" + bfusm.getScreenWidth() + " screenHeight:" + bfusm.getScreenHeight());
	}

	private void addMouseView(){

		//获取WindowManager
		mouseIV	=	new ImageView(bfusm.getApplicationContext());
		Bitmap pic	=	defaultIcon;
		try {
			pic = BitmapFactory.decodeStream(bfusm.getApplicationContext().getAssets().open(FileDir.getInstance().MOUSE));
		} catch (IOException e) {
			e.printStackTrace();
		}

		mouseIV.setImageBitmap(pic);

		mWindowManager=(WindowManager)bfusm.getApplicationContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		//设置LayoutParams(全局变量）相关参数
		android.view.WindowManager.LayoutParams  param = new WindowManager.LayoutParams();

		param.type=WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;     // 系统提示类型,重要
		param.format=1;
		param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
		param.flags = param.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		param.flags = param.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制



		param.gravity=Gravity.LEFT|Gravity.TOP;   //调整悬浮窗口至左上角
		//以屏幕左上角为原点，设置x、y初始值


		//设置悬浮窗口长宽数据
		param.width	= pic != null ? pic.getWidth() : 0;
		param.height	= pic != null ? pic.getHeight() : 0;
		param.x		=	bfusm.getScreenWidth() / 2 - pic.getWidth() / 2;
		param.y		=	bfusm.getScreenHeight() / 2 - pic.getHeight() / 2;

		mouseX			=	 param.x;
		mouseY			=	 param.y;

		//显示myFloatView图像
		mWindowManager.addView(mouseIV, param);
		doMouseDelayGone();

		mouseIV.setTag(0);

	}


	private void markMouseDown(int playerID)
	{
		this.playerID	=	playerID;
		SDKManager.MOUSE_DOWN_PLAYER_ID	=	playerID;
		isMouseActionDowned	=	true;
		mouseDownX	=	mouseX;
		mouseDownY	=	mouseY;
		mouseActionDownTime	=	System.currentTimeMillis();
	}

	private void checkMouseClick(int playerID)
	{
		long t	=	System.currentTimeMillis();
		Log.v(TAG,"checkMouseClick " + "playerID:" + playerID + "time:" + (t - mouseActionDownTime));
		if(isMouseActionDowned && t - mouseActionDownTime <= 500)
		{
			if(Math.abs(mouseX - mouseDownX) <= 10 && Math.abs(mouseY - mouseDownY) <= 10)
			{
				if(this.playerID == playerID)
				{
					doMouseClick(playerID);
				}

			}
		}
	}

	void resetMouseDown()
	{
		isMouseActionDowned	=	false;
		isDowned			=	false;
		//playerID			=	0;
	}

	void hide()
	{
		isHide	=	true;
		handler.sendEmptyMessage(MESSAGE_WHAT_SET_MOUSE_GONE);
	}

	void show()
	{
		isHide	=	false;
	}

	public void tempHide()
	{
		handler.sendEmptyMessage(MESSAGE_WHAT_SET_MOUSE_GONE);
	}

	void tempShow()
	{
		if(isHide)
			return;
		handler.sendEmptyMessage(MESSAGE_WHAT_SET_MOUSE_SHOW_WITH_GONE);
	}

	public void setHold(int hold)
	{
		if(this.hold == 0)
		{
			if(hold == 1)
			{
				Touch touch	=	 bfusm.getTouch();
				touch.touch(MotionEvent.ACTION_DOWN,mouseX,mouseY);
				this.hold = 1;
			}
		}
		else if(this.hold == 1)
		{
			if(hold == 2)
			{
				Touch touch	=	 bfusm.getTouch();
				touch.touch(MotionEvent.ACTION_UP,mouseX,mouseY);
				this.hold	=	0;
			}
		}

	}

	public void doMouseClick()
	{
		Touch touch	=	 bfusm.getTouch();
		touch.touch(MotionEvent.ACTION_DOWN,mouseX,mouseY);
		touch.touch(MotionEvent.ACTION_UP,mouseX,mouseY,100);
	}

	public void doMouseClick(int playerID)
	{
		doMouseClick();
		SDKManager.MOUSE_CLICK_PLAYER_ID	=	playerID;
		Message msg	=	new Message();
		msg.what	=	MESSAGE_WHAT_SET_MOUSE_CLICK_PIC;
		msg.arg1	=	playerID;
		handler.sendMessage(msg);
	}

	public void debugForTouchShow(int x,int y)
	{
		if(!debug)
			return;
		mouseX	=	x;
		mouseY	=	y;
		doMouseMove(0,0);
	}

	public void setMouseXY(int playerID,int x,int y)
	{
		Message msg	=	new Message();
		msg.what	=	MESSAGE_WHAT_SET_MOUSE_XY;
		int[] args	=	new int[3];
		args[0]		=	x;
		args[1]		=	y;
		args[2]		=	playerID;
		msg.obj		=	args;
		handler.sendMessage(msg);
	}

	public void doSetMouseXY(int playerID,int x,int y)
	{
		this.playerID	=	playerID;
		mouseX	=	x;
		mouseY	=	y;
		checkMouseBound();
		android.view.WindowManager.LayoutParams fllp	=	(android.view.WindowManager.LayoutParams) mouseIV.getLayoutParams();
		fllp.x	=	mouseX;
		fllp.y	=	mouseY;
		mWindowManager.updateViewLayout(mouseIV, fllp);
		showMouseWithGone();
	}

	private void doMouseDelayGone()
	{
		if(!isAlwaysShow)
			handler.sendEmptyMessageDelayed(MESSAGE_WHAT_SET_MOUSE_GONE, 5000);
	}

	private void showMouse()
	{
		handler.removeMessages(MESSAGE_WHAT_SET_MOUSE_GONE);
		if(mouseIV.getVisibility() == View.GONE)
			mouseIV.setVisibility(View.VISIBLE);
		int tPlayerId	=	(Integer)mouseIV.getTag();

		Log.v(TAG, "showMouse " + "tPlayerId:" + tPlayerId + " playerID:" + playerID);

		if(playerID != tPlayerId)
		{
			mouseIV.setTag(playerID);
			Bitmap[] icon	=	playerID2Icon.get(this.playerID);
			if(icon == null || icon[0] == null)
				mouseIV.setImageBitmap(defaultIcon);
			else
				mouseIV.setImageBitmap(icon[0]);
		}
	}

	private void doSetMouseClickPic(int playerID)
	{
		Bitmap[] icon	=	playerID2Icon.get(this.playerID);
		if(icon != null && icon[1] != null)
		{
			mouseIV.setImageBitmap(icon[1]);
			if(handler.hasMessages(MESSAGE_WHAT_SET_MOUSE_NORMAL_PIC))
			{
				handler.removeMessages(MESSAGE_WHAT_SET_MOUSE_NORMAL_PIC);
			}
			Message msg	=	new Message();
			msg.what	=	MESSAGE_WHAT_SET_MOUSE_NORMAL_PIC;
			msg.arg1	=	playerID;
			handler.sendMessageDelayed(msg, 200);
		}

	}

	private void doSetMouseNormalPic(int playerID)
	{
		Bitmap[] icon	=	playerID2Icon.get(this.playerID);
		if(icon != null && icon[0] != null)
		{
			mouseIV.setImageBitmap(icon[0]);
		}
		else
		{
			mouseIV.setImageBitmap(defaultIcon);
		}
	}

	private void showMouseWithGone()
	{
		showMouse();
		doMouseDelayGone();
	}

}
