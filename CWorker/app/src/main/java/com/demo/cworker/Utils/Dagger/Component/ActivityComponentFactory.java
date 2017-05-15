package com.demo.cworker.Utils.Dagger.Component;

import android.app.Activity;

import com.demo.cworker.App;
import com.demo.cworker.Utils.Dagger.Module.ActivityModule;

public final class ActivityComponentFactory {
    private ActivityComponentFactory() {
        //no op
    }

    public static ActivityComponent create(Activity activity) {
        return App.get(activity).getComponent()
                .plus(new ActivityModule(activity));
    }
}