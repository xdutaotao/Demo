package com.bz.fitness.View;

import com.bz.fitness.Bean.UpdateVersionBean;

/**
 * Created by
 */
public interface SettingView extends BaseView {
    void getUpdateVersion(UpdateVersionBean.DataBean bean);
    void getProgress(float progress);
    void getData(String data);
}
