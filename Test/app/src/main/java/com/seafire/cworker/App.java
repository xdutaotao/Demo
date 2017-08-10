package com.seafire.cworker;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.seafire.cworker.Utils.Dagger.Component.ApplicationComponent;
import com.seafire.cworker.Utils.Dagger.Component.DaggerApplicationComponent;
import com.seafire.cworker.Utils.Dagger.Module.ApplicationModule;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.im.android.api.JMessageClient;
import cn.smssdk.SMSSDK;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/15 11:16.
 */

public class App  extends android.support.multidex.MultiDexApplication{
    private static App mAPPLike;
    private static Application mApplication;
    private static Context mContext;
    private ApplicationComponent applicationComponent;

    public ApplicationComponent getComponent(){
        if (applicationComponent == null){
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(mApplication))
                    .build();
        }
        return applicationComponent;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public static Application getApplicationInstance(){
        return mApplication;
    }

    public static Context getContext(){
        return mContext;
    }

    public static final int REQUEST_CODE_TAKE_PHOTO = 4;
    public static final int REQUEST_CODE_SELECT_PICTURE = 6;
    public static final int RESULT_CODE_SELECT_PICTURE = 8;
    public static final int REQUEST_CODE_SELECT_ALBUM = 10;
    public static final int RESULT_CODE_SELECT_ALBUM = 11;
    public static final int REQUEST_CODE_BROWSER_PICTURE = 12;
    public static final int RESULT_CODE_BROWSER_PICTURE = 13;
    public static final int REQUEST_CODE_CHAT_DETAIL = 14;
    public static final int RESULT_CODE_CHAT_DETAIL = 15;
    public static final int REQUEST_CODE_FRIEND_INFO = 16;
    public static final int RESULT_CODE_FRIEND_INFO = 17;
    public static final int REQUEST_CODE_CROP_PICTURE = 18;
    public static final int REQUEST_CODE_ME_INFO = 19;
    public static final int RESULT_CODE_ME_INFO = 20;
    public static final int REQUEST_CODE_ALL_MEMBER = 21;
    public static final int RESULT_CODE_ALL_MEMBER = 22;
    public static final int RESULT_CODE_SELECT_FRIEND = 23;
    public static final int REQUEST_CODE_SEND_LOCATION = 24;
    public static final int RESULT_CODE_SEND_LOCATION = 25;
    public static final int REQUEST_CODE_SEND_FILE = 26;
    public static final int RESULT_CODE_SEND_FILE = 27;
    public static final int REQUEST_CODE_EDIT_NOTENAME = 28;
    public static final int RESULT_CODE_EDIT_NOTENAME = 29;
    public static final int REQUEST_CODE_AT_MEMBER = 30;
    public static final int RESULT_CODE_AT_MEMBER = 31;
    public static final int ON_GROUP_EVENT = 3004;

    private static final String JCHAT_CONFIGS = "JChat_configs";
    public static final String CONV_TITLE = "convTitle";
    public static final String TARGET_APP_KEY = "targetAppKey";
    public static final String TARGET_ID = "targetId";
    public static final String AVATAR = "avatar";
    public static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    public static final String GROUP_ID = "groupId";
    public static final String GROUP_NAME = "groupName";
    public static final String NOTENAME = "notename";
    public static final String GENDER = "gender";
    public static final String REGION = "region";
    public static final String SIGNATURE = "signature";
    public static final String STATUS = "status";
    public static final String POSITION = "position";
    public static final String MsgIDs = "msgIDs";
    public static final String DRAFT = "draft";
    public static final String DELETE_MODE = "deleteMode";
    public static final String MEMBERS_COUNT = "membersCount";
    public static String PICTURE_DIR = "sdcard/cworker/pictures/";
    public static String FILE_DIR = "sdcard/cworker/recvFiles/";
    public static boolean isNeedAtMsg = true;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        SMSSDK.initSDK(this, "1d79b47d1e280", "b4cfa85b04cfee6601067e1aa9b0b5d8");

        CrashReport.initCrashReport(getApplicationContext(), "9ce26c6dd7", false);

        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);

        //设置Notification的模式
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);

        ActiveAndroid.initialize(this);
    }



}
