package com.bagelplay.gameset.evagame.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.activity.NumGameActivity;
import com.bagelplay.gameset.evagame.ise.result.Result;
import com.bagelplay.gameset.evagame.ise.result.xml.XmlResultParser;
import com.bagelplay.gameset.utils.SoundUtil;
import com.bagelplay.sdk.cocos.SDKCocosManager;
import com.bagelplay.sdk.common.OnVoiceListener;
import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.jimmy.wavelibrary.WaveLineView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zhangtianjie on 2017/8/25.
 */

public class EvaluationView extends RelativeLayout implements View.OnClickListener {
    private static String TAG = EvaluationView.class.getSimpleName();
    Context mContext;

    private final static String PREFER_NAME = "evagame_settings";
    private SpeechEvaluator mIse;
    private Toast mToast;
    private EditText mEvaTextEditText;
    private EditText mResultEditText;
    private Button mIseStartButton;
    private Button mIsePlayButton;

    private WaveLineView mWaveLineView;


    // 评测语种
    private String language;
    // 评测题型
    private String category;
    // 结果等级
    private String result_level;

    private String mLastResult;


    public static int frequency = 16000;
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

                if (!TextUtils.isEmpty(builder)) {
                    mResultEditText.setText(builder.toString());
                }
                mIseStartButton.setEnabled(true);
                mLastResult = builder.toString();

                showTip("评测结束");


                // 解析最终结果
                if (!TextUtils.isEmpty(mLastResult)) {
                    XmlResultParser resultParser = new XmlResultParser();
                    Result result1 = resultParser.parse(mLastResult);

                    if (null != result1) {
                        mResultEditText.setText(result1.toString());
                    } else {
                        showTip("解析结果为空");
                    }
                }

            }
        }

        @Override
        public void onError(SpeechError error) {
            mIseStartButton.setEnabled(true);
            if (error != null) {
                showTip("error:" + error.getErrorCode() + "," + error.getErrorDescription());
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
            Log.d(TAG, "返回音频数据：" + data.length);

            mWaveLineView.setVolume(volume*5);
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


    public EvaluationView(Context context) {
        super(context);
    }

    public EvaluationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.evaluation_view_layout, this, true);
        mIse = SpeechEvaluator.createEvaluator(mContext, null);

        initUI();
        setEvaText();
    }


    private void initUI() {

        mEvaTextEditText = (EditText) findViewById(R.id.ise_eva_text);
        mResultEditText = (EditText) findViewById(R.id.ise_result_text);
        mIseStartButton = (Button) findViewById(R.id.ise_start);
        mIseStartButton.setOnClickListener(this);
        mIseStartButton.setOnClickListener(this);

        mIsePlayButton = (Button) findViewById(R.id.ise_play);
        mIsePlayButton.setOnClickListener(this);

        findViewById(R.id.ise_stop).setOnClickListener(this);

        mWaveLineView= (WaveLineView) findViewById(R.id.wave_line_view);


        mToast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
    }

    private void setEvaText() {


        SharedPreferences pref = mContext.getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        language = pref.getString(SpeechConstant.LANGUAGE, "zh_cn");
        category = pref.getString(SpeechConstant.ISE_CATEGORY, "read_sentence");

        String text = "";
        if ("en_us".equals(language)) {
            if ("read_word".equals(category)) {
                text = mContext.getString(R.string.text_en_word);
            } else if ("read_sentence".equals(category)) {
                text = mContext.getString(R.string.text_en_sentence);
            }
        } else {
            // 中文评测
            if ("read_syllable".equals(category)) {
                text = mContext.getString(R.string.text_cn_syllable);
            } else if ("read_word".equals(category)) {
                text = mContext.getString(R.string.text_cn_word);
            } else if ("read_sentence".equals(category)) {
                text = mContext.getString(R.string.text_cn_sentence);
            }
        }

        mEvaTextEditText.setText(text);
        mResultEditText.setText("");
        mLastResult = null;
        mResultEditText.setHint("请点击“开始评测”按钮");
    }

    private void setParams() {
        SharedPreferences pref = mContext.getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
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
        //mIse.setParameter(SpeechConstant.AUDIO_SOURCE,"-1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
    }

    public EvaluationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View view) {


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


                SDKCocosManager.getInstance().startVoice(frequency, channelConfiguration,
                        audioEncoding, new OnVoiceListener() {

                            @Override
                            public void OnVoice(byte[] data, int arg1) {


                                boolean flag = mIse.writeAudio(data, 0, data.length);


                                // Log.d(TAG,flag+data.toString()+" "+offset+" "+data.length);
                            }
                        });

                mWaveLineView.startAnim();

                break;

            case R.id.ise_stop:
                mWaveLineView.stopAnim();
                SDKCocosManager.getInstance().stopVoice();
                if (mIse.isEvaluating()) {
                    mResultEditText.setHint("评测已停止，等待结果中...");
                    mIse.stopEvaluating();


                }
                break;

            case R.id.ise_play: {

                SoundUtil.getInstance(mContext).startPlaySoundFromSD(Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
            }
        }
    }

    private void showTip(String str) {
        if (!TextUtils.isEmpty(str)) {
            mToast.setText(str);
            mToast.show();
        }
    }
}
