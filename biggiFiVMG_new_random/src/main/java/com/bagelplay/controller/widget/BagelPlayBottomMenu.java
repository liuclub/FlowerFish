package com.bagelplay.controller.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagelplay.controller.com.R;
import com.bagelplay.controller.domotion.SensorListener;


public class BagelPlayBottomMenu extends LinearLayout{
	
	private int[] imageIds;
	
	private String[] imageNames;
	
	private GridView menuGV;
	
	private View frameV;
	
	public static BagelPlayBottomMenu bfm;
	
	public BagelPlayBottomMenu(final Context context, AttributeSet attrs) {
		super(context, attrs);
		
		View view	=	View.inflate(context, R.layout.bottommenu, null);
		addView(view);
		
		menuGV	=	(GridView) findViewById(R.id.menu);
		
		frameV	=	findViewById(R.id.frame);
		
		imageIds = new int[]{
				R.drawable.keypad,
				R.drawable.sensor_on_no_word,
				R.drawable.setting_no_word,
				R.drawable.appcenter,
				R.drawable.back_nofocus,
				R.drawable.menu,
				R.drawable.home,//,
				//R.drawable.keyboand_nt
				R.drawable.exit
				
			};
		
		
		imageNames = new String[]{
				context.getString(R.string.keypad).toString(),
				context.getString(R.string.sensor).toString(),
				context.getString(R.string.setting).toString(),
				context.getString(R.string.appcenter).toString(),
				context.getString(R.string.back).toString(),
				context.getString(R.string.menu).toString(),
				context.getString(R.string.home).toString(),//,
				//getString(R.string.keyboard).toString()
				context.getString(R.string.exit).toString()
				
		};
		
		/*if(Config.level == 2)
		{
			menuGV.setNumColumns(3);
			imageIds = new int[]{
					R.drawable.keypad,
 					R.drawable.back_nofocus,
 					R.drawable.setting_no_word,
 					R.drawable.exit
					
	 		};
			imageNames = new String[]{
					context.getString(R.string.keypad).toString(),
					context.getString(R.string.back).toString(),
					context.getString(R.string.setting).toString(),
					context.getString(R.string.exit).toString()
					
	 		};
		}*/
		
		
		menuGV.setAdapter(new GridViewAdapter(context, imageIds, imageNames));
		
		menuGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				int imgID	=	imageIds[arg2];
				BagelPlayVibrator.getInstance().vibrate(30);
				setVisibility(View.GONE);
				
				switch (imgID) {
				case R.drawable.keypad:
					 
					//BiggiFiActivity.getInstance().changeDoMotionView(DoMotionViewManager.DOMOTION_SCREEN_REMOTECONTROL);
					
					break;
				case R.drawable.sensor_on_no_word:
				case R.drawable.sensor_no_word:
					boolean isOpend	=	SensorListener.getInstance().isOpened();
					if(isOpend)
					{
						SensorListener.getInstance().close();
						view.findViewById(R.id.icon_image).setBackgroundResource(R.drawable.sensor_no_word);
					}
					else
					{
						SensorListener.getInstance().open();
						view.findViewById(R.id.icon_image).setBackgroundResource(R.drawable.sensor_on_no_word);
					}
					setVisibility(View.VISIBLE);
 					break;
				case R.drawable.setting_no_word:

					/*Intent settingIntent = new Intent();
					settingIntent.setClass(context, 
							BiggiFiSettingListViewActivity.class );
					context.startActivity(settingIntent);*/
					
					break;
				case R.drawable.appcenter:


					break;
				case R.drawable.back_nofocus:


					break;
				case R.drawable.menu:


					break;
				case R.drawable.home:


					break;
				case R.drawable.exit:


					break;
				 
				default:
					break;
				}
			}
		});
		
		frameV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
 				setVisibility(View.GONE);
			}
		});
		
		bfm	=	this;
	}
	
	public static BagelPlayBottomMenu getInstance()
	{
		return bfm;
	}
	
	 
 	class GridViewAdapter extends BaseAdapter {
 		private Context mContext;
 		private int[] mImageIds;
 		private String[] mImageNames;
 		private int mPos; 
 		
 		public GridViewAdapter (Context c, int[] imageIds) {
 			mContext = c;
 			mImageIds = imageIds;
 			mPos = 0;
 		}
 		
 		public GridViewAdapter (Context c, int[] imageIds, String[] imageNames) {
 			mContext = c;
 			mImageIds = imageIds;
 			mImageNames = imageNames;
 			mPos = 1;
 		}
 		
 		@Override
 		public int getCount() {
 			return mImageIds.length;
 		}

 		@Override
 		public Object getItem(int position) {
 			return position;
 		}

 		@Override
 		public long getItemId(int position) {
 			return position;
 		}

 		@Override
 		public View getView(int position, View convertView, ViewGroup parent) {
 			if (mPos == 0) {
 				ImageView imageView;  
 				if (convertView == null) {
 					imageView = new ImageView(mContext);
 					imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
 		            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
 				}
 				else {
 					imageView = (ImageView) convertView;
 				}
 				
 				imageView.setBackgroundResource(mImageIds[position]);
 				return imageView;
 			}
 			else {
 				View view;
 				if (convertView == null) {
 					view = LayoutInflater.from(mContext).inflate(R.layout.icon_item, null);
 				}
 				else {
 					view = convertView;
 				}
 				ImageView imageView = (ImageView) view.findViewById(R.id.icon_image);
 				imageView.setBackgroundResource(mImageIds[position]);
 				TextView textView = (TextView) view.findViewById(R.id.icon_text);
 				textView.setText(mImageNames[position]);
 				return view;
 			}
 		}
 	}


}
