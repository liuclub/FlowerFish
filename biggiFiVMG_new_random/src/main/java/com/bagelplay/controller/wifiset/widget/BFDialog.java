package com.bagelplay.controller.wifiset.widget;

import com.bagelplay.controller.com.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class BFDialog extends Dialog{

	private TextView titleTV;
	
	protected LinearLayout contentLL;
	
	protected RelativeLayout rl;
	
	protected RelativeLayout titleFrameRL;
	
	private View bottomFrameV;
	
	private Button okBtn;

	private Button cancelBtn;
	
	public BFDialog(Context context) {
		super(context,R.style.Translucent_NoTitle);
 
		setContentView(R.layout.bfdialog);
		
		
		
		titleTV	=	(TextView) findViewById(R.id.title);
		
		contentLL	=	(LinearLayout) findViewById(R.id.content);
		
		bottomFrameV	=	findViewById(R.id.bottomFrame);
		
		okBtn	=	(Button) findViewById(R.id.ok);
		
		cancelBtn	=	(Button) findViewById(R.id.cancel);
		
		titleFrameRL	=	(RelativeLayout) findViewById(R.id.titleFrame);
		
 	}
	
	public void setTit(Object str)
	{
		setText(titleTV,str);
	}
	
	public void setOnOKClickLinstener(View.OnClickListener vocl,Object text)
	{
		setText(okBtn,text);
		okBtn.setOnClickListener(vocl);
		okBtn.setVisibility(View.VISIBLE);
		bottomFrameV.setVisibility(View.VISIBLE);
	}
	
	public void setOnCancelClickLinstener(View.OnClickListener vocl,Object text)
	{
		setText(cancelBtn,text);
		cancelBtn.setOnClickListener(vocl);
		cancelBtn.setVisibility(View.VISIBLE);
		bottomFrameV.setVisibility(View.VISIBLE);
	}
	
	protected void setText(TextView tv,Object str)
	{
		if(str.getClass() == int.class)
		{
			tv.setText((Integer)str);
		}
		else if(str.getClass() == String.class)
		{
			tv.setText((String)str);
		}
	}
	
	
	

}
