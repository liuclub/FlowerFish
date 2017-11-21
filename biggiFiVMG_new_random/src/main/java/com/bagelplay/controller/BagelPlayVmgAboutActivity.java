package com.bagelplay.controller;


import com.bagelplay.controller.com.R;
import com.bagelplay.controller.widget.BaseActivity;

import android.os.Bundle;

public class BagelPlayVmgAboutActivity extends BaseActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.about);
	}

	@Override
	public void orientationChanged(int orientation) {

	}

}
