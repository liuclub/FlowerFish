package com.bagelplay.controller.wifiset.widget;



import com.bagelplay.controller.com.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SignText extends LinearLayout{
	
	public static final int UP		=	0;
	
	public static final int DOWN	=	1;
	
	public static final int LEFT	=	2;
	
	public static final int RIGHT	=	3;
	
	private ImageView sign;
	
	private TextView tv;
	
	private Bitmap[] signs	=	new Bitmap[4];
	
	private int direct;
	
	public SignText(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		signs[UP]	=	BitmapFactory.decodeResource(context.getResources(), R.drawable.sign_up);
		signs[DOWN]	=	BitmapFactory.decodeResource(context.getResources(), R.drawable.sign_down);
		signs[LEFT]	=	BitmapFactory.decodeResource(context.getResources(), R.drawable.sign_left);
		signs[RIGHT]	=	BitmapFactory.decodeResource(context.getResources(), R.drawable.sign_right);
		
		
		View view	=	View.inflate(context, R.layout.signtext, null);
		addView(view);
		
		
		
		
		
		
		sign	=	(ImageView) view.findViewById(R.id.sign);
		tv		=	(TextView) view.findViewById(R.id.text);
 		
 	}
	
	public void setDirect(int direct)
	{
		this.direct	=	direct;
		sign.setImageBitmap(signs[direct]);
	}
	
	public void setText(String str)
	{
		tv.setText(str);
	}
	
	public int getSTWidth()
	{
		return signs[direct].getWidth();
	}
	
	public int getSTHeight()
	{
		return signs[direct].getHeight();
	}
	
	

}
