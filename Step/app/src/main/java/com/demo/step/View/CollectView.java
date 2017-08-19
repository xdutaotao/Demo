package com.demo.step.View;

import com.demo.step.Bean.NumberBean;
import com.demo.step.Bean.PackageBean;

/**
 * Created by
 */
public interface CollectView extends BaseView {
    void getData(PackageBean.ResultBean bean);
    void getPostTxt(String s);
    void getSearchData(NumberBean s);
}
