package com.bagelplay.controller.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.os.Handler;

import com.bagelplay.controller.BagelPlayActivity;
import com.bagelplay.controller.BagelPlayHeartService;
import com.bagelplay.controller.domotion.DoMotionViewManager;
import com.bagelplay.controller.payment.Payment;
import com.bagelplay.controller.voice.VoiceRecorder;
import com.bagelplay.controller.wifiset.StickList;
import com.umeng.analytics.AnalyticsConfig;

public class BagelPlayManager {

    private static BagelPlayManager bvm;

    private Context context;

    private SocketReader socketReader;

    private BagelPlayVmaStub bvs;

    private InputStream is;

    private Handler handler = new Handler();

    public boolean Loginsuccess;

    private String lastZipName;

    private static String TAG = "BagelPlayManager";

    private BagelPlayManager(Context context) {
        this.context = context;
        socketReader = new SocketReader(ByteOrder.LITTLE_ENDIAN);
    }

    public static void init(Context context) {
        if (bvm == null)
            bvm = new BagelPlayManager(context);
    }

    public static BagelPlayManager getInstance() {
        return bvm;
    }

    public void start() {
        bvs = BagelPlayVmaStub.getInstance();
        is = bvs.getDataInputStream();
        BMThread b = new BMThread();
        b.start();
    }

    private void handle() {
        while (true) {
            try {

                int len = socketReader.readInt(is);
                int cmd = socketReader.readInt(is);

                Log.v("=----------------------------------------------=", len
                        + " " + cmd);
                if (len <= 0) {
                    bvs.close();
                    return;
                }

                if (cmd == CmdProtocol.ServerToClient.LOGIN) {

                    login();

                } else if (cmd == CmdProtocol.ServerToClient.APPINFO) {

                    appInfo();
                } else if (cmd == CmdProtocol.ServerToClient.APPINFO_ZIPNAME) {

                    appInfoZipName();
                } else if (cmd == CmdProtocol.ServerToClient.SHOWINPUT) {
                    showInput();
                } else if (cmd == CmdProtocol.ServerToClient.HEART) {


                    BagelPlayHeartService.getInstance().resetLastHeart();


                } else if (cmd == CmdProtocol.ServerToClient.LOGOFF) {
                    BagelPlayHeartService.stop(context);
                    android.os.Process.killProcess(android.os.Process.myPid());
                } else if (cmd == CmdProtocol.ServerToClient.PAYMENT_REQUEST) {
                    paymentRequest();

                } else if (cmd == CmdProtocol.ServerToClient.EXTRAL) {

                    getExtra();
                } else if (cmd == CmdProtocol.ServerToClient.VOICE_START) {


                    voiceStart();
                } else if (cmd == CmdProtocol.ServerToClient.VOICE_STOP) {
                    voiceStop();
                } else if (cmd == CmdProtocol.ServerToClient.VOICE_CHECK) {
                    voiceCheck();
                } else {
                    is.skip(len - 4);
                }

            } catch (Exception e) {

                e.printStackTrace();
                bvs.close();
                return;
            }

        }
    }

    private void getExtra() throws Exception {
        String extra = socketReader.readStr(is);

        JSONObject jo = new JSONObject(extra);

        if (jo != null) {
            Config.tvgamechannel = jo.getString("qudao");
        }

        // AnalyticsConfig.setAppkey(context, "566aa23ee0f55a319a0018d3");
        AnalyticsConfig.setChannel(Config.tvgamechannel);

    }

    private void login() throws Exception {
        int result = socketReader.readInt(is);
        if (result == 0) {
            bvs.sendSensorSwitchData(1);
            Config.tvWidthPixels = socketReader.readInt(is);
            Config.tvHeightPixels = socketReader.readInt(is);

        } else {

            StickList sl = StickList.getInstance();
            if (sl != null)
                sl.finishConnect(false, result);
            bvs.close();

        }
    }

    private void appInfo2() throws Exception {
        SocketReader socketReader = new SocketReader(ByteOrder.BIG_ENDIAN);
        final int doMotionView = socketReader.readInt(is);
        String tPayment = "";
        byte[] tBgPicData = new byte[0];
        if (doMotionView != 0) {
            tBgPicData = socketReader.readBytes(is);
            tPayment = socketReader.readStr(is);
        }

        final byte[] bgPicData = tBgPicData;

        handler.post(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(context, BagelPlayActivity.class);
                BagelPlayActivity.bgPicData = bgPicData;
                // intent.putExtra("bgPicData", bgPicData);
                intent.putExtra("doMotionView", doMotionView);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                StickList sl = StickList.getInstance();
                if (sl != null)
                    sl.finishConnect(true, 0);
            }
        });
    }

    private void appInfo() throws Exception {

        SocketReader socketReader = new SocketReader(ByteOrder.BIG_ENDIAN);

        String zipName = socketReader.readStr(is);

        File file0 = new File(DoMotionViewManager.ZIP_DIR);
        File[] files = file0.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }


        File file = new File(DoMotionViewManager.ZIP_DIR + zipName);


        //File file = new File(DoMotionViewManager.ZIP_DIR + zipName);
        file.delete();

        byte[] buf = socketReader.readBytes(is);


        FileOutputStream fos = new FileOutputStream(file);
        fos.write(buf);
        fos.close();

        Log.v("=--------------------appInfo-----------------------", buf.length
                + " ");

        Intent intent = new Intent(context, BagelPlayActivity.class);
        intent.putExtra("zipName", zipName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        StickList sl = StickList.getInstance();
        if (sl != null) {
            Loginsuccess = true;
            sl.finishConnect(true, 0);
        }

        if (zipName.equals(lastZipName)) {
            lastZipName = null;
        }

    }

    private void appInfoZipName() throws Exception {
        SocketReader socketReader = new SocketReader(ByteOrder.BIG_ENDIAN);
        String zipName = socketReader.readStr(is);
        File file = new File(DoMotionViewManager.ZIP_DIR + zipName);
        if (file.exists()) {
            Intent intent = new Intent(context, BagelPlayActivity.class);
            intent.putExtra("zipName", zipName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            if (!zipName.equals(lastZipName)) {
                bvs.sendAppInfoData();
                zipName = lastZipName;
            }
        }
    }

    private void showInput() {
        handler.post(new Runnable() {
            public void run() {
                BagelPlayActivity.getInstance().showKeyboard();
            }
        });
    }

    private void paymentRequest() throws Exception {
        SocketReader socketReader = new SocketReader(ByteOrder.BIG_ENDIAN);
        String version = socketReader.readStr(is);
        String platform = socketReader.readStr(is);
        String token = socketReader.readStr(is);
        String packageName = socketReader.readStr(is);
        String macAddr = socketReader.readStr(is);
        String brand = socketReader.readStr(is);
        String paramJson = socketReader.readStr(is);

        Log.v("=--------------------paymentRequest-----------------------",
                version + " ");
        Log.v("=--------------------paymentRequest-----------------------",
                platform + " ");
        Log.v("=--------------------paymentRequest-----------------------",
                token + " ");
        Log.v("=--------------------paymentRequest-----------------------",
                packageName + " ");
        Log.v("=--------------------paymentRequest-----------------------",
                macAddr + " ");
        Log.v("=--------------------paymentRequest-----------------------",
                brand + " ");
        Log.v("=--------------------paymentRequest-----------------------",
                paramJson + " ");

        Payment payment = new Payment(context);
        payment.pay(version, platform, token, packageName, macAddr, "", brand,
                paramJson);

    }

    private void heart() {

    }


    // frequency音频采样率:16000  channelConfiguration录制的声道  单声道:2 audioEncoding  ENCODING_PCM_16BIT 16位每个样本:2

    private void voiceStart() throws Exception {
        SocketReader socketReader = new SocketReader(ByteOrder.BIG_ENDIAN);
        int frequency = socketReader.readInt(is);
        int channelConfiguration = socketReader.readInt(is);
        int audioEncoding = socketReader.readInt(is);


        VoiceRecorder.getInstance().start(frequency, channelConfiguration,
                audioEncoding);

    }

    private void voiceStop() {
        VoiceRecorder.getInstance().stop();
    }

    private void voiceCheck() throws Exception {
        SocketReader socketReader = new SocketReader(ByteOrder.BIG_ENDIAN);
        int id = socketReader.readInt(is);
        String right = socketReader.readStr(is);

        // 需要保存id作为本次通话的标识，这个要和对比的结果一起发给sdk端 right为sdk端传来的正确的文字
        // 调用讯飞的sdk进行判断
        // 再调用BagelPlayVmaStub类的sendVoiceCheckResult(int id,String right,String
        // result)方法将结果返回给sdk端

        bvs.sendVoiceCheckResult(id, right, "result something ");

    }

    class BMThread extends Thread {
        @Override
        public void run() {
            handle();
        }
    }

}
