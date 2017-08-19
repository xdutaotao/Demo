package com.demo.step.View;

import com.demo.step.Bean.UpdateVersionBean;

/**
 * Created by
 */
public interface SettingView extends BaseView {
    void getUpdateVersion(UpdateVersionBean.DataBean bean);
    void getProgress(float progress);
    void getData(String data);
}
