package com.bagelplay.flowerfish;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    String Tag = "MainActivity";

    private ImageView iv1,iv2,iv3,iv4,iv5;




    Bitmap[] mFlowerBitmaps;


    ImageView[] mFlowerImageViews;


    int mFlowerBitmapsSelect[];

    int flowerSize=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlowerBitmaps=new Bitmap[flowerSize];
        mFlowerBitmapsSelect=new int[flowerSize];
        mFlowerImageViews =new ImageView[flowerSize];



        iv1 = (ImageView) findViewById(R.id.im_flower1);
        iv2 = (ImageView) findViewById(R.id.im_flower2);
        iv3 = (ImageView) findViewById(R.id.im_flower3);
        iv4 = (ImageView) findViewById(R.id.im_flower4);
        iv5 = (ImageView) findViewById(R.id.im_flower5);

        mFlowerImageViews[0]=iv1;
        mFlowerImageViews[1]=iv2;
        mFlowerImageViews[2]=iv3;
        mFlowerImageViews[3]=iv4;
        mFlowerImageViews[4]=iv5;

        mFlowerBitmapsSelect[0]= R.drawable.newflower_1_1;
        mFlowerBitmapsSelect[1]=R.drawable.newflower_2_1;
        mFlowerBitmapsSelect[2]=R.drawable.newflower_3_1;
        mFlowerBitmapsSelect[3]=R.drawable.newflower_4_1;
        mFlowerBitmapsSelect[4]=R.drawable.newflower_5_1;

        for(int i=0;i<mFlowerImageViews.length;i++){
            mFlowerBitmaps[i]=((BitmapDrawable) (mFlowerImageViews[i].getDrawable())).getBitmap();



        }







        iv1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                // TODO Auto-generated method stub




                for(int i=0;i<mFlowerBitmaps.length;i++){

                    if(mFlowerBitmaps[i].getPixel((int)(arg1.getX()),((int)arg1.getY()))==0)
                    {
                        Log.i(Tag, "图"+i+"透明区域");
                        //iv1Transparent = true;  //透明区域设置true

                        continue;

                    }

                    else
                    {
                        Log.i(Tag, "图"+i+"实体区域");
                       // iv2Transparent = false;

                        mFlowerImageViews[i].setImageResource(mFlowerBitmapsSelect[i]);

                      //  Log.i(Tag, "success2");

                        return true;
                    }




                }


                return true;




            }
        });












    }




}
