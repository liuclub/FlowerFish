package com.bagelplay.sdk.common;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.bagelplay.prj.lib.controller.Controller;
import com.bagelplay.prj.lib.controller.ControllerService;
import com.bagelplay.prj.lib.data.BFDevice;
import com.bagelplay.prj.lib.listener.OnKeyListener;
import com.bagelplay.prj.lib.listener.OnStateListener;
import com.bagelplay.prj.lib.listener.OnStickListener;
import com.bagelplay.sdk.common.ghandle.GHandle;
import com.bagelplay.sdk.common.player.PlayerManager;
import com.bagelplay.sdk.common.remoteControl.RControl;
import com.bagelplay.sdk.common.remoteControl.RControl2;
import com.bagelplay.sdk.common.util.FileDir;
import com.bagelplay.sdk.common.util.Log;
import com.bagelplay.sdk.common.util.Utils;
import com.bagelplay.sdk.gun.Gun;
import com.bagelplay.sdk.gun.GunEvent;
import com.bagelplay.sdk.payment.OnPaymentListener;
import com.bagelplay.sdk.payment.Payment;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;


public class SDKManager implements OnKeyListener, OnStickListener, OnStateListener {

    private String TAG = this.getClass().getSimpleName();

    private static SDKManager sdkManager;

    private int screenWidth;

    private int screenHeight;

    private List<Window.Callback> wcwr = new ArrayList<Window.Callback>();

    private Context applicationContext;

    private Mouse mouse;

    private Touch touch;

    private KeyMotion keyMotion;

    private GHandle gHandle;

    private RControl rControl;

    private RControl2 rControl2;

    private Sensor sensor;

    private CServer bfServer;

    private int[] currentWindowLocationXY = new int[2];

    private InputEditText iet;

    private String controlZip = "default.zip";

    private Payment payment;

    private Voice voice;

    private Base base;

    private SocketStub socketStub;

    private SocketManager socketManager;

    private SocketThirdManager socketThirdManager;

    private SocketThirdStub socketThirdStub;

    private List<OnShowPlayerLinstener> ospls = new ArrayList<OnShowPlayerLinstener>();

    private Handler handler = new Handler();

    private ControllerService mControllerService;

    private Count count;

    private static boolean show_gameHandler_remoteControl = false;

    private static boolean gameHandler_remoteControl = false;

    private static boolean gun_control = false;

    private static boolean openDebug = false;

    private static boolean youmeng = false;

    private Gun gun;

    private boolean normalLaunch = true;

    public static int MOUSE_CLICK_PLAYER_ID;

    public static int MOUSE_DOWN_PLAYER_ID;

    public static int MOUSE_UP_PLAYER_ID;

    protected static WeakReference<Activity> lastActivity;

    private OnSDKKeyListener onSDKKeyListener;

    public static SDKManager getInstance(Activity activity) {
        lastActivity = new WeakReference<Activity>(activity);
        if (sdkManager == null) {
            sdkManager = new SDKManager(activity);
        }
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return sdkManager;
    }

    protected SDKManager(final Activity activity) {
        new CloseBroadcast(activity);
        if (check(activity)) {
            normalLaunch = false;
            return;
        }
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getResolution(activity);
        this.applicationContext = activity.getApplicationContext();
        FileDir.init(applicationContext);
        parseConfigJson();
        if (gameHandler_remoteControl)
            initControllerService(activity);
        socketStub = new SocketStub(this);
        socketManager = new SocketManager(this);
        socketThirdManager = new SocketThirdManager(this);
        socketThirdStub = new SocketThirdStub(this);
        mouse = new Mouse(this);
        parseConfigJsonMouse();
        touch = new Touch(this);
        keyMotion = new KeyMotion(this);
        gHandle = new GHandle(this);
        rControl = new RControl(this);
        rControl2 = new RControl2(this);
        sensor = new Sensor(this);
        iet = new InputEditText(this);
        bfServer = new CServer(this);
        voice = new Voice(this);
        bfServer.startServer();
        base = new Base(this);
        if (base.connectToServer()) {
            socketManager.start();
            socketStub.sendLoginData(applicationContext.getPackageName(), socketThirdManager.getPort(), getID());
        }
        socketThirdManager.start();
        if (gun_control) {
            gun = new Gun(applicationContext, this);
            gun.start(new GunEvent(), gun);
        }
        count = new Count(applicationContext);
        count.start();

        allChangeMouseMode();

        if (openDebug)
            mouse.openDebug();

        if (youmeng)
            initMobclickAgent();

        PlayerManager.init(this);
    }

    public void addWindowCallBack(Window.Callback wc) {
        if (!normalLaunch)
            return;
        for (int i = 0; i < wcwr.size(); i++) {
            if (wc == wcwr.get(i)) {
                wcwr.remove(i);
                iet.clear();
            }
        }
        wcwr.add(wc);

        Log.v("SDKManager", "addWindowCallBack  " + wc + "");

    }

    public void removeWindowCallBack(Window.Callback wc) {
        if (!normalLaunch)
            return;
        for (int i = 0; i < wcwr.size(); i++) {
            if (wc == wcwr.get(i)) {
                wcwr.remove(i);
                iet.clear();
                Log.v("SDKManager", "removeWindowCallBack  " + wc + "");
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!gameHandler_remoteControl)
            return false;
        if (ll != null)
            ll.onLogListener("dispatchKeyEvent" + " event:" + event.getAction() + " " + event.getKeyCode());
        return mControllerService.dispatchKeyEvent(event);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        if (!gameHandler_remoteControl)
            return false;
        return mControllerService.dispatchGenericMotionEvent(ev);
    }

    public void destroy() {
        if (!normalLaunch)
            return;
        sdkManager = null;
        base.stop();
        bfServer.stopServer();
        gHandle.destroy();
        socketStub.close();
        socketManager.close();
        socketThirdManager.close();
        socketThirdStub.close();
        if (wcwr != null)
            wcwr.clear();
        count.close();
        if (gameHandler_remoteControl)
            mControllerService.unregister();
        if (gun_control)
            gun.stop();
        PlayerManager.getInstance().destroy();
        Log.v(TAG, "destroy");
    }

    public void onStart() {

    }

    public void onResume() {
        Log.i(TAG, "onResume");
        if (gun_control)
            gun.resume();
        if (youmeng)
            setMobclickAgentActivityResume();
    }

    public void onStop() {
        if (!normalLaunch)
            return;
        mouse.tempHide();
    }

    public void onPause() {
        if (!normalLaunch)
            return;
        if (gun_control)
            gun.pause();
        if (youmeng)
            setMobclickAgentActivityPause();
    }

    public void showKeyboardForEditText(EditText et) {
        iet.showKeyboardForEditText(et);
    }

    public void showKeyboardForEditTextAndKeepKbdAfterFill(EditText et) {
        iet.showKeyboardForEditTextAndKeepKbdAfterFill(et);
    }

    public void setOnShowPlayerLinstener(OnShowPlayerLinstener ospl) {
        if (!ospls.contains(ospl)) {
            ospls.add(ospl);
        }
    }

    public void removeOnShowPlayerLinstener(OnShowPlayerLinstener ospl) {
        if (!normalLaunch)
            return;
        ospls.remove(ospl);
    }

    public static void gameHandler_remoteControl(boolean gameHandler_remoteControl) {
        SDKManager.gameHandler_remoteControl = gameHandler_remoteControl;
    }

    public static void gun_control(boolean gun_control) {
        SDKManager.gun_control = gun_control;
    }

    public boolean isNormalLaunched() {
        return normalLaunch;
    }

    void handleWantPlayerIDsDataFromClient(final int[] ids, final int num) {
        if (ospls.size() > 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < ospls.size(); i++) {
                        ospls.get(i).OnGetPlayerIDs(ids, num);
                    }
                }
            });
        }
    }

    public void wantPlayerIDs() {
        socketStub.sendWantPlayerIdData();
    }

    public void setControlZip(String controlZip) {
        this.controlZip = controlZip;
        socketStub.sendAppInfoDataZipName(controlZip);
        if ("mouse.zip".equals(controlZip)) {
            allChangeMouseMode();
        } else if ("default.zip".equals(controlZip)) {
            allChangeMouseMode();
        } else if ("touch.zip".equals(controlZip)) {
            allChangeTouchMode();
        }
    }

    public void pay(String platform, String paramJson, OnPaymentListener opl) {
        payment = new Payment(this);
        payment.start(platform, opl, paramJson);
    }

    String getControlZip() {
        return controlZip;
    }

    public Window.Callback getWindowCallback() {
        int size = wcwr.size();
        if (size > 0)
            return wcwr.get(size - 1);

        return null;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public Touch getTouch() {
        return touch;
    }

    KeyMotion getKeyMotion() {
        return keyMotion;
    }

    public GHandle getGHandle() {
        return gHandle;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    InputEditText getInputEditText() {
        return iet;
    }

    int[] getCurrentWindowLocationXY() {
        resetWindowScreen(getWindowCallback());
        return currentWindowLocationXY;
    }

    Payment getPayment() {
        return payment;
    }

    Voice getVoice() {
        return voice;
    }

    public SocketStub getSocketStub() {
        return socketStub;
    }

    public SocketManager getSocketManager() {
        return socketManager;
    }

    public SocketThirdManager getSocketThirdManager() {
        return socketThirdManager;
    }

    public SocketThirdStub getSocketThirdStub() {
        return socketThirdStub;
    }

    public void setRControl(RControl rc) {
        rControl = rc;
    }

    public RControl getRControl() {
        return rControl;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public RControl2 getRControl2() {
        return rControl2;
    }

    public void setOnSensorListener(OnSensorListener osl) {
        sensor.setOnSensorListener(osl);
    }

    public void startVoice(int frequency, int channelConfiguration, int audioEncoding, OnVoiceListener ovl) {
        voice.setOnVoiceListener(ovl);
        socketStub.sendVoiceStart(frequency, channelConfiguration, audioEncoding);
    }

    public void stopVoice() {
        voice.setOnVoiceListener(null);
        socketStub.sendVoiceStop();
    }

    public void startVoice(int playerID, int frequency, int channelConfiguration, int audioEncoding, OnVoiceListener ovl) {
        voice.setOnVoiceListener(ovl);
        socketStub.sendVoiceStartByPlayerID(playerID, frequency, channelConfiguration, audioEncoding);
    }

    public void stopVoice(int playerID) {
        voice.setOnVoiceListener(null);
        socketStub.sendVoiceStopByPlayerID(playerID);
    }

    //第一个参数right为正确的文字
    //第二个参数OnVoiceResultLinstener为手机调用讯飞处理过后的给sdk端的回调
    public void checkVoice(String right, OnVoiceResultLinstener ovrl) {
        voice.checkVoice(right, ovrl);
    }

    Base getBase() {
        return base;
    }


    private void resetWindowScreen(Window.Callback wc) {
        if (wc == null)
            return;
        if (wc instanceof Activity) {
            Activity a = (Activity) wc;
            a.getWindow().getDecorView().getLocationOnScreen(currentWindowLocationXY);
        } else if (wc instanceof Dialog) {
            Dialog d = (Dialog) wc;
            d.getWindow().getDecorView().getLocationOnScreen(currentWindowLocationXY);
        }

    }

    private void getResolution(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    private String getID() {
        String str = "";
        try {
            InputStream is = applicationContext.getAssets().open(FileDir.getInstance().ID);
            str = Utils.inStreamToStr(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getExtral() {
        String str = "";
        try {
            InputStream is = applicationContext.getAssets().open(FileDir.getInstance().EXTRAL);
            str = Utils.inStreamToStr(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        str = str.replace("\r", "");
        str = str.replace("\n", "");
        str = str.replace(" ", "");
        str = str.replace("\t", "");


        return str;
    }

    private void initControllerService(Activity activity) {

        mControllerService = Controller.getInstance(applicationContext);

        // 注册手柄服务
        mControllerService.register();

        // 设置手柄按键监听器
        mControllerService.setKeyListener(this);
        mControllerService.setStickListener(this);

        //设置手柄连接状态监听器
        mControllerService.setStateListener(this);

        // 设置摇杆模拟方向键，如果需要的话
        //mControllerService.setLeftStick2Dpad(true);
        //mControllerService.setRightStick2Dpad(true);

        //判断手柄是否已经连接
        if (mControllerService.hasDeviceConnected()) {
            Log.i(TAG, "has device connected");
            List<BFDevice> bfDevices = mControllerService.getDeviceList();
            for (BFDevice bfDevice : bfDevices) {
                Log.i(TAG, bfDevice.toString());
            }
        } else {
            Log.i(TAG, "no device connected");
            //提示用户连接手柄
            if (show_gameHandler_remoteControl)
                mControllerService.showDeviceManagerUI(activity, true);
        }
    }

    @Override
    public void onControllerKeyDown(int playerOrder, int keyCode) {
        //playerOrder	+=	100;
        Log.i(TAG, "onControllerKeyDown" + " playerOrder:" + playerOrder + " keyCode:" + keyCode);
        if (ll != null)
            ll.onLogListener("onControllerKeyDown" + " playerOrder:" + playerOrder + " keyCode:" + keyCode);
        if (!rControl2.setKeyEvent(playerOrder, keyCode, KeyEvent.ACTION_DOWN) && !rControl.setKeyEvent(playerOrder, keyCode, KeyEvent.ACTION_DOWN))
            gHandle.onControllerKeyDown(playerOrder, keyCode);

        if (onSDKKeyListener != null) {
            onSDKKeyListener.OnSDKKeyDown(playerOrder, keyCode);
        }
    }

    @Override
    public void onControllerKeyUp(int playerOrder, int keyCode) {
        Log.i(TAG, "onControllerKeyUp" + " playerOrder:" + playerOrder + " keyCode:" + keyCode);
        if (ll != null)
            ll.onLogListener("onControllerKeyUp" + " playerOrder:" + playerOrder + " keyCode:" + keyCode);
        if (!rControl2.setKeyEvent(playerOrder, keyCode, KeyEvent.ACTION_UP) && !rControl.setKeyEvent(playerOrder, keyCode, KeyEvent.ACTION_UP))
            gHandle.onControllerKeyUp(playerOrder, keyCode);

        if (onSDKKeyListener != null) {
            onSDKKeyListener.OnSDKKeyUp(playerOrder, keyCode);
        }
    }

    @Override
    public void onLeftStickChanged(int playerOrder, float x, float y) {
        Log.v(TAG, "onLeftStickChanged " + "playerOrder:" + playerOrder + " x:" + x + " y:" + y);
        gHandle.onLeftStickChanged(playerOrder, x, y);
    }

    @Override
    public void onRightStickChanged(int playerOrder, float x, float y) {
        Log.v(TAG, "onRightStickChanged " + "playerOrder:" + playerOrder + " x:" + x + " y:" + y);
        gHandle.onRightStickChanged(playerOrder, x, y);
    }

    @Override
    public void onControllerStateChanged(int playerOrder, int state, BFDevice device) {
        Log.v(TAG, "onControllerStateChanged " + "playerOrder:" + playerOrder + " state:" + state);
    }

    public void onLeftStickChangedOnHandler(int playerOrder, final float x, final float y) {
        final int playerOrder2 = playerOrder + 100;
        Log.v(TAG, "onLeftStickChanged " + "playerOrder2:" + playerOrder + " x:" + x + " y:" + y);
        handler.post(new Runnable() {
            public void run() {
                //MainGun.mainGun.tv.setText(MainGun.mainGun.tv.getText() + "\r\n" + "x:" + x + " y:" + y);
                gHandle.onLeftStickChanged(playerOrder2, x, y);
            }
        });
    }

    public void onRightStickChangedOnHandler(final int playerOrder, final float x, final float y) {
        final int playerOrder2 = playerOrder + 100;
        Log.v(TAG, "onLeftStickChanged " + "playerOrder2:" + playerOrder + " x:" + x + " y:" + y);
        handler.post(new Runnable() {
            public void run() {
                //MainGun.mainGun.tv.setText(MainGun.mainGun.tv.getText() + "\r\n" + "x:" + x + " y:" + y);
                gHandle.onRightStickChanged(playerOrder2, x, y);
            }
        });
    }

    private LogListener ll;

    public void setLogListener(LogListener ll) {
        this.ll = ll;
    }

    public interface LogListener {
        public void onLogListener(String str);
    }

    public static void showTouchEvent(MotionEvent paramMotionEvent) {


        int count = paramMotionEvent.getPointerCount();
        for (int i = 0; i < count; i++) {
            android.view.MotionEvent.PointerProperties a = new android.view.MotionEvent.PointerProperties();
            paramMotionEvent.getPointerProperties(i, a);
        }

    }

    public void allChangeMouseMode() {
        if (gameHandler_remoteControl) {
            rControl.setMouseMode(true);
            gHandle.setRightStickAsMouse(true);

        }
    }

    public void allChangeTouchMode() {
        if (gameHandler_remoteControl) {
            rControl.setMouseMode(false);
            gHandle.setRightStickAsMouse(false);
        }
    }

    private boolean check(final Activity activity) {
        String appName = CServer.checkAlreadyRun(activity);
        if (appName != null) {
            AlertDialog dialog = new AlertDialog.Builder(activity).create();
            dialog.setTitle("错误");
            dialog.setMessage("请先关闭" + appName);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }

            });
            dialog.setCancelable(false);
            dialog.show();
        }
        return appName != null;
    }

    private void parseConfigJson() {
        String jStr = null;
        try {
            InputStream is = getApplicationContext().getAssets().open(FileDir.getInstance().CONFIG_JSON);
            jStr = Utils.inStreamToStr(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            JSONObject jo = new JSONObject(jStr);
            show_gameHandler_remoteControl = jo.getBoolean("show_gameHandler_remoteControl_dialog");
            gameHandler_remoteControl = jo.getBoolean("gameHandler_remoteControl");
            gun_control = jo.getBoolean("gun_control");
            openDebug = jo.getBoolean("openTouchDebug");
            youmeng = jo.getBoolean("youmeng");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseConfigJsonMouse() {
        String jStr = null;
        try {
            InputStream is = getApplicationContext().getAssets().open(FileDir.getInstance().CONFIG_JSON);
            jStr = Utils.inStreamToStr(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            JSONObject jo = new JSONObject(jStr);
            JSONArray mouseJoa = jo.getJSONArray("mouse");
            for (int i = 0; i < mouseJoa.length(); i++) {
                JSONObject mouseJo = mouseJoa.getJSONObject(i);
                int id = mouseJo.getInt("id");
                JSONObject picJo = mouseJo.getJSONObject("pic");
                String normalPic = picJo.getString("normal");
                String pressPic = picJo.getString("press");

                mouse.putIcon(id, normalPic, pressPic);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setMobclickAgentActivityResume() {
        if (lastActivity != null) {
            Activity activity = lastActivity.get();
            if (activity != null) {
                MobclickAgent.onPageStart(activity.getClass().getSimpleName());
                MobclickAgent.onResume(activity);
            }

        }

    }

    private void setMobclickAgentActivityPause() {
        if (lastActivity != null) {
            Activity activity = lastActivity.get();
            if (activity != null) {
                MobclickAgent.onPageEnd(activity.getClass().getSimpleName());
                MobclickAgent.onPause(activity);
            }
        }

    }

    private void initMobclickAgent() {
        String str = "";
        try {
            InputStream is = applicationContext.getAssets().open(FileDir.getInstance().YOUMENG);
            str = Utils.inStreamToStr(is);
            JSONObject jo = new JSONObject(str);
            String appKey = jo.getString("AppKey");
            AnalyticsConfig.setAppkey(applicationContext, appKey);

            is = applicationContext.getAssets().open(FileDir.getInstance().EXTRAL);
            str = Utils.inStreamToStr(is);
            jo = new JSONObject(str);
            String qudao = jo.getString("qudao");
            AnalyticsConfig.setChannel(qudao);

            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        MobclickAgent.setDebugMode(false);
        MobclickAgent.openActivityDurationTrack(false);
    }

    public void setOnSDKKeyListener(OnSDKKeyListener osdkkl) {
        this.onSDKKeyListener = osdkkl;
    }

    public void removeOnSDKKeyListener(OnSDKKeyListener osdkkl) {
        this.onSDKKeyListener = null;
    }

    public void setLocateRemoteControlJson(String fileName) {
        rControl2.setJson(fileName);
    }

    public interface OnSDKKeyListener {
        public void OnSDKKeyDown(int playerOrder, int keyCode);

        public void OnSDKKeyUp(int playerOrder, int keyCode);
    }

}
