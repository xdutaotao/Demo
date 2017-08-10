package com.seafire.cworker.View;

import com.seafire.cworker.Bean.SearchResponseBean;

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
