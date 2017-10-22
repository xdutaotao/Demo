package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.View.BaseView;

import java.util.List;

/**
 * Created by
 */
public interface DocDetailView extends BaseView {
    void getData(List<DocRes> list);
}
