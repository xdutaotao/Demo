package com.xunao.diaodiao;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xunao.diaodiao.Utils.Dagger.Component.ApplicationComponent;
import com.xunao.diaodiao.Utils.Dagger.Component.DaggerApplicationComponent;
import com.xunao.diaodiao.Utils.Dagger.Module.ApplicationModule;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.Widget.GlideImageLoader;

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
        //SMSSDK.initSDK(this, "20ad18688fcee", "a0536022d8f2db4260ae91feb3d84878");

        //CrashReport.initCrashReport(getApplicationContext(), "9ce26c6dd7", false);

        //友盟推送
        //注册推送服务，每次调用register方法都会回调该接口
        PushAgent.getInstance(this).register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                Utils.handleNotification(context, uMessage.custom);
            }
        };
        PushAgent.getInstance(this).setNotificationClickHandler(notificationClickHandler);


//        JMessageClient.setDebugMode(true);
//        JMessageClient.init(this);
//
//        //设置Notification的模式
//        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        initPicker();


        if (Build.VERSION.SDK_INT >23) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }

    }

    private void initPicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth(600);
        imagePicker.setFocusHeight(600);
        imagePicker.setShowCamera(false);
    }



}
