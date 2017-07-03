package com.bz.fitness.View;

import com.bz.fitness.Bean.NumberBean;
import com.bz.fitness.Bean.PackageBean;

/**
 * Created by
 */
public interface CollectView extends BaseView {
    void getData(PackageBean.ResultBean bean);
    void getPostTxt(String s);
    void getSearchData(NumberBean s);
}
