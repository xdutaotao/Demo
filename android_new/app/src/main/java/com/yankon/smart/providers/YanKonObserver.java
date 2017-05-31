package com.yankon.smart.providers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.yankon.smart.activities.LightInfoActivity;

/**
 * Created by guzhenfu on 2015/9/1.
 */
public class YanKonObserver extends ContentObserver {
    private Handler handler;

    public YanKonObserver(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        handler.obtainMessage(LightInfoActivity.DATA_CHANGE).sendToTarget();
    }
}
