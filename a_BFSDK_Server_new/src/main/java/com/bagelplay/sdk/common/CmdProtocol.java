package com.bagelplay.sdk.common;

public class CmdProtocol {

	public static class SDKToServer {
		public static final int LOGIN = 40001;

		public static final int GAMEINFO = 40100;

		public static final int GAMEINFO_ZIPNAME = 40101;

		public static final int HEART = 40200;

		public static final int WANTPLAYERID = 40700;

		public static final int WANTPLAYERID2 = 40701;

		public static final int SHOWINPUT = 40800;

		public static final int PAYMENT_REQUEST = 43001;

		public static final int VOICE_START = 44001;

		public static final int VOICE_STOP = 44002;

		public static final int VOICE_START_PLAYERID = 44003;

		public static final int VOICE_STOP_PLAYERID = 44004;

		public static final int VOICE_CHECK = 44011;
	}

	public static class ServerToSDK {
		public static final int LOGIN = 30001;

		public static final int APPINFO = 30100;

		public static final int MOUSE = 30200;

		public static final int MOUSE_HOLD = 30201;

		public static final int TOUCH = 30300;

		public static final int KEY = 30400;

		public static final int SENSOR = 30500;

		public static final int INPUTTEXT = 30600;

		public static final int WANTPLAYERID = 30700;

		public static final int WANTPLAYERID2 = 30701;

		public static final int PLAYERCONNECT = 30900;

		public static final int GAMEHANDLER_HANDLER_STICK = 32011;

		public static final int GAMEHANDLER_HANDLER_BUTTON = 32012;

		public static final int PAYMENT_HEART = 33002;

		public static final int PAYMENT_RESULT = 33003;

		public static final int VOICE_DATA = 34003;

		public static final int VOICE_CHECK_RESULT = 34012;

		public static final int SERVER_CLOSE = 31100;
	}

	public static class SDKToUnity {
		public static final int LOGIN = 50001;

		public static final int SENSOR = 50500;

		public static final int PLAYERCONNECT = 50900;

		public static final int GAMEHANDLER_HANDLER_STICK = 52011;

		public static final int GAMEHANDLER_HANDLER_BUTTON = 52012;

		public static final int WANTPLAYERID = 50700;

		public static final int PHYSICSHANDLER_HANDLER_STICK = 52111;

		public static final int PHYSICSHANDLER_HANDLER_BUTTON = 52112;

		public static final int REMOTECONTROL = 53000;
	}

	public static class UnityToSDK {
		public static final int CHANGE_DOMOTION_VIEW = 60100;

		public static final int UDP_PORT = 61200;

		public static final int CHANGE_MOUSE_XY = 61300;

		public static final int LOCATE_REMOTECONTROL_JSON = 68000;

		//��ʱΪ������
		public static final int XSG_CONTROL_STICK = 67001;

	}

	public static class SDKTOCP {
		public static final int GUNEVENT = 54000;
	}
}
