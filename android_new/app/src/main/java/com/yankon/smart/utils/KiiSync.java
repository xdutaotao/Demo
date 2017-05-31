package com.yankon.smart.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiServerCodeEntry;
import com.kii.cloud.storage.KiiServerCodeEntryArgument;
import com.kii.cloud.storage.KiiServerCodeExecResult;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.yankon.smart.App;
import com.yankon.smart.model.Command;
import com.yankon.smart.providers.YanKonProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Evan on 14/12/20.
 */
public class KiiSync {

    private static final String LOG_TAG = "KiiSync";

    private static boolean isSyncing = false;


    public static String fireLamp(ArrayList<Pair<String, String>> lightsArr, boolean verify,
            Command cmd) {
        String result = null;
        if (!KiiUser.isLoggedIn()) {
            return result;
        }
        JSONObject resultSuccJSON = new JSONObject();

        while (lightsArr.size() > 0) {
            int count = 0;
            JSONObject lights = new JSONObject();
            for (int i = lightsArr.size() - 1; i >= 0; i--) {
                Pair<String, String> light = lightsArr.remove(i);
                try {
                    lights.put(light.first, light.second);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                count++;
                if (count >= 20) {
                    break;
                }
            }
            KiiServerCodeEntry entry = Kii.serverCodeEntry("fireLamp");

            try {
                JSONObject rawArg = new JSONObject();
                JSONObject action = new JSONObject();
                if (!verify) {
                    if (cmd.type == Command.CommandType.CommandTypeResetWifi) {
                        return result;
                    } else if (cmd.type == Command.CommandType.CommandTypeColor) {
                        long colorL = cmd.value & 0x0000000000ffffffL;
                        action.put("color", colorL);
                    } else if (cmd.type == Command.CommandType.CommandTypeAll) {
                        action.put("state",cmd.info.state?1:0);
                        action.put("brightness",cmd.info.brightness);
                        if (cmd.info.mode == Constants.MODE_COLOR) {
                            long colorL = cmd.info.color & 0x0000000000ffffffL;
                            action.put("color", colorL);
                            LogUtils.i("fireLamp:", colorL + "");
                        } else {
                            action.put("CT", cmd.info.CT);
                        }
                    } else {
                        action.put(cmd.type.getColumnName(), cmd.value);
                    }

                } else {
                    rawArg.put("verify", true);
                }
                rawArg.put("lights", lights);
                rawArg.put("action", action);
                rawArg.put("type", "command");
                LogUtils.d(LOG_TAG, "fireLamp:" + rawArg.toString());
                KiiServerCodeEntryArgument arg = KiiServerCodeEntryArgument
                        .newArgument(rawArg);

                // Execute the Server Code
                KiiServerCodeExecResult res = entry.execute(arg);

                // Parse the result.
                JSONObject returned = res.getReturnedValue();
                String sub_result = returned.getString("returnedValue");
                JSONObject root = new JSONObject(sub_result);
                JSONObject succ = root.getJSONObject("lights");
                Iterator iter = succ.keys();
                while (iter.hasNext()) {
                    String mac = (String) iter.next();
                    int value = succ.optInt(mac);
                    resultSuccJSON.put(mac, value);
                }
                LogUtils.d(LOG_TAG, "FireLamp result:" + sub_result);
            } catch (Exception e) {
                LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
            }
        }
        JSONObject root = new JSONObject();
        try {
            root.put("lights", resultSuccJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = root.toString();
        LogUtils.d(LOG_TAG, "FireLamp final result:" + result);
        return result;
    }

    public static String upgradeLamp(JSONObject lights, String url, String version) {
        String result = null;
        if (!KiiUser.isLoggedIn()) {
            return result;
        }
        KiiServerCodeEntry entry = Kii.serverCodeEntry("upgradeLamp");

        try {
            JSONObject rawArg = new JSONObject();
            JSONObject action = new JSONObject();
            action.put("firmwareUrl", url);
            action.put("versionName", version);
            rawArg.put("lights", lights);
            rawArg.put("action", action);
            rawArg.put("type", "firmwareUpgrade");
            LogUtils.d(LOG_TAG, "fireLamp:" + rawArg.toString());
            KiiServerCodeEntryArgument arg = KiiServerCodeEntryArgument
                    .newArgument(rawArg);

            // Execute the Server Code
            KiiServerCodeExecResult res = entry.execute(arg);

            // Parse the result.
            JSONObject returned = res.getReturnedValue();
            result = returned.getString("returnedValue");
            LogUtils.d(LOG_TAG, "upgradeLamp result:" + result);
        } catch (Exception e) {
            LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
        }
        return result;
    }

    public static String migrateLamp(String thingID, String adminPwd) {
        String result = null;
        if (!KiiUser.isLoggedIn()) {
            return result;
        }
        KiiServerCodeEntry entry = Kii.serverCodeEntry("migrateLamp");

        try {
            JSONObject rawArg = new JSONObject();
            rawArg.put("thingID", thingID);
            rawArg.put("pwd",adminPwd);
            LogUtils.d(LOG_TAG, "migrateLamp:" + rawArg.toString());
            KiiServerCodeEntryArgument arg = KiiServerCodeEntryArgument
                    .newArgument(rawArg);

            // Execute the Server Code
            KiiServerCodeExecResult res = entry.execute(arg);

            // Parse the result.
            JSONObject returned = res.getReturnedValue();
            result = returned.getString("returnedValue");
            LogUtils.d(LOG_TAG, "migrateLamp result:" + result);
        } catch (Exception e) {
            LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
        }
        return result;
    }


    public static String changePwd(String[] mac, String currPwd, String adminPwd,
            String remotePwd) {
        JSONObject succList = new JSONObject();
        String result = null;
        if (!KiiUser.isLoggedIn()) {
            return result;
        }
        ArrayList<String> macs = new ArrayList<>();
        for (String s : mac) {
            macs.add(s);
        }
        while (macs.size() > 0) {
            KiiServerCodeEntry entry = Kii.serverCodeEntry("batchChangeThingPwd");
            try {
                JSONObject rawArg = new JSONObject();
                JSONArray things = new JSONArray();
                int count = 0;
                for (int i = macs.size() - 1; i >= 0; i--) {
                    String s = macs.remove(i);
                    things.put(s);
                    count++;
                    if (count >= 20) {
                        break;
                    }
                }

                rawArg.put("thingID", things);
                rawArg.put("adminPwd", currPwd);
                if (!TextUtils.isEmpty(adminPwd)) {
                    rawArg.put("newAdminPwd", adminPwd);
                }
                if (!TextUtils.isEmpty(remotePwd)) {
                    rawArg.put("newRemotePwd", remotePwd);
                }
                LogUtils.d(LOG_TAG, "doChangeThingPwd:" + rawArg.toString());
                KiiServerCodeEntryArgument arg = KiiServerCodeEntryArgument
                        .newArgument(rawArg);

                // Execute the Server Code
                KiiServerCodeExecResult res = entry.execute(arg);

                // Parse the result.
                JSONObject returned = res.getReturnedValue();
                String aRes = returned.getString("returnedValue");
                JSONObject root = new JSONObject(aRes);
                JSONObject succ = root.getJSONObject("success");

                Iterator<String> it = succ.keys();
                while (it.hasNext()) {//遍历JSONObject
                    String amac = it.next();
                    boolean single_res = succ.getBoolean(amac);
                    succList.put(amac, single_res);
                }
                LogUtils.d(LOG_TAG, "doChangeThingPwd result:" + aRes);
            } catch (Exception ignored) {

            }
        }
        JSONObject root = new JSONObject();
        try {
            root.put("success", succList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = root.toString();
        return result;
    }

    public static String getCursorString(Cursor cursor, int col) {
        String s = cursor.getString(col);
        if (s == null) {
            return "";
        }
        return s;
    }

    public static void asyncFullSync() {
        new Thread() {
            @Override
            public void run() {
                KiiSync.sync(true);
            }
        }.start();
    }

    public static void sync(boolean forceFullSync) {
        if (isSyncing) {
            return;
        }
        isSyncing = true;
        if (forceFullSync || !Global.hasFullSynced) {
            downloadLights();
            LogUtils.i("TAG", "downloadLights");
            downloadGroups();
            LogUtils.i("TAG", "downloadGroups");
            downloadScenes();
            LogUtils.i("TAG", "downloadScenes");
//            downloadColors();
            downloadSchedules();
            LogUtils.i("TAG", "downloadSchedules");
            downloadLightThings();
            LogUtils.i("TAG", "downloadLightThings");
            Global.hasFullSynced = true;
        }
        uploadLights();
        LogUtils.i("TAG", "uploadLights");
        uploadGroups();
        LogUtils.i("TAG", "uploadGroups");
        uploadScenes();
        LogUtils.i("TAG", "uploadScenes");
//        uploadColors();
        uploadSchedules();
        LogUtils.i("TAG", "uploadSchedules");

        isSyncing = false;
    }

    private static void uploadSchedules() {
        KiiBucket bucket = getScheduleBucket();
        if (bucket == null) {
            return;
        }
        Context context = App.getApp();
        Cursor cursor = context.getContentResolver().query(YanKonProvider.URI_SCHEDULE, null,
                null, null, null);
        TimeZone tz = TimeZone.getDefault();
        int tzOffset = tz.getRawOffset() / 1000 / 3600;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                boolean synced = cursor.getInt(cursor.getColumnIndex("synced")) > 0;
                if (synced) {
                    continue;
                }
                int light_id = cursor.getInt(cursor.getColumnIndex("_id"));
                LogUtils.i("schedule", "_id = " + getCursorString(cursor, cursor.getColumnIndex("_id")));
                KiiObject scheduleObject;
                String objectID = cursor.getString(cursor.getColumnIndex("objectID"));
                scheduleObject = bucket.object(objectID);
                scheduleObject.set("name", getCursorString(cursor, cursor.getColumnIndex("name")));
                LogUtils.i("schedule", "name = " + getCursorString(cursor, cursor.getColumnIndex("name")));
                scheduleObject.set("enabled", cursor.getInt(cursor.getColumnIndex("enabled")));
                LogUtils.i("schedule", "enabled = " + getCursorString(cursor, cursor.getColumnIndex("enabled")));
                scheduleObject.set("color", cursor.getInt(cursor.getColumnIndex("color")));
                scheduleObject
                        .set("brightness", cursor.getInt(cursor.getColumnIndex("brightness")));
                LogUtils.i("schedule", "brightness = " + getCursorString(cursor, cursor.getColumnIndex("brightness")));
                scheduleObject.set("CT", cursor.getInt(cursor.getColumnIndex("CT")));
                LogUtils.i("schedule", "CT = " + getCursorString(cursor, cursor.getColumnIndex("CT")));
                scheduleObject.set("state", cursor.getInt(cursor.getColumnIndex("state")));
                LogUtils.i("schedule", "state = " + getCursorString(cursor, cursor.getColumnIndex("state")));
                scheduleObject.set("mode", cursor.getInt(cursor.getColumnIndex("mode")));
                LogUtils.i("schedule", "mode = " + getCursorString(cursor, cursor.getColumnIndex("mode")));
                scheduleObject.set("closeAll", cursor.getInt(cursor.getColumnIndex("closeAll")));
                LogUtils.i("schedule", "closeAll = " + getCursorString(cursor, cursor.getColumnIndex("closeAll")));
                scheduleObject.set("time", cursor.getInt(cursor.getColumnIndex("time")));
                scheduleObject.set("scene_id",
                        getCursorString(cursor, cursor.getColumnIndex("scene_id")));
                LogUtils.i("schedule", "scene_id = " + getCursorString(cursor, cursor.getColumnIndex("scene_id")));
                scheduleObject.set("light_id",
                        getCursorString(cursor, cursor.getColumnIndex("light_id")));
                scheduleObject.set("group_id",
                        getCursorString(cursor, cursor.getColumnIndex("group_id")));
                scheduleObject.set("deleted", cursor.getInt(cursor.getColumnIndex("deleted")));
                scheduleObject.set("timezone", tzOffset);
                String repeat = cursor.getString(cursor.getColumnIndex("repeat"));
                JSONArray repeatArr = null;
                try {
                    repeatArr = new JSONArray(repeat);
                } catch (Exception e) {
                    repeatArr = new JSONArray();
                    for (int i = 0; i < 7; i++) {
                        repeatArr.put(false);
                    }
                }
                scheduleObject.set("repeat", repeatArr);
                try {
                    scheduleObject.saveAllFields(true);
                    scheduleObject.refresh();
                    int version = scheduleObject.getInt("_version");
                    ContentValues values = new ContentValues();
                    values.put("synced", true);
                    values.put("ver", version);
                    context.getContentResolver()
                            .update(YanKonProvider.URI_SCHEDULE, values, "_id=" + light_id, null);

                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, Log.getStackTraceString(e));  
                }
            }

            cursor.close();
        }
    }

    private static void downloadSchedules() {
        KiiBucket bucket = getScheduleBucket();
        if (bucket == null) {
            return;
        }
        try {
            KiiQueryResult<KiiObject> result = bucket.query(null);
            List<KiiObject> objList = result.getResult();
            saveRemoteScheduleRecords(objList);
            while (result.hasNext()) {
                result = result.getNextQueryResult();
                objList = result.getResult();
                saveRemoteScheduleRecords(objList);
            }
        } catch (Exception e) {
            LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
        }
    }

    private static void saveRemoteScheduleRecords(List<KiiObject> objList) {
        for (KiiObject object : objList) {
            try {
                saveRemoteScheduleRecord(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveRemoteScheduleRecord(KiiObject object) {
        ContentValues values = new ContentValues();
        String objectId = object.getString("_id");
        values.put("objectID", objectId);
        values.put("name", object.getString("name"));
        values.put("enabled", object.getInt("enabled"));
        values.put("scene_id", object.getString("scene_id"));
        values.put("light_id", object.getString("light_id"));
        values.put("group_id", object.getString("group_id"));
        values.put("color", object.getInt("color"));
        values.put("brightness", object.getInt("brightness"));
        values.put("CT", object.getInt("CT"));
        if (object.has("mode")) {
            values.put("mode", object.getInt("mode"));
        }
        values.put("state", object.getInt("state"));
        values.put("closeAll", object.getInt("closeAll", 0));
        values.put("time", object.getInt("time"));
        values.put("repeat", object.getJsonArray("repeat").toString());
        values.put("deleted", object.getInt("deleted"));
        values.put("synced", true);
        Uri uri = YanKonProvider.URI_SCHEDULE;
        saveRemoteObject(values, uri, object);
    }

    private static void uploadColors() {
        KiiBucket bucket = getColorBucket();
        if (bucket == null) {
            return;
        }
        final Context context = App.getApp();
        Cursor cursor = context.getContentResolver().query(YanKonProvider.URI_COLORS, null,
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                boolean synced = cursor.getInt(cursor.getColumnIndex("synced")) > 0;
                if (synced) {
                    continue;
                }
                int light_id = cursor.getInt(cursor.getColumnIndex("_id"));
                KiiObject colorObject;
                String objectID = cursor.getString(cursor.getColumnIndex("objectID"));
                if (TextUtils.isEmpty(objectID)) {
                    continue;
                }
                colorObject = bucket.object(objectID);
                colorObject.set("name", cursor.getString(cursor.getColumnIndex("name")));
                colorObject.set("value", cursor.getInt(cursor.getColumnIndex("value")));
                colorObject.set("objectID", objectID);
                colorObject.set("objID", objectID);
                colorObject.set("deleted", cursor.getInt(cursor.getColumnIndex("deleted")));
                try {
                    colorObject.saveAllFields(true);
                    colorObject.refresh();
                    int version = colorObject.getInt("_version");
                    ContentValues values = new ContentValues();
                    values.put("synced", true);
                    values.put("ver", version);
                    context.getContentResolver()
                            .update(YanKonProvider.URI_COLORS, values, "_id=" + light_id, null);
                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
                }
            }
            cursor.close();
        }
    }

    private static void downloadColors() {
        KiiBucket bucket = getColorBucket();
        if (bucket == null) {
            return;
        }
        try {
            KiiQueryResult<KiiObject> result = bucket.query(null);
            List<KiiObject> objList = result.getResult();
            saveRemoteColorRecords(objList);
            while (result.hasNext()) {
                result = result.getNextQueryResult();
                objList = result.getResult();
                saveRemoteColorRecords(objList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveRemoteColorRecords(List<KiiObject> objects) {
        for (KiiObject object : objects) {
            saveRemoteColorRecord(object);
        }
    }

    private static void saveRemoteColorRecord(KiiObject object) {
        ContentValues values = new ContentValues();
        String objectId = object.getString("_id");
        values.put("objectID", objectId);
        values.put("name", object.getString("name"));
        values.put("value", object.getString("value"));
        values.put("synced", true);
        values.put("deleted", object.getInt("deleted"));
        Uri uri = YanKonProvider.URI_COLORS;
        saveRemoteObject(values, uri, object);
    }

    private static final int NO_ACTION = 0;

    private static final int INSERTED = 1;

    private static final int UPDATED = 2;

    private static final int NEW_INSERTED = 3;

    private static Pair<String, Integer> saveRemoteObject(ContentValues values, Uri uri,
            KiiObject object) {
        int action = NO_ACTION;
        String objectId = object.getString("_id");
        String id = null;
        int objVersion = object.getInt("_version");
        values.put("ver", objVersion);
        Cursor cursor = App.getApp().getContentResolver()
                .query(uri, null, "objectID=?",
                        new String[]{objectId}, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                boolean synced = cursor.getInt(cursor.getColumnIndex("synced")) == 1;
                int version = cursor.getInt(cursor.getColumnIndex("ver"));
                if (synced || Settings.isServerWin()) {
                    if (version != objVersion) {
                        App.getApp().getContentResolver()
                                .update(uri, values, "objectID=?",
                                        new String[]{objectId});
                        action = UPDATED;
                        id = objectId;
                    }
                } else if (Settings.isBothWin()) {
                    String newId = UUID.randomUUID().toString();
                    values.put("objectID", newId);
                    App.getApp().getContentResolver().insert(uri, values);
                    action = NEW_INSERTED;
                    id = newId;
                }
            } else {
                //the remote record does not exist in local storage, save it
                App.getApp().getContentResolver().insert(uri, values);
                action = INSERTED;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return new Pair<>(id, action);
    }

    private static void uploadScenes() {
        KiiBucket bucket = getSceneBucket();
        if (bucket == null) {
            return;
        }
        final Context context = App.getApp();
        Cursor cursor = context.getContentResolver().query(YanKonProvider.URI_SCENES, null,
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                boolean synced = cursor.getInt(cursor.getColumnIndex("synced")) > 0;
                if (synced) {
                    continue;
                }
                String objectID = cursor.getString(cursor.getColumnIndex("objectID"));
                int sceneId = cursor.getInt(cursor.getColumnIndex("_id"));
                JSONArray childItems = new JSONArray();
                Cursor childCursor = context.getContentResolver()
                        .query(YanKonProvider.URI_SCENES_DETAIL, null, "scene_id=" + sceneId, null,
                                null);
                if (childCursor != null) {
                    while (childCursor.moveToNext()) {
                        JSONObject childObject = new JSONObject();
                        try {
                            childObject.put("light_id",
                                    DataHelper.getLightMacById(childCursor
                                            .getInt(childCursor.getColumnIndex("light_id"))));
                            childObject.put("group_id",
                                    DataHelper.getGroupById(childCursor
                                            .getInt(childCursor.getColumnIndex("group_id"))));
                            childObject.put("state",
                                    childCursor.getInt(childCursor.getColumnIndex("state")));
                            childObject.put("color",
                                    childCursor.getInt(childCursor.getColumnIndex("color")));
                            childObject.put("brightness",
                                    childCursor.getInt(childCursor.getColumnIndex("brightness")));
                            childObject.put("CT",
                                    childCursor.getInt(childCursor.getColumnIndex("CT")));
                            childObject.put("mode",
                                    childCursor.getInt(childCursor.getColumnIndex("mode")));
                            childItems.put(childObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    childCursor.close();
                }
                KiiObject sceneObject;
                sceneObject = bucket.object(objectID);
                sceneObject.set("objectID", objectID);
                sceneObject.set("objID", objectID);
                sceneObject.set("name", cursor.getString(cursor.getColumnIndex("name")));
                sceneObject
                        .set("created_time", cursor.getLong(cursor.getColumnIndex("created_time")));
                sceneObject.set("deleted", cursor.getInt(cursor.getColumnIndex("deleted")));
                sceneObject.set("scene_detail", childItems);
                try {
                    sceneObject.saveAllFields(true);
                    sceneObject.refresh();
                    int version = sceneObject.getInt("_version");
                    ContentValues values = new ContentValues();
                    values.put("synced", true);
                    values.put("ver", version);
                    context.getContentResolver()
                            .update(YanKonProvider.URI_SCENES, values, "_id=" + sceneId,
                                    null);
                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
                }
            }
            cursor.close();
        }
    }

    private static void uploadGroups() {
        KiiBucket bucket = getGroupBucket();
        if (bucket == null) {
            return;
        }
        final Context context = App.getApp();
        Cursor cursor = context.getContentResolver().query(YanKonProvider.URI_LIGHT_GROUPS, null,
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                boolean synced = cursor.getInt(cursor.getColumnIndex("synced")) > 0;
                if (synced) {
                    continue;
                }
                int group_id = cursor.getInt(cursor.getColumnIndex("_id"));
                String objectID = cursor.getString(cursor.getColumnIndex("objectID"));
                JSONObject childLights = new JSONObject();
                Cursor childCursor = context.getContentResolver()
                        .query(YanKonProvider.URI_LIGHT_GROUP_REL, new String[]{"MAC"},
                                "group_id=" + group_id, null, null);
                if (childCursor != null) {
                    while (childCursor.moveToNext()) {
                        try {
                            childLights.put(childCursor.getString(0), true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    childCursor.close();
                }
                KiiObject groupObj;
                groupObj = bucket.object(objectID);
                groupObj.set("objectID", objectID);
                groupObj.set("objID", objectID);
                groupObj.set("name", cursor.getString(cursor.getColumnIndex("name")));
                groupObj.set("brightness", cursor.getInt(cursor.getColumnIndex("brightness")));
                groupObj.set("CT", cursor.getInt(cursor.getColumnIndex("CT")));
                groupObj.set("color", cursor.getInt(cursor.getColumnIndex("color")));
                groupObj.set("state", cursor.getInt(cursor.getColumnIndex("state")));
                groupObj.set("created_time", cursor.getLong(cursor.getColumnIndex("created_time")));
                groupObj.set("lights", childLights);
                groupObj.set("deleted", cursor.getInt(cursor.getColumnIndex("deleted")));
                try {
                    groupObj.saveAllFields(true);
                    groupObj.refresh();
                    int version = groupObj.getInt("_version");
                    ContentValues values = new ContentValues();
                    values.put("synced", true);
                    values.put("ver", version);
                    context.getContentResolver()
                            .update(YanKonProvider.URI_LIGHT_GROUPS, values, "_id=" + group_id,
                                    null);
                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
                }
            }
            cursor.close();
        }
    }

    private static void uploadLights() {
        KiiBucket bucket = getLightBucket();
        if (bucket == null) {
            return;
        }
        final Context context = App.getApp();
        Cursor cursor = context.getContentResolver().query(YanKonProvider.URI_LIGHTS, null,
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                boolean synced = cursor.getInt(cursor.getColumnIndex("synced")) > 0;
                if (synced) {
                    continue;
                }
                int light_id = cursor.getInt(cursor.getColumnIndex("_id"));
                KiiObject lightObj;
                String mac = cursor.getString(cursor.getColumnIndex("MAC"));
                lightObj = bucket.object(mac);
                lightObj.set("name", cursor.getString(cursor.getColumnIndex("name")));
                lightObj.set("model", getCursorString(cursor, cursor.getColumnIndex("model")));
                lightObj.set("remote_pwd",
                        getCursorString(cursor, cursor.getColumnIndex("remote_pwd")));
                lightObj.set("admin_pwd",
                        getCursorString(cursor, cursor.getColumnIndex("admin_pwd")));
                lightObj.set("MAC", mac);
//                lightObj.set("brightness", cursor.getInt(cursor.getColumnIndex("brightness")));
//                lightObj.set("CT", cursor.getInt(cursor.getColumnIndex("CT")));
//                lightObj.set("color", cursor.getInt(cursor.getColumnIndex("color")));
//                lightObj.set("state", cursor.getInt(cursor.getColumnIndex("state")) > 0);
                lightObj.set("owned_time", cursor.getLong(cursor.getColumnIndex("owned_time")));
                lightObj.set("deleted", cursor.getInt(cursor.getColumnIndex("deleted")));
                lightObj.set("firmware_version",
                        getCursorString(cursor, cursor.getColumnIndex("firmware_version")));
                try {
                    lightObj.saveAllFields(true);
                    lightObj.refresh();
                    int version = lightObj.getInt("_version");
                    ContentValues values = new ContentValues();
                    values.put("synced", true);
                    values.put("ver", version);
                    context.getContentResolver()
                            .update(YanKonProvider.URI_LIGHTS, values, "_id=" + light_id, null);
                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
                }
            }
            cursor.close();
        }
    }

    private static void downloadLights() {
        KiiBucket bucket = getLightBucket();
        if (bucket == null) {
            return;
        }
        try {
            KiiQueryResult<KiiObject> result = bucket.query(null);
            List<KiiObject> objList = result.getResult();
            saveRemoteLightRecords(objList);
            while (result.hasNext()) {
                result = result.getNextQueryResult();
                objList = result.getResult();
                saveRemoteLightRecords(objList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadGroups() {
        KiiBucket bucket = getGroupBucket();
        if (bucket == null) {
            return;
        }
        try {
            KiiQueryResult<KiiObject> result = bucket.query(null);
            List<KiiObject> objList = result.getResult();
            saveRemoteGroupRecords(objList);
            while (result.hasNext()) {
                result = result.getNextQueryResult();
                objList = result.getResult();
                saveRemoteGroupRecords(objList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadScenes() {
        KiiBucket bucket = getSceneBucket();
        if (bucket == null) {
            return;
        }
        try {
            KiiQueryResult<KiiObject> result = bucket.query(null);
            List<KiiObject> objList = result.getResult();
            saveRemoteSceneRecords(objList);
            while (result.hasNext()) {
                result = result.getNextQueryResult();
                objList = result.getResult();
                saveRemoteSceneRecords(objList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveRemoteSceneRecords(List<KiiObject> objects) {
        for (KiiObject object : objects) {
            try {
                saveRemoteSceneRecord(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveRemoteGroupRecords(List<KiiObject> objects) {
        for (KiiObject object : objects) {
            try {
                saveRemoteGroupRecord(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveRemoteSceneRecord(KiiObject object) {
        ContentValues values = new ContentValues();
        String objectId = object.getString("_id");
        Uri uri = YanKonProvider.URI_SCENES;
        values.put("objectID", objectId);
        values.put("name", object.getString("name"));
        values.put("deleted", object.getInt("deleted"));
        Pair<String, Integer> pair = saveRemoteObject(values, uri, object);
        String newId = pair.first;
        int action = pair.second;
        processSceneDetail(object, newId, action);
    }

    private static void processSceneDetail(KiiObject object, String newId, int action) {
        if (action == NO_ACTION)
            return;

        String objectId = newId == null ? object.getString("_id") : newId;
        int scene_id = -1;
        Cursor c = App.getApp().getContentResolver()
                .query(YanKonProvider.URI_SCENES, new String[]{"_id"}, "objectID=?",
                        new String[]{objectId}, null);
        if (c != null) {
            if (c.moveToFirst()) {
                scene_id = c.getInt(0);
            }
            c.close();
        }
        if (scene_id == -1) {
            return;
        }
        /*??   修改scenes  颜色 ct 不一样 ??*/
        if (Settings.isServerWin() && action == UPDATED) {
                App.getApp().getContentResolver()
                        .delete(YanKonProvider.URI_SCENES_DETAIL, "scene_id=" + scene_id, null);
        }

        JSONArray array = object.getJsonArray("scene_detail");
        if (array != null && array.length() != 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject detailObject = array.optJSONObject(i);
                if (detailObject == null) {
                    continue;
                }
                try {
                    ContentValues values = new ContentValues();
                    values.put("state", detailObject.getInt("state"));
                    values.put("color", detailObject.getInt("color"));
                    values.put("brightness", detailObject.getInt("brightness"));
                    values.put("CT", detailObject.getInt("CT"));
                    if (detailObject.has("mode")) {
                        values.put("mode", detailObject.getInt("mode"));
                    }
                    values.put("scene_id", scene_id);
                    values.put("light_id",
                            DataHelper.getLightIdByMac(detailObject.optString("light_id", null)));
                    values.put("group_id",
                            DataHelper.getGroupIdByObjId(detailObject.optString("group_id", null)));
                    App.getApp().getContentResolver()
                            .insert(YanKonProvider.URI_SCENES_DETAIL, values);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
//        ContentValues cv = new ContentValues(1);
//        cv.put("synced", 1);
//        App.getApp().getContentResolver()
//                .update(YanKonProvider.URI_SCENES, cv, "objectID=?", new String[]{objectId});
    }

    private static void saveRemoteGroupRecord(KiiObject object) {
        ContentValues values = new ContentValues();
        String objectId = object.getString("_id");
        values.put("objectID", objectId);
        values.put("name", object.getString("name"));
        values.put("state", object.getInt("state"));
        values.put("color", object.getInt("color"));
        values.put("brightness", object.getInt("brightness"));
        values.put("CT", object.getInt("CT"));
        values.put("deleted", object.getInt("deleted"));
        values.put("synced", true);
        Uri uri = YanKonProvider.URI_LIGHT_GROUPS;
        Pair<String, Integer> pair = saveRemoteObject(values, uri, object);
        processRel(object, pair.first, pair.second);
    }

    private static void processRel(KiiObject object, String newId, int action) {
        String groupId = String
                .valueOf(newId == null ? DataHelper.getGroupIdByObjId(object.getString("objectID"))
                        : newId);

        if (Settings.isServerWin() && action == UPDATED) {
            App.getApp().getContentResolver()
                    .delete(YanKonProvider.URI_LIGHT_GROUP_REL, "group_id=?",
                            new String[]{groupId});
        }
        JSONObject array = object.getJSONObject("lights");
        if (array != null) {
            Iterator<String> it = array.keys();
            while (it.hasNext()) {
                String mac = it.next();
                if (!TextUtils.isEmpty(mac)) {
                    int lightId = DataHelper.getLightIdByMac(mac);
                    ContentValues cv = new ContentValues(2);
                    cv.put("light_id", lightId);
                    cv.put("group_id", groupId);
                    App.getApp().getContentResolver()
                            .insert(YanKonProvider.URI_LIGHT_GROUP_REL, cv);
                }
            }
        }
    }


    private static void saveRemoteLightRecords(List<KiiObject> objects) {
        for (KiiObject object : objects) {
            try {
                saveRemoteLightRecord(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveRemoteLightRecord(KiiObject object) {
        ContentValues values = new ContentValues();
        String mac = object.getString("MAC");
        values.put("MAC", mac);
        values.put("name", object.getString("name"));
        values.put("model", object.getString("model"));
        values.put("remote_pwd", object.getString("remote_pwd", ""));
        values.put("admin_pwd", object.getString("admin_pwd", ""));
        values.put("owned_time", object.getLong("owned_time"));
        values.put("deleted", object.getInt("deleted"));
        values.put("firmware_version", object.getString("firmware_version", ""));
        values.put("synced", true);
        int objVersion = object.getInt("_version");
        values.put("ver", objVersion);
        Uri uri = YanKonProvider.URI_LIGHTS;
        Cursor cursor = App.getApp().getContentResolver()
                .query(uri, null, "MAC=?",
                        new String[]{mac}, null);
        if (cursor != null && cursor.moveToFirst()) {
            boolean synced = cursor.getInt(cursor.getColumnIndex("synced")) == 1;
            int version = cursor.getInt(cursor.getColumnIndex("ver"));

            if (version != objVersion) {
                //if (synced || Settings.isServerWin()) {
                if (synced) {
                    App.getApp().getContentResolver()
                            .update(uri, values, "MAC=?",
                                    new String[]{mac});
                    LogUtils.i("TAG", "downloadLights db");
                }
            }
        } else {
            //the remote record does not exist in local storage, save it
            App.getApp().getContentResolver().insert(uri, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private static KiiBucket getLightBucket() {
        return getBucket("lights");
    }

    private static KiiBucket getGroupBucket() {
        return getBucket("light_groups");
    }

    private static KiiBucket getSceneBucket() {
        return getBucket("light_scenes");
    }

    private static KiiBucket getColorBucket() {
        return getBucket("colors");
    }

    private static KiiBucket getScheduleBucket() {
        return getBucket("schedules");
    }

    private static KiiBucket getBucket(String bucketName) {
        KiiUser kiiUser = KiiUser.getCurrentUser();
        if (kiiUser == null) {
            return null;
        }
        return kiiUser.bucket(bucketName);
    }

    public static void getModels() {
        KiiBucket bucket = Kii.bucket("models");
        try {
            KiiQueryResult<KiiObject> result = bucket.query(null);
            List<KiiObject> objList = result.getResult();
            saveModels(objList);
            while (result.hasNext()) {
                result = result.getNextQueryResult();
                objList = result.getResult();
                saveModels(objList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveModels(List<KiiObject> objects) {
        List<ContentValues> valuesList = new ArrayList<>(objects.size());
        for (KiiObject object : objects) {
            ContentValues values = new ContentValues();
            values.put("model", object.getString("model"));
            values.put("pic", object.getString("pic", ""));
            values.put("des", object.getString("des", ""));
            values.put("firmware_version", object.getString("firmware_version", ""));
            valuesList.add(values);
        }
        if (!valuesList.isEmpty()) {
            App.getApp().getContentResolver().bulkInsert(YanKonProvider.URI_MODELS,
                    valuesList.toArray(new ContentValues[valuesList.size()]));
        }
    }

    public static boolean isSyncing() {
        return isSyncing;
    }

    public static void downloadLightThings() {
        KiiBucket bucket = Kii.bucket("globalThingInfo");
        ArrayList<String> macList = new ArrayList<>();
        Cursor c = App.getApp().getContentResolver()
                .query(YanKonProvider.URI_LIGHTS, new String[]{"MAC"}, "deleted=0", null, null);
        if (c != null) {
            while (c.moveToNext()) {
                macList.add(getCursorString(c, 0));
            }
            c.close();
        }
        if (macList.isEmpty()) {
            return;
        }
        String[] args = macList.toArray(new String[macList.size()]);
        KiiQuery lightsQuery = new KiiQuery(KiiClause.inWithStringValue("thingID", args));
        try {
            KiiQueryResult<KiiObject> result = bucket.query(lightsQuery);
            List<KiiObject> objList = result.getResult();
            saveLightThing(objList);
            while (result.hasNext()) {
                result = result.getNextQueryResult();
                objList = result.getResult();
                saveLightThing(objList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveLightThing(List<KiiObject> objects) {
        for (KiiObject object : objects) {
            try {
                ContentValues values = new ContentValues();
                String site = object.getString("remote", "");
                values.put("remote", site);
                try {
                    JSONObject currAction = object.getJSONObject("currAction");
                    values.put("state", currAction.optInt("state"));
                    values.put("color", currAction.optInt("color"));
                    values.put("brightness", currAction.optInt("brightness"));
                    values.put("CT", currAction.optInt("CT"));
                    values.put("mode", currAction.optInt("mode"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String thingId = object.getString("thingID");
                App.getApp().getContentResolver()
                        .update(YanKonProvider.URI_LIGHTS, values, "MAC=?", new String[]{thingId});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
