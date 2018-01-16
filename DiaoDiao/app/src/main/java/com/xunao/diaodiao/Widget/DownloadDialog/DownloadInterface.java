package com.xunao.diaodiao.Widget.DownloadDialog;

import android.content.Context;

/**
 * Description:
 * Created by guzhenfu on 2017/1/20 11:34.
 */

public interface DownloadInterface {
    void showDialog(Context context);
    void showProgress(float progress);
    void dismissDialog();
}
