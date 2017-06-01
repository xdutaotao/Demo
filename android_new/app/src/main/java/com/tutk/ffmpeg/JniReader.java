

package com.tutk.ffmpeg;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JniReader {
	
	private static final String TAG = JniReader.class.getCanonicalName();
	
	private byte[] value = new byte[16];
	private int position;

	public JniReader(String url, int flags) {
		Log.d(TAG, String.format("Reading: %s", url));
		try {
			byte[] key = "dupadupadupadupa".getBytes("UTF-8");
			
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(key);
			System.arraycopy(m.digest(), 0, value, 0, 16);

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		position = 0;
	}
	
	public int read(byte[] buffer) {
		int end = position + buffer.length;
		if (end >= value.length)
			end = value.length;

		int length = end - position;
		System.arraycopy(value, position, buffer, 0, length);
		position += length;
		
		return length;
	}
	
	public int write(byte[] buffer) {
		return 0;
	}
	
	public int check(int mask) {
		return 0;
	}
	
	public long seek(long pos, int whence) {
		return -1;
	}
}
