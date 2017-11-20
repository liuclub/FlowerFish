package com.bageplay.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
private TextView textView;
private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);

        animation = new TranslateAnimation(-20, 20, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(50);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.startAnimation(animation);
            }
        });
    }
}
