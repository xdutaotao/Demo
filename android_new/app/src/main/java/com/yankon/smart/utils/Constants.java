package com.yankon.smart.utils;

import android.graphics.Color;

/**
 * Created by tian on 14/11/30:下午5:46.
 */
public class Constants {

    public static int BROADCAST_IP = 0xFFFFFFFF;

    public static final String INTENT_LOGGED_IN = "com.yankon.smart.intent.log_in";

    public static final String INTENT_LOGGED_OUT = "com.yankon.smart.intent.log_out";

//    public static final int DEFAULT_PORT = 5015;
    public static final int DEFAULT_PORT = 8755;

    public static final int DEFAULT_COLOR = Color.BLUE;

    public static final int DEFAULT_CT = 70;

    public static final int DEFAULT_MODE = 0;

    public static final int DEFAULT_BRIGHTNESS = 80;

    public static final int MIN_CT = 15;

    public static final int MIN_BRIGHTNESS = 15;

    public static final int MODE_CT = 1;

    public static final int MODE_COLOR = 0;

    public static final String ACTION_UPDATED = "com.yankon.smart.light.updated";
    public static final String SEND_TYPE      = "send_type";

    public static final int UI_OPERATION_DELAY_TIME = 80;

    public static final int TRANS_CHECK_PERIOD = 100;

    public static final int SCAN_LIGHTS_NORMAL = 0;
    public static final int SCAN_LIGHTS_ADDLIGHTS = 1;
    public static final int SCAN_LIGHTS_BACKGROUND = 2;
    public static final int SCAN_LIGHTS_QUICK = 3;

    public static final int SCAN_SWITCHS_ADDSWITCHS = 1;

    public static final int[] SCAN_PERIOD = new int[]{10 * 1000, 3 * 1000, 5 * 60 * 1000,300};

    public static final int SCAN_ACTIVE_CHECK_PERIOD = 5000;


    //    public static final byte[] SEARCH_LIGHTS_CMD = new byte[]{00, 00, 00, 00, 0x1e, 00, 01, 01, 00, 00, 00, 0x0a, 00, 00, 00, 00, 0x0a, 01, 00, 00, 00, 0x0a, 0x02, 00, 00, 00, 0x0a, 03, 00, 00, 00, 00, 03, 00, 00, 00};
    /* 向IP为 255 255 255 255 广播，想得到MAC地址、 IP地址、 软件版本、 model型号、灯开关、灯颜色、 灯亮度、 灯色调、定时器开关*/
    public static final byte[] SEARCH_LIGHTS_CMD = new byte[]{00, 00, 00, 02, 00, 00, 00, 00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x4B, 00,
            01, 01, 00, 00, 00, 01, 02, 00, 00, 00, 00, 05, 00, 00, 00, 00, 07, 00, 00, 00,
            0x0a, 00, 00, 00, 00, 0x0a, 01, 00, 00, 00, 0x0a, 02, 00, 00, 00, 0x0a, 03, 00, 00, 00,
            0x0b, 00, 00, 00, 00, 0x0b, 01, 00, 00, 00, 0x0b, 02, 00, 00, 00, 0x0b, 03, 00, 00, 00,
            0x0E, 00, 00, 00, 00, 0x0E, 01, 00, 00, 00,
            00, 03, 00, 00, 00};

    /* 向IP为 255 255 255 255 广播，想得到MAC地址、 IP地址、 软件版本、 model型号、灯开关、灯颜色、 灯亮度、 灯色调、定时器开关*/
    public static final byte[] SEARCH_SWITCHS_CMD = new byte[]{00, 00, 00, 02, 00, 00, 00, 00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x2D, 00,
            01, 01, 00, 00, 00, 01, 02, 00, 00, 00, 00, 05, 00, 00, 00, 00, 07, 00, 00, 00,
            0x0C, 00, 00, 00, 00, 0x0C, 01, 00, 00, 00, 0x0C, 0x02, 00, 00, 00, 0x0C, 03, 00, 00, 00, 00, 03, 00, 00, 00};

    public static final byte[] RESET_ALL_WIFI = new byte[]{00, 00, 00, 02, 00, 00, 00, 00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 06, 00,
            02, 04, 01, 01, 00, 00};

}
