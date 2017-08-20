package com.bagelplay.gameset.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by zhangtianjie on 2017/8/3.
 */

public class FllScreenVideoView extends VideoView {

    public FllScreenVideoView(Context context) {
        super(context);
    }

    public FllScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FllScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=getDefaultSize(0, widthMeasureSpec);
        int height=getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
