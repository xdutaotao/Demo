package com.xunao.diaodiao.Common;

import android.graphics.Color;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/16 16:45.
 */

public class Constants {
    public static final String TOKEN_KEY = "token_key";
    public static final String USER_INFO_KEY = "user_info_key";
    public static final String INTENT_KEY = "intent_key";
    public static final String LOGIN_AGAIN = "login_again";
    public static final String SEARCH_KEY = "SEARCH_KEY";
    public static final String CACHE_DIR = "cwork";
    public static final String POST_COLLECT_TIME = "POST_COLLECT_TIME";
    public static final String COLLECT_LIST = "COLLECT_LIST";
    public static final String IS_COME_OVER = "IS_COME_OVER";

    public static final String TYPE_KEY = "TYPE";
    public static final int COMPANY_TYPE = 1;
    public static final int SKILL_TYPE = 2;
    public static final int CUSTOM_TYPE = 3;

    public static String latData;
    public static String lngData;
    public static String address;
    public static String city;

    public static final String STATUS = "STATUS";
    public static final String DESTORY = "destory";

    public static final int SKILL_RELEASE_LINGGONG = 0;
    public static final int SKILL_RECIEVE_LINGGONG = 1;
    public static final int SKILL_RECIEVE_PROJECT = 2;
    public static final int SKILL_RELEASE_PROJECT = 3;
    public static final int COMPANY_RELEASE_PROJECT_WAIT = 4;
    public static final int COMPANY_RELEASE_PROJECT_DOING = 5;
    public static final int COMPANY_RELEASE_PROJECT_DONE = 6;

    public static final int NO_PASS = 9999;
    public static final int SKILL_RELEASE_LINGGONG_NO_PASS = 9998;


    //----------------- separator Tag Item-----------------//
    public static final float DEFAULT_TAG_TEXT_SIZE = 18f;
    public static final float DEFAULT_TAG_DELETE_INDICATOR_SIZE = 14f;
    public static final float DEFAULT_TAG_LAYOUT_BORDER_SIZE = 0f;
    public static final float DEFAULT_TAG_RADIUS = 100;
    public static final int DEFAULT_TAG_LAYOUT_COLOR = Color.parseColor("#AED374");
    public static final int DEFAULT_TAG_LAYOUT_COLOR_PRESS = Color.parseColor("#88363636");
    public static final int DEFAULT_TAG_TEXT_COLOR = Color.parseColor("#4889db");
    public static final int DEFAULT_TAG_DELETE_INDICATOR_COLOR = Color.parseColor("#ffffff");
    public static final int DEFAULT_TAG_LAYOUT_BORDER_COLOR = Color.parseColor("#ffffff");
    public static final String DEFAULT_TAG_DELETE_ICON = "×";
    public static final boolean DEFAULT_TAG_IS_DELETABLE = false;

    //----------------- separator TagView-----------------//
    public static final float DEFAULT_LINE_MARGIN = 5;
    public static final float DEFAULT_TAG_MARGIN = 3;
    public static final float DEFAULT_TAG_TEXT_PADDING_LEFT = 8;
    public static final float DEFAULT_TAG_TEXT_PADDING_TOP = 5;
    public static final float DEFAULT_TAG_TEXT_PADDING_RIGHT = 8;
    public static final float DEFAULT_TAG_TEXT_PADDING_BOTTOM = 5;

    public static final float LAYOUT_WIDTH_OFFSET = 2;


}
