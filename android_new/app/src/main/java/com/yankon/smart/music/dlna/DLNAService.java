package com.yankon.smart.music.dlna;

import org.cybergarage.upnp.ControlPoint;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.yankon.smart.App;
import com.yankon.smart.activities.AudioActivity;
import com.yankon.smart.model.Event;
import com.yankon.smart.music.Deiviceutis;
import com.yankon.smart.utils.Global;

import de.greenrobot.event.EventBus;

/**
 * The service to search the DLNA Device in background all the time.
 * 
 * @author CharonChui
 * 
 */
public class DLNAService extends Service {
	private static final String TAG = "DLNAService";
	private ControlPoint mControlPoint;
	private SearchThread mSearchThread;
	private WifiStateReceiver mWifiStateReceiver;
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	public static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
	public static final String WIFI_CHANGE_ACTION = "android.net.wifi.STATE_CHANGE";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unInit();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startThread();
		return START_REDELIVER_INTENT;
	}

	private void init() {
		mControlPoint = new ControlPoint();
		DMCApplication.getInstance().setControlPoint(mControlPoint);
		mSearchThread = new SearchThread(mControlPoint);
		registerWifiStateReceiver();
	}

	private void unInit() {
		stopThread();
		unregisterWifiStateReceiver();
	}

	/**
	 * Make the thread start to search devices.
	 */
	private void startThread() {
		if (mSearchThread != null) {
			Log.e(TAG, "thread is not null");
			mSearchThread.setSearcTimes(0);
		} else {
			Log.e(TAG, "thread is null, create a new thread");
			mSearchThread = new SearchThread(mControlPoint);
		}

		if (mSearchThread.isAlive()) {
			Log.e(TAG, "thread is alive");
			mSearchThread.awake();
		} else {
			Log.e(TAG, "start the thread");
			mSearchThread.start();
		}
	}

	private void stopThread() {
		if (mSearchThread != null) {
			mSearchThread.stopThread();
			mControlPoint.stop();
			mSearchThread = null;
			mControlPoint = null;
			Log.e(TAG, "stop dlna service");
		}
	}

	private void registerWifiStateReceiver() {
		if (mWifiStateReceiver == null) {
			mWifiStateReceiver = new WifiStateReceiver();
			registerReceiver(mWifiStateReceiver, new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION));
			registerReceiver(mWifiStateReceiver, new IntentFilter(CONNECTIVITY_CHANGE_ACTION));
			registerReceiver(mWifiStateReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
			registerReceiver(mWifiStateReceiver, new IntentFilter(WIFI_CHANGE_ACTION));
		}
	}

	private void unregisterWifiStateReceiver() {
		if (mWifiStateReceiver != null) {
			unregisterReceiver(mWifiStateReceiver);
			mWifiStateReceiver = null;
		}
	}

	private class WifiStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context c, Intent intent) {
			Bundle bundle = intent.getExtras();
			int statusInt = bundle.getInt("wifi_state");
			switch (statusInt) {
			case WifiManager.WIFI_STATE_UNKNOWN:
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				break;
			case WifiManager.WIFI_STATE_ENABLED:
				startThread();
				break;
			case WifiManager.WIFI_STATE_DISABLING:
//				AudioActivity.postdevies = 0;
				EventBus.getDefault().post(new Event.PostDeviceEvent(0));
				break;
			case WifiManager.WIFI_STATE_DISABLED:
				break;
			default:
				break;
			}
		}
	}

}