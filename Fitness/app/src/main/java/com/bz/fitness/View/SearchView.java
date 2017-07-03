package com.bz.fitness.View;

import com.bz.fitness.Bean.SearchResponseBean;

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
