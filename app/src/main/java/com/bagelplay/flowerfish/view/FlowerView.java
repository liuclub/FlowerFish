package com.bagelplay.flowerfish.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bagelplay.flowerfish.R;
import com.bagelplay.flowerfish.utils.DimenUtil;



/**
 * Created by zhangtianjie on 2017/7/28.
 */

public class FlowerView extends RelativeLayout {
    String Tag = "FlowerView";

    private ImageView iv1, iv2, iv3, iv4, iv5,iv6;


    Bitmap[] mFlowerBitmaps;


    ImageView[] mFlowerImageViews;

    RelativeLayout mFlParent;


    int mFlowerBitmapsSelect[];

    int flowerSize = 6;

    //判断该关是否通过
     boolean[] isPassed;

   public  int CURRENT_SATGE;


    public FlowerView(Context context) {
        super(context);
    }

    public FlowerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.flower_view_layout, this, true);

        mFlowerBitmaps = new Bitmap[flowerSize];
        mFlowerBitmapsSelect = new int[flowerSize];
        mFlowerImageViews = new ImageView[flowerSize];
        isPassed=new boolean[flowerSize];

        mFlParent= (RelativeLayout) findViewById(R.id.fl_parent);

        mFlParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        iv1 = (ImageView) findViewById(R.id.im_flower1);
        iv2 = (ImageView) findViewById(R.id.im_flower2);
        iv3 = (ImageView) findViewById(R.id.im_flower3);
        iv4 = (ImageView) findViewById(R.id.im_flower4);
        iv5 = (ImageView) findViewById(R.id.im_flower5);
        iv6 = (ImageView) findViewById(R.id.im_flower6);

        mFlowerImageViews[0] = iv1;
        mFlowerImageViews[1] = iv2;
        mFlowerImageViews[2] = iv3;
        mFlowerImageViews[3] = iv4;
        mFlowerImageViews[4] = iv5;
        mFlowerImageViews[5] = iv6;

        mFlowerBitmapsSelect[0] = R.drawable.flower1_1;
        mFlowerBitmapsSelect[1] = R.drawable.flower2_1;
        mFlowerBitmapsSelect[2] = R.drawable.flower3_1;
        mFlowerBitmapsSelect[3] = R.drawable.flower4_1;
        mFlowerBitmapsSelect[4] = R.drawable.flower5_1;
        mFlowerBitmapsSelect[5] = R.drawable.flower6_1;

        for (int i = 0; i < mFlowerImageViews.length; i++) {


            Bitmap resizedBitmap = getDecodeBitmap(context, mFlowerBitmapsSelect[i]);


            mFlowerBitmaps[i] = resizedBitmap;


        }


        iv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub


                if (arg1.getAction() == KeyEvent.ACTION_DOWN) {


                    for (int i = 0; i < mFlowerBitmaps.length; i++) {

                        if (mFlowerBitmaps[i].getPixel((int) (arg1.getX()), ((int) arg1.getY())) == 0) {
                            //  Log.i(Tag, "图" + i + "透明区域");


                            continue;

                        } else {

                            // Log.i(Tag, "图" + i + "实体区域");

                           // mFlowerImageViews[i].setImageResource(mFlowerBitmapsSelect[i]);

                            if (mFlowerChoose != null) {


                                CURRENT_SATGE=i;
                                mFlowerChoose.flowerChoose(i,isPassed[i]);

                            }

                            return true;
                        }


                    }

                }
                return true;


            }
        });
    }



    public void setFlowerStagePass(int stage){

        mFlowerImageViews[stage].setImageResource(mFlowerBitmapsSelect[stage]);
        //该关卡通过
        isPassed[stage]=true;
    }


    private Bitmap getDecodeBitmap(Context context, int id) {
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
                id);

        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();


        int w = getResources().getDimensionPixelSize(R.dimen.FlowerImangeViewHeight);

        int h = getResources().getDimensionPixelSize(R.dimen.FlowerImangeViewWidth);

        // Log.d(Tag,w+"~"+h);

        w = DimenUtil.px2Dp(context, w);

        h = DimenUtil.px2Dp(context, h);


        int newWidth = DimenUtil.dip2px(context, w);
        int newHeight = DimenUtil.dip2px(context, h);


        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmapOrg, 0, 0,
                width, height, matrix, true);
    }





    public FlowerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    public FlowerChoose mFlowerChoose;

    public void setonFlowerChooseListener(FlowerChoose flowerChoose) {

        this.mFlowerChoose = flowerChoose;
    }

    public interface FlowerChoose {

        public void flowerChoose(int chooseid,boolean isPassed);


    }


}
