package com.bagelplay.flowerfish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.bagelplay.flowerfish.R;

/**
 * Created by zhangtianjie on 2017/7/28.
 */

public class NumberGameView extends RelativeLayout {
    public NumberGameView(Context context) {
        super(context);
    }

    public NumberGameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.number_game_layout, this, true);


    }

    public NumberGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NumberGameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
