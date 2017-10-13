package com.xunao.diaodiao.Widget.DownloadDialog;

/**
 * Description:
 * Created by guzhenfu on 2017/1/20 11:49.
 */

public class DownloadDialogFactory {

    public static DownloadInterface getDownloadDialogManager(){
        return DownloadDialog.getInstance();
    }
}
