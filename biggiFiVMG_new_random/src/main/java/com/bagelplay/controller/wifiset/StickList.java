package com.bagelplay.controller.wifiset;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagelplay.controller.BagelPlayActivity;
import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.BagelPlayManager;
import com.bagelplay.controller.wifiset.widget.MessageDialog;
import com.bagelplay.controller.wifiset.widget.WifiListDialog;

public class StickList extends Screen {

	private View sticksV;

	private TextView currentWifiTV;

	private WifManager wm;

	private Welcome wt;

	private View welcomeV;

	public WifiLoading wifiLoading;

	private static WeakReference<StickList> slw;

	private StickFinder sf;

	private StickConnecter sc;

	private List<StickInfo> sticks = new ArrayList<StickInfo>();

	private StickFinder.OnAfterFindListener sfoafl = new StickFinder.OnAfterFindListener() {

		@Override
		public void OnAfterFind(List<StickInfo> sis) {
			wifiLoading.stop();
			sticks = sis;
			finishFindSticks(sticks);
		}
	};

	private StickConnecter.OnAfterConnectListener scoacl = new StickConnecter.OnAfterConnectListener() {

		@Override
		public void OnAfterConnect(boolean result) {
			if (result) {
				wifiLoading.stop();
				wifisetact.finish();
			} else {
				StickList.getInstance().restartFindSticks();
			}
		}
	};

	public StickList(final WifiSetAct wifisetact, boolean needWelcome) {
		super(wifisetact);

		sticksV = View.inflate(wifisetact, R.layout.sticklist, null);
		this.addView(sticksV, new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));

		wifiLoading = (WifiLoading) sticksV.findViewById(R.id.wifiloading);
		wifiLoading.turn();

		wm = WifManager.getInstance();

		wifisetact.setTitle(null);

		currentWifiTV = (TextView) findViewById(R.id.currentWifi);

		setHotTitle();

		startFindSticks();

		if (needWelcome)
			startWelcom();

		slw = new WeakReference(this);
	}

	public static StickList getInstance() {
		if (slw != null)
			return slw.get();
		return null;
	}

	public void startFindSticks() {
		if (sf == null || sf.isFinish()) {
			wifiLoading.start();
			sf = new StickFinder(sticks);
			sf.startFind(sfoafl);
		}
	}
	
	
	
	public void restartFindSticks() {
		if (sf == null || sf.isFinish()) {
			wifiLoading.start();
			sticks= new ArrayList<StickInfo>();
			sf = new StickFinder(sticks);
		
			sf.restartFind(sfoafl);
		}
	}

	public void startFindSticks2() {
		final EditText et = new EditText(wifisetact);
		new AlertDialog.Builder(wifisetact).setTitle("请输入电视端的ip地址")
				.setIcon(android.R.drawable.ic_dialog_info).setView(et)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (sf == null || sf.isFinish()) {
							wifiLoading.start();
							sf = new StickFinder(sticks);
							sf.startFind2(sfoafl, et.getEditableText()
									.toString());
						}
					}

				}).setNegativeButton("取消", null).show();

	}

	private void finishFindSticks(List<StickInfo> sis) {
		wifiLoading.stop();
		Dialog d = null;
		String ssid = wm.getCurrentSsid();
		if (sis.size() == 0) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(wifisetact);
			builder.setTitle(getResources().getString(R.string.no_device));
	 		builder.setMessage(getResources().getString(R.string.current_WiFi_network)
					+ ssid
					+ getResources()
							.getString(
									R.string.please_connect_your_phone_to_the_same_network));
			builder.setCancelable(false);
			builder.setPositiveButton(getResources().getString(R.string.wifi_set_refresh), new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					startFindSticks();
				}
				
			});
			
			builder.setNegativeButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					wifisetact.finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					
					
					
				}
				
			});
	 		Dialog dialog	=	builder.show();
		} else {
			final WifiListDialog dialog = new WifiListDialog(wifisetact);
			dialog.setTit(getResources().getString(
					R.string.choose_the_network_connection));
			dialog.setMess(getResources().getString(
					R.string.current_WiFi_network)
					+ ssid
					+ getResources()
							.getString(
									R.string.please_connect_your_phone_to_the_same_network1));
			dialog.setStickInfo(sis);
			dialog.show();

			dialog.setOnWDClickListener(new WifiListDialog.OnWDClickListener() {

				@Override
				public void OnWDClick(StickInfo si) {
					startConnect(si);
					dialog.dismiss();
				}
			});

			dialog.setOnReFreshClickListener(new WifiListDialog.OnReFreshClickListener() {

				@Override
				public void OnReFreshClick() {
					startFindSticks();
					dialog.dismiss();
				}
			});

		}
	}

	private void startConnect(StickInfo si) {
		if (sc == null || sc.isFinish()) {
			wifiLoading.start();
			sc = new StickConnecter(wifisetact, si);
			sc.startConnect(scoacl);
		}
	}

	public void finishConnect(final boolean success, final int result) {
		if (sc != null)
			sc.finishConnect(success, result);
	}

	private void startWelcom() {
		if (welcomeV != null)
			return;
		welcomeV = View.inflate(wifisetact, R.layout.welcome, null);
		wifisetact.setFullView(welcomeV);
		wifisetact.handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				wifisetact.removeFullView(welcomeV);
				welcomeV = null;
			}
		}, 3000);
	}

	private void setHotTitle() {
		String ssid = wm.getCurrentSsid();
		String cssid = wifisetact.getString(R.string.currentwifi, ssid);
		currentWifiTV.setText(cssid);
	}

}
