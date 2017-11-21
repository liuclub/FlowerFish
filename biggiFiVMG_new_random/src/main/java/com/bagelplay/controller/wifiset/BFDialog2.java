package com.bagelplay.controller.wifiset;



import com.bagelplay.controller.com.R;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class BFDialog2 extends LinearLayout{
	
	private TextView titleTV;
	
	private TextView messageTV;
	
	private TextView positiveButtonTV;
	
	private TextView negativeButton;
	
	private WifiSetAct wsa;
	
	public BFDialog2(WifiSetAct wsa) {
		super(wsa);
		
		this.wsa	=	wsa;

		View view	=	View.inflate(wsa, R.layout.bfdialog, null);
		addView(view);
 		
		titleTV			=	(TextView) findViewById(R.id.title);
		messageTV		=	(TextView) findViewById(R.id.message);
		//positiveButtonTV=	(TextView) findViewById(R.id.positivebutton);
		//negativeButton	=	(TextView) findViewById(R.id.negativebutton);
 	}
	
	public void show()
	{
		wsa.showDialog(this);
	}
	
	
	public void setTitle(String title)
	{
		titleTV.setVisibility(View.VISIBLE);
		titleTV.setText(title);
	}
	
	public void setMessage(String message)
	{
		messageTV.setVisibility(View.VISIBLE);
		messageTV.setText(message);
	}
	
	public void setPositiveButton(int text,final View.OnClickListener btnLisn)
	{
		positiveButtonTV.setVisibility(View.VISIBLE);
		positiveButtonTV.setText(text);
		View.OnClickListener oc	=	new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if(btnLisn != null)
					btnLisn.onClick(v);
				wsa.removeDialog();
			}
		};
		positiveButtonTV.setOnClickListener(oc);
	}
	
	public void setNegativeButton(int text,final View.OnClickListener btnLisn)
	{
		negativeButton.setVisibility(View.VISIBLE);
		negativeButton.setText(text);
		negativeButton.setOnClickListener(btnLisn);
		View.OnClickListener oc	=	new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if(btnLisn != null)
					btnLisn.onClick(v);
				wsa.removeDialog();
			}
		};
		negativeButton.setOnClickListener(oc);
	}
	
	 
	
	

}
