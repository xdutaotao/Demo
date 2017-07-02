package com.seafire.cworker.Utils.Dagger.Component;

import android.app.Activity;

import com.seafire.cworker.App;
import com.seafire.cworker.Utils.Dagger.Module.ActivityModule;

public final class ActivityComponentFactory {
    private ActivityComponentFactory() {
        //no op
    }

    public static ActivityComponent create(Activity activity) {
        return App.get(activity).getComponent()
                .plus(new ActivityModule(activity));
    }
}