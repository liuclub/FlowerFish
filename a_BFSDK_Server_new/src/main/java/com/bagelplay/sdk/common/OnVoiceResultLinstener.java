package com.bagelplay.sdk.common;

public interface OnVoiceResultLinstener {

	//第一个参数right为正确的文字
	//第二个参数手机调用讯飞处理过后的结果，建议用json格式
	public void OnVoiceResult(String right,String result);
}
