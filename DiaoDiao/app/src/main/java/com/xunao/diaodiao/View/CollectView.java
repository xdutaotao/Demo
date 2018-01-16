package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.NumberBean;
import com.xunao.diaodiao.Bean.PackageBean;

/**
 * Created by
 */
public interface CollectView extends BaseView {
    void getData(PackageBean.ResultBean bean);
    void getPostTxt(String s);
    void getSearchData(NumberBean s);
    void postTxtError();
}
