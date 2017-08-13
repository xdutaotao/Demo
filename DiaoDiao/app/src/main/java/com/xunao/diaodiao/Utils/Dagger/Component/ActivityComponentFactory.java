package com.xunao.diaodiao.Utils.Dagger.Component;

import android.app.Activity;

import com.xunao.diaodiao.App;
import com.xunao.diaodiao.Utils.Dagger.Module.ActivityModule;

public final class ActivityComponentFactory {
    private ActivityComponentFactory() {
        //no op
    }

    public static ActivityComponent create(Activity activity) {
        return App.get(activity).getComponent()
                .plus(new ActivityModule(activity));
    }
}