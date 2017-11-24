package com.bagelplay.controller.wifiset.widget;

import com.bagelplay.controller.com.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class Btn extends ImageView {

    private int src_normal;

    private int src_selected;

    public Btn(Context context) {
        super(context);
    }

    public Btn(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.btn);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.btn_src_normal:
                    src_normal = a.getResourceId(R.styleable.btn_src_normal, -1);
                    break;

                case R.styleable.btn_src_selected:
                    src_selected = a.getResourceId(R.styleable.btn_src_selected, -1);
                    break;

            }
        }
        a.recycle();

        initSelector(context);

    }

    public void setSelector(int src_normal, int src_selected) {
        this.src_normal = src_normal;
        this.src_selected = src_selected;
        initSelector(getContext());
    }

    private void initSelector(Context context) {
        StateListDrawable drawable = new StateListDrawable();

        Drawable normal = context.getResources().getDrawable(src_normal);
        Drawable pressed = src_selected == -1 ? null : context.getResources().getDrawable(src_selected);

        drawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        drawable.addState(new int[]{}, normal);

        this.setImageDrawable(drawable);

    }


}
