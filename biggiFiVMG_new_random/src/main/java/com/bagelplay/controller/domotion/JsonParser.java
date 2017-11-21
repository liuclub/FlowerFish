package com.bagelplay.controller.domotion;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;

import com.bagelplay.controller.domotion.customize.CButton;
import com.bagelplay.controller.domotion.customize.CShaker;
import com.bagelplay.controller.domotion.customize.CHideButton;
import com.bagelplay.controller.domotion.customize.CSlider;
import com.bagelplay.controller.domotion.customize.CStick;
import com.bagelplay.controller.domotion.customize.CView;
import com.bagelplay.controller.utils.Config;

public class JsonParser {

	private JSONObject jo;

	private int type;

	private int orientation;

	private Bitmap background;

	private boolean isSimulateTouch;

	private List<CView> components = new ArrayList<CView>();

	private Context context;

	private ZipParser zp;

	private int[] TVScreen = new int[2];

	private int[] phoneScreen = new int[2];

	private RelativeLayout rl;

	private int width, height;

	public JsonParser(Context context, ZipParser zp, RelativeLayout rl) {
		this.context = context;
		this.zp = zp;
		this.rl = rl;
		String str = zp.getJson();

		try {
			jo = new JSONObject(str);
			parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		zp = null;
	}

	public List<CView> getComponents() {
		return components;
	}

	public int getType() {
		return type;
	}

	public int getOrientation() {
		return orientation;
	}

	public Bitmap getBackground() {
		return background;
	}

	public boolean isSimulateTouch() {
		return isSimulateTouch;
	}

	private String getStringFromFile(String filePath) throws Exception {
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(filePath);
		byte[] buf = new byte[(int) file.length()];
		int readed = 0;
		while (true) {
			int needRead = fis.read(buf, readed, buf.length - readed);
			readed += needRead;
			if (readed == buf.length)
				break;
		}
		String str = new String(buf);
		return str;
	}

	private void getRealWidthHeight() {
		if (orientation == 0) {
			if ((Config.widthPixels - Config.heightPixels) > 0) {
				width = Config.widthPixels;
				height = Config.heightPixels;
			} else {
				width = Config.heightPixels;
				height = Config.widthPixels;
			}
		} else {
			if ((Config.widthPixels - Config.heightPixels) < 0) {
				width = Config.widthPixels;
				height = Config.heightPixels;
			} else {
				width = Config.heightPixels;
				height = Config.widthPixels;
			}
		}
	}

	private void parse() throws Exception {

		type = jo.getInt("type");

		orientation = jo.getInt("orientation");

		getRealWidthHeight();
		background = jo.opt("background") != null ? zp.getPic(jo
				.getString("background")) : null;

		isSimulateTouch = jo.opt("simulateTouch") != null ? jo
				.getBoolean("simulateTouch") : false;
		if (jo.opt("TVScreen") != null) {
			TVScreen[0] = jo.getJSONObject("TVScreen").getInt("width");
			TVScreen[1] = jo.getJSONObject("TVScreen").getInt("height");
		}

		if (jo.opt("phoneScreen") != null) {
			phoneScreen[0] = jo.getJSONObject("phoneScreen").getInt("width");
			phoneScreen[1] = jo.getJSONObject("phoneScreen").getInt("height");
		}

		if (jo.opt("component") == null)
			return;

		JSONArray ja = jo.getJSONArray("component");
		for (int i = 0; i < ja.length(); i++) {

			parseComponent(ja.getJSONObject(i));
		}
	}

	private void parseComponent(JSONObject jo) throws Exception {
		CView cv = null;
		String type = jo.getString("type");

		if ("button".equals(type)) {
			cv = parseButton(jo);
		} else if ("stick".equals(type)) {
			cv = parseStick(jo);
		} else if ("slider".equals(type)) {

			cv = parseSlider(jo);
		} else if ("shaker".equals(type)) {
			cv = parseShaker(jo);
		}
		if (cv != null) {

			cv.arrange();
			cv.setType(type);
			components.add(cv);
		}

	}

	private CButton parseButton(JSONObject jo) throws Exception {

		CButton cb = new CButton(context, rl);
		int up = jo.getInt("up");
		int down = jo.getInt("down");
		int value = jo.getInt("value");
		cb.setUp(up);
		cb.setDown(down);
		cb.setValue(value);
		JSONObject phoneParameterJO = jo.getJSONObject("phoneParameter");
		cb.setPhoneXY(phoneParameterJO.getInt("x") * width / phoneScreen[0],
				phoneParameterJO.getInt("y") * height / phoneScreen[1]);
		if (isSimulateTouch) {
			JSONObject TVParameterJO = jo.getJSONObject("TVParameter");
			int x = TVParameterJO.getInt("x");
			int y = TVParameterJO.getInt("y");
			x = x * Config.tvWidthPixels / TVScreen[0];
			y = y * Config.tvHeightPixels / TVScreen[1];
			cb.setTVXY(x, y);
		}
		JSONObject picJO = jo.getJSONObject("pic");
		cb.setPic_normal(zp.getPic(picJO.getString("normal")));
		cb.setPic_press(zp.getPic(picJO.getString("press")));
		return cb;
	}

	/*
	 * private CButtonDIY parseButtonDIY(JSONObject jo) throws Exception {
	 * CButtonDIY cb = new CButtonDIY(context); int up = jo.getInt("up"); int
	 * down = jo.getInt("down"); int value = jo.getInt("value"); cb.setUp(up);
	 * cb.setDown(down); cb.setValue(value); cb.setPic( jo.opt("pic_normal") !=
	 * null ? zp.getPic(jo .getString("pic_normal")) : null, jo.opt("pic_press")
	 * != null ? zp.getPic(jo .getString("pic_press")) : null); if
	 * (isSimulateTouch) { int stX = jo.getInt("stX"); int stY =
	 * jo.getInt("stY"); cb.setSimulateX(stX); cb.setSimulateY(stY); }
	 * 
	 * return cb; }
	 */

	private CStick parseStick(JSONObject jo) throws Exception {

		CStick cs = new CStick(context, rl);
		cs.setId(jo.getInt("id"));

		JSONObject phoneParameterJO = jo.getJSONObject("phoneParameter");
		cs.setPhoneParameter(phoneParameterJO.getInt("radius") * height
				/ phoneScreen[1], phoneParameterJO.getInt("x") * width
				/ phoneScreen[0], phoneParameterJO.getInt("y") * height
				/ phoneScreen[1]);
		JSONObject TVParameterJO = jo.getJSONObject("TVParameter");
		int tvRadius = TVParameterJO.getInt("radius");
		int tvX = TVParameterJO.getInt("x");
		int tvY = TVParameterJO.getInt("y");
		float rateX = (Config.tvWidthPixels + 0f) / TVScreen[0];
		float rateY = (Config.tvHeightPixels + 0f) / TVScreen[1];
		float rate = rateX < rateY ? rateX : rateY;
		tvRadius = (int) (tvRadius * rate);
		tvX = (int) (tvX * rateX);
		tvY = (int) (tvY * rateY);
		cs.setTVParameter(tvRadius, tvX, tvY);

		JSONObject rangeJO = jo.getJSONObject("range");

		cs.setRange(rangeJO.getInt("x") * width / phoneScreen[0],
				rangeJO.getInt("y") * height / phoneScreen[1],
				rangeJO.getInt("width") * width / phoneScreen[0],
				rangeJO.getInt("height") * height / phoneScreen[1]);

		JSONObject picJO = jo.getJSONObject("pic");
		cs.setBottomPic(zp.getPic(picJO.getString("bottom")));
		cs.setStickPic(zp.getPic(picJO.getString("stick")));
		return cs;

	}

	private CSlider parseSlider(JSONObject jo) throws Exception {

		CSlider cs = new CSlider(context, rl);
		cs.setSlide(jo.getInt("maxSpeed"), jo.getInt("maxDistance"));

		JSONObject phoneParameterJO = jo.getJSONObject("phoneParameter");

		cs.setPhoneXY(phoneParameterJO.getInt("x") * width / phoneScreen[0],
				phoneParameterJO.getInt("y") * height / phoneScreen[1]);
		cs.setPhoneWH(
				phoneParameterJO.getInt("width") * width / phoneScreen[0],
				phoneParameterJO.getInt("height") * height / phoneScreen[1]);

		JSONArray gestureJOA = jo.getJSONArray("gesture");
		for (int i = 0; i < gestureJOA.length(); i++) {
			JSONObject gjo = gestureJOA.getJSONObject(i);
			CHideButton csb = new CHideButton(context, rl);
			int x = gjo.getInt("TVBtnX");
			int y = gjo.getInt("TVBtnY");
			x = x * Config.tvWidthPixels / TVScreen[0];
			y = y * Config.tvHeightPixels / TVScreen[1];
			csb.setTVXY(x, y);
			String action = gjo.getString("action");
			if ("slide".equals(action)) {
				csb.setAction(CSlider.ACTION_SLIDE);
				String direct = gjo.getString("direct");
				if ("up".equals(direct)) {
					csb.setDirect(CSlider.SLIDE_UP);
				} else if ("down".equals(direct)) {
					csb.setDirect(CSlider.SLIDE_DOWN);
				} else if ("left".equals(direct)) {
					csb.setDirect(CSlider.SLIDE_LEFT);
				} else if ("right".equals(direct)) {
					csb.setDirect(CSlider.SLIDE_RIGHT);
				}
			} else if ("click".equals(action)) {
				csb.setAction(CSlider.ACTION_CLICK);
			} else if ("long_touch".equals(action)) {
				csb.setAction(CSlider.ACTION_LONG_TOUCH);
			}

			cs.putSlideButton(csb);
			csb.arrange();
			components.add(csb);
		}

		return cs;
	}

	private CShaker parseShaker(JSONObject jo) throws Exception {

		int x = jo.getInt("TVBtnX");
		int y = jo.getInt("TVBtnY");
		CShaker cs = new CShaker(context, rl);
		x = x * Config.tvWidthPixels / TVScreen[0];
		y = y * Config.tvHeightPixels / TVScreen[1];

		CHideButton chb = new CHideButton(context, rl);
		chb.setTVXY(x, y);

		cs.setHideButton(chb);
		chb.arrange();
		components.add(chb);

		return cs;

	}

}
