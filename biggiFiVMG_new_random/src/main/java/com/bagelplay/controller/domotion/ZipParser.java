package com.bagelplay.controller.domotion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bagelplay.controller.utils.Log;

public class ZipParser {

	private Hashtable<String, Bitmap> ht = new Hashtable<String, Bitmap>();

	private ZipInputStream zin;

	private String json;

	private ZipFile zip;

	public boolean fileIsExists(String filePath) {
		try {
			File f = new File(filePath);
			if (!f.exists()) {

				return false;
			}

		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	public ZipParser(Context context, String fileName) {

		

		String addstr = DoMotionViewManager.ZIP_DIR + fileName;// 里面的包名换成你自己的

		if (!fileIsExists(addstr)) {
			try {

				InputStream assetsDB = context.getAssets().open(fileName);// strln是assets文件夹下的文件名

				OutputStream dbOut = new FileOutputStream(addstr);// strout是你要保存的文件名

				byte[] buffer = new byte[1024];
				int length;
				while ((length = assetsDB.read(buffer)) > 0) {
					dbOut.write(buffer, 0, length);
				}

				dbOut.flush();
				dbOut.close();
				assetsDB.close();
			} catch (Exception e) {
			}
		}

		try {

			zin = new ZipInputStream(new FileInputStream(addstr));
			zip = new ZipFile(addstr);
			parse();
			zin.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ZipParser(String filePath) {

		try {

			zin = new ZipInputStream(new FileInputStream(filePath));
			zip = new ZipFile(filePath);
			parse();
			zin.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bitmap getPic(String fileName) {
		return ht.get(fileName);
	}

	public String getJson() {
		return json;
	}

	private void parse() throws Exception {
		while (true) {
			ZipEntry ze = zin.getNextEntry();
			if (ze == null)
				return;
			InputStream is = zip.getInputStream(ze);
			String fileName = ze.getName();
			if ("view.json".equals(fileName)) {
				json = inStreamToStr(is);

			
			} else {
				Bitmap pic = BitmapFactory.decodeStream(is);
				ht.put(fileName, pic);
			}
			is.close();
		}
	}

	private String inStreamToStr(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		while (true) {
			int len = is.read(buf);
			if (len == -1)
				break;
			baos.write(buf, 0, len);
		}
		byte[] data = baos.toByteArray();
		baos.close();
		return new String(data);

	}
}
