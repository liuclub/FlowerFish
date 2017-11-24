package com.bagelplay.gameset.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.utils.VOUtils;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DeviceActivity extends Activity {
    private TextView fullscreen_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        fullscreen_content = findViewById(R.id.fullscreen_content);

        String content =
                "codeName = " + Build.VERSION.CODENAME + "\r\n" +
                "INCREMENTAL = " + Build.VERSION.INCREMENTAL + "\r\n" +
                "RELEASE = " + Build.VERSION.RELEASE + "\r\n" +
                "SDK = " + Build.VERSION.SDK + "\r\n" +
                "SDK_INT = " + Build.VERSION.SDK_INT + "\r\n" +
                "BOARD = " + Build.BOARD + "\r\n" +
                "BOOTLOADER = " + Build.BOOTLOADER + "\r\n" +
                "BRAND = " + Build.BRAND + "\r\n" +
                "DEVICE = " + Build.DEVICE + "\r\n" +
                "DISPLAY = " + Build.DISPLAY + "\r\n" +
                "FINGERPRINT = " + Build.FINGERPRINT + "\r\n" +
                "getRadioVersion = " + Build.getRadioVersion() + "\r\n" +
                "HARDWARE = " + Build.HARDWARE + "\r\n" +
                "HOST = " + Build.HOST + "\r\n" +
                "id = " + Build.ID + "\r\n" +
                "MANUFACTURER = " + Build.MANUFACTURER + "\r\n" +
                "MODEL = " + Build.MODEL + "\r\n" +
                "PRODUCT = " + Build.PRODUCT + "\r\n" +
                "TAGS = " + Build.TAGS + "\r\n" +
                "TYPE = " + Build.TYPE + "\r\n" +
                "UNKNOWN = " + Build.UNKNOWN + "\r\n" +
                "USER = " + Build.USER + "\r\n" +
                "CPU_ABI = " + Build.CPU_ABI + "\r\n" +
                "CPU_ABI2 = " + Build.CPU_ABI2 + "\r\n" +
                "RADIO = " + Build.RADIO + "\r\n";
        fullscreen_content.setText("" + content);

    }

}
