package com.bagelplay.sdk.common.ghandle;

public class KeyConst {
	
	public static final int KEY_L2		=	0;
	
	public static final int KEY_L1		=	1;
	
	public static final int KEY_R2		=	2;
	
	public static final int KEY_R1		=	3;
	
	public static final int KEY_SELECT	=	4;
	
	public static final int KEY_START	=	5;
	
	public static final int KEY_UP		=	6;
	
	public static final int KEY_DOWN	=	7;
	
	public static final int KEY_LEFT	=	8;
	
	public static final int KEY_RIGHT	=	9;
	
	public static final int KEY_Y		=	10;
	
	public static final int KEY_X		=	11;
	
	public static final int KEY_A		=	12;
	
	public static final int KEY_B		=	13;
	
	public static final int KEY_LEFT_STICK_UP		=	14;
	
	public static final int KEY_LEFT_STICK_DOWN		=	15;
	
	public static final int KEY_LEFT_STICK_LEFT		=	16;
	
	public static final int KEY_LEFT_STICK_RIGHT	=	17;
	
	public static final int KEY_LEFT_STICK			=	18;
	
	public static final int KEY_RIGHT_STICK_UP		=	19;
	
	public static final int KEY_RIGHT_STICK_DOWN	=	20;
	
	public static final int KEY_RIGHT_STICK_LEFT	=	21;
	
	public static final int KEY_RIGHT_STICK_RIGHT	=	22;
	
	public static final int KEY_RIGHT_STICK			=	23;
	
	public static final int KEY_LEFT_UP				=	24;
	
	public static final int KEY_LEFT_DOWN			=	25;
	
	public static final int KEY_RIGHT_UP			=	26;
	
	public static final int KEY_RIGHT_DOWN			=	27;
	
	public static final int KEY_MEDIA_PREVIOUS		=	28;
	
	public static final int KEY_MEDIA_NEXT			=	29;
	
	public static final int KEY_MEDIA_PAUSE			=	30;
	
	
	
	public static final int[] PHYSICSKEY			=	new int[31];
	
	public static final String[] KEYCODESTR			=	new String[31];
	
	static
	{
		PHYSICSKEY[KEY_L2]		=	104;		//
		PHYSICSKEY[KEY_L1]		=	102;		//
		PHYSICSKEY[KEY_R2]		=	105;		//
		PHYSICSKEY[KEY_R1]		=	103;		//
		PHYSICSKEY[KEY_SELECT]	=	109;		//back
		PHYSICSKEY[KEY_START]	=	108;		//
		PHYSICSKEY[KEY_UP]		=	19;			//
		PHYSICSKEY[KEY_DOWN]	=	20;			//
		PHYSICSKEY[KEY_LEFT]	=	21;			//
		PHYSICSKEY[KEY_RIGHT]	=	22;			//
		PHYSICSKEY[KEY_Y]		=	100;			//
		PHYSICSKEY[KEY_X]		=	99;				//
		PHYSICSKEY[KEY_A]		=	96;				//
		PHYSICSKEY[KEY_B]		=	97;				//
		PHYSICSKEY[KEY_LEFT_STICK_UP]		=	51;		//
		PHYSICSKEY[KEY_LEFT_STICK_DOWN]		=	47;		//
		PHYSICSKEY[KEY_LEFT_STICK_LEFT]		=	29;	//		
		PHYSICSKEY[KEY_LEFT_STICK_RIGHT]	=	32;	//
		PHYSICSKEY[KEY_LEFT_STICK]			=	106;	//
		PHYSICSKEY[KEY_RIGHT_STICK_UP]		=	15;	//
		PHYSICSKEY[KEY_RIGHT_STICK_DOWN]	=	12;	//
		PHYSICSKEY[KEY_RIGHT_STICK_LEFT]	=	11;	//
		PHYSICSKEY[KEY_RIGHT_STICK_RIGHT]	=	13;	//
		PHYSICSKEY[KEY_RIGHT_STICK]			=	107;	//
		PHYSICSKEY[KEY_LEFT_UP]			=	302;	//
		PHYSICSKEY[KEY_LEFT_DOWN]		=	303;	//
		PHYSICSKEY[KEY_RIGHT_UP]		=	300;	//
		PHYSICSKEY[KEY_RIGHT_DOWN]		=	301;	//
		PHYSICSKEY[KEY_MEDIA_PREVIOUS]	=	88;	//
		PHYSICSKEY[KEY_MEDIA_NEXT]		=	87;	//
		PHYSICSKEY[KEY_MEDIA_PAUSE]		=	85;	//
 	}
	
	static
	{
		KEYCODESTR[KEY_L2]		=	"KEY_L2";
		KEYCODESTR[KEY_L1]		=	"KEY_L1";
		KEYCODESTR[KEY_R2]		=	"KEY_R2";
		KEYCODESTR[KEY_R1]		=	"KEY_R1";
		KEYCODESTR[KEY_SELECT]	=	"KEY_SELECT";
		KEYCODESTR[KEY_START]	=	"KEY_START";
		KEYCODESTR[KEY_UP]		=	"KEY_UP";
		KEYCODESTR[KEY_DOWN]	=	"KEY_DOWN";
		KEYCODESTR[KEY_LEFT]	=	"KEY_LEFT";
		KEYCODESTR[KEY_RIGHT]	=	"KEY_RIGHT";
		KEYCODESTR[KEY_Y]		=	"KEY_Y";
		KEYCODESTR[KEY_X]		=	"KEY_X";
		KEYCODESTR[KEY_A]		=	"KEY_A";
		KEYCODESTR[KEY_B]		=	"KEY_B";
		KEYCODESTR[KEY_LEFT_STICK_UP]		=	"KEY_LEFT_STICK_UP";
		KEYCODESTR[KEY_LEFT_STICK_DOWN]		=	"KEY_LEFT_STICK_DOWN";
		KEYCODESTR[KEY_LEFT_STICK_LEFT]		=	"KEY_LEFT_STICK_LEFT";
		KEYCODESTR[KEY_LEFT_STICK_RIGHT]	=	"KEY_LEFT_STICK_RIGHT";
		KEYCODESTR[KEY_LEFT_STICK]			=	"KEY_LEFT_STICK";
		KEYCODESTR[KEY_RIGHT_STICK_UP]		=	"KEY_RIGHT_STICK_UP";
		KEYCODESTR[KEY_RIGHT_STICK_DOWN]	=	"KEY_RIGHT_STICK_DOWN";
		KEYCODESTR[KEY_RIGHT_STICK_LEFT]	=	"KEY_RIGHT_STICK_LEFT";
		KEYCODESTR[KEY_RIGHT_STICK_RIGHT]	=	"KEY_RIGHT_STICK_RIGHT";
		KEYCODESTR[KEY_RIGHT_STICK]			=	"KEY_RIGHT_STICK";
		KEYCODESTR[KEY_LEFT_UP]				=	"KEY_LEFT_UP";
		KEYCODESTR[KEY_LEFT_DOWN]			=	"KEY_LEFT_DOWN";
		KEYCODESTR[KEY_RIGHT_UP]			=	"KEY_RIGHT_UP";
		KEYCODESTR[KEY_RIGHT_DOWN]			=	"KEY_RIGHT_DOWN";
		KEYCODESTR[KEY_MEDIA_PREVIOUS]		=	"KEY_MEDIA_PREVIOUS";
		KEYCODESTR[KEY_MEDIA_NEXT]			=	"KEY_MEDIA_NEXT";
		KEYCODESTR[KEY_MEDIA_PAUSE]			=	"KEY_MEDIA_PAUSE";
	}
	
	public static final int STICK_CENTER_X	=	0;
	
	public static final int STICK_CENTER_Y	=	0;
	
	public static int translateKeyCodefromPhysicsHandle(int keyCodePhysics)
	{
		for(int i=0;i<PHYSICSKEY.length;i++)
		{
			if(PHYSICSKEY[i] == keyCodePhysics)
				return i;
		}
		return -1;
 	}
	
	public static int translatePhysicsHandlefromKeyCode(int keyCode)
	{
		if(keyCode >= 0 && keyCode < PHYSICSKEY.length)
			return PHYSICSKEY[keyCode];
		return -1;
	}
	
	public static int translateKeyCodefromKeyCodeStr(String keyCodeStr)
	{
		for(int i=0;i<KEYCODESTR.length;i++)
		{
			if(KEYCODESTR[i].equals(keyCodeStr))
				return i;
		}
 
		return -1;
	}
	
}
