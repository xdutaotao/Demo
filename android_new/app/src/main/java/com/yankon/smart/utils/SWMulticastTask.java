package com.yankon.smart.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yankon.smart.SWDefineConst;

public class SWMulticastTask extends AsyncTask<String, Void, String> {
	static int count = 1;
	public byte[] passphrase;
	public char[] mac;
	public int passLen;
	public int passCRC;
	public Handler handler;
	Context mContext;
	private int state, substate;
	
	
	
	
	public void resetStateMachine() {
		state = 0;
		substate = 0;
	}
	public void initBase(Context mContext){
		this.mContext = mContext;
	}
	private void xmitRaw(int u, int m, int l) {
		MulticastSocket ms = null;
		InetAddress sessAddr;
		DatagramPacket dp;
		byte[] data = new byte[2];
		data = "a".getBytes();
		u = u & 0x7f; /* multicast's uppermost byte has only 7 chr */
		try {
			//Log.d("jack-" + count, "225." + u + "." + m + "." + l);
			count++;
			sessAddr = InetAddress
					.getByName("225." + u + "." + m + "." + l);
			ms = new MulticastSocket(9059);
			dp = new DatagramPacket(data, data.length, sessAddr, 5500);
			ms.send(dp);
			//Log.d("UdpHelper", "------------------------225." + u + "." + m + "." + l);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
			LogUtils.e("jack", "Exiting 5");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(ms != null)
				ms.close();
		}
		
	}
	private void xmitState0(int substate) {
		int i, j, k;

		k = mac[2 * substate];
		j = mac[2 * substate + 1];
		i = substate;

		xmitRaw(i, k, j);
	}

	private void xmitState1() {
		int u = (0x01 << 5) | (0x0);
		xmitRaw(u, passLen, passLen);
	}

	private void xmitState2(int substate, int len) {
		LogUtils.d("jack", "xmitState2 " + substate);
		int u = substate | (0x2 << 5);
		int l = (0xff & passphrase[2 * substate]);
		int m;
		if (len == 2)
			m = (0xff & passphrase[2 * substate + 1]);
		else
			m = 0;
		xmitRaw(u, l, m);
	}

	private void xmitState3(int substate) {
		int i, j, k;
		k = (int) (passCRC >> ((2 * substate + 0) * 8)) & 0xff;
		j = (int) (passCRC >> ((2 * substate + 1) * 8)) & 0xff;
		i = substate | (0x3 << 5);

		xmitRaw(i, j, k);
	}

	private void stateMachine() {
		switch (state) {
		case 0:
			if (substate == 3) {
				state = 1;
				substate = 0;
			} else {
				xmitState0(substate);
				substate++;
			}
			break;
		case 1:
			xmitState1();
			state = 2;
			substate = 0;
			break;
		case 2:
			xmitState2(substate, 2);
			substate++;
			if (passLen % 2 == 1) {
				if (substate * 2 == passLen - 1) {
					xmitState2(substate, 1);
					state = 3;
					substate = 0;
				}
			} else {
				if (substate * 2 == passLen) {
					state = 3;
					substate = 0;
				}
			}
			break;
		case 3:
			xmitState3(substate);
			substate++;
			if (substate == 2) {
				substate = 0;
				state = 0;
				count = 1;
			}
			break;
		default:
			LogUtils.e("jack", "I shouldn't be here");
		}
	}

	protected String doInBackground(String... params) {
		WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		WifiLock mWifiLock = wm.createWifiLock("LockWifiSet");
		MulticastLock mcastLock = wm.createMulticastLock("mcastlocktest");
		mcastLock.acquire();
		for (int i = 0; i < passLen; i++) {
			LogUtils.d("jack", " " + (0xff & passphrase[i]));
		}

		int i = 0;

		while (true) {
			if (state == 0 && substate == 0)
				i++;

			if (i % 5 == 0) {
				Message msg = handler.obtainMessage();
				msg.what = 43;
				msg.arg1 = i;
				handler.sendMessage(msg);
			}

			/* Stop trying after doing 50 iterations. Let user retry. */
			if (i >= SWDefineConst.MulticastCount)
				break;

			if (isCancelled())
				break;
            stateMachine();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				 e.printStackTrace();
				break;
			}
		}

		mcastLock.release();

		if (i >= SWDefineConst.MulticastCount) {
			Message msg = handler.obtainMessage();
			msg.what = 42;
			handler.sendMessage(msg);
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Void... values) {
	}

}
