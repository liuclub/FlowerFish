package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;

/**
 * Created by zhangtianjie on 2017/8/26.
 */

public class EvaluationGameView extends RelativeLayout {

    Context mContext;

    public EvaluationGameView(Context context) {
        super(context);
    }

    public EvaluationGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.evaluation_game_view_layout, this, true);
        initUI();

    }

    private void initUI() {

    }

    public EvaluationGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
