package com.bz.fitness.View;

import com.bz.fitness.Bean.SearchResponseBean;

/**
 * Created by
 */
public interface SearchResultView extends BaseView {
    void getData(SearchResponseBean bean);
    void fail(String msg);
}
