package com.bagelplay.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bagelplay.controller.com.R;
import com.bagelplay.controller.widget.BaseActivity;
import com.bagelplay.controller.widget.BagelPlaySharedPreferences;
import com.bagelplay.controller.wifiset.widget.BFSeekBarFrame;
import com.bagelplay.controller.wifiset.widget.KnobView;
import com.bagelplay.controller.wifiset.widget.SignText;

public class BagelPlaySettingActivity extends BaseActivity {

	private BFSeekBarFrame sensorFrame;
	private SeekBar sensorSB;
	private ImageView sensorText;

	private BFSeekBarFrame volumeFrame;
	private SeekBar volumeSB;
	private ImageView volumeText;

	private BFSeekBarFrame vibrateFrame;
	private SeekBar vibrateSB;
	private ImageView vibrateText;

	private View sign_frameV0, sign_frameV1;

	private RelativeLayout parent;

	private SignText mouse_st;

	private KnobView kv;

	private View closeV, submitV;

	private int sensor_data, volume_data, vibrate_data, mouse_data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		parent = (RelativeLayout) findViewById(R.id.parent);

		View view0 = View.inflate(this, R.layout.signtext0, null);
		parent.addView(view0);
		sign_frameV0 = findViewById(R.id.sign_frame0);

		View view1 = View.inflate(this, R.layout.signtext1, null);
		parent.addView(view1);
		sign_frameV1 = findViewById(R.id.sign_frame1);

		mouse_st = (SignText) findViewById(R.id.mouse_sign);

		kv = (KnobView) findViewById(R.id.mouse_nob);

		kv.setSignText(mouse_st);

		closeV = findViewById(R.id.close);

		closeV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.save_setting),
						Toast.LENGTH_SHORT).show();

				// volume_data
				// + "; 震动" + vibrate_data+"; 鼠标灵敏度"+kv.index);
				finish();
			}
		});

		vibrateFrame = (BFSeekBarFrame) findViewById(R.id.vibrateBar);
		vibrateText = (ImageView) findViewById(R.id.vibrateText);
		vibrateSB = vibrateFrame.getSeekBar();

		vibrate_data = BagelPlaySharedPreferences.Vibrate.getRatio(this);
		vibrateSB.setProgress(vibrate_data);
		vibrateSB.setMax(10);

		vibrateSB.setOnSeekBarChangeListener(new SeekBarChangeListener(0,
				vibrateFrame, sign_frameV0, vibrateText, R.drawable.vibrate1,
				R.drawable.vibrate2));

		volumeFrame = (BFSeekBarFrame) findViewById(R.id.volumeBar);
		volumeText = (ImageView) findViewById(R.id.volumText);
		volumeSB = volumeFrame.getSeekBar();

		volume_data = BagelPlaySharedPreferences.Volume.getRatio(this);

		volumeSB.setProgress(volume_data);
		volumeSB.setMax(10);
		volumeSB.setOnSeekBarChangeListener(new SeekBarChangeListener(1,
				volumeFrame, sign_frameV1, volumeText, R.drawable.volume1,
				R.drawable.volume2));
		
		sensorFrame = (BFSeekBarFrame) findViewById(R.id.sensorBar);

		sensorText = (ImageView) findViewById(R.id.sensortext);
		sensorSB = sensorFrame.getSeekBar();

		sensor_data = BagelPlaySharedPreferences.Sensor.getRatio(this);

		sensorSB.setProgress(sensor_data);
		sensorSB.setMax(10);
		sensorSB.setOnSeekBarChangeListener(new SeekBarChangeListener(2,
				sensorFrame, sign_frameV1, sensorText, R.drawable.sensor1,
				R.drawable.sensor2));

		

		// submitV = findViewById(R.id.submit);
		//
		// submitV.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });

	}

	@Override
	public void orientationChanged(int orientation) {

	}

	class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
		private int orientation;

		private TextView signText;

		private BFSeekBarFrame frame;

		private View sign;

		private ImageView textIV;

		private int res1;

		private int res2;

		public SeekBarChangeListener(int orientation, BFSeekBarFrame frame,
				View sign, ImageView textIV, int res1, int res2) {
			this.orientation = orientation;
			this.frame = frame;
			this.sign = sign;
			this.signText = (TextView) sign.findViewById(R.id.text0);

			this.textIV = textIV;
			this.res1 = res1;
			this.res2 = res2;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			int x = 0;
			int y = 0;

			if (orientation == 0) // 竖
			{

				x = frame.getLeft() + frame.getWidth() + 50;
				y = frame.getTop()
						+ frame.getHeight()
						- seekBar.getPaddingLeft()
						- (int) ((seekBar.getHeight()
								- seekBar.getPaddingLeft()
								- seekBar.getPaddingRight() + 0f)
								* progress / 10) + 2 * progress;
			} else {

				x = frame.getLeft()
						+ seekBar.getPaddingLeft()
						+ (int) ((seekBar.getWidth() - seekBar.getPaddingLeft()
								- seekBar.getPaddingRight() + 0f)
								* progress / seekBar.getMax()) - 2 * progress;
				y = frame.getTop() - 50;
			}
			RelativeLayout.LayoutParams rllp = (LayoutParams) sign
					.getLayoutParams();
			rllp.leftMargin = x;
			rllp.topMargin = y;
			sign.setLayoutParams(rllp);
			sign.setVisibility(View.VISIBLE);
			signText.setText(progress + "");

			switch (orientation) {
			case 0:
				vibrate_data = progress;
				break;
			case 1:
				volume_data = progress;
				break;
			case 2:
				sensor_data = progress;
				
				break;

			default:
				break;
			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

			signText.setText(seekBar.getProgress() + "");
			textIV.setImageResource(res2);
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

			sign.setVisibility(View.GONE);
			textIV.setImageResource(res1);

			switch (orientation) {
			case 0:
				BagelPlaySharedPreferences.Vibrate.setRatio(
						BagelPlaySettingActivity.this, vibrate_data);
			
				break;
			case 1:
				BagelPlaySharedPreferences.Volume.setRatio(
						BagelPlaySettingActivity.this, volume_data);
				break;
			case 2:
				BagelPlaySharedPreferences.Sensor.setRatio(
						BagelPlaySettingActivity.this, sensor_data);
				BagelPlaySharedPreferences.Sensor
						.setRuntime(BagelPlaySettingActivity.this);
			

				break;

			default:
				break;
			}

		}

	}

}
