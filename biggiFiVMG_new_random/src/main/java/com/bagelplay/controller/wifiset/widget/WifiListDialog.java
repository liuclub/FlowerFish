package com.bagelplay.controller.wifiset.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.bagelplay.controller.com.R;
import com.bagelplay.controller.wifiset.StickInfo;
import com.bagelplay.controller.wifiset.StickList;

public class WifiListDialog extends MessageDialog {

    private ListView lv;

    private Context context;

    private List<StickInfo> sis = new ArrayList<StickInfo>();

    private Item item;

    private OnWDClickListener osdcl;

    private OnReFreshClickListener orfcl;

    public WifiListDialog(Context context) {
        super(context);

        this.context = context;


        initList();
        initRefresh();

        this.setCancelable(false);

    }

    public void setStickInfo(List<StickInfo> sis) {
        this.sis = sis;
        item.notifyDataSetChanged();
    }

    public void setOnWDClickListener(OnWDClickListener osdcl) {
        this.osdcl = osdcl;
    }

    public void setOnReFreshClickListener(OnReFreshClickListener orfcl) {
        this.orfcl = orfcl;
    }

    private void initList() {
        lv = new ListView(context);
        lv.setCacheColorHint(0);

        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 500);
        lllp.topMargin = 20;
        contentLL.addView(lv, lllp);
        item = new Item();
        lv.setAdapter(item);
    }

    private Button initBtn(String text) {
        StateListDrawable drawable = new StateListDrawable();

        Drawable normal = context.getResources().getDrawable(R.drawable.divicebg);
        ColorDrawable pressed = new ColorDrawable(0x0);

        drawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        drawable.addState(new int[]{}, normal);

        Button b = new Button(context);
        b.setText(text);
        b.setBackgroundDrawable(drawable);
        b.setTextColor(0xff4cddff);

        return b;
    }

    private void initRefresh() {
        ImageView iv = new ImageView(context);
        StateListDrawable drawable = new StateListDrawable();

        Drawable normal = context.getResources().getDrawable(R.drawable.refresh1);

        Drawable pressed = context.getResources().getDrawable(R.drawable.refresh2);
        //ColorDrawable pressed = new ColorDrawable(R.drawable.refresh2);

        drawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        drawable.addState(new int[]{}, normal);

        iv.setImageDrawable(drawable);
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rllp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        titleFrameRL.addView(iv, rllp);

        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (orfcl != null)
                    orfcl.OnReFreshClick();
            }
        });
    }


    class Item extends BaseAdapter {

        @Override
        public int getCount() {
            return sis.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final StickInfo si = sis.get(position);

            Button b = initBtn(si.name);

            b.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (osdcl != null)
                        osdcl.OnWDClick(si);
                }
            });

            return b;
        }

    }

    public interface OnWDClickListener {
        public void OnWDClick(StickInfo si);
    }

    public interface OnReFreshClickListener {
        public void OnReFreshClick();
    }

}
