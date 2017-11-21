 
#include "com_bagelplay_controller_payment_Jni.h"
 
 jint JNI_OnLoad(JavaVM* vm,void *reserved)
{
	return JNI_VERSION_1_4;
}

JNIEXPORT jbyteArray JNICALL Java_com_bagelplay_controller_payment_Jni_getAppID
  (JNIEnv * env, jclass cla, jint platform)
 {
	char * id;
	if(platform == 2)			//电信
	{
		id	=	"198054610000038012";
	}
	else if(platform == 1)		//移动
	{
		id	=	"300008666949";
	}
	if(id == NULL)
	{
		return NULL;
	}
	int rlmLen	=	strlen(id);
	jbyteArray iarr=(*env)->NewByteArray(env,rlmLen);
	(*env)->SetByteArrayRegion(env,iarr,0,rlmLen,id);
	return iarr;
}


JNIEXPORT jbyteArray JNICALL Java_com_bagelplay_controller_payment_Jni_getAppKey
  (JNIEnv * env, jclass cla, jint platform)
  {
	char * key;
	if(platform == 2)			//电信
	{
		key	=	"3da2472be05682e9e3e892e76c052aff";
	}
	else if(platform == 1)		//移动
	{
		key	=	"4021808C1FF35050A11C4619AE3F0521";
	}
	if(key == NULL)
	{
		return NULL;
	}
	int rlmLen	=	strlen(key);
	jbyteArray iarr=(*env)->NewByteArray(env,rlmLen);
	(*env)->SetByteArrayRegion(env,iarr,0,rlmLen,key);
	return iarr;
 }


JNIEXPORT jobjectArray JNICALL Java_com_bagelplay_controller_payment_Jni_getProductPrice
  (JNIEnv * env, jclass cla, jint platform, jint price)
  {
	char dianxinProductPrice[11][50]	=	{
		"",
		"90001083",
		"30000866694901",
		"30000866694902",
		"4块",
		"30000866694903",
		"90001083",
		"30000866694901",
		"30000866694902",
		"4块",
		"30000866694903",
		""
	};
	char mobileProductPrice[11][50]	=	{
		"",
		"30000866694901",
		"30000866694901",
		"30000866694902",
		"4块",
		"30000866694903",
		"90001083",
		"30000866694901",
		"30000866694902",
		"4块",
		"30000866694903",
		""
	};
	char googleProductPrice[11][50]	=	{
		"",
		"1yuan_um",
		"2yuan_um",
		"30000866694902",
		"4块",
		"30000866694903",
		"90001083",
		"30000866694901",
		"30000866694902",
		"4块",
		"30000866694903",
		""
	};
	
	if(price > 10)
		price	=	0;
	char  * res;
	if(platform == 2)			//电信
	{
		res	=	dianxinProductPrice[price];
	}
	else if(platform == 1)		//移动
	{
		res	=	mobileProductPrice[price];
	}
	else if(platform == 3)		//google
	{
		res	=	googleProductPrice[price];
	}
	int rlmLen	=	strlen(res);
	jbyteArray iarr=(*env)->NewByteArray(env,rlmLen);
	(*env)->SetByteArrayRegion(env,iarr,0,rlmLen,res);
	return iarr;
 }
