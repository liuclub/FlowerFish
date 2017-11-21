/*
 * XMLParse.java
 * 
 * Copyright (c) 2012 by DigiLink Software, Inc.
 * 19 Goddard, Irvine, California 92618, U.S.A.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * DigiLink Software, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with DigiLink Software, Inc.
 */

package com.bagelplay.controller.update;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;



import android.util.Xml;

public class XMLParse {
	//update version
	private static final String _VERSION = "Version";
	private static final String _VERSION_CODE = "VersionCode";
	private static final String _VERSION_NAME = "VersionName";
	private static final String _DESCRIPTION = "Description";
	
	private static final String _VALUE = "value";
	private static final String _VALUE_SPACE = "";

	//support tv
	private static final String _SUPPORT_TV = "SupportTv";
	private static final String _TV_ITEM = "TvItem";
	
	public XMLParse() {
	}

	public static void parseVersionXML(InputStream ins)
			throws XmlPullParserException, IOException {
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(ins, "utf-8");

		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_TAG:
				if (_SUPPORT_TV.equals(parser.getName())) {
//					 Log.i(TAG, "version_code.xml document start parse");
				} else if (_VERSION_CODE.equals(parser.getName())) {
					Global.mSerVerCodeStr = parser.getAttributeValue(
							_VALUE_SPACE, _VALUE);
					Global.mSerVerCode = Integer.parseInt(Global.mSerVerCodeStr);
//					Log.i(TAG, "Got version code:" + Global.mSerVerCodeStr);
				} else if (_VERSION_NAME.equals(parser.getName())) {
					Global.mSerVerNameStr = parser.getAttributeValue(
							_VALUE_SPACE, _VALUE);
//					Log.i(TAG, "Got version code:" + Global.mSerVerNameStr);
				} else if (_DESCRIPTION.equals(parser.getName())) {
					Global.mDescription = parser.nextText();
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
	}
	
	public static List<String> parseSupportTVXML(InputStream ins)
			throws XmlPullParserException, IOException {
		
		List<String> tvList = new ArrayList<String>();
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(ins, "utf-8");

		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_TAG:
				if (_VERSION.equals(parser.getName())) {
//					 Log.i(TAG, "version_code.xml document start parse");
				} else if (_TV_ITEM.equals(parser.getName())) {
					String item = parser.nextText();
					tvList.add(item);
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
		
		return tvList;
	}
}
