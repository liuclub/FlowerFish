package com.bagelplay.controller.voice;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.Log;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

public class VoiceRecorder {
	private String TAG = "VoiceRecorder";

	private boolean isRecording;

	private RecordTh rth;

	private AudioRecord audioRecord;

	private byte[] buffer;

	private OnVoiceRecorderListener ovrl;

	public static VoiceRecorder voiceRecorder;

	private VoiceRecorder() {

	}

	public static VoiceRecorder getInstance() {
		if (voiceRecorder == null)
			voiceRecorder = new VoiceRecorder();
		return voiceRecorder;
	}

	public void start(int frequency, int channelConfiguration, int audioEncoding) {
		if (isRecording || rth != null) {
			return;
		}

		// bufferSize=1280
		int bufferSize = AudioRecord.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);

		// Log.d(TAG, "bufferSize:"+bufferSize);

		try {
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
					frequency, channelConfiguration, audioEncoding, bufferSize);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		buffer = new byte[bufferSize * 2];
		isRecording = true;
		rth = new RecordTh();

	}

	public void stop() {
		isRecording = false;
	}

	public void setOnVoiceRecorderListener(OnVoiceRecorderListener ovrl) {
		this.ovrl = ovrl;
	}

	class RecordTh extends Thread {
		public RecordTh() {
			start();
		}

		public void run() {
			audioRecord.startRecording();
			while (isRecording) {
				int bufferReadResult = audioRecord.read(buffer, 0,
						buffer.length);
				Log.v(TAG, "bufferReadResult:" + bufferReadResult);
				if (bufferReadResult > 0) {
					send(bufferReadResult);
				}
			}
			release();
		}

		private void send(int bufferReadResult) {
			int sendLen = 0;
			int sendUnit = 640;
			if (bufferReadResult < sendUnit) {
				BagelPlayVmaStub.getInstance().sendVoiceData(buffer, sendLen,
						bufferReadResult);
			} else {
				for (; sendLen < bufferReadResult; sendLen += sendUnit) {
					if (sendLen + sendUnit >= bufferReadResult)
						BagelPlayVmaStub.getInstance().sendVoiceData(buffer,
								sendLen, bufferReadResult - sendLen);
					else
						BagelPlayVmaStub.getInstance().sendVoiceData(buffer,
								sendLen, sendUnit);
				}

			}
		}

		private void release() {
			buffer = null;
			try {
				audioRecord.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			audioRecord.release();
			audioRecord = null;
			rth = null;
		}
	}

	public interface OnVoiceRecorderListener {
		public void OnVoiceRecorder(byte[] data, int len);
	}

}
