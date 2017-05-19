package com.demo.cworker.View;

import com.demo.cworker.Bean.SearchResponseBean;
import com.demo.cworker.View.BaseView;

import java.util.List;

/**
 * Created by
 */
public interface SearchView extends BaseView {
    void getData(SearchResponseBean bean);
    void getHistoryList(List<String> list);
    void fail(String msg);
}
