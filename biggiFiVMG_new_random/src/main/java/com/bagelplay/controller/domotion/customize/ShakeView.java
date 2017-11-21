package com.bagelplay.controller.domotion.customize;

import com.bagelplay.controller.domotion.DoMotionView;
import com.bagelplay.controller.domotion.JsonParser;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;

public class ShakeView extends DoMotionView implements SensorEventListener {

	// 定义sensor管理器
	private SensorManager mSensorManager;
	// 震动
	private Vibrator vibrator;

	public ShakeView(Context context, JsonParser jp) {
		super(context, jp);
		// TODO Auto-generated constructor stub

		// 获取传感器管理服务
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		// 震动
		vibrator = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				// 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
				// 根据不同应用，需要的反应速率不同，具体根据实际情况设定
				SensorManager.SENSOR_DELAY_NORMAL);

	}

	private void Stop() {
		mSensorManager.unregisterListener(this);

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		int sensorType = event.sensor.getType();

		// values[0]:X轴，values[1]：Y轴，values[2]：Z轴
		float[] values = event.values;

		float x = values[0];
		float y = values[1];
		float z = values[2];

		if (sensorType == Sensor.TYPE_ACCELEROMETER) {

			/*
			 * 因为一般正常情况下，任意轴数值最大就在9.8~10之间，只有在你突然摇动手机的时候，瞬时加速度才会突然增大或减少。
			 * 所以，经过实际测试，只需监听任一轴的加速度大于14的时候，改变你需要的设置就OK了~~~
			 */
			if ((Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math
					.abs(values[2]) > 14)) {

				// 摇动手机后，设置button上显示的字为空
				// clear.setText(null);

				// 摇动手机后，再伴随震动提示~~
				vibrator.vibrate(500);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public Bitmap getDefaultBackground() {
		return null;
	}

}
