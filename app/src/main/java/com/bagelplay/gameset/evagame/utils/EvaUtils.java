package com.bagelplay.gameset.evagame.utils;

import android.content.Context;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bagelplay.gameset.R;
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

import static android.content.ContentValues.TAG;


/**
 * Created by zhangtianjie on 2017/8/28.
 */

public class EvaUtils {

    Context mContext;
    private SpeechEvaluator mIse;
    private Toast mToast;
    boolean isChineseEva = true;

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


    String evaText;


    private static EvaUtils mEvaUtils=null;


    public static EvaUtils getInstance(Context context){
      if (mEvaUtils==null){
          mEvaUtils=new EvaUtils(context);
        }

        return mEvaUtils;
    }


    private EvaUtils(Context context) {
        this.mContext = context;
        mIse = SpeechEvaluator.createEvaluator(mContext, null);
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);

        //setChinese();

    }

    // 评测监听接口
    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            Log.d(TAG, "evaluator result :" + isLast);
            // mWaveLineView.setVisibility(View.GONE);
            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());

                if (!TextUtils.isEmpty(builder)) {
                    //  mResultEditText.setText(builder.toString());
                }
                // mIseStartButton.setEnabled(true);
                mLastResult = builder.toString();

                showTip("评测结束");


                // 解析最终结果
                if (!TextUtils.isEmpty(mLastResult)) {
                    XmlResultParser resultParser = new XmlResultParser();
                    Result result1 = resultParser.parse(mLastResult);

                    if (null != result1) {
                        // mResultEditText.setText(result1.toString());

                        if(mEvaListener!=null){
                            mEvaListener.onResult((int)(result1.total_score+0.5));

                            mEvaListener=null;
                        }

                    } else {
                        showTip("解析结果为空");
                    }
                }



            }
        }

        @Override
        public void onError(SpeechError error) {
            // mIseStartButton.setEnabled(true);
            if (error != null) {
                showTip("error:" + error.getErrorCode() + "," + error.getErrorDescription());
//                mResultEditText.setText("");
//                mResultEditText.setHint("请点击“开始评测”按钮");
                if(mEvaListener!=null) {
                    mEvaListener.onError();
                }
            } else {
                Log.d(TAG, "evaluator over");
            }
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d(TAG, "evaluator begin");
            if(mEvaListener!=null) {
                mEvaListener.onBeginOfSpeech();
            }

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

            //mWaveLineView.setVolume(volume*3);


            if(mEvaListener!=null){
                mEvaListener.onVolumeChanged(volume);
            }
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


    private void showTip(String str) {
        if (!TextUtils.isEmpty(str)) {
            mToast.setText(str);
            mToast.show();
        }
    }


    private void setParams() {
    /*    SharedPreferences pref = mContext.getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
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
        String speech_timeout = pref.getString(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");*/

        result_level = "complete";
        String vad_bos = "5000";
        String vad_eos = "1800";
        String speech_timeout = "10000";

        mIse.setParameter(SpeechConstant.LANGUAGE, language);
        mIse.setParameter(SpeechConstant.ISE_CATEGORY, category);
        mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");

        mIse.setParameter(SpeechConstant.VAD_BOS, vad_bos);
        mIse.setParameter(SpeechConstant.VAD_EOS, vad_eos);

        mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
        mIse.setParameter(SpeechConstant.RESULT_LEVEL, result_level);


        //设置后数据从其 writeAudio(byte[] buffer,int offset,int length)方法来
       // mIse.setParameter(SpeechConstant.AUDIO_SOURCE,"-1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
    }


    private void setEvaText(String text) {


        if (isChineseEva) {
            language = "zh_cn";
            category = "read_word";
        } else {
            language = "en_us";
            category = "read_word";
        }


        if ("en_us".equals(language)) {
            if ("read_word".equals(category)) {
                evaText = "[word]"+text;
            } else if ("read_sentence".equals(category)) {
                evaText = mContext.getString(R.string.text_en_sentence);
            }
        } else {
            // 中文评测
            if ("read_syllable".equals(category)) {
                evaText = mContext.getString(R.string.text_cn_syllable);
            } else if ("read_word".equals(category)) {
                evaText = text;
            } else if ("read_sentence".equals(category)) {
                evaText = mContext.getString(R.string.text_cn_sentence);
            }
        }

        // mEvaTextTextView.setText(text);
        // mResultEditText.setText("");
        mLastResult = null;
        // mResultEditText.setHint("请点击“开始评测”按钮");
    }


    private void stopEva() {
        // mWaveLineView.stopAnim();

        SDKCocosManager.getInstance().stopVoice();
        if (mIse.isEvaluating()) {
            // mResultEditText.setHint("评测已停止，等待结果中...");
            mIse.stopEvaluating();


        }
    }

    public void cancelEva(){
        mIse.cancel();
    }

    public void destroyEva(){
        if (null != mIse) {
            mIse.destroy();
            mIse = null;
        }
    }



    public void startEvaWithListener(EvaListener mEvaListener,boolean isChineseEva,String myEvatext) {


        if (mIse == null) {
            return;
        }


         setOnEvaListener(mEvaListener);

      if(isChineseEva) {
          setChinese(myEvatext);
      }else{
          setEnglish(myEvatext);
      }

        // mWaveLineView.setVisibility(View.VISIBLE);


        mLastResult = null;
//        mResultEditText.setText("");
//        mResultEditText.setHint("请朗读以上内容");
//        mIseStartButton.setEnabled(false);

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

        // mWaveLineView.startAnim();
    }

    public void evaPlay() {
        SoundUtil.getInstance(mContext).startPlaySoundFromSD(Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");

    }


    private void setChinese(String text) {

        isChineseEva = true;
        setEvaText(text);
    }

    private void setEnglish(String text) {
        isChineseEva = false;
        setEvaText(text);
    }

    private EvaListener mEvaListener;

    public void setOnEvaListener(EvaListener mEvaListener){
        this.mEvaListener=mEvaListener;
    }

    public interface EvaListener{

        void onVolumeChanged(int volume);

        void onResult(int grade);

        void onError();

        void onBeginOfSpeech();
    }


}
