package com.bagelplay.controller.utils;

public class CmdProtocol {

	public static class ClientToServer {
		public static final int LOGIN = 10001;

		public static final int APPINFO = 10100;

		public static final int MOUSE = 10200;

		public static final int MOUSE_HOLD = 10201;

		public static final int TOUCH = 10300;

		public static final int KEY = 10400;

		public static final int SENSOR = 10500;

		public static final int SENSOR_SWITCH = 10501;

		public static final int INPUTTEXT = 10600;

		public static final int HEART = 11000;

		public static final int GAMEHANDLER_HANDLER_STICK = 12011;

		public static final int GAMEHANDLER_HANDLER_BUTTON = 12012;

		public static final int PAYMENT_HEART = 13002;

		public static final int PAYMENT_RESULT = 13003;
		
		public static final int VOICE_DATA = 14003;
		
		public static final int VOICE_CHECK_RESULT		=	14012;
		
		public static final int SERVER_CLOSE		=	11100;
		
		
	}

	public static class ServerToClient {
		public static final int LOGIN = 20001;

		public static final int APPINFO = 20100;

		public static final int APPINFO_ZIPNAME = 20101;

		public static final int SHOWINPUT = 20800;

		public static final int HEART = 21000;

		public static final int LOGOFF = 21100;

		public static final int PAYMENT_REQUEST = 23001;

		public static final int EXTRAL = 21200;
		
		public static final int VOICE_START = 24001;

		public static final int VOICE_STOP = 24002;
		
		public static final int VOICE_CHECK = 24011;
	}
	
	public static class ClientToTranslateServer {
		
		public static final int REGISTER = 50001;
		
		public static final int DATA = 51000;

	}

	public static final int ERRNO_UNKNOWN = -100;
	public static final int ERRNO_SUEECSS = 0;
	public static final int ERRNO_CODE_NOT_SET = -1;
	public static final int ERRNO_CODE_WRONG = -2;
	public static final int ERRNO_ACCOUNT_EXISTED = -3;
	public static final int ERRNO_ACCOUNT_NOT_REG = -4;
	public static final int ERRNO_ACCOUNT_HAVE_LOGGED = -5;
	public static final int ERRNO_MAX_USERS = -6;
}
