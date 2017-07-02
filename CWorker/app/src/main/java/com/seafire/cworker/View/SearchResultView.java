package com.seafire.cworker.View;

import com.seafire.cworker.Bean.SearchResponseBean;

/**
 * Created by
 */
public interface SearchResultView extends BaseView {
    void getData(SearchResponseBean bean);
    void fail(String msg);
}
