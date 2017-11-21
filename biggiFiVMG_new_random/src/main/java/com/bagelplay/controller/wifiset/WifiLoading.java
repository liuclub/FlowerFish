package com.bagelplay.controller.wifiset;

import com.bagelplay.controller.com.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WifiLoading extends RelativeLayout {

	private static final int MSG_WHAT_LADING = 1;

	private View frameV;

	private View[] picIV;

	private Context context;

	private int index;

	private boolean turn;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_WHAT_LADING) {
				doLoading();
			}
		}
	};

	public WifiLoading(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		reset();
	}

	public void turn() {
		turn = true;
		this.removeAllViews();
		reset();
	}

	public void start() {
		index = 0;
		handler.sendEmptyMessage(MSG_WHAT_LADING);
	}

	public void stop() {
		for (int i = 0; i < picIV.length; i++) {
			picIV[i].setVisibility(View.INVISIBLE);
		}
		handler.removeMessages(MSG_WHAT_LADING);
	}

	private void doLoading() {
		int id = index++ % picIV.length;
		for (int i = 0; i < picIV.length; i++) {
			if (id == i)
				picIV[i].setVisibility(View.VISIBLE);
			else
				picIV[i].setVisibility(View.INVISIBLE);
		}
		handler.sendEmptyMessageDelayed(MSG_WHAT_LADING, 500);
	}

	private Bitmap turnPic(Bitmap pic) {
		Matrix matrix = new Matrix();

		matrix.postRotate(-90);

		Bitmap turnedBitmap = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(),
				pic.getHeight(), matrix, true);

		return turnedBitmap;
	}

	private ImageView getIV(Bitmap pic, boolean turn) {
		ImageView iv = new ImageView(context);

		if (turn)
			iv.setImageBitmap(turnPic(pic));
		else
			iv.setImageBitmap(pic);
		return iv;
	}

	private void reset() {
		Bitmap back = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.w_0);
		Bitmap[] pic = new Bitmap[3];
		pic[0] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.w_1);
		pic[1] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.w_2);
		pic[2] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.w_3);

		ImageView backIV = getIV(back, turn);
		picIV = new ImageView[3];
		picIV[0] = getIV(pic[0], turn);
		picIV[1] = getIV(pic[1], turn);
		picIV[2] = getIV(pic[2], turn);

		addView(backIV);
		addView(picIV[0]);
		addView(picIV[1]);
		addView(picIV[2]);

	}
}
