package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.ImageUtil;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtianjie on 2017/8/28.
 */

public class EvaHamburger2 extends LinearLayout {
    private Context context;
    private RelativeLayout hamburger_v_container;

    public EvaHamburger2(Context context) {
        this(context, null, 0);
    }

    public EvaHamburger2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EvaHamburger2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.evaluation_hamburger_view_layout2, this, true);
        initUI();
    }

    private void initUI() {
        hamburger_v_container = findViewById(R.id.hamburger_v_container);
    }

    public void addVegetable(int resId) {
        ImageView imageView = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(
                ImageUtil.decodeSampledBitmapFromResource(
                        context.getResources(), resId, 100, 100));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        hamburger_v_container.addView(imageView);
    }
}
