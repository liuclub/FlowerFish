package com.bagelplay.controller.wifiset.widget;

import com.bagelplay.controller.com.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;



public class Btn3 extends RelativeLayout{
		
	private ImageView bgIV;
	
	private ImageView iconBgIV;
	
	private ImageView iconIV;
	
	private int iconRs;
	
	private int iconTouchedRs;
 	
	public Btn3(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		View view	=	View.inflate(context, R.layout.btn3, null);
		addView(view);
		
		bgIV		=	(ImageView) view.findViewById(R.id.bg);
		
		iconBgIV	=	(ImageView) view.findViewById(R.id.iconbg);
		
		iconIV		=	(ImageView) view.findViewById(R.id.icon);
		
 		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.btn3);
		for(int i=0;i<a.getIndexCount();i++)
		{
			int attr = a.getIndex(i);
			switch(attr)
			{
				case R.styleable.btn3_bg:
					int resId	=	a.getResourceId(R.styleable.btn3_bg, -1);
					if(resId != -1)
						bgIV.setBackgroundResource(resId);
				break;
				
				case R.styleable.btn3_iconbg:
					resId	=	a.getResourceId(R.styleable.btn3_iconbg, -1);
					if(resId != -1)
						iconBgIV.setBackgroundResource(resId);
				break;
				
				case R.styleable.btn3_icon:
					resId	=	a.getResourceId(R.styleable.btn3_icon, -1);
					iconRs	=	resId;
 					if(resId != -1)
 						iconIV.setImageResource(resId);
				break;
				
				case R.styleable.btn3_icontouched:
					resId	=	a.getResourceId(R.styleable.btn3_icontouched, -1);
					iconTouchedRs	=	resId;
 					if(resId != -1)
 						iconIV.setImageResource(resId);
				break;
				
				case R.styleable.btn3_bg_width:
					int pixel	=	a.getDimensionPixelSize(R.styleable.btn3_bg_width, -10000);
					if(pixel != -10000)
					{
						setWidth(bgIV,pixel);
					}
				break;
				
				case R.styleable.btn3_bg_height:
					pixel	=	a.getDimensionPixelSize(R.styleable.btn3_bg_height, -10000);
					if(pixel != -10000)
					{
						setHeight(bgIV,pixel);
					}
				break;
				
				case R.styleable.btn3_iconbg_width:
					pixel	=	a.getDimensionPixelSize(R.styleable.btn3_iconbg_width, -10000);
					if(pixel != -10000)
					{
						setWidth(iconBgIV,pixel);
					}
				break;
				
				case R.styleable.btn3_iconbg_height:
					pixel	=	a.getDimensionPixelSize(R.styleable.btn3_iconbg_height, -10000);
					if(pixel != -10000)
					{
						setHeight(iconBgIV,pixel);
					}
				break;
				
				case R.styleable.btn3_icon_width:
					pixel	=	a.getDimensionPixelSize(R.styleable.btn3_icon_width, -10000);
					if(pixel != -10000)
					{
						setWidth(iconIV,pixel);
					}
				break;
				
				case R.styleable.btn3_icon_height:
					pixel	=	a.getDimensionPixelSize(R.styleable.btn3_icon_height, -10000);
					if(pixel != -10000)
					{
						setHeight(iconIV,pixel);
					}
				break;
			}
		}
		a.recycle();
		
		iconIV.setImageResource(iconRs);

        setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
				Log.v("=------------------------------------------", "onClick:");
			}
		});
		
  	}
	
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			iconIV.setImageResource(iconTouchedRs);
			iconBgIV.setVisibility(View.INVISIBLE);
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			iconIV.setImageResource(iconRs);
			iconBgIV.setVisibility(View.VISIBLE);
		}
 		return super.dispatchTouchEvent(event);
	}
	
	private void setWidth(View view,int width)
	{
		LayoutParams lp	=	(LayoutParams) view.getLayoutParams();
		lp.width	=	width;
		view.setLayoutParams(lp);
	}
	
	private void setHeight(View view,int height)
	{
		LayoutParams lp	=	(LayoutParams) view.getLayoutParams();
		lp.height	=	height;
		view.setLayoutParams(lp);
	}
	
	public interface OnBtn3ClickListener
	{
		public void OnClick();
	}


}
