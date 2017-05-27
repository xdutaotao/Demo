package com.demo.cworker.View;

import com.demo.cworker.Bean.NumberBean;
import com.demo.cworker.Bean.PackageBean;

/**
 * Created by
 */
public interface CollectView extends BaseView {
    void getData(PackageBean.ResultBean bean);
    void getPostTxt(String s);
    void getSearchData(NumberBean s);
}
