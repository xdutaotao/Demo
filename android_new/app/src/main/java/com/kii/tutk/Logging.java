package com.kii.tutk;

import android.util.Log;

/**
 * Created by Evan on 15/12/23.
 */
public class Logging {
    public static void d(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String TAG, String msg) {
        Log.e(TAG, msg);
    }

    public static void w(String TAG, String msg) {
        Log.w(TAG, msg);
    }
}
