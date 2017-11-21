package com.bagelplay.sdk.common.remoteControl;

import com.bagelplay.sdk.common.util.Log;

import android.view.KeyEvent;

public class KeyConst {

	private static String TAG	=	"KeyConst";

	public static final int KEY_UP		=	100;

	public static final int KEY_DOWN	=	101;

	public static final int KEY_LEFT	=	102;

	public static final int KEY_RIGHT	=	103;

	public static final int KEY_CENTER	=	104;

	public static final int KEY_BACK	=	105;

	public static final int KEY_MENU	=	106;

	public static final int KEY_HOME	=	107;

	public static final int KEY_VOLUME_UP	=	108;

	public static final int KEY_VOLUME_DOWN	=	109;

	public static final int KEY_POWER		=	110;

	public static final int KEY_SETTINGS	=	111;

	public static final int KEY_VOLUME_MUTE	=	112;			//静音

	public static final int KEY_PAGE_UP		=	113;

	public static final int KEY_PAGE_DOWN	=	114;

	public static final int KEY_CHANNEL_UP	=	115;

	public static final int KEY_CHANNEL_DOWN	=	116;

	public static final int KEY_MEDIA_REWIND	=	117;

	public static final int KEY_MEDIA_FORWARD	=	118;

	public static final int KEY_MEDIA_PAUSE		=	119;

	public static final int KEY_MEDIA_STOP		=	120;

	public static final int KEY_0	=	121;

	public static final int KEY_1	=	122;

	public static final int KEY_2	=	123;

	public static final int KEY_3	=	124;

	public static final int KEY_4	=	125;

	public static final int KEY_5	=	126;

	public static final int KEY_6	=	127;

	public static final int KEY_7	=	128;

	public static final int KEY_8	=	129;

	public static final int KEY_9	=	130;

	public static final int[] PHYSICSKEY			=	new int[31];

	public static final String[] KEYCODESTR			=	new String[31];

	static
	{

		PHYSICSKEY[KEY_UP - 100]			=	19;
		PHYSICSKEY[KEY_DOWN - 100]			=	20;
		PHYSICSKEY[KEY_LEFT - 100]			=	21;
		PHYSICSKEY[KEY_RIGHT - 100]			=	22;
		PHYSICSKEY[KEY_CENTER - 100]		=	23;
		PHYSICSKEY[KEY_BACK - 100]			=	4;
		PHYSICSKEY[KEY_MENU - 100]			=	82;
		PHYSICSKEY[KEY_HOME - 100]			=	3;
		PHYSICSKEY[KEY_VOLUME_UP - 100]		=	24;
		PHYSICSKEY[KEY_VOLUME_DOWN - 100]	=	25;
		PHYSICSKEY[KEY_POWER - 100]			=	179;
		PHYSICSKEY[KEY_SETTINGS - 100]		=	176;
		PHYSICSKEY[KEY_VOLUME_MUTE - 100]	=	164;			//静音
		PHYSICSKEY[KEY_PAGE_UP - 100]		=	92;
		PHYSICSKEY[KEY_PAGE_DOWN - 100]		=	93;
		PHYSICSKEY[KEY_CHANNEL_UP - 100]	=	166;
		PHYSICSKEY[KEY_CHANNEL_DOWN - 100]	=	167;
		PHYSICSKEY[KEY_MEDIA_REWIND - 100]	=	89;
		PHYSICSKEY[KEY_MEDIA_FORWARD - 100]	=	90;
		PHYSICSKEY[KEY_MEDIA_PAUSE - 100]	=	85;
		PHYSICSKEY[KEY_MEDIA_STOP - 100]	=	86;
		PHYSICSKEY[KEY_0 - 100]				=	7;
		PHYSICSKEY[KEY_1 - 100]				=	8;
		PHYSICSKEY[KEY_2 - 100]				=	9;
		PHYSICSKEY[KEY_3 - 100]				=	10;
		PHYSICSKEY[KEY_4 - 100]				=	11;
		PHYSICSKEY[KEY_5 - 100]				=	12;
		PHYSICSKEY[KEY_6 - 100]				=	13;
		PHYSICSKEY[KEY_7 - 100]				=	14;
		PHYSICSKEY[KEY_8 - 100]				=	15;
		PHYSICSKEY[KEY_9 - 100]				=	16;


	}

	static
	{
		KEYCODESTR[KEY_UP - 100]			=	"KEY_UP";
		KEYCODESTR[KEY_DOWN - 100]			=	"KEY_DOWN";
		KEYCODESTR[KEY_LEFT - 100]			=	"KEY_LEFT";
		KEYCODESTR[KEY_RIGHT - 100]			=	"KEY_RIGHT";
		KEYCODESTR[KEY_CENTER - 100]		=	"KEY_CENTER";
		KEYCODESTR[KEY_BACK - 100]			=	"KEY_BACK";
		KEYCODESTR[KEY_MENU - 100]			=	"KEY_MENU";
		KEYCODESTR[KEY_HOME - 100]			=	"KEY_HOME";
		KEYCODESTR[KEY_VOLUME_UP - 100]		=	"KEY_VOLUME_UP";
		KEYCODESTR[KEY_VOLUME_DOWN - 100]	=	"KEY_VOLUME_DOWN";
		KEYCODESTR[KEY_POWER - 100]			=	"KEY_POWER";
		KEYCODESTR[KEY_SETTINGS - 100]		=	"KEY_SETTINGS";
		KEYCODESTR[KEY_VOLUME_MUTE - 100]	=	"KEY_VOLUME_MUTE";			//静音
		KEYCODESTR[KEY_PAGE_UP - 100]		=	"KEY_PAGE_UP";
		KEYCODESTR[KEY_PAGE_DOWN - 100]		=	"KEY_PAGE_DOWN";
		KEYCODESTR[KEY_CHANNEL_UP - 100]	=	"KEY_CHANNEL_UP";
		KEYCODESTR[KEY_CHANNEL_DOWN - 100]	=	"KEY_CHANNEL_DOWN";
		KEYCODESTR[KEY_MEDIA_REWIND - 100]	=	"KEY_MEDIA_REWIND";
		KEYCODESTR[KEY_MEDIA_FORWARD - 100]	=	"KEY_MEDIA_FORWARD";
		KEYCODESTR[KEY_MEDIA_PAUSE - 100]	=	"KEY_MEDIA_PAUSE";
		KEYCODESTR[KEY_MEDIA_STOP - 100]	=	"KEY_MEDIA_STOP";
		KEYCODESTR[KEY_0 - 100]				=	"KEY_0";
		KEYCODESTR[KEY_1 - 100]				=	"KEY_1";
		KEYCODESTR[KEY_2 - 100]				=	"KEY_2";
		KEYCODESTR[KEY_3 - 100]				=	"KEY_3";
		KEYCODESTR[KEY_4 - 100]				=	"KEY_4";
		KEYCODESTR[KEY_5 - 100]				=	"KEY_5";
		KEYCODESTR[KEY_6 - 100]				=	"KEY_6";
		KEYCODESTR[KEY_7 - 100]				=	"KEY_7";
		KEYCODESTR[KEY_8 - 100]				=	"KEY_8";
		KEYCODESTR[KEY_9 - 100]				=	"KEY_9";
	}

	public static int translateKeyCodefromPhysicsKey(int keyCodePhysics)
	{
		for(int i=0;i<PHYSICSKEY.length;i++)
		{
			if(PHYSICSKEY[i] == keyCodePhysics)
				return i + 100;
		}
		return -1;
	}

	public static int translatePhysicsKeyfromKeyCode(int keyCode)
	{
		if(keyCode >= 0 + 100 && keyCode < PHYSICSKEY.length + 100)
			return PHYSICSKEY[keyCode - 100];
		return -1;
	}

	public static int translateKeyCodefromKeyCodeStr(String keyCodeStr)
	{
		for(int i= 0;i<KEYCODESTR.length;i++)
		{
			if(KEYCODESTR[i].equals(keyCodeStr))
				return i + 100;
		}

		return -1;
	}


}
