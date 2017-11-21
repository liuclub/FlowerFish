package com.bagelplay.controller.domotion;

import android.content.Context;

import com.bagelplay.controller.utils.BagelPlayUtil;
import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.Log;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MouseListener implements OnTouchListener {

	private int mLastx = -1, mLasty = -1, mStartx, mStarty;
	private int mXoffset, mYoffset;
	private static int ratio;

	private Context context;

	private BagelPlayVmaStub bfVmaStub;

	public MouseListener(Context context) {
		this.context = context;

		bfVmaStub = BagelPlayVmaStub.getInstance();

	}

	public static void setRatio(int ratio) {
		MouseListener.ratio = ratio;
	}

	public boolean isLongPressed(float lastX, float lastY, float thisX,
			float thisY) {
		float offsetX = Math.abs(thisX - lastX);
		float offsetY = Math.abs(thisY - lastY);
		if (offsetX <= 30 && offsetY <= 30) {

			return true;
		}

		return false;
	}

	int x;
	int y;

	int mLastdownx;
	int mLastdowny;
	boolean istouch;

	protected Handler handler = new Handler();

	Runnable r = new Runnable() {

		@Override
		public void run() {
			bfVmaStub.sendMouseHoldData(1);
		}

	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();

		for (int i = 0; i < event.getPointerCount(); i++) {
			if (event.getPointerId(i) == 0) {
				x = (int) event.getX(0);
				y = (int) event.getY(0);

				if (mLastx == -1) {
					mXoffset = mYoffset = 0;
				} else {
					mXoffset = BagelPlayUtil.multiply(x - mLastx, ratio);
					mYoffset = BagelPlayUtil.multiply(y - mLasty, ratio);
				}
				mLastx = x;
				mLasty = y;

				break;
			}
		}

		/*
		 * if(action==261) { bfVmaStub.sendMouseHoldData(1); }
		 * 
		 * else if(action==262|| action == MotionEvent.ACTION_UP &&
		 * event.getPointerId(0) == 1) {
		 * 
		 * bfVmaStub.sendMouseHoldData(2); }
		 */

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // change down to move action when
										// in mouse mode

			Log.v("<--->", "down");

			mLastdownx = x;
			mLastdowny = y;
			istouch = false;

			handler.postDelayed(r, 100);
			bfVmaStub
					.sendMouseData(MotionEvent.ACTION_MOVE, mXoffset, mYoffset);

			break;
		case MotionEvent.ACTION_MOVE: // move

			if (!istouch&&!isLongPressed(mLastdownx, mLastdowny, x, y)) {
				istouch = true;

				handler.removeCallbacks(r);
				Log.v("<--->", "true");

			}
			if (mXoffset != 0 || mYoffset != 0) {
				bfVmaStub.sendMouseData(MotionEvent.ACTION_MOVE, mXoffset,
						mYoffset);
				Log.v("=-------------------------mouse move---------------------=",
						mXoffset + " " + mYoffset);
			}

			break;
		case MotionEvent.ACTION_UP: // up
			bfVmaStub.sendMouseHoldData(2);
			if (event.getPointerId(0) == 0) {

				bfVmaStub.sendMouseData(MotionEvent.ACTION_UP, mXoffset,
						mYoffset);
				mLastx = mLasty = -1;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP: // up
			bfVmaStub.sendMouseHoldData(2);
			bfVmaStub.sendMouseData(MotionEvent.ACTION_UP, mXoffset, mYoffset);
			mLastx = mLasty = -1;

			break;
		}

		return true;

	}

}
