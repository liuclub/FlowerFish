package com.bagelplay.sdk.gun;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;

public class ASpeed {

	private static final int MESSAGE_WHAT_MOVE	=	1;
		
	private float radius;
		
	private List<TimeToSpeed> ttsl	=	new ArrayList<TimeToSpeed>();
			
	private Handler handler	=	new Handler()
	{
		@Override
		public void handleMessage(Message msg) {   
            switch (msg.what) {   
            
            	case MESSAGE_WHAT_MOVE:
            		
            		TimeToSpeed tts	=	(TimeToSpeed) msg.obj;
            		radius		=	tts.radius;
            		
            	break;
            	
            	
            }
		}
	};
	
	public ASpeed()
	{
		
	}
	
	public void move()
	{
		for(int i=0;i<ttsl.size();i++)
		{
			TimeToSpeed tts	=	ttsl.get(i);
			Message msg	=	new Message();
			msg.what	=	MESSAGE_WHAT_MOVE;
			msg.obj		=	tts;
			handler.sendMessageDelayed(msg, tts.time);
		}
	}
	
	public void clear()
	{
		handler.removeMessages(MESSAGE_WHAT_MOVE);
		radius	=	0;
	}
	
	public void appendTts(int time,float speed)
	{
		ttsl.add(new TimeToSpeed(time,speed));
	}
	
	public float getRadius()
	{
		return radius;
	}
	
	class TimeToSpeed
	{
		public int time;
		
		public float radius;
				
		public TimeToSpeed(int time,float radius)
		{
			this.time	=	time;
			this.radius	=	radius;
		}
	}
	
}
