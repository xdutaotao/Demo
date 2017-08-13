package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.UpdateVersionBean;

/**
 * Created by
 */
public interface SettingView extends BaseView {
    void getUpdateVersion(UpdateVersionBean.DataBean bean);
    void getProgress(float progress);
    void getData(String data);
}
