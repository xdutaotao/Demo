package com.demo.cworker.View;

import com.demo.cworker.Bean.UpdateVersionBean;
import com.demo.cworker.View.BaseView;

/**
 * Created by
 */
public interface SettingView extends BaseView {
    void getUpdateVersion(UpdateVersionBean.DataBean bean);
    void getProgress(float progress);
    void getData(String data);
}
