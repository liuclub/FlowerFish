package com.bagelplay.gameset.activity;

import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.evagame.ise.result.Result;
import com.bagelplay.gameset.evagame.ise.result.xml.XmlResultParser;
import com.bagelplay.sdk.cocos.SDKCocosManager;
import com.bagelplay.sdk.common.OnVoiceListener;
import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;


public class EvaluationGameActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = EvaluationGameActivity.class.getSimpleName();
    private final static String PREFER_NAME = "evagame_settings";
    private SpeechEvaluator mIse;
    private Toast mToast;
    private EditText mEvaTextEditText;
    private EditText mResultEditText;
    private Button mIseStartButton;

    // 评测语种
    private String language;
    // 评测题型
    private String category;
    // 结果等级
    private String result_level;

    private String mLastResult;


    public static  int frequency = 16000;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    public static int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    public static int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;


    // 评测监听接口
    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            Log.d(TAG, "evaluator result :" + isLast);

            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());

                if(!TextUtils.isEmpty(builder)) {
                    mResultEditText.setText(builder.toString());
                }
                mIseStartButton.setEnabled(true);
                mLastResult = builder.toString();

                showTip("评测结束");
            }
        }

        @Override
        public void onError(SpeechError error) {
            mIseStartButton.setEnabled(true);
            if(error != null) {
                showTip("error:"+ error.getErrorCode() + "," + error.getErrorDescription());
                mResultEditText.setText("");
                mResultEditText.setHint("请点击“开始评测”按钮");
            } else {
                Log.d(TAG, "evaluator over");
            }
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d(TAG, "evaluator begin");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.d(TAG, "evaluator stoped");


        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前音量：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_evaluation_game);
        SDKCocosManager.getInstance(this).addWindowCallBack(this);
        mIse = SpeechEvaluator.createEvaluator(EvaluationGameActivity.this, null);
        initUI();
        setEvaText();
    }
    private void initUI() {

        mEvaTextEditText = (EditText) findViewById(R.id.ise_eva_text);
        mResultEditText = (EditText)findViewById(R.id.ise_result_text);
        mIseStartButton = (Button) findViewById(R.id.ise_start);
        mIseStartButton.setOnClickListener(this);
        mIseStartButton.setOnClickListener(this);
        findViewById(R.id.ise_parse).setOnClickListener(this);
        findViewById(R.id.ise_stop).setOnClickListener(this);
        findViewById(R.id.ise_cancel).setOnClickListener(this);

        mToast = Toast.makeText(EvaluationGameActivity.this, "", Toast.LENGTH_LONG);
    }
    private void setEvaText() {


        SharedPreferences pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        language = pref.getString(SpeechConstant.LANGUAGE, "zh_cn");
        category = pref.getString(SpeechConstant.ISE_CATEGORY, "read_sentence");

        String text = "";
        if ("en_us".equals(language)) {
            if ("read_word".equals(category)) {
                text = getString(R.string.text_en_word);
            } else if ("read_sentence".equals(category)) {
                text = getString(R.string.text_en_sentence);
            }
        } else {
            // 中文评测
            if ("read_syllable".equals(category)) {
                text = getString(R.string.text_cn_syllable);
            } else if ("read_word".equals(category)) {
                text = getString(R.string.text_cn_word);
            } else if ("read_sentence".equals(category)) {
                text = getString(R.string.text_cn_sentence);
            }
        }

        mEvaTextEditText.setText(text);
        mResultEditText.setText("");
        mLastResult = null;
        mResultEditText.setHint("请点击“开始评测”按钮");
    }

    private void setParams() {
        SharedPreferences pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        // 设置评测语言
        language = pref.getString(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置需要评测的类型
        category = pref.getString(SpeechConstant.ISE_CATEGORY, "read_sentence");
        // 设置结果等级（中文仅支持complete）
        result_level = pref.getString(SpeechConstant.RESULT_LEVEL, "complete");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        String vad_bos = pref.getString(SpeechConstant.VAD_BOS, "5000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        String vad_eos = pref.getString(SpeechConstant.VAD_EOS, "1800");
        // 语音输入超时时间，即用户最多可以连续说多长时间；
        String speech_timeout = pref.getString(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");

        mIse.setParameter(SpeechConstant.LANGUAGE, language);
        mIse.setParameter(SpeechConstant.ISE_CATEGORY, category);
        mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");

        mIse.setParameter(SpeechConstant.VAD_BOS, vad_bos);
        mIse.setParameter(SpeechConstant.VAD_EOS, vad_eos);

        mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
        mIse.setParameter(SpeechConstant.RESULT_LEVEL, result_level);




        //设置后数据从其 writeAudio(byte[] buffer,int offset,int length)方法来
        mIse.setParameter(SpeechConstant.AUDIO_SOURCE,"-1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
    }


    int offset =0;
    @Override
    public void onClick(View view) {
        if( null == mIse ){
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip( "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化" );
            return;
        }

        switch (view.getId()) {

            case R.id.ise_start:
                if (mIse == null) {
                    return;
                }

                String evaText = mEvaTextEditText.getText().toString();
                mLastResult = null;
                mResultEditText.setText("");
                mResultEditText.setHint("请朗读以上内容");
                mIseStartButton.setEnabled(false);

                setParams();

                mIse.startEvaluating(evaText, null, mEvaluatorListener);

                offset=0;

                SDKCocosManager.getInstance(this).startVoice(frequency, channelConfiguration,
                      audioEncoding, new OnVoiceListener() {

                            @Override
                            public void OnVoice(byte[] data, int arg1) {
                                offset +=data.length;



                               boolean flag= mIse.writeAudio(data,0,data.length);



                                Log.d(TAG,flag+data.toString()+" "+offset+" "+data.length);
                            }
                        });

                break;
            case R.id.ise_parse:
                // 解析最终结果
                if (!TextUtils.isEmpty(mLastResult)) {
                    XmlResultParser resultParser = new XmlResultParser();
                    Result result = resultParser.parse(mLastResult);

                    if (null != result) {
                        mResultEditText.setText(result.toString());
                    } else {
                        showTip("解析结果为空");
                    }
                }
                break;
            case R.id.ise_stop:

                SDKCocosManager.getInstance(this).stopVoice();
                if (mIse.isEvaluating()) {
                    mResultEditText.setHint("评测已停止，等待结果中...");
                    mIse.stopEvaluating();
                }
                break;
            case R.id.ise_cancel: {
                mIse.cancel();
                mIseStartButton.setEnabled(true);
                mResultEditText.setText("");
                mResultEditText.setHint("请点击“开始评测”按钮");
                mLastResult = null;
                break;
            }
        }
    }

    private void showTip(String str) {
        if(!TextUtils.isEmpty(str)) {
            mToast.setText(str);
            mToast.show();
        }
    }


    public boolean dispatchKeyEvent(KeyEvent event) {
        if (SDKCocosManager.getInstance().dispatchKeyEvent(event))
            return true;
        return super.dispatchKeyEvent(event);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        if (SDKCocosManager.getInstance().dispatchGenericMotionEvent(ev))
            return true;
        return super.dispatchGenericMotionEvent(ev);

    }

    protected void onStop() {
        super.onStop();
        SDKCocosManager.getInstance().onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SDKCocosManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKCocosManager.getInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();

        SDKCocosManager.getInstance().removeWindowCallBack(this);
    }
}
