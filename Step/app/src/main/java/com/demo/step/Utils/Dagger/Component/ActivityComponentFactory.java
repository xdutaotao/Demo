package com.demo.step.Utils.Dagger.Component;

import android.app.Activity;

import com.demo.step.App;
import com.demo.step.Utils.Dagger.Module.ActivityModule;

public final class ActivityComponentFactory {
    private ActivityComponentFactory() {
        //no op
    }

    public static ActivityComponent create(Activity activity) {
        return App.get(activity).getComponent()
                .plus(new ActivityModule(activity));
    }
}