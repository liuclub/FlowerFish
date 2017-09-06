package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bagelplay.gameset.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtianjie on 2017/9/6.
 */

public class EvaPlate extends RelativeLayout {
    ImageView mIvPlateFruit1, mIvPlateFruit2, mIvPlateFruit3, mIvPlateFruit4;

    List<ImageView> fruitsArrayIV;

    public EvaPlate(Context context) {
        super(context);
    }

    public EvaPlate(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.evaluation_plate_view_layout, this, true);



        initUI();
    }


    private void initUI() {
        fruitsArrayIV = new ArrayList<>();
        mIvPlateFruit1 = (ImageView) findViewById(R.id.iv_plate_fruit1);
        mIvPlateFruit2 = (ImageView) findViewById(R.id.iv_plate_fruit2);
        mIvPlateFruit3 = (ImageView) findViewById(R.id.iv_plate_fruit3);
        mIvPlateFruit4 = (ImageView) findViewById(R.id.iv_plate_fruit4);
        fruitsArrayIV.add(mIvPlateFruit1);
        fruitsArrayIV.add(mIvPlateFruit2);
        fruitsArrayIV.add(mIvPlateFruit3);
        fruitsArrayIV.add(mIvPlateFruit4);


        initFruit();


    }

    private void initFruit(){
        if(fruitsArrayIV!=null){
            for (int i = 0; i < fruitsArrayIV.size(); i++) {
                fruitsArrayIV.get(i).setVisibility(INVISIBLE);
            }
        }
    }

    public void setFruitInvisibleByIndex(int index) {
        fruitsArrayIV.get(index).setVisibility(INVISIBLE);
    }

    public void setFruitVisibleByIndex(int index,int img) {

        if(index>3)
            return;
        fruitsArrayIV.get(index).setVisibility(VISIBLE);
        fruitsArrayIV.get(index).setImageResource(img);
    }



    public EvaPlate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
}
