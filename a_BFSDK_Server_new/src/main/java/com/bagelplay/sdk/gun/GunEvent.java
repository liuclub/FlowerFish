package com.bagelplay.sdk.gun;

public class GunEvent {


	///trigger down
	public static final int mKEY_TRIGGER =           0x0001;
	///joystick forward
	public static final int mKEY_UP =                0x0002;
	///joystick backward
	public static final int mKEY_DOWN =              0x0004;
	///joystick left
	public static final int  mKEY_LEFT =              0x0008;
	///sight on
	public static final int  mKEY_SIGHT =             0x0020;
	///joystick right
	public static final int mKEY_RIGHT =             0x0040;
	///joystick middle press down
	public static final int  mKEY_MIDDLE =            0x0080;
	/// throw grenade
	public static final int mKEY_GRENADE =           0x0010;
	///reload grenade Luncher
	public static final int mKEY_LUNCHER_RELOAD =    0x0800;


	///gun appear
	public static final int mGUN_APPEAR	=	100;
	///gun lost
	public static final int  mGUN_LOST		=	101;
	///gun reload box magazine
	public static final int  mGUN_RELOAD	=	102;
	///gun invalid
	public static final int mGUN_INVALID	=	103;
	///gun motion similay with mGUN_RELOAD,but with mirrored direction
	public static final int  mGUN_RELOAD_UP	=	104;



	///gun is in correct using range
	public static final int  mGUN_IN_RANGE=  0x0000;
	///gun is too close
	public static final int  mGUN_TOO_CLOSE= 0x0001;
	///gun is too far away
	public static final int  mGUN_TOO_FAR=   0x0002;
	///gun is to the left bound
	public static final int  mGUN_TOO_LEFT=  0x0010;
	///gun is to the right bound
	public static final int  mGUN_TOO_RIGHT= 0x0020;
	///gun is too high
	public static final int  mGUN_TOO_HIGH=  0x0100;
	///gun is too low
	public static final int  mGUN_TOO_LOW=   0x0200;













	///pixel coord x
	public double pointX;
	///pixel coord y
	public double pointY;
	///light gun 3d position x in mm
	public double positiontX;
	///light gun 3d position y in mm
	public double positiontY;
	///light gun 3d position z in mm
	public double positiontZ;
	///light gun pointing dir angle in rad
	public double positiontAngle;

	///flag for data validity： data above is valid if dataFlag & 0x02 is true
	public int dataFlag;

	/**
	 * @brief    Keys sent from light gun, key is valid if dataFlag > 0
	 * @par example:
	 * @code
	 *          if(Key & mKEY_TRIGGER)  {
	 *              // process key tragger down
	 *           };
	 * @endcode
	 * @see keyInfo
	 */
	public int key;

	/**
	 *@brief    event list stroes EventInfo
	 *@see EventInfo
	 */
	public int[] events	=	new int[16] ;
	/**
	 *@brief    gun postion code
	 *@par example:
	 *@code
	 *          // high/low check is prior to close/far and letf/right check;
	 *          if(gunPos & mGUN_TOO_HIGH) {
	 *              //gun is too high
	 *          }
	 *          else if(gunPos & mGUN_TOO_LOW){
	 *              //gun is too low
	 *          }
	 *          else{
	 *              if(gunPos & mGUN_TOO_CLOSE){//gun is too close to the tv};
	 *              if(gunPos & mGUN_TOO_LEFT){// gun is too close to left bound};
	 *              ...
	 *          }
	 *@endcode
	 *@see  GunPosInfo
	 */
	public int gunPos;

	public String toString()
	{
		return "pointX:" + pointX + " pointY:" + pointY + " positiontX:" + positiontX + " positiontY:" + positiontY + " positiontZ:" + positiontZ + " positiontAngle:" + positiontAngle + " dataFlag:" + dataFlag + " key:" + key + " gunPos:" + gunPos;
	}

}
