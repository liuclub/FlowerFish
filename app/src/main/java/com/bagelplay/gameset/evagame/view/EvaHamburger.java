package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bagelplay.gameset.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtianjie on 2017/8/28.
 */

public class EvaHamburger extends LinearLayout {

    ImageView mIvHamFruit1, mIvHamFruit2, mIvHamFruit3, mIvHamFruit4;

    List<ImageView> fruitsArrayIV;

    public EvaHamburger(Context context) {
        super(context);
    }

    public EvaHamburger(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.evaluation_hamburger_view_layout, this, true);

        initUI();
    }

    private void initUI() {
        fruitsArrayIV = new ArrayList<>();
        mIvHamFruit1 = (ImageView) findViewById(R.id.iv_ham_fruit1);
        mIvHamFruit2 = (ImageView) findViewById(R.id.iv_ham_fruit2);
        mIvHamFruit3 = (ImageView) findViewById(R.id.iv_ham_fruit3);
        mIvHamFruit4 = (ImageView) findViewById(R.id.iv_ham_fruit4);
        fruitsArrayIV.add(mIvHamFruit1);
        fruitsArrayIV.add(mIvHamFruit2);
        fruitsArrayIV.add(mIvHamFruit3);
        fruitsArrayIV.add(mIvHamFruit4);

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

    public void setFruitVisibleByIndex(int index) {
        fruitsArrayIV.get(index).setVisibility(VISIBLE);
    }

    public EvaHamburger(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
