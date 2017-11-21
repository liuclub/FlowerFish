package com.bagelplay.controller.wifiset.widget;

import com.bagelplay.controller.com.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;



public class BFSeekBarFrame extends LinearLayout{
	
	private SeekBar sb;
	
	private int thumbRes;
	
	public BFSeekBarFrame(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.seekbar);
		for(int i=0;i<a.getIndexCount();i++)
		{
			int attr = a.getIndex(i);
			switch(attr)
			{
				case R.styleable.seekbar_orientation:
					String orientation	=	a.getString(R.styleable.seekbar_orientation);
					if("horizontal".equals(orientation))
					{
						View view	=	View.inflate(context, R.layout.seekbar_horizontal, null);
						addView(view);
					}
					else if("vertical".equals(orientation))
					{
						View view	=	View.inflate(context, R.layout.seekbar_vertical, null);
						addView(view);
						
					}
					
				break;
				
				case R.styleable.seekbar_thumb:
					thumbRes	=	a.getResourceId(R.styleable.seekbar_thumb, -1);
				break;
			}
		}
		
		sb	=	(SeekBar)findViewById(R.id.seekbar);
		sb.setThumb(context.getResources().getDrawable(thumbRes));
		
 	}
	
	public SeekBar getSeekBar()
	{
		return sb;
	}
	

}
