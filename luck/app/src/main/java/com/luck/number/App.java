package com.luck.number;

import android.app.Application;
import android.content.Context;

/**
 * Description:
 * Created by guzhenfu on 2018/4/5.
 */

public class App extends Application {

    private static Context context;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
