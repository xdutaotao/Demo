package com.seafire.cworker.adapters;

import com.seafire.cworker.Bean.FreindBean;
import com.seafire.cworker.Bean.Item;

/**
 * Created by lenovo on 2/23/2016.
 */
public interface ItemClickListener {
    void itemClicked(FreindBean.UsersBean item, String projectName);
}
