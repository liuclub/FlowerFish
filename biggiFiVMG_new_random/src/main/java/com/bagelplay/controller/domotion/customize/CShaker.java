package com.bagelplay.controller.domotion.customize;

import com.bagelplay.controller.domotion.DoMotionView;
import com.bagelplay.controller.domotion.JsonParser;
import com.bagelplay.controller.domotion.customize.CView;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class CShaker extends CView {

	private SensorManager sensorManager;
	private Vibrator vibrator;
	private long lastShakeTime = System.currentTimeMillis();
	private static final int SENSOR_SHAKE = 10;
	
	private CHideButton chb;
	
	private Handler handler	=	new Handler();
	
	private Runnable clickDownRunable;
	
	private Runnable clickUpRunable;
 
	public CShaker(Context context,RelativeLayout rl) {
		super(context,rl);
		
		// 获取传感器管理服务
				sensorManager = (SensorManager) context
						.getSystemService(Context.SENSOR_SERVICE);
				// 震动
				vibrator = (Vibrator) context
						.getSystemService(Service.VIBRATOR_SERVICE);

				if (sensorManager != null) {// 注册监听器
					sensorManager.registerListener(sensorEventListener,
							sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							SensorManager.SENSOR_DELAY_NORMAL);
					// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
				}
 	}
	
	 
	public void sensorManagerStop() {

		if (sensorManager != null) {// 取消监听器
			sensorManager.unregisterListener(sensorEventListener);
		}
	}
	
	public void setHideButton(CHideButton chb)
	{
		this.chb	=	chb;
	}
 
	/**
	 * 重力感应监听
	 */
	private SensorEventListener sensorEventListener = new SensorEventListener() {

		private boolean allowShake() {
			long time = System.currentTimeMillis();
			long timeD = time - lastShakeTime;
			if (timeD < 2000) {
				return false;
			}

			return true;
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 传感器信息改变时执行该方法
			float[] values = event.values;
			float x = values[0]; // x轴方向的重力加速度，向右为正
			float y = values[1]; // y轴方向的重力加速度，向前为正
			float z = values[2]; // z轴方向的重力加速度，向上为正

			// 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
			int medumValue = 15;
			if (Math.abs(x) > medumValue || Math.abs(y) > medumValue
					|| Math.abs(z) > medumValue) {

				if (allowShake()) {// 判断是否为重复晃动
 					vibrator.vibrate(200);
					lastShakeTime = System.currentTimeMillis();
					OnShacked();
					 
				} else {
					
				}

			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};
	
	private void OnShacked()
	{
		doClick(chb);
	}

	@Override
	public int[] getTVXY() {
		return null;
	}

	@Override
	public void arrange() {
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		return false;
	}

	private void doClick(final CHideButton csb)
	{
		if(clickDownRunable != null && clickUpRunable != null)
		{
			return;
		}
		clickDownRunable	=	new Runnable()
		{
			public void run()
			{
				csb.setSimulateTouchAction(MotionEvent.ACTION_DOWN);
				csb.setSimulateHold(false);
				cv.checkSimulateScreenTouch();
				clickDownRunable	=	null;
			}
		}; 
		
		clickUpRunable	=	new Runnable()
		{
			public void run()
			{
				csb.setSimulateTouchAction(MotionEvent.ACTION_UP);
				csb.setSimulateHold(false);
				cv.checkSimulateScreenTouch();
				clickUpRunable	=	null;
			}
		}; 
		
		handler.post(clickDownRunable);
		
		handler.postDelayed(clickUpRunable, 100);
	}


}
