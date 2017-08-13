package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.SearchResponseBean;

import java.util.List;

/**
 * Created by
 */
public interface SearchView extends BaseView {
    void getData(SearchResponseBean bean);
    void getHistoryList(List<String> list);
    void fail(String msg);
    void getHotWord(List<String> list);
}
