package com.demo.cworker.View;

import com.demo.cworker.Bean.SearchResponseBean;

/**
 * Created by
 */
public interface SearchResultView extends BaseView {
    void getData(SearchResponseBean bean);
    void fail(String msg);
}
