package com.seafire.cworker.View;

import com.seafire.cworker.Bean.NumberBean;
import com.seafire.cworker.Bean.PackageBean;

/**
 * Created by
 */
public interface CollectView extends BaseView {
    void getData(PackageBean.ResultBean bean);
    void getPostTxt(String s);
    void getSearchData(NumberBean s);
}
