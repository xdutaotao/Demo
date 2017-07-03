package com.bz.fitness.Utils.Dagger.Component;

import android.app.Activity;

import com.bz.fitness.App;
import com.bz.fitness.Utils.Dagger.Module.ActivityModule;

public final class ActivityComponentFactory {
    private ActivityComponentFactory() {
        //no op
    }

    public static ActivityComponent create(Activity activity) {
        return App.get(activity).getComponent()
                .plus(new ActivityModule(activity));
    }
}