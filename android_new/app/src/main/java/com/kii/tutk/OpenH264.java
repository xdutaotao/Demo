package com.kii.tutk;

import android.util.Log;

/**
 * Created by Evan on 15/12/20.
 */
public class OpenH264 {
    public static final String TAG = "OpenH264";
    public int iWidth;
    public int iHeight;
    public byte[] pData0;
    public byte[] pData1;
    public byte[] pData2;

    public long decoderRef = 0;

    public OpenH264() {
        super();
        decoderRef = initDecoder();
    }

    public native long initDecoder();
    public native int decodeFrame(long pDecoder, long time, byte[] buf, int length);
    public native void uninitialize(long pDecoder);

    static {
        try {
            System.loadLibrary ("openh264");
            System.loadLibrary ("stlport_shared");
            System.loadLibrary ("welsdecdemo");
            Log.v(TAG, "Load libwelsdec successful");
        } catch (Exception e) {
            Log.e (TAG, "Failed to load welsdec" + e.getMessage());
        }
    }
}
