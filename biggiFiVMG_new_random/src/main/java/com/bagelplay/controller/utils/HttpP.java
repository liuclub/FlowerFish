package com.bagelplay.controller.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpP {

	private String TAG = "HttpP";

	private HttpURLConnection con;

	private DataOutputStream dos;

	private DataInputStream dis;

	private String keyValue = "";

	private String url;

	private String response;

	public HttpP(String url) {
		this.url = url;
	}

	public void appendValue(String key, String value) {
		String str = key + "=" + value;
		if (keyValue.equals("")) {
			keyValue += str;
		} else {
			keyValue += "&" + str;
		}
	}

	public void appendValue(String key, int value) {
		appendValue(key, value + "");
	}

	public int post() {
		try {
			con = (HttpURLConnection) new URL(url).openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(10000);
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.v(TAG + "==---------------------", "HttpURLConnection open faile");
			close();
			return -1;
		}

		try {
			con.connect();
		} catch (IOException e1) {
			Log.v(TAG + "==---------------------", "HttpURLConnection connect faile");
			e1.printStackTrace();
			close();
			return -2;
		}

		try {
			dos = new DataOutputStream(con.getOutputStream());
			dos.write(keyValue.getBytes());
			dos.flush();
			dos.close();
			Log.v(TAG + "==---------------------", "post success   " + url);
		} catch (Exception e) {
			e.printStackTrace();
			Log.v(TAG + "==---------------------", "post faile");
			close();
			return -3;
		}

		try {
			response = readResponse();
		} catch (Exception e) {
			e.printStackTrace();
			Log.v(TAG + "==---------------------", "getResponse faile");
			close();
			return -4;
		}

		close();

		return 0;
	}

	public String getResponse() {
		return response;
	}

	private String readResponse() throws Exception {
		dis = new DataInputStream(con.getInputStream());
		byte[] data = new byte[1024];
		int leng = dis.read(data, 0, 1024);
		response = new String(data, 0, leng);
		dis.close();
		return response;
	}

	private void close() {
		try {
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
