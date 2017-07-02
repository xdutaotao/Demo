package com.seafire.cworker.View;

import com.seafire.cworker.Bean.UpdateVersionBean;

/**
 * Created by
 */
public interface SettingView extends BaseView {
    void getUpdateVersion(UpdateVersionBean.DataBean bean);
    void getProgress(float progress);
    void getData(String data);
}
