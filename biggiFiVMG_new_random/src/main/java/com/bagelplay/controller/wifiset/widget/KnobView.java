package com.bagelplay.controller.wifiset.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.controller.BagelPlaySettingActivity;
import com.bagelplay.controller.com.R;
import com.bagelplay.controller.widget.BagelPlaySharedPreferences;


public class KnobView extends LinearLayout {

	private int r1;

	private int r2;

	private int[][] spot2 = new int[10][2];

	private int[][] spot1 = new int[10][2];

	private ImageView[] spot2IV = new ImageView[10];

	private RelativeLayout frameV;

	private Context context;

	private Knob knob;

	public int index;

	private ImageView cPointIV;

	private Bitmap point_red;

	private Bitmap point_blue;

	private Bitmap point_black;

	private Bitmap stick_red;

	private Bitmap stick_blue;

	private Bitmap stick_black;

	private SignText st;

	private int width;

	private int height;

	private int centerX;

	private int centerY;

	private int perDegree;

	private ImageView mouseCenterIV;
	private boolean signtext_canshow;

	public KnobView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		View view = View.inflate(context, R.layout.knob, null);
		addView(view);

		frameV = (RelativeLayout) view.findViewById(R.id.frame);

		mouseCenterIV = (ImageView) view.findViewById(R.id.mouseCenter);

		point_red = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.point_red);

		point_blue = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.point_blue);

		point_black = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.point_black);

		stick_red = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.stick_red);

		stick_blue = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.stick_blue);

		stick_black = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.stick_black);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.knob);
		for (int i = 0; i < a.getIndexCount(); i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.knob_r1:
				r1 = a.getDimensionPixelSize(R.styleable.knob_r1, 0);
				break;

			case R.styleable.knob_r2:
				r2 = a.getDimensionPixelSize(R.styleable.knob_r2, 0);
				break;

			case R.styleable.knob_width:
				this.width = a.getDimensionPixelSize(R.styleable.knob_width, 0);
				break;

			case R.styleable.knob_height:
				this.height = a.getDimensionPixelSize(R.styleable.knob_height,
						0);
				break;
			}
		}

		LinearLayout.LayoutParams rllp = (android.widget.LinearLayout.LayoutParams) frameV
				.getLayoutParams();
		rllp.width = this.width;
		rllp.height = this.height;
		frameV.setLayoutParams(rllp);
		
		
		index= BagelPlaySharedPreferences.Mouse.getRatio(context);

		init();

		changeCPoint();

		knob = new Knob();
		knob.setDegree(360 / spot2.length);
		knob.setR(r2);
		knob.setCenter(this.width / 2, this.height / 2);
		knob.setOnClockwiseListener(new Knob.OnClockwiseListener() {

			@Override
			public void OnClockwise(int clockwise) {
				if (clockwise == Knob.CLOCKWISE) // 顺时针
				{
					if (++index > 9)
						index = 0;
					
					changeCPoint();
					changeSign(spot2[index][0], spot2[index][1]);
				} else {
					if (--index < 0)
						index = 9;
					changeCPoint();
					changeSign(spot2[index][0], spot2[index][1]);
				}
			}
		});

		frameV.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int x = (int) event.getX();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
				
					
				
					knob.setLastXY(x, y);
					if(signtext_canshow)
					st.setVisibility(View.VISIBLE);
					
					mouseCenterIV.setImageResource(R.drawable.mouse_sensor2);
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					
					knob.setXY(x, y);
					
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					
					knob.setXY(x, y);
					st.setVisibility(View.GONE);
					mouseCenterIV.setImageResource(R.drawable.mouse_sensor1);
					
					
					BagelPlaySharedPreferences.Mouse.setRatio(getContext(),index);
					BagelPlaySharedPreferences.Mouse.setRuntime(getContext());
				}

				return true;
			}
		});

	}

	public void setSignText(SignText st) {
		this.st = st;
		st.setVisibility(View.INVISIBLE);
	}

	private void init() {
		centerX = this.width / 2;
		centerY = this.height / 2;

		perDegree = 360 / spot2.length;
		
		
		for (int i = 0; i < spot2.length; i++) {
			int x2 = centerX
					+ (int) (r2 * Math.cos(Math.toRadians(i * perDegree - 90)));
			int y2 = centerY
					+ (int) (r2 * Math.sin(Math.toRadians(i * perDegree - 90)));
			int x1 = centerX
					+ (int) (r1 * Math.cos(Math.toRadians(i * perDegree - 90)));
			int y1 = centerY
					+ (int) (r1 * Math.sin(Math.toRadians(i * perDegree - 90)));
			spot2[i][0] = x2;
			spot2[i][1] = y2;
			spot1[i][0] = x1;
			spot1[i][1] = y1;

			
			
			ImageView iv = new ImageView(context);
			iv.setImageBitmap(point_black);
			RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rllp.leftMargin = x2 - point_black.getWidth() / 2;
			rllp.topMargin = y2 - point_black.getHeight() / 2;
			frameV.addView(iv, rllp);

			spot2IV[i] = iv;

		}

		cPointIV = new ImageView(context);
		cPointIV.setImageBitmap(point_red);
		frameV.addView(cPointIV);

	}

	private void changeCPoint() {
		int x = centerX
				+ (int) (r1 * Math.cos(Math.toRadians(index * perDegree - 90)));
		int y = centerY
				+ (int) (r1 * Math.sin(Math.toRadians(index * perDegree - 90)));
		RelativeLayout.LayoutParams rllp = (android.widget.RelativeLayout.LayoutParams) cPointIV
				.getLayoutParams();
		rllp.leftMargin = x - point_red.getWidth() / 2;
		rllp.topMargin = y - point_red.getHeight() / 2;
		cPointIV.setLayoutParams(rllp);

		for (int i = 0; i < 10; i++) {
			if (i < index) {
				if (i == 0 || i == 5)
					spot2IV[i].setImageBitmap(stick_blue);
				else
					spot2IV[i].setImageBitmap(point_blue);
			} else if (index == i) {
				if (i == 0 || i == 5)
					spot2IV[i].setImageBitmap(stick_red);
				else
					spot2IV[i].setImageBitmap(point_red);
			} else {
				if (i == 0 || i == 5)
					spot2IV[i].setImageBitmap(stick_black);
				else
					spot2IV[i].setImageBitmap(point_black);
			}
		}

	}

	private void changeSign(int x, int y) {
		RelativeLayout.LayoutParams rllp = (android.widget.RelativeLayout.LayoutParams) st
				.getLayoutParams();
		int sx = 0;
		int sy = 0;
		if (index == 0) {
			st.setDirect(SignText.DOWN);
			sx = x - st.getSTWidth() / 2;
			sy = y - stick_red.getHeight() / 2 - st.getSTHeight();
		} else if (index == 5) {
			st.setDirect(SignText.UP);
			sx = x - st.getSTWidth() / 2;
			sy = y + stick_red.getHeight() / 2;
		} else if (index > 0 && index < 5) {
			st.setDirect(SignText.LEFT);
			sx = x + point_red.getWidth() / 2;
			sy = y - st.getSTHeight() / 2;
		} else {
			st.setDirect(SignText.RIGHT);
			sx = x - point_red.getWidth() / 2 - st.getSTWidth();
			sy = y - st.getSTHeight() / 2;
		}

		rllp.leftMargin = getLeft() + sx;
		rllp.topMargin = getTop() + sy;
		st.setLayoutParams(rllp);

		st.setText(index + "");
		
		signtext_canshow=true;

	}
}
