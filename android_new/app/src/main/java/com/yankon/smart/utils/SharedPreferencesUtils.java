package com.yankon.smart.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

public class SharedPreferencesUtils {
	Context context;
	private SharedPreferences sp;

	public SharedPreferencesUtils(Context context) {
		this.context=context;
		sp = context.getSharedPreferences("WifiLEDconfig", Context.MODE_PRIVATE);
	}
	public SharedPreferences GetConfig(){
		return sp;
	}
	public void ClearConfig(){
		sp.edit().clear().commit();
	}
	public void putBoolean(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	public void putFloat(String key, float value) {
		sp.edit().putFloat(key, value).commit();
	}

	public void putInt(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	public void putLong(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}

	public void putString(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	public boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public float getFloat(String key) {
		return sp.getFloat(key, 0);
	}

	public int getInt(String key) {
		return sp.getInt(key, 0);
	}

	public long getLong(String key) {
		return sp.getLong(key, 0);
	}

	public String getString(String key) {
		return sp.getString(key, null);
	}

	public static String getString(Preference p,String key,String defValue) {
		String s=null;
		try {
			s = p.getSharedPreferences().getString(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void putString(Preference p,String key,String value) {
		SharedPreferences.Editor sharedata= p.getSharedPreferences().edit();
		sharedata.putString(key, value);
		sharedata.commit();
	}

}
