package com.bagelplay.controller.update;

public class Global {
	
	public static final String HOSTNAME			=	"http://data2.biggifi.com";
	
	public static final String UPDATE_SERVER_URI = "http://data2.biggifi.com/update/assistant/index.php";
	
	public static final String UPDATE_APKNAME = "BiggiFiVMG.apk";
	public static final String UPDATE_VERXML_EN = "version_en.xml";
	public static final String UPDATE_VERXML_ZH = "version_zh.xml";
	
	public static final String APP_ANME = "BiggiFiVMG";
	
	public static int mLocalVersion = 0;
	public static String mLocalVersionName;
	
	public static int mSerVerCode = 0;
	public static String mSerVerCodeStr = "";
	public static String mSerVerNameStr = "";
	public static String mDescription = "";
    
	public static String mDownloadDir = "/";
	public static String mFullDir = null;
	public static boolean mHaveExternalStorage = false;
}
