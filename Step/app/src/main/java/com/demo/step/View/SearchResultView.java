package com.demo.step.View;

import com.demo.step.Bean.SearchResponseBean;

/**
 * Created by
 */
public interface SearchResultView extends BaseView {
    void getData(SearchResponseBean bean);
    void fail(String msg);
}
