package com.yankon.smart.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.kii.cloud.storage.Kii;
import com.yankon.smart.DaemonHandler;
import com.yankon.smart.model.Light;
import com.yankon.smart.model.Switchs;
import com.yankon.smart.model.TransData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Evan on 14/11/23.
 */
public class Global {

    public static int myIP = 0;
    public static byte[] myIPBytes = null;
    public static ConcurrentHashMap<String, Light> gLightsMacMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Long> gLightsMacTimeMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Switchs> gSwitchsMacMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> transLightsMap = new ConcurrentHashMap<>();
    public static List<String> transSwitchsList = new ArrayList();

    public static boolean isWifiConnected = false;

    public static ConcurrentHashMap<String, TransData> gCommandTransactions = new ConcurrentHashMap<>();

    public static DaemonHandler gDaemonHandler;
    public static int gScanLightsType = 0;
    public static int gScanSwitchsType = 0;
    public static int isQuickSeach = 1;
    public static long oldTime = 1;


    public static boolean hasFullSynced = false;
    public static boolean kiiInited = false;

    public static int X_DISTANCE = 300;
    public static int Y_DISTANCE = 400;

    public static void init(Context context) {
        String site = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("KII_SITE", null);
        if (!TextUtils.isEmpty(site)) {
            kiiInited = true;
            if (site.equals("US")) {
                Kii.initialize("5318608a", "52659c75c002a3264a0896fc32979c4d", Kii.Site.US);
            } else if (site.equals("JP")) {
//                Kii.initialize("06e806e2", "31afdcdfd72ade025559176a40a20875", Kii.Site.JP);
                Kii.initialize("09184164", "af805347b32bcee13af834fc1821e9e4", Kii.Site.JP);
            } else if (site.equals("SG")) {
                Kii.initialize("ab467af6", "53984bbbbdb983be22a0b0b37e40f749", Kii.Site.SG);
            } else if (site.equals("CN3")) {
                Kii.initialize("d93defd9", "4e96ba956153ef6c68c512ed0900f787", "https://api-cn3.kii.com/api");
            } else {
                kiiInited = false;
            }
        }
    }
}
