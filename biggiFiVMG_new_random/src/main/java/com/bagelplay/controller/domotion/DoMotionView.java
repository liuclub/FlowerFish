package com.bagelplay.controller.domotion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bagelplay.controller.utils.Log;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.RelativeLayout;

public abstract class DoMotionView extends RelativeLayout{

	protected JsonParser jp;
	
	private Bitmap bg;
	
	public DoMotionView(Context context,JsonParser jp) {
		super(context);
		this.jp		=	jp;
				
		((Activity)context).setRequestedOrientation(jp.getOrientation());
	//	((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		//((Activity)context).setRequestedOrientation(0);
		
		if(Log.isDiyChooseModel){
			
			  File f = new File("/sdcard/" + "diychoosemodelbg.png");  
		        if (f.exists()) { /* 产生Bitmap对象，并放入mImageView中 */  
		            Bitmap bg = BitmapFactory.decodeFile("/sdcard/" +"diychoosemodelbg.png");  
		            this.setBackgroundDrawable(new BitmapDrawable(bg));
		        } else {  
		        	 
		        }  
			
			return;
		}
		if(jp.getBackground() != null)
		{
			
			bg	=	jp.getBackground();
			
		
			this.setBackgroundDrawable(new BitmapDrawable(bg));
			
			try {
				saveMyBitmap("diychoosemodelbg",bg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			bg	=	getDefaultBackground();
			this.setBackgroundDrawable(new BitmapDrawable(bg));
		}
		
		
			
	}
	
	public void saveMyBitmap(String bitName, Bitmap mBitmap) throws IOException {  
        File f = new File("/sdcard/" + bitName + ".png");  
        f.createNewFile();  
        FileOutputStream fOut = null;  
        try {  
                fOut = new FileOutputStream(f);  
        } catch (FileNotFoundException e) {  
                e.printStackTrace();  
        }  
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  
        try {  
                fOut.flush();  
        } catch (IOException e) {  
                e.printStackTrace();  
        }  
        try {  
                fOut.close();  
        } catch (IOException e) {  
                e.printStackTrace();  
        }  
}  
	
	public Bitmap getBg()
	{
		return bg;
	}
	
	public abstract Bitmap getDefaultBackground();

 
}
