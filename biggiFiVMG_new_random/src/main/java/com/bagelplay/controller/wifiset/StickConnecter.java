package com.bagelplay.controller.wifiset;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;

import com.bagelplay.controller.BagelPlayActivity;
import com.bagelplay.controller.BagelPlayHeartService;
import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.BagelPlayManager;
import com.bagelplay.controller.utils.BagelPlayUtil;
import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.CmdProtocol;
import com.bagelplay.controller.utils.Config;
import com.bagelplay.controller.wifiset.StickFinder.OnAfterFindListener;
import com.bagelplay.controller.wifiset.widget.MessageDialog;

public class StickConnecter extends Thread {

	private Handler handler = new Handler();

	private OnAfterConnectListener oacl;

	private boolean finish;

	private StickInfo si;

	private Activity activity;

	public StickConnecter(Activity activity, StickInfo si) {
		this.activity = activity;
		this.si = si;
	}

	public void startConnect(OnAfterConnectListener oacl) {
		this.oacl = oacl;
		finish = false;
		start();
	}

	public void run() {
		connectStick();
	}

	public boolean isFinish() {
		return finish;
	}

	private void connectStick() {
		Config.vma_ip = si.ip;
		Config.vma_cmd_port = si.VMA_CMD_PORT;
		BagelPlayVmaStub bvfs = BagelPlayVmaStub.getInstance();
		boolean result = false;
		if (bvfs.connect(Config.vma_ip, Config.vma_cmd_port)) {
			BagelPlayManager.getInstance().start();
			bvfs.sendLoginData();

			result = true;

			handler.postDelayed(new Runnable() {
				public void run() {
					if (!BagelPlayManager.getInstance().Loginsuccess) {

						/*
						 * MessageDialog md = new MessageDialog(activity);
						 * md.setTit(activity.getResources().getString(R.string.
						 * failconnect_service));
						 * md.setMess(activity.getResources
						 * ().getString(R.string.connect_to_other_devices));
						 * md.show(); md.setOnDismissListener(new
						 * OnDismissListener(){
						 * 
						 * @Override public void onDismiss(DialogInterface
						 * dialog) { finish = true;
						 * 
						 * BagelPlayVmaStub.getInstance().close();
						 * StickList.getInstance().wifiLoading.stop();
						 * StickList.getInstance().startFindSticks(); }
						 * 
						 * });
						 */

						AlertDialog.Builder builder = new AlertDialog.Builder(
								activity);
						builder.setTitle(activity.getResources().getString(
								R.string.failconnect_service));
						builder.setMessage(activity.getResources().getString(
								R.string.connect_to_other_devices));
						builder.setCancelable(false);
						builder.setPositiveButton(activity.getResources()
								.getString(R.string.ok),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										finish = true;

										BagelPlayVmaStub.getInstance().close();
										StickList.getInstance().wifiLoading
												.stop();
//										StickList.getInstance()
//												.startFindSticks();

										if (oacl != null) {

											BagelPlayVmaStub.getInstance()
													.close();

											handler.post(new Runnable() {
												public void run() {
													oacl.OnAfterConnect(false);
													finish = true;
												}
											});

										}

									}

								});

						builder.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								
								activity.finish();
								android.os.Process.killProcess(android.os.Process.myPid());
							}
							
						});
						Dialog dialog = builder.show();

					}

				}
			}, 3000);

		}

		else {
			handler.post(new Runnable() {
				public void run() {

				

					AlertDialog.Builder builder = new AlertDialog.Builder(
							activity);
					builder.setTitle(activity.getResources().getString(
							R.string.failconnect_service));
					builder.setMessage(activity.getResources().getString(
							R.string.connect_to_other_devices));
					builder.setCancelable(false);
					builder.setPositiveButton(activity.getResources()
							.getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									finish = true;

									BagelPlayVmaStub.getInstance().close();
									StickList.getInstance().wifiLoading.stop();
//									StickList.getInstance().startFindSticks();

									if (oacl != null) {

										BagelPlayVmaStub.getInstance().close();

										handler.post(new Runnable() {
											public void run() {
												oacl.OnAfterConnect(false);
												finish = true;
											}
										});

									}

								}

							});
					
					
					builder.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
							activity.finish();
							android.os.Process.killProcess(android.os.Process.myPid());
						}
						
					});

					Dialog dialog = builder.show();

				}
			});

		}

	}

	public void finishConnect(final boolean success, final int result) {
		handler.post(new Runnable() {
			public void run() {
				oacl.OnAfterConnect(success);
				finish = true;
				if (!success) {
					switch (result) {
					case CmdProtocol.ERRNO_ACCOUNT_EXISTED:
						BagelPlayUtil.errorInfoShow(activity,
								R.string.account_existed_error);
						break;

					case CmdProtocol.ERRNO_CODE_WRONG:
						BagelPlayUtil.errorInfoShow(activity,
								R.string.code_error);
						break;

					case CmdProtocol.ERRNO_CODE_NOT_SET:
						BagelPlayUtil.errorInfoShow(activity,
								R.string.code_set_error);
						break;

					case CmdProtocol.ERRNO_ACCOUNT_HAVE_LOGGED:
						BagelPlayUtil.errorInfoShow(activity,
								R.string.max_user_error);
						break;

					case CmdProtocol.ERRNO_MAX_USERS:
						BagelPlayUtil.errorInfoShow(activity,
								R.string.max_user_error);
						break;

					case -100:
						BagelPlayUtil.errorInfoShow(activity,
								R.string.disconnect_error);
						break;

					default:
						BagelPlayUtil.errorInfoShow(activity,
								R.string.unknown_error);
						break;
					}
				}
			}
		});
	}

	public interface OnAfterConnectListener {
		public void OnAfterConnect(boolean result);
	}
}
